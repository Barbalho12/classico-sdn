package net.floodlightcontroller.mactracker;

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

import net.floodlightcontroller.routing.Path;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.types.NodePortTuple;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.routing.IRoutingService;
import net.floodlightcontroller.routing.IRoutingService.PATH_METRIC;
import net.floodlightcontroller.statistics.IStatisticsService;
import net.floodlightcontroller.util.FlowModUtils;

public class Monitor extends Thread{
	
	private IRoutingService routingService;
	private ILinkDiscoveryService linkDiscoveryService;
	private IStatisticsService statisticsService;
	private TableSessionMultiuser tableSM;
	protected IOFSwitchService switchService;
	
	private HashMap<String, CandidatePath> bestPaths;
	private HashMap<String, CandidatePath> oldBestPaths;
	
	public Monitor(TableSessionMultiuser tableSM, IRoutingService routingService, IOFSwitchService switchService, 
			ILinkDiscoveryService linkDiscoveryService, IStatisticsService statisticsService){
		this.tableSM = tableSM;
		this.routingService = routingService;
		this.statisticsService = statisticsService;
		this.linkDiscoveryService = linkDiscoveryService;
		this.oldBestPaths = new HashMap<>();
		this.bestPaths = new HashMap<>();
		this.switchService = switchService;
	}
	
	
	private void updatePathsInformations(List<CandidatePath> candidatePaths){
		
		for (CandidatePath path : candidatePaths) {
			
			long higherBWC = 0;
			
			for (Iterator<Link> iterator = path.getLinks().iterator(); iterator.hasNext();) {
				Link link = (Link) iterator.next();
				if(statisticsService.getBandwidthConsumption(link.getSrc(), link.getSrcPort()) != null){
					long bwc = statisticsService.getBandwidthConsumption(link.getSrc(), link.getSrcPort()).getBitsPerSecondRx().getValue();
					if(higherBWC < bwc){
						higherBWC = bwc;
					}
				}
			}
			path.setBandwidthConsumption(higherBWC);
		}
	}
	
	public synchronized List<CandidatePath> calculatePaths(DatapathId src,DatapathId dst, PATH_METRIC metric){
		if(routingService != null){
			if(metric != null){
				routingService.setPathMetric(metric);
			}else{
				routingService.setPathMetric(PATH_METRIC.HOPCOUNT);
			}
			
			
			List<Path> paths = routingService.getPathsSlow(src, dst, 100);
			
			List<CandidatePath> candidatePaths = new ArrayList<>();
			for (Path path : paths) {
				candidatePaths.add(new CandidatePath(path));
			}
			insertLinksInPaths(candidatePaths);
			updatePathsInformations(candidatePaths);
			return candidatePaths;
		}else{
			return null;
		}
	}
	
	private List<CandidatePath> insertLinksInPaths(List<CandidatePath> candidatePaths){
		for (CandidatePath path : candidatePaths) {
			path.setLinks(new ArrayList<Link>());

			for (Iterator<Link> iteratorLink = linkDiscoveryService.getLinks().keySet().iterator(); iteratorLink.hasNext();) {
				Link link = (Link) iteratorLink.next();

				for(int i = 0; i < path.getPath().size()-1; i+=2){
					DatapathId d1 = path.getPath().get(i).getNodeId();
					DatapathId d2 = path.getPath().get(i+1).getNodeId();
					
					if(d1.equals(link.getSrc()) && d2.equals(link.getDst())){
						path.getLinks().add(link);
						break;
					}
				}
			}
		}
		return candidatePaths;
	}
	
	private CandidatePath calculateBestPath(List<CandidatePath> candidatePaths, PATH_METRIC metric){
		CandidatePath bestPath = null;
		for (int i = 1; i < candidatePaths.size(); i++) {
			if(bestPath == null){
				bestPath = candidatePaths.get(0);
			}
			if(bestPath.getBandwidthConsumption() > candidatePaths.get(i).getBandwidthConsumption()){
				bestPath = candidatePaths.get(i);
			}
		}
		return bestPath;
	}
	
	
	
	
	public void update(){
		
//		HashMap<List<CandidatePath>, CandidatePath> bestPaths = new HashMap<>();
		
		/*Percorre cada grupo de caminhos candidato da tabela (Servidor -> Cliente)*/
		for (Iterator<MultipathSession> iterator = tableSM.getMultipathSessions().iterator(); iterator.hasNext();) {
			MultipathSession ms = (MultipathSession) iterator.next();
			
//			System.out.println(ms.hashCode());
			
			/*Recalcula os caminhos e informações adicionais do servidor até o cliente*/
			List<CandidatePath> newPaths = calculatePaths(ms.getServerSession().getDatapathId(), ms.getUserSession().getDatapathId(), null);
			ms.setPaths(newPaths);
			
			/*Atualiza o conjunto de melhores caminhos*/
			if(!bestPaths.containsKey(newPaths)){
				bestPaths.put(ms.getPathIndex(), calculateBestPath(newPaths, null));
			}else{
				bestPaths.replace(ms.getPathIndex(), calculateBestPath(newPaths, null));
			}
			
			System.out.println(bestPaths.keySet().toString());
			System.out.println(bestPaths.values());
		}
		
		if(tableSM.getMultipathSessions().isEmpty()){
			System.out.println("------There are no candidate paths to update------");
		}else{
			System.out.println("------Update Candidate Paths------");
			tableSM.show();
		}
	}
	
	public void deleteFlow(DatapathId datapathid, Rule rule, OFPort ofPort){

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
			        .setExact(MatchField.IP_PROTO, IpProtocol.TCP)
			       
			        .build())
			    
			    .build();
		iofs.write(f);
		
		System.out.println("[FLOW_MOD] DELETE");
	}
	
	public void createFlow(DatapathId datapathid, Rule rule, OFPort ofPort){
		
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
		System.out.println("[FLOW_MOD] ADD");
	}
	
	private void  updateFlowPaths(){
		
		for (Iterator<MultipathSession> iterator = tableSM.getMultipathSessions().iterator(); iterator.hasNext();) {
			MultipathSession ms = (MultipathSession) iterator.next();
			
			
			/*Deleta os gravados anteriormente*/
			CandidatePath oldbp = oldBestPaths.get(ms.getPathIndex());
			if(oldbp != null){
				for (int i = 0; i < oldbp.getPath().size(); i+=2) {
					NodePortTuple npt = oldbp.getPath().get(i);
					
					DatapathId dpi = npt.getNodeId();
					OFPort ofp = npt.getPortId();
					System.out.println("Switch:" + dpi.toString());
					System.out.println("Port:" + ofp.getPortNumber());
					/*Reagra para pacotes do servidor para o cliente pelo IP*/
					Rule matchIpFLow = new Rule(ms.getServerSession().getIp(), ms.getUserSession().getSrcIp().toString());
					
					try{
						deleteFlow(dpi, matchIpFLow, ofp);
					}catch (Exception e) {
						System.out.println("[ERROR_FLOW_MOD] Error to delete flow!");
					}
				}
			}
			
			/*Cria os novos*/
			CandidatePath bp = bestPaths.get(ms.getPathIndex());
			if(bp != null){
				
				for (int i = 0; i < bp.getPath().size(); i+=2) {
					NodePortTuple npt = bp.getPath().get(i);
					DatapathId dpi = npt.getNodeId();
					OFPort ofp = npt.getPortId();
					
					System.out.println("Switch:" + dpi.toString());
					System.out.println("Port:" + ofp.getPortNumber());
					
					/*Reagra para pacotes do servidor para o cliente pelo IP*/
					Rule matchIpFLow = new Rule(ms.getServerSession().getIp(), ms.getUserSession().getSrcIp().toString());
					
					
					try{
						createFlow(dpi, matchIpFLow, ofp);
					}catch (Exception e) {
						System.out.println("[ERROR_FLOW_MOD] Error to create flow!");
					}
					
				}
//				for (Iterator<NodePortTuple> iterator1 = bp.getPath().iterator(); iterator1.hasNext();) {
//					NodePortTuple npt = iterator1.next();
//					
//					DatapathId dpi = npt.getNodeId();
//					OFPort ofp = npt.getPortId();
//					
//					System.out.println("Switch:" + dpi.toString());
//					System.out.println("Port:" + ofp.getPortNumber());
//					
//					/*Reagra para pacotes do servidor para o cliente pelo IP*/
//					Rule matchIpFLow = new Rule(ms.getServerSession().getIp(), ms.getUserSession().getSrcIp().toString());
//					
//					
//					try{
//						createFlow(dpi, matchIpFLow, ofp);
//					}catch (Exception e) {
//						System.out.println("[ERROR_FLOW_MOD] Error to create flow!");
//					}
//				}
				
			}
			
			
		}
		oldBestPaths.clear();
		oldBestPaths.putAll(bestPaths);
		System.out.println("------Update Flows------");
	}


	@Override
	public void run() {
		while(true){
			sleepSeconds(20);
			update();
			updateFlowPaths();
		}
	}
	
	private void sleepSeconds(long seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
