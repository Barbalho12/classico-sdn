package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;

import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowDelete;
//import org.projectfloodlight.openflow.protocol.OFFlowModify;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.OFGroup;
import org.projectfloodlight.openflow.types.OFPort;

import jline.History;
import net.floodlightcontroller.classico.sessionmanager.Rule;
import net.floodlightcontroller.classico.sessionmanager.Session;
import net.floodlightcontroller.classico.sessionmanager.UserSession;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.types.NodePortTuple;
import net.floodlightcontroller.util.FlowModUtils;

public class ExecutorPathFlowSDN {

	protected IOFSwitchService switchService;

	private HashMap<String, CandidatePath> oldBestPaths;
	private List<NodePath> oldNodePaths;
	
	
	//Armazena os grupos criados, para não tentar criar novamente
//	private HashMap<Integer, IOFSwitch> oldGroupsCreated;
	GroupHistory groupHistory;
	FlowHistory flowModHistory;

	public ExecutorPathFlowSDN(IOFSwitchService switchService) {
		this.oldBestPaths = new HashMap<>();
		this.oldNodePaths = new ArrayList<>();
//		this.oldGroupsCreated = new HashMap<>();
		this.switchService = switchService;
		
		groupHistory = new GroupHistory();
		flowModHistory = new FlowHistory();
	}


	public void execute(NodePath nodePath) {
		
		// Caso base quando o nó é nulo
		if (nodePath == null) { return; } 
		
		IOFSwitch iofs = switchService.getSwitch(nodePath.getDataPathId());
		
		if (oldNodePaths.contains(nodePath)) {
			System.out.println("[ExecutorPathFlowSDN] No changes in NodePath " + nodePath.getDataPathId()
					+ " of session " + nodePath.getIdSession());

			flowModHistory.markAsUnchanged(nodePath.getIdSession(), iofs);
		
			// Executa o procedimento para o próximo switch
			for (EdgeMap edgMap : nodePath.getConections()) {
				execute(edgMap.getNextNodePath());
			}

		} else if (nodePath.isBranch()) { // Condição para criar um grupo

			GroupMod gmod = new GroupMod(iofs, nodePath.getIdSession());
			
			// Para cada conexão do switch
			for (EdgeMap edgeMap : nodePath.getConections()) {
				UserSession client = edgeMap.getClients().get(0);
				// Se está conectado a outro switch
				if (edgeMap.getNextNodePath() != null) {
					gmod.createBucket(iofs, edgeMap.getOfPort(), client.getIp(), client.getPort(),
							client.getMACadreess());

					// Caso contrário, se está conectado a um host
				} else {
					gmod.createBucket(iofs, client.getSwitchInPort(), client.getIp(), client.getPort(),
							client.getMACadreess());
				}
			}

			// Pega a primeira conexão do switch, e cria uma regra
			EdgeMap edgeMap = nodePath.getFirstConnection();
			UserSession client = edgeMap.getClients().get(0);
			Rule rule = new Rule(client.getDstIp().toString(), client.getIp().toString());

			if (groupHistory.contains(gmod)){
				gmod.modifyGroup();
				groupHistory.mark(gmod);
			}else{
				gmod.writeGroup();
				groupHistory.add(gmod);
			}

			FlowMod flowm = new FlowMod(iofs, nodePath.getIdSession(), rule);
			if(!flowModHistory.contains(flowm)){
				flowm.createFlow(gmod.getGroup());
				flowModHistory.add(flowm);
			}

			for (EdgeMap edgMap : nodePath.getConections()) {
				// Executa o procedimento para o próximo switch
				execute(edgMap.getNextNodePath());
			}

		} else if (nodePath.isBridge()) {
			EdgeMap edgeMap = nodePath.getFirstConnection();
			UserSession client = edgeMap.getClients().get(0);
			Rule rule = new Rule(client.getDstIp().toString(), client.getIp().toString());
			
			FlowMod flowm = new FlowMod(iofs, nodePath.getIdSession(), rule);
			if(!flowModHistory.contains(flowm)){
				flowm.createFlow(edgeMap.getOfPort());
				flowModHistory.add(flowm);
			}else{
				flowm.modifyFlow(edgeMap.getOfPort());
				flowModHistory.mark(flowm);
			}
			
			execute(edgeMap.getNextNodePath());
		}else {
			EdgeMap edgeMap = nodePath.getFirstConnection();
			UserSession client = edgeMap.getClients().get(0);
			Rule rule = new Rule(client.getDstIp().toString(), client.getIp().toString());
			
			FlowMod flowm = new FlowMod(iofs, nodePath.getIdSession(), rule);
			if(!flowModHistory.contains(flowm)){
				flowm.createFlow(client.getSwitchInPort());
				flowModHistory.add(flowm);
			}else{
//				flowModHistory.mark(flowm);
			}
		}
	}

	public void write(HashMap<Integer, TreePath> treesMap) {
//		updatePreviousRecording();
		flowModHistory.unmarkAll();
		groupHistory.unmarkAll();
		for (Iterator<Integer> iterator = treesMap.keySet().iterator(); iterator.hasNext();) {
			Integer sessionID = (Integer) iterator.next();
			TreePath treePath = treesMap.get(sessionID);
			execute(treePath.getNodePaths().get(0));
			oldNodePaths.clear();
			oldNodePaths.addAll(treePath.getNodePaths());
		}
		groupHistory.deleteUnusedGroup();
		flowModHistory.deleteUnusedFlows();
	}


	/**
	 * Verifica se houve mudanças nos caminhos
	 * 
	 * @param bestPaths
	 *            conjunto de molhres caminhos, que será comparado com o
	 *            conjunto anterior
	 * @return True se houve alterações de caminhos
	 */
	public boolean checkIfChange(HashMap<String, CandidatePath> bestPaths) {
		/* Controla se houve alterações */
		List<String> keyEquals = new ArrayList<>();
		for (String keyPath : bestPaths.keySet()) {
			for (String keyoldPath : oldBestPaths.keySet()) {
				if (keyPath.equals(keyoldPath)) {
					if (bestPaths.containsKey(keyPath)
							&& bestPaths.get(keyPath).getPath().equals(oldBestPaths.get(keyPath).getPath())) {
						keyEquals.add(keyPath);
					}
				}
			}
		}
		if (keyEquals.size() == bestPaths.size() && keyEquals.size() == oldBestPaths.size()) {
			return false;
		}

		return true;
	}

	public void updateFlowPaths(List<MultipathSession> multipathSessions, HashMap<String, CandidatePath> bestPaths) {

		// Se não alterações nos melhores caminhos é encerrado a execução
		if (!checkIfChange(bestPaths)) {
			System.out.println("[ExecutorPathFlowSDN] No changes in Flows");
			return;
		}

		HashMap<Integer, TreePath> treesMap = new HashMap<>();

		for (Iterator<MultipathSession> iterator1 = multipathSessions.iterator(); iterator1.hasNext();) {
			MultipathSession ms = (MultipathSession) iterator1.next();
			Session session = ms.getSessionMultiUser();

			CandidatePath bp = bestPaths.get(ms.getPathIndex());

			TreePath treePath;

			if (treesMap.containsKey(session.getId())) {
				treePath = treesMap.get(session.getId());
			} else {
				treePath = new TreePath();
			}

			NodePath nodePathprev = null;
			NodePath nodePath = null;
			for (int i = 0; i < bp.getPath().size(); i++) {

				if (i % 2 != 0 && i != bp.getPath().size() - 1)
					continue;

				OFPort ofPort;
				NodePortTuple npt = bp.getPath().get(i);

				if (i == bp.getPath().size() - 1) {
					// Se for o switch de borda, pega a interface do host
					ofPort = ms.getUserSession().getSwitchInPort();
				} else {
					// Caso contrário pega a interface dos links
					ofPort = npt.getPortId();
				}

				if (treePath.containsNode(npt)) {

					nodePath = treePath.getNodePathByRef(npt.getNodeId());

					if (nodePath.containsConnection(ofPort)) {
						EdgeMap edgeMap = nodePath.getConnection(ofPort);
						edgeMap.addClientIfNotExists(ms.getUserSession());
					} else {
						EdgeMap edgeMap = new EdgeMap();
						edgeMap.setOfPort(ofPort);
						edgeMap.addClient(ms.getUserSession());
						nodePath.addConnectionIfNotExists(edgeMap);
						if (nodePathprev != null) {
							nodePathprev.getLastConnection().setNextNodePath(nodePath);
						}
					}
				} else {

					nodePath = new NodePath(npt.getNodeId(), session.getId());

					EdgeMap edgeMap = new EdgeMap();
					edgeMap.setOfPort(ofPort);
					edgeMap.addClient(ms.getUserSession());
					nodePath.addConnectionIfNotExists(edgeMap);
					if (nodePathprev != null) {
						nodePathprev.getLastConnection().setNextNodePath(nodePath);
					}
					treePath.addNodePath(nodePath);
				}
				nodePathprev = nodePath;

			}

			if (!treesMap.containsKey(session.getId())) {
				treesMap.put(session.getId(), treePath);
			}
		}

//		System.out.println(treesMap.toString());
		write(treesMap);

		oldBestPaths.clear();
		oldBestPaths.putAll(bestPaths);

	}

}
