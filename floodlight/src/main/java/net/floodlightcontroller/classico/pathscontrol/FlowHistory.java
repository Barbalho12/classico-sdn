package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.floodlightcontroller.core.IOFSwitch;


public class FlowHistory {
	
	List<FlowMod> flowModHistory;
	
	public FlowHistory(){
		flowModHistory = new ArrayList<FlowMod>();
	}
	
	FlowMod getFlowMod(FlowMod fm){
		return flowModHistory.get(flowModHistory.indexOf(fm));
	}
	
	public void add(FlowMod fm){
		fm.setMarked(true);
		flowModHistory.add(fm);
	}
	
	boolean contains(FlowMod flowMod){
		if(flowModHistory.contains(flowMod)){
			return true;
		}else{
			return false;
		}
	}
	
	public void mark(FlowMod flowMod) {
		int index = flowModHistory.indexOf(flowMod);
		flowModHistory.get(index).setMarked(true);;
	}
	
	public void unmarkAll() {
		for(FlowMod fm : flowModHistory){
			fm.setMarked(false);
		}
	}
	
	
	public void markAll() {
		for(FlowMod fm : flowModHistory){
			fm.setMarked(true);
		}
	}
	
	public void deleteUnusedFlows() {
		for (Iterator<FlowMod> iterator = flowModHistory.iterator(); iterator.hasNext();) {
			FlowMod flowMod = (FlowMod) iterator.next();
			if(!flowMod.isMarked()){
				flowMod.deleteFlow();
				iterator.remove();
			}
			
		}
	}
	
	public void markAsUnchanged(int sessionId, IOFSwitch iofs) {
		for (FlowMod fm : flowModHistory) {
			if (fm.getSessionID() == sessionId && fm.getDatapathId().equals(iofs.getId())) {
				fm.setMarked(true);
			}
		}
	}


}
