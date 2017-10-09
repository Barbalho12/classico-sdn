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
		
		long maxLatency = 0;
		long maxBw = 0;
		
		//Get max BW value and latency value 
		for (int i = 0; i < candidatePaths.size(); i++) {
			if(maxLatency <  candidatePaths.get(i).getLatency().getValue()){
				maxLatency = candidatePaths.get(i).getLatency().getValue();
			}
			if(maxBw < candidatePaths.get(i).getBandwidthConsumption()){
				maxBw = candidatePaths.get(i).getBandwidthConsumption();
			}	
		}
//		System.out.println(maxLatency+", "+maxBw);
		
		//Evitar divisão por zero
		double factor = 0.000001;
		
		for (int i = 0; i < candidatePaths.size(); i++) {
			double scoreBW = candidatePaths.get(i).getBandwidthConsumption()/(maxBw*1.0+factor);
			double scoreLatency = candidatePaths.get(i).getLatency().getValue()/(maxLatency*1.0+factor);
//			System.out.println(scoreBW+" , "+scoreLatency);
			double score = 0.5*(1.0 - scoreBW) + 0.5*(1.0 - scoreLatency);
			candidatePaths.get(i).setScore(score);
		}
		
		System.out.println("----------Candidate Paths------------");
		for (int i = 1; i < candidatePaths.size(); i++) {
			
			if(bestPath == null){
				bestPath = candidatePaths.get(0);
				System.out.println(candidatePaths.get(0).getStringResumePath()+
						"\tBW: "+candidatePaths.get(0).getBandwidthConsumption()+" b/s" +
						"\tLatency: "+candidatePaths.get(0).getLatency().getValue()+" ms" +
						"\tscore: "+candidatePaths.get(0).getScore());
			}
			if(bestPath.getScore() < candidatePaths.get(i).getScore()){
				bestPath = candidatePaths.get(i);
			}

			System.out.println(candidatePaths.get(i).getStringResumePath()+
					"\tBW: "+candidatePaths.get(i).getBandwidthConsumption()+" b/s" +
					"\tLatency: "+candidatePaths.get(i).getLatency().getValue()+" ms"+
					"\tScore: "+candidatePaths.get(i).getScore());
		}
		if(bestPath!= null){
			System.out.println("----------Best Path------------");
			System.out.println(bestPath.getStringResumePath()+"\tBW: "+bestPath.getBandwidthConsumption()+" b/s"+
					"\tLatency: "+bestPath.getLatency().getValue()+" ms"+"\tScore: "+bestPath.getScore());
			System.out.println("----------------------");
		}
		
//		System.out.println("------++++Candidate Paths++++--------");
//		for (int i = 1; i < candidatePaths.size(); i++) {
//			
//			if(bestPath == null){
//				bestPath = candidatePaths.get(0);
//				System.out.println(candidatePaths.get(0).getStringResumePath()+" + BW: "+candidatePaths.get(0).getBandwidthConsumption()+" b/s" +
//						"    "+" + Latency: "+candidatePaths.get(0).getLatency().getValue()+" ms");
//			}
//			if(bestPath.getBandwidthConsumption() > candidatePaths.get(i).getBandwidthConsumption()){
//				bestPath = candidatePaths.get(i);
//			}
//
//			System.out.println(candidatePaths.get(i).getStringResumePath()+" + BW: "+candidatePaths.get(i).getBandwidthConsumption()+" b/s" +
//					"    "+" + Latency: "+candidatePaths.get(i).getLatency().getValue()+" ms");
//		}
//		if(bestPath!= null){
//			System.out.println("------++++Best Path++++--------");
//			System.out.println(bestPath.getStringResumePath()+"   BW: "+bestPath.getBandwidthConsumption()+" b/s"+
//					"     Latency: "+bestPath.getLatency().getValue()+" ms");
//			System.out.println("----------------------");
//		}
		
		return bestPath;
	}

}
