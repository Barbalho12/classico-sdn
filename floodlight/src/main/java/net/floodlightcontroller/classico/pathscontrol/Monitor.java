package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.routing.Path;
import net.floodlightcontroller.classico.CLASSICOModule;
import net.floodlightcontroller.classico.sessionmanager.MultiuserSessionControl;
import net.floodlightcontroller.classico.sessionmanager.UserSession;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.routing.IRoutingService;
import net.floodlightcontroller.routing.IRoutingService.PATH_METRIC;
import net.floodlightcontroller.statistics.IStatisticsService;

public class Monitor extends Thread{
	
	private final int REFRESH_INTERVAL_SECONDS = 5;
	
	private IRoutingService routingService;
	private ILinkDiscoveryService linkDiscoveryService;
	private IStatisticsService statisticsService;
	private MultiuserSessionControl tableSM;
	protected IOFSwitchService switchService;
	CLASSICOModule classicoModuleREF;
	
	public Monitor(CLASSICOModule classicoModuleREF, MultiuserSessionControl tableSM, IRoutingService routingService, IOFSwitchService switchService, 
			ILinkDiscoveryService linkDiscoveryService, IStatisticsService statisticsService){
		
		this.classicoModuleREF = classicoModuleREF;
		this.tableSM = tableSM;
		this.routingService = routingService;
		this.statisticsService = statisticsService;
		this.linkDiscoveryService = linkDiscoveryService;
		this.switchService = switchService;
	}
	
	
	private void updatePathsInformations(List<CandidatePath> candidatePaths){
		
		for (CandidatePath path : candidatePaths) {
			
			long higherBWC = 0;
			
			for (Iterator<Link> iterator = path.getLinks().iterator(); iterator.hasNext();) {
				Link link = (Link) iterator.next();
				if(statisticsService.getBandwidthConsumption(link.getSrc(), link.getSrcPort()) != null){
					long bwc = statisticsService.getBandwidthConsumption(link.getDst(), link.getDstPort()).getBitsPerSecondRx().getValue();
//					System.out.println(link.getDst()+", "+link.getSrc()+" -> "+bwc);
					if(higherBWC < bwc){
						higherBWC = bwc;
					}
				}
			}
//			System.out.println("----------------");
			path.setBandwidthConsumption(higherBWC);
		}
//		System.out.println("===" +higherBWC);
	}
	
	public synchronized List<CandidatePath> calculatePaths(UserSession userSession, DatapathId src,DatapathId dst, PATH_METRIC metric){
		if(routingService != null){
			
			if(metric != null){
				routingService.setPathMetric(metric);
			}else{
				routingService.setPathMetric(PATH_METRIC.HOPCOUNT);
			}
			
			List<Path> paths = routingService.getPathsFast(src, dst, 100);
			
			List<CandidatePath> candidatePaths = new ArrayList<>();
			for (Path path : paths) {
				candidatePaths.add(new CandidatePath(userSession, path));
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
				link.setLatency(linkDiscoveryService.getLinkInfo(link).getLatencyHistory().getLast());
//				link.setLatency(linkDiscoveryService.getLinkInfo(link).getCurrentLatency());
//				System.out.println(linkDiscoveryService.getLinkInfo(link).getLatencyHistory().toString());
			
				
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

	public void update(List<MultipathSession> multipathSessions){
		
		/*Percorre cada grupo de caminhos candidato da tabela (Servidor -> Cliente)*/
		for (Iterator<MultipathSession> iterator = multipathSessions.iterator(); iterator.hasNext();) {
			MultipathSession ms = (MultipathSession) iterator.next();
			
			/*Recalcula os caminhos e informações adicionais do servidor até o cliente*/
			List<CandidatePath> newPaths = calculatePaths(ms.getUserSession(), ms.getServerSession().getDatapathId(), ms.getUserSession().getDatapathId(), null);
			ms.setPaths(newPaths);
		}
		
		if(multipathSessions.isEmpty()){
//			System.out.println("[MONITOR] There are no candidate paths to update");
		}else{
//			System.out.println("[MONITOR] Update Candidate Paths");
		}
		
	}
	
	public void alertUpdate(){
		this.interrupt();
//		long startTime,duration;
//		startTime = System.currentTimeMillis();
//		update(tableSM.getMultipathSessions());
//		if(tableSM.getMultipathSessions() != null){
//			classicoModuleREF.notifyUpdates(tableSM.getMultipathSessions());
//		}
//		duration = System.currentTimeMillis() - startTime; 
//		System.out.println("[MONITOR] Duration time: "+duration+"ms");
	}
//
	@Override
	public void run() {
		long startTime,duration;
		while(true){
			System.out.println("-----------------");

			sleepSeconds(REFRESH_INTERVAL_SECONDS);
			startTime = System.currentTimeMillis();
			update(tableSM.getMultipathSessions());
			if(tableSM.getMultipathSessions() != null){
				classicoModuleREF.notifyUpdates(tableSM.getMultipathSessions());
			}
			duration = System.currentTimeMillis() - startTime; 
			System.out.println("[MONITOR] Duration time: "+duration+"ms");
			System.out.println("-----------------");
		}
	}
	
	private void sleepSeconds(long seconds){
		try {
			Thread.sleep(seconds*1000);
			
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
