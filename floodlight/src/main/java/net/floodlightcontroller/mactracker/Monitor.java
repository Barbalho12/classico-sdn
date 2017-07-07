package net.floodlightcontroller.mactracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.routing.Path;
import net.floodlightcontroller.core.types.NodePortTuple;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.routing.IRoutingService;
import net.floodlightcontroller.routing.IRoutingService.PATH_METRIC;
import net.floodlightcontroller.statistics.IStatisticsService;

public class Monitor {
	
	private IRoutingService routingService;
	private ILinkDiscoveryService linkDiscoveryService;
	private IStatisticsService statisticsService;
	
	public Monitor(IRoutingService routingService, ILinkDiscoveryService linkDiscoveryService, IStatisticsService statisticsService){
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
	
	public List<CandidatePath> calculatePaths(DatapathId src,DatapathId dst, PATH_METRIC metric){
		if(routingService != null){
			if(metric != null){
				routingService.setPathMetric(metric);
			}else{
				routingService.setPathMetric(PATH_METRIC.HOPCOUNT);
			}
			
			
			List<Path> paths = routingService.getPathsSlow(src, dst, 100);
//			System.out.println(paths.get(0).getHopCount());
//			System.out.println(paths.get(0).getLatency());
			
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
		System.out.println(candidatePaths.get(0).getLinks().size());
		return candidatePaths;
	}
}
