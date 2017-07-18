package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowDelete;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.classico.sessionmanager.Rule;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.types.NodePortTuple;
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
	
	public void  updateFlowPaths(List<MultipathSession> multipathSessions, HashMap<String, CandidatePath> bestPaths){
		
		try{
			
			boolean hasUpdate = false;
			
			/*Controla se houve alterações*/
			List<String> keyEquals = new ArrayList<>();
			for (String keyPath : bestPaths.keySet()) {
				for (String keyoldPath : oldBestPaths.keySet()) {
					if(keyPath.equals(keyoldPath)){
						if(bestPaths.get(keyPath).getPath().equals(oldBestPaths.get(keyPath).getPath())){
							keyEquals.add(keyPath);
						}
					}
				}
			}
			
			for (Iterator<MultipathSession> iterator = multipathSessions.iterator(); iterator.hasNext();) {
				MultipathSession ms = (MultipathSession) iterator.next();
				
				
				if(keyEquals.contains(ms.getPathIndex())) continue;
				
				/*Deleta os gravados anteriormente*/
				CandidatePath oldbp = oldBestPaths.get(ms.getPathIndex());
				if(oldbp != null){
					for (int i = 0; i < oldbp.getPath().size(); i+=2) {
						NodePortTuple npt = oldbp.getPath().get(i);
						
						DatapathId dpi = npt.getNodeId();
						OFPort ofp = npt.getPortId();
//						System.out.print("[ExecutorPathFlowSDN] FLOW_MOD DELETE: Switch:" + dpi.toString() + 
//								", Port:" + ofp.getPortNumber());
						/*Reagra para pacotes do servidor para o cliente pelo IP*/
						Rule matchIpFLow = new Rule(ms.getServerSession().getIp(), ms.getUserSession().getSrcIp().toString());
						
						try{
							deleteFlow(dpi, matchIpFLow, ofp);
							hasUpdate = true;
						}catch (Exception e) {
							System.out.print("[ExecutorPathFlowSDN] FLOW_MOD DELETE: Error to delete flow!");
						}
					}
				}
				
				/*Cria os novos*/
				CandidatePath bp = bestPaths.get(ms.getPathIndex());
//				System.out.println(bestPaths.size());
				if(bp != null){
					
					for (int i = 0; i < bp.getPath().size(); i+=2) {
						NodePortTuple npt = bp.getPath().get(i);
						DatapathId dpi = npt.getNodeId();
						OFPort ofp = npt.getPortId();
						
//						System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Switch:" + dpi.toString() + 
//								", Port:" + ofp.getPortNumber());
						
						/*Reagra para pacotes do servidor para o cliente pelo IP*/
						Rule matchIpFLow = new Rule(ms.getServerSession().getIp(), ms.getUserSession().getSrcIp().toString());
						
						
						try{
							createFlow(dpi, matchIpFLow, ofp);
							hasUpdate = true;
						}catch (Exception e) {
							System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Error to create flow!");
						}
					}
				}
				
				
			}
			
			oldBestPaths.clear();
			oldBestPaths.putAll(bestPaths);
			if(hasUpdate){
				System.out.println("[ExecutorPathFlowSDN] Update Flows");
			}else{
				System.out.println("[ExecutorPathFlowSDN] No changes in Flows");
			}
			
			
		}catch (Exception error){
			oldBestPaths.clear();
			System.out.println("[ExecutorPathFlowSDN] Not has possible update Flows");
		}

	}

}
