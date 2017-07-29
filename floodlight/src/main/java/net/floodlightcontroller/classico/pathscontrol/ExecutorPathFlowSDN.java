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

//import net.floodlightcontroller.classico.pathscontrol.ExecutorPathFlowSDN.PreviousRecordingFlow;
//import net.floodlightcontroller.classico.pathscontrol.ExecutorPathFlowSDN.PreviousRecordingGroup;
import net.floodlightcontroller.classico.sessionmanager.GroupMod;
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

	public ExecutorPathFlowSDN(IOFSwitchService switchService) {
		this.oldBestPaths = new HashMap<>();
		this.oldNodePaths = new ArrayList<>();
		this.switchService = switchService;
	}

	// private void modifyFlow(DatapathId datapathid, Rule rule, OFPort ofPort){
	//
	// IOFSwitch iofs = switchService.getSwitch(datapathid);
	// OFFactory factory = iofs.getOFFactory();
	//
	// OFFlowAdd flowAdd = factory.buildFlowAdd()
	// .setHardTimeout(0)
	// .setIdleTimeout(0)
	// .setPriority(FlowModUtils.PRIORITY_MAX)
	// .setMatch(factory.buildMatch()
	// .setExact(MatchField.ETH_TYPE, EthType.IPv4)
	// .setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
	// .setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
	// .setExact(MatchField.IP_PROTO, IpProtocol.UDP)
	// .build())
	// .setActions(Collections.singletonList(factory.actions().buildOutput()
	// .setMaxLen(0xffFFffFF)
	// .setPort(ofPort)
	// .build()))
	// .build();
	//
	// OFFlowModify flowModify = FlowModUtils.toFlowModify(flowAdd);
	//
	// iofs.write(flowModify);
	//
	// System.out.println("[ExecutorPathFlowSDN] FLOW_MOD Modify: Switch: " +
	// datapathid.toString() +
	// ", Port: " + ofPort.getPortNumber() +", Reference: "+rule.getIpv4Src()+"
	// -> "+rule.getIpv4Dst());
	// }

//	private void deleteFlow2(DatapathId datapathid, Rule rule) {
//
//		IOFSwitch iofs = switchService.getSwitch(datapathid);
//		OFFactory factory = iofs.getOFFactory();
//
//		OFFlowDelete f = factory.buildFlowDelete().setPriority(FlowModUtils.PRIORITY_MAX)
//				.setMatch(factory.buildMatch()
//						/*
//						 * .setExact(MatchField.IN_PORT,
//						 * iof_switch.getPort(rule.getInPort()).getPortNo())
//						 */
//						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
//						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
//						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
//						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
//
//				.build();
//		iofs.write(f);
//
//		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD DELETE: Switch: " + datapathid.toString() + ", Reference: "
//				+ rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
//	}

	private void deleteFlow(DatapathId datapathid, Rule rule) {

		IOFSwitch iofs = switchService.getSwitch(datapathid);
		OFFactory factory = iofs.getOFFactory();

		OFFlowDelete f = factory.buildFlowDelete().setHardTimeout(0).setIdleTimeout(0)
				.setPriority(FlowModUtils.PRIORITY_MAX)
				.setMatch(factory.buildMatch()
						/*
						 * .setExact(MatchField.IN_PORT,
						 * iof_switch.getPort(rule.getInPort()).getPortNo())
						 */
						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
						.setExact(MatchField.IP_PROTO, IpProtocol.UDP)

						.build())

				.build();
		iofs.write(f);

		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD DELETE: Switch: " + datapathid.toString() + ", Reference: "
				+ rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
	}

	private void createFlow(DatapathId datapathid, Rule rule, OFGroup group) {

		IOFSwitch iofs = switchService.getSwitch(datapathid);
		OFFactory factory = iofs.getOFFactory();

		OFFlowAdd flowAdd = factory.buildFlowAdd().setHardTimeout(0).setIdleTimeout(0)
				.setPriority(FlowModUtils.PRIORITY_MAX)
				.setMatch(factory.buildMatch()
						/*
						 * .setExact(MatchField.IN_PORT,
						 * iof_switch.getPort(rule.getInPort()).getPortNo())
						 */
						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
				.setActions(Collections.singletonList(factory.actions().buildGroup().setGroup(group).build())).build();
		iofs.write(flowAdd);
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Switch: " + datapathid.toString() + ", Port: "
				+ group.getGroupNumber() + ", Reference: " + rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
	}

//	private void modifyFlow(DatapathId datapathid, Rule rule, OFGroup group) {
//
//		IOFSwitch iofs = switchService.getSwitch(datapathid);
//		OFFactory factory = iofs.getOFFactory();
//
//		OFFlowModify flowmodify = factory.buildFlowModify().setHardTimeout(0).setIdleTimeout(0)
//				.setPriority(FlowModUtils.PRIORITY_MAX)
//				.setMatch(factory.buildMatch()
//						/*
//						 * .setExact(MatchField.IN_PORT,
//						 * iof_switch.getPort(rule.getInPort()).getPortNo())
//						 */
//						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
//						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
//						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
//						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
//				.setActions(Collections.singletonList(factory.actions().buildGroup().setGroup(group).build())).build();
//		iofs.write(flowmodify);
//		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD MODIFY: Switch: " + datapathid.toString() + ", Port: "
//				+ group.getGroupNumber() + ", Reference: " + rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
//	}

//	private void modifyFlow(DatapathId datapathid, Rule rule, OFPort ofPort) {
//
//		IOFSwitch iofs = switchService.getSwitch(datapathid);
//		OFFactory factory = iofs.getOFFactory();
//
//		OFFlowModify flowmodify = factory.buildFlowModify().setHardTimeout(0).setIdleTimeout(0)
//				.setPriority(FlowModUtils.PRIORITY_MAX)
//				.setMatch(factory.buildMatch()
//						/*
//						 * .setExact(MatchField.IN_PORT,
//						 * iof_switch.getPort(rule.getInPort()).getPortNo())
//						 */
//						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
//						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
//						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
//						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
//				.setActions(Collections
//						.singletonList(factory.actions().buildOutput().setMaxLen(0xffFFffFF).setPort(ofPort).build()))
//				.build();
//		iofs.write(flowmodify);
//		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD MODIFY: Switch: " + datapathid.toString() + ", Port: "
//				+ ofPort.getPortNumber() + ", Reference: " + rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
//	}

	private void createFlow(DatapathId datapathid, Rule rule, OFPort ofPort) {

		IOFSwitch iofs = switchService.getSwitch(datapathid);
		OFFactory factory = iofs.getOFFactory();

		OFFlowAdd flowAdd = factory.buildFlowAdd().setHardTimeout(0).setIdleTimeout(0)
				.setPriority(FlowModUtils.PRIORITY_MAX)
				.setMatch(factory.buildMatch()
						/*
						 * .setExact(MatchField.IN_PORT,
						 * iof_switch.getPort(rule.getInPort()).getPortNo())
						 */
						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
				.setActions(Collections
						.singletonList(factory.actions().buildOutput().setMaxLen(0xffFFffFF).setPort(ofPort).build()))
				.build();
		iofs.write(flowAdd);
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Switch: " + datapathid.toString() + ", Port: "
				+ ofPort.getPortNumber() + ", Reference: " + rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
	}

	//
	// private boolean consultIfExistsGroup(int sessionid, DatapathId did){
	// for (PreviousRecordingGroup prg : previousRecordingGroups) {
	// if(prg.group.getId() == sessionid &&
	// prg.group.getIof_switch().getId().equals(did)){
	// prg.setMarked(true);
	// return true;
	// }
	// }
	// return false;
	// }
	//
	private boolean consultIfExistsFlow(int sessionid, DatapathId did, Rule rule) {
		PreviousRecordingFlow previousRecordingFlow = new PreviousRecordingFlow(did, sessionid, rule);
		for (PreviousRecordingFlow prf : previousRecordingFlows) {
			if (prf.equals(previousRecordingFlow)) {
				prf.setMarked(true);
				return true;
			}
		}
		return false;
	}

	// List<PreviousRecordingGroup> previousRecordingGroups = new ArrayList<>();
	List<PreviousRecordingFlow> previousRecordingFlows = new ArrayList<>();

	private void updatePreviousRecording() {
		for (Iterator<PreviousRecordingFlow> iterator = previousRecordingFlows.iterator(); iterator.hasNext();) {
			PreviousRecordingFlow prf = (PreviousRecordingFlow) iterator.next();
			if (!prf.isMarked()) {
				deleteFlow(prf.getDatapathId(), prf.getRule());
				iterator.remove();
			} else {
				prf.setMarked(false);
			}
		}
		// for (Iterator<PreviousRecordingGroup> iterator =
		// previousRecordingGroups.iterator(); iterator.hasNext();) {
		// PreviousRecordingGroup prg = (PreviousRecordingGroup)
		// iterator.next();
		// if(!prg.isMarked()){
		// iterator.remove();
		// prg.getGroup().deleteGroup();
		// }else{
		// prg.setMarked(false);
		// }
		// }
	}

	private void markAsUnchanged(NodePath nodePath) {
		for (PreviousRecordingFlow prf : previousRecordingFlows) {
			if (prf.getSessionID() == nodePath.getIdSession() && prf.getDatapathId().equals(nodePath.getDataPathId())) {
				prf.setMarked(true);
				System.out.println("AQUI" + nodePath.toString());
			}
		}
	}

	// public class PreviousRecordingGroup{
	//
	// private GroupMod group;
	// private boolean marked;
	// private Rule rule;
	//
	// public PreviousRecordingGroup(GroupMod group, Rule rule){
	// this.group = group;
	// marked = true;
	// this.setRule(rule);
	// }
	//
	// public GroupMod getGroup() {
	// return group;
	// }
	// public void setGroup(GroupMod group) {
	// this.group = group;
	// }
	// public boolean isMarked() {
	// return marked;
	// }
	// public void setMarked(boolean marked) {
	// this.marked = marked;
	// }
	// public Rule getRule() {
	// return rule;
	// }
	// public void setRule(Rule rule) {
	// this.rule = rule;
	// }
	//
	// }

	public class PreviousRecordingFlow {

		private DatapathId datapathId;
		private int sessionID;
		private boolean marked;
		private Rule rule;

		public PreviousRecordingFlow(DatapathId datapathId, int sessionID, Rule rule) {
			this.datapathId = datapathId;
			this.sessionID = sessionID;
			this.rule = rule;
			marked = true;
		}

		public DatapathId getDatapathId() {
			return datapathId;
		}

		public void setDatapathId(DatapathId datapathId) {
			this.datapathId = datapathId;
		}

		public int getSessionID() {
			return sessionID;
		}

		public void setSessionID(int sessionID) {
			this.sessionID = sessionID;
		}

		public boolean isMarked() {
			return marked;
		}

		public void setMarked(boolean marked) {
			this.marked = marked;
		}

		public Rule getRule() {
			return rule;
		}

		public void setRule(Rule rule) {
			this.rule = rule;
		}

	}

	public void execute(NodePath nodePath) {

		if (nodePath == null) {
			// Caso base quando o nó é nulo
			return;
		} else if (oldNodePaths.contains(nodePath)) {
			System.out.println("[ExecutorPathFlowSDN] No changes in NodePath " + nodePath.getDataPathId()
					+ " of session " + nodePath.getIdSession());

			// previousRecordingFlows.s
			markAsUnchanged(nodePath);

			// Executa o procedimento para o próximo switch
			for (EdgeMap edgMap : nodePath.getConections()) {
				execute(edgMap.getNextNodePath());
			}

		} else if (nodePath.isBranch()) { // Condição para criar um grupo

			IOFSwitch iofs = switchService.getSwitch(nodePath.getDataPathId());

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
			// EdgeMap edgeMap = nodePath.getLastConnection();
			UserSession client = edgeMap.getClients().get(0);
			Rule rule = new Rule(client.getDstIp().toString(), client.getIp().toString());


			gmod.writeGroup();

			if (!consultIfExistsFlow(nodePath.getIdSession(), nodePath.getDataPathId(), rule)) {
				createFlow(nodePath.getDataPathId(), rule, gmod.getGroup());
				previousRecordingFlows
						.add(new PreviousRecordingFlow(nodePath.getDataPathId(), nodePath.getIdSession(), rule));
			}

			for (EdgeMap edgMap : nodePath.getConections()) {

				// Executa o procedimento para o próximo switch
				execute(edgMap.getNextNodePath());
			}

		} else if (nodePath.isBridge()) {
			EdgeMap edgeMap = nodePath.getFirstConnection();
			UserSession client = edgeMap.getClients().get(0);
			Rule rule = new Rule(client.getDstIp().toString(), client.getIp().toString());

			if (!consultIfExistsFlow(nodePath.getIdSession(), nodePath.getDataPathId(), rule)) {
				createFlow(nodePath.getDataPathId(), rule, edgeMap.getOfPort());
				previousRecordingFlows
						.add(new PreviousRecordingFlow(nodePath.getDataPathId(), nodePath.getIdSession(), rule));
			}
			
			execute(edgeMap.getNextNodePath());
		}
	}

	public void write(HashMap<Integer, TreePath> treesMap) {

		updatePreviousRecording();
		for (Iterator<Integer> iterator = treesMap.keySet().iterator(); iterator.hasNext();) {
			Integer sessionID = (Integer) iterator.next();

			TreePath treePath = treesMap.get(sessionID);

			execute(treePath.getNodePaths().get(0));
			oldNodePaths.clear();
			oldNodePaths.addAll(treePath.getNodePaths());
//			System.out.println("--- " + treesMap);
		}
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

		System.out.println(treesMap.toString());
		write(treesMap);

		oldBestPaths.clear();
		oldBestPaths.putAll(bestPaths);

	}

}
