package net.floodlightcontroller.mactracker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.routing.Path;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.routing.IRoutingService;
import net.floodlightcontroller.routing.IRoutingService.PATH_METRIC;
import net.floodlightcontroller.statistics.IStatisticsService;

public class Monitor extends Thread{
	
	private IRoutingService routingService;
	private ILinkDiscoveryService linkDiscoveryService;
	private IStatisticsService statisticsService;
	private TableSessionMultiuser tableSM;
	
	public Monitor(TableSessionMultiuser tableSM, IRoutingService routingService, ILinkDiscoveryService linkDiscoveryService, IStatisticsService statisticsService){
		this.tableSM = tableSM;
		this.routingService = routingService;
		this.statisticsService = statisticsService;
		this.linkDiscoveryService = linkDiscoveryService;
	}
	
	
	public void updatePathsInformations(List<CandidatePath> candidatePaths){
		
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


	@Override
	public void run() {
		while(true){
			sleepSeconds(10);
			for (Iterator<MultipathSession> iterator = tableSM.getMultipathSessions().iterator(); iterator.hasNext();) {
				MultipathSession ms = (MultipathSession) iterator.next();
				List<CandidatePath> newPaths = calculatePaths(ms.getServerSession().getDatapathId(), 
						ms.getUserSession().getDatapathId(), null);
				ms.setPaths(newPaths);
			}
			if(tableSM.getMultipathSessions().isEmpty()){
				System.out.println("------There are no candidate paths to update------");
			}else{
				System.out.println("------Update Candidate Paths------");
				tableSM.show();
			}
			
		}
	}
	
	private void sleepSeconds(long seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
