package net.floodlightcontroller.mactracker;

import java.util.List;

import org.projectfloodlight.openflow.types.DatapathId;

import net.floodlightcontroller.routing.Path;
import net.floodlightcontroller.routing.IRoutingService;
import net.floodlightcontroller.routing.IRoutingService.PATH_METRIC;

public class Monitor {
	
	private IRoutingService routingService;
	
	public Monitor(IRoutingService routingService){
		this.routingService = routingService;
	}
	
	public List<Path> calculatePaths(DatapathId src,DatapathId dst, PATH_METRIC metric){
		if(routingService != null){
			if(metric != null){
				routingService.setPathMetric(metric);
			}else{
				routingService.setPathMetric(PATH_METRIC.HOPCOUNT);
			}
			
			
			List<Path> paths = routingService.getPathsSlow(src, dst, 100);
			return paths;
			
		}else{
			return null;
		}
		
	}

}
