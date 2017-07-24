package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowDelete;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.OFGroup;
import org.projectfloodlight.openflow.types.OFPort;
import org.python.antlr.PythonParser.continue_stmt_return;
import org.python.antlr.runtime.tree.Tree;

import net.floodlightcontroller.classico.sessionmanager.GroupMod;
import net.floodlightcontroller.classico.sessionmanager.Rule;
import net.floodlightcontroller.classico.sessionmanager.Session;
import net.floodlightcontroller.classico.sessionmanager.UserSession;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.types.NodePortTuple;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.util.FlowModUtils;

public class ExecutorPathFlowSDN {
	
	protected IOFSwitchService switchService;
	
	private HashMap<String, CandidatePath> oldBestPaths;
	
	
	public ExecutorPathFlowSDN(IOFSwitchService switchService){
		oldBestPaths = new HashMap<>();
		this.switchService = switchService;
		
	}
	
	private void deleteFlow(DatapathId datapathid, Rule rule, OFPort ofPort){

		IOFSwitch iofs = switchService.getSwitch(datapathid);
		OFFactory factory = iofs.getOFFactory();

		OFFlowDelete f = factory.buildFlowDelete()
				.setHardTimeout(0)
			    .setIdleTimeout(0)
			    .setPriority(FlowModUtils.PRIORITY_MAX)
			    .setMatch(factory.buildMatch()
			    	/*.setExact(MatchField.IN_PORT, iof_switch.getPort(rule.getInPort()).getPortNo())*/
			        .setExact(MatchField.ETH_TYPE, EthType.IPv4)
			        .setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
			        .setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
			        .setExact(MatchField.IP_PROTO, IpProtocol.UDP)
			       
			        .build())
			    
			    .build();
		iofs.write(f);
		
//		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD DELETE");
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD DELETE: Switch: " + datapathid.toString() + 
				", Port: " + ofPort.getPortNumber() +", Reference: "+rule.getIpv4Src()+" -> "+rule.getIpv4Dst());
	}
	
	private void createFlow(DatapathId datapathid, Rule rule, OFGroup group ){
		
		IOFSwitch iofs = switchService.getSwitch(datapathid);
		OFFactory factory = iofs.getOFFactory();
		
		OFFlowAdd flowAdd = factory.buildFlowAdd()
			    .setHardTimeout(0)
			    .setIdleTimeout(0)
			    .setPriority(FlowModUtils.PRIORITY_MAX)
			    .setMatch(factory.buildMatch()
			    	/*.setExact(MatchField.IN_PORT, iof_switch.getPort(rule.getInPort()).getPortNo())*/
			        .setExact(MatchField.ETH_TYPE, EthType.IPv4)
			        .setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
			        .setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
			        .setExact(MatchField.IP_PROTO, IpProtocol.UDP)
			        .build())
			    .setActions(Collections.singletonList(factory.actions().buildGroup()
			    	.setGroup(group)
			        .build()))
			    .build();
		iofs.write(flowAdd);
//		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD");
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Switch: " + datapathid.toString() + 
				", Port: " + group.getGroupNumber() +", Reference: "+rule.getIpv4Src()+" -> "+rule.getIpv4Dst());
	}
	
	private void createFlow(DatapathId datapathid, Rule rule, OFPort ofPort){
		
		IOFSwitch iofs = switchService.getSwitch(datapathid);
		OFFactory factory = iofs.getOFFactory();
		
		OFFlowAdd flowAdd = factory.buildFlowAdd()
			    .setHardTimeout(0)
			    .setIdleTimeout(0)
			    .setPriority(FlowModUtils.PRIORITY_MAX)
			    .setMatch(factory.buildMatch()
			    	/*.setExact(MatchField.IN_PORT, iof_switch.getPort(rule.getInPort()).getPortNo())*/
			        .setExact(MatchField.ETH_TYPE, EthType.IPv4)
			        .setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
			        .setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
			        .setExact(MatchField.IP_PROTO, IpProtocol.UDP)
			        .build())
			    .setActions(Collections.singletonList(factory.actions().buildOutput()
			            .setMaxLen(0xffFFffFF)
			            .setPort(ofPort)
			            .build()))
			    .build();
		iofs.write(flowAdd);
//		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD");
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Switch: " + datapathid.toString() + 
				", Port: " + ofPort.getPortNumber() +", Reference: "+rule.getIpv4Src()+" -> "+rule.getIpv4Dst());
	}
	
	public void execute(NodePath nodePath){
		
		if(nodePath == null){
			System.out.println(2);
			return;
			
		}else if(nodePath.isBranch()){
			System.out.println(nodePath.getDataPathId()+" "+3);
			
			IOFSwitch iofs = switchService.getSwitch(nodePath.getDataPathId());
			System.out.println(iofs);
			GroupMod gmod = new GroupMod(iofs, nodePath.getIdSession());
			for (EdgeMap edgeMap : nodePath.getConections()) {
				System.out.println(nodePath.getDataPathId()+" "+4);
				UserSession client = edgeMap.getClients().get(0);
				if(edgeMap.getNextNodePath() != null){
					gmod.createBucket(iofs, edgeMap.getOfPort(), client.getSrcIp(), client.getSrcPort(), client.getMACadreess());
				}else{
					gmod.createBucket(iofs, null, client.getSrcIp(), client.getSrcPort(), client.getMACadreess());
				}
				execute(edgeMap.getNextNodePath());
			}
			
			EdgeMap edgeMap = nodePath.getLastConnection();
			UserSession client = edgeMap.getClients().get(0);
			Rule rule = new Rule( client.getDstIp().toString(),client.getSrcIp().toString());
			
			
			gmod.writeGroup();
			createFlow(nodePath.getDataPathId(), rule, gmod.getGroup());

		}else if(nodePath.isBridge()){
			System.out.println(nodePath.getDataPathId()+" "+5);
			EdgeMap edgeMap = nodePath.getLastConnection();
			UserSession client = edgeMap.getClients().get(0);
			Rule rule = new Rule( client.getDstIp().toString(),client.getSrcIp().toString());
			createFlow(nodePath.getDataPathId(), rule, edgeMap.getOfPort());	
			execute(edgeMap.getNextNodePath());
		}else{
			System.out.println(nodePath.getDataPathId()+" "+6);
		}
	
	}
	
	public void write(HashMap<Integer, TreePath> treesMap){
		System.out.println(0);
		for (Iterator<Integer> iterator = treesMap.keySet().iterator(); iterator.hasNext();) {
			Integer sessionID = (Integer) iterator.next();
			
			TreePath treePath = treesMap.get(sessionID);
			execute(treePath.getNodePaths().get(0));
			System.out.println(1);
		}
	}
	
	public void  updateFlowPathsTest2(List<MultipathSession> multipathSessions, HashMap<String, CandidatePath> bestPaths){
		
		
		HashMap<Integer, TreePath> treesMap = new HashMap<>();
		
		for (Iterator<MultipathSession> iterator1 = multipathSessions.iterator(); iterator1.hasNext();) {
			MultipathSession ms = (MultipathSession) iterator1.next();
			Session session = ms.getSessionMultiUser();
			
			
			CandidatePath bp = bestPaths.get(ms.getPathIndex());

			
			System.out.println(bp.getLinks());
			
			TreePath treePath;
			
			if(treesMap.containsKey(session.getId())){
				treePath = treesMap.get(session.getId());
			}else{
				treePath = new TreePath();
			}
			
			NodePath nodePathprev = null;
			NodePath nodePath = null;
			for (int i = 0; i < bp.getPath().size(); i++) {
				
				if(i%2!=0 && i != bp.getPath().size()-1) continue;
				
				NodePortTuple npt = bp.getPath().get(i);
//				System.out.println(treePath+ " "+session.getId());
				if(treePath.containsNode(npt)){
					
					nodePath = treePath.getNodePathByRef(npt.getNodeId());
					
					if(nodePath.containsConnection(npt.getPortId())){
						EdgeMap edgeMap = nodePath.getConnection(npt.getPortId());
						edgeMap.addClientIfNotExists(ms.getUserSession());
					}else{
						EdgeMap edgeMap = new EdgeMap();
						edgeMap.setOfPort(npt.getPortId());
						edgeMap.addClient(ms.getUserSession());
						nodePath.addConnectionIfNotExists(edgeMap);
						if(nodePathprev != null){
							nodePathprev.getLastConnection().setNextNodePath(nodePath);
						}
					}
//					System.out.println(nodePath.getConections().size());
				}else{
					
					nodePath = new NodePath(npt.getNodeId(), session.getId());
					
					EdgeMap edgeMap = new EdgeMap();
					edgeMap.setOfPort(npt.getPortId());
					edgeMap.addClient(ms.getUserSession());
					nodePath.addConnectionIfNotExists(edgeMap);
					if(nodePathprev != null){
						nodePathprev.getLastConnection().setNextNodePath(nodePath);
					}
					treePath.addNodePath(nodePath);
				}
				nodePathprev = nodePath;	
				
			}
			
			if(!treesMap.containsKey(session.getId())){
				treesMap.put(session.getId(), treePath);
			}
		}
		
		System.out.println(treesMap.toString());
		write(treesMap);
	}
	
	
	
	public void  updateFlowPathsTest(List<MultipathSession> multipathSessions, HashMap<String, CandidatePath> bestPaths){
		
		HashMap<Session, HashMap<DatapathId, List<UserSession>>> branchsModel = new HashMap<>();
		
		for (Iterator<MultipathSession> iterator1 = multipathSessions.iterator(); iterator1.hasNext();) {
			MultipathSession ms1 = (MultipathSession) iterator1.next();
			Session session = ms1.getSessionMultiUser();
			CandidatePath bp1 = bestPaths.get(ms1.getPathIndex());
			
			
			for (Iterator<MultipathSession> iterator2 = multipathSessions.iterator(); iterator2.hasNext();) {
				MultipathSession ms2 = (MultipathSession) iterator2.next();
				
				//Se ambos forem iguais vai para próxima iteração
				if(ms1.equals(ms2)) continue;
				CandidatePath bp2 = bestPaths.get(ms2.getPathIndex());
				
				//Se estão na mesma sessão
				if(session.equals(ms2.getSessionMultiUser())){
					
					
					HashMap<DatapathId, List<UserSession>> branch;

					//Se ainda não existe a sessão no modelo
					if(!branchsModel.containsKey(session)){
						
						branch = new HashMap<>();
						
					}else{
						
						branch = branchsModel.get(session);
						
					}
					
					//Pega o último nos dois grupos de caminhos
					for (int i = bp2.getSwitchesRefs().size()-1; i >= 0; i--) {
						
						//Pega cada switch de trás pra frente do caminho-candidato-2
						DatapathId dataPathId = bp2.getSwitchesRefs().get(i);
						
						//Se o switch tiver no caminho-candidato-1
						if(bp1.getSwitchesRefs().contains(dataPathId)){
							
							//E ainda não tiver cadastrado como chave
							if(!branchsModel.containsKey(session) || !branchsModel.get(session).containsKey(dataPathId)){
								
//								System.out.println(1);
								
								//Adiciona os dois usuários na lista
								List<UserSession> users = new ArrayList<>();
								users.add(ms1.getUserSession());
								users.add(ms2.getUserSession());
								//E adiciona em um branch, onde o switch apontará para os dois usuários
								branch.put(dataPathId, users);
								
//								if(!branchsModel.containsKey(session)){
								branchsModel.put(session, branch);
//								}else{
//									branchsModel.get(session).;
//								}
								
								
							}else if(branchsModel.get(session).containsKey(dataPathId)){
//								System.out.println(2);
								//Se ainda não tiver o usuário cadastrado
								if(!branchsModel.get(session).get(dataPathId).contains(ms2.getUserSession())){
									branchsModel.get(session).get(dataPathId).add(ms2.getUserSession());
									
								}		
							}
							
							break;
						}
					}
				}
				
				
				
				
				//pegaUltimoSwitch em que pb1 e bp2 se interceptam
				//o switch resultado é salvo num hashmap como chave para os dois
				//se a chave já existe bp2 é adicionado a lista (caso ele não esteja) e adicionado a uma lista de não titular
				
				//38 -> h1 e h2
				//32 -> h1 e h3 e h4
				//14 -> h3 e h4
				
				
//				HashMap<DatapathId, List<UserSession>> branchsModel;
				
				//cada sessão está associada a um mapeamento de (
					//cada switche mapeia uma lista de usuários
				
				//)
				//< Sessão-1, <Switch-s1 , 192.168.2.100, 192.168.2.110>>
				//o ID da sessão determinará  o ID do grupo
//				HashMap<Session, HashMap<DatapathId, List<UserSession>>> branchsModel;
				// o numero de seesões determianrá o numero máximo de fluxos concorrentes
				
				//<Switch> <Cliente Titular, Lista de Demais>
			}
			
		}
//		System.out.println(branchsModel.toString());
	}
	
	
	public void  updateFlowPaths(List<MultipathSession> multipathSessions, HashMap<String, CandidatePath> bestPaths){
		
		updateFlowPathsTest2(multipathSessions, bestPaths);
		
//		try{
//			
//			boolean hasUpdate = false;
//			
//			/*Controla se houve alterações*/
//			List<String> keyEquals = new ArrayList<>();
//			for (String keyPath : bestPaths.keySet()) {
//				for (String keyoldPath : oldBestPaths.keySet()) {
//					if(keyPath.equals(keyoldPath)){
//						if(bestPaths.get(keyPath).getPath().equals(oldBestPaths.get(keyPath).getPath())){
//							keyEquals.add(keyPath);
//						}
//					}
//				}
//			}
//			
//			for (Iterator<MultipathSession> iterator = multipathSessions.iterator(); iterator.hasNext();) {
//				MultipathSession ms = (MultipathSession) iterator.next();
//				
//			
//				
//				if(keyEquals.contains(ms.getPathIndex())) continue;
//				
//				/*Deleta os gravados anteriormente*/
//				CandidatePath oldbp = oldBestPaths.get(ms.getPathIndex());
//				if(oldbp != null){
//					for (int i = 0; i < oldbp.getPath().size(); i+=2) {
//						NodePortTuple npt = oldbp.getPath().get(i);
//						
//						DatapathId dpi = npt.getNodeId();
//						OFPort ofp = npt.getPortId();
//
//						/*Reagra para pacotes do servidor para o cliente pelo IP*/
//						Rule matchIpFLow = new Rule(ms.getServerSession().getIp(), ms.getUserSession().getSrcIp().toString());
//						
//						try{
//							deleteFlow(dpi, matchIpFLow, ofp);
//							hasUpdate = true;
//						}catch (Exception e) {
//							System.out.print("[ExecutorPathFlowSDN] FLOW_MOD DELETE: Error to delete flow!");
//						}
//					}
//				}
//				
//				/*Cria os novos*/
//				CandidatePath bp = bestPaths.get(ms.getPathIndex());
//
//				if(bp != null){
//					
//					for (int i = 0; i < bp.getPath().size(); i+=2) {
//						NodePortTuple npt = bp.getPath().get(i);
//						DatapathId dpi = npt.getNodeId();
//						OFPort ofp = npt.getPortId();
//						
//						/*Reagra para pacotes do servidor para o cliente pelo IP*/
//						Rule matchIpFLow = new Rule(ms.getServerSession().getIp(), ms.getUserSession().getSrcIp().toString());
//						
//						
//						try{
//							createFlow(dpi, matchIpFLow, ofp);
//							hasUpdate = true;
//						}catch (Exception e) {
//							System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Error to create flow!");
//						}
//					}
//				}
//				
//				
//			}
//			
//			oldBestPaths.clear();
//			oldBestPaths.putAll(bestPaths);
//			if(hasUpdate){
//				System.out.println("[ExecutorPathFlowSDN] Update Flows");
//			}else{
//				System.out.println("[ExecutorPathFlowSDN] No changes in Flows");
//			}
//			
//			
//		}catch (Exception error){
//			oldBestPaths.clear();
//			System.out.println("[ExecutorPathFlowSDN] Not has possible update Flows");
//		}

	}

}
