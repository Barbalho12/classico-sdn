package net.floodlightcontroller.classico.pathscontrol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.floodlightcontroller.routing.IRoutingService.PATH_METRIC;

public class MulticriteriaPathSelection {
	


	public MulticriteriaPathSelection(){
		
	}
	
	public HashMap<String, CandidatePath> calculateBestPaths(List<MultipathSession> multipathSessions){
		
		HashMap<String, CandidatePath> bestPaths = new HashMap<>();
		
		/*Percorre cada grupo de caminhos candidato da tabela (Servidor -> Cliente)*/
		for (Iterator<MultipathSession> iterator = multipathSessions.iterator(); iterator.hasNext();) {
			MultipathSession ms = (MultipathSession) iterator.next();
			
			if(!bestPaths.containsKey(ms.getPathIndex())){
				bestPaths.put(ms.getPathIndex(), calculateBestPath(ms.getPaths(), null));
			}/*else{
				bestPaths.replace(ms.getPathIndex(), calculateBestPath(ms.getPaths(), null));
			}*/
		}
		
		return bestPaths;
		
	}
	
	private CandidatePath calculateBestPath(List<CandidatePath> candidatePaths, PATH_METRIC metric){
		CandidatePath bestPath = null;
		for (int i = 1; i < candidatePaths.size(); i++) {
			if(bestPath == null){
				bestPath = candidatePaths.get(0);
				System.out.println(candidatePaths.get(0).getStringResumePath()+" + BW: "+candidatePaths.get(0).getBandwidthConsumption() +
						"    "+" + Latency: "+candidatePaths.get(0).getLatency().getValue());
			}
			if(bestPath.getBandwidthConsumption() > candidatePaths.get(i).getBandwidthConsumption()){
				bestPath = candidatePaths.get(i);
			}

			System.out.println(candidatePaths.get(i).getStringResumePath()+" + BW: "+candidatePaths.get(i).getBandwidthConsumption() +
					"    "+" + Latency: "+candidatePaths.get(i).getLatency().getValue());
		}
		if(bestPath!= null){
			System.out.println("------++++BEST++++--------");
			System.out.println(bestPath.getStringResumePath()+"   BW: "+bestPath.getBandwidthConsumption()+
					"     Latency: "+bestPath.getLatency().getValue());
//			System.out.println("----------------------");
		}
		
		return bestPath;
	}

}
