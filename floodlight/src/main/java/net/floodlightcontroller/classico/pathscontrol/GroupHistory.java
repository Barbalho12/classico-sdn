package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.floodlightcontroller.core.IOFSwitch;

public class GroupHistory {
	List<GroupMod> groupHistory;
	
	public GroupHistory(){
		groupHistory = new ArrayList<GroupMod>();
	}
	
	void add(GroupMod groupMod){
		groupMod.setMarked(true);
		groupHistory.add(groupMod);
	}
	
//	void set(int i, GroupMod groupMod){
//		
//	}
	
	public void markAsUnchanged(int sessionId, IOFSwitch iofs) {
		for (GroupMod gm : groupHistory) {
			if (gm.getId() == sessionId && gm.getIof_switch().getId().equals(iofs.getId())) {
				gm.setMarked(true);
			}
		}
	}
	
	boolean contains(GroupMod groupMod){
		if(groupHistory.contains(groupMod)){
			return true;
		}else{
			return false;
		}
	}

	public void mark(GroupMod groupMod) {
		int index = groupHistory.indexOf(groupMod);
		groupHistory.get(index).setMarked(true);;
	}
	
	public void unmarkAll() {
		for(GroupMod gm : groupHistory){
			gm.setMarked(false);
		}
	}
	
	public void deleteUnusedGroup() {
		for (Iterator<GroupMod> iterator = groupHistory.iterator(); iterator.hasNext();) {
			GroupMod groupMod = (GroupMod) iterator.next();
			
			if(!groupMod.isMarked()){
				groupMod.deleteGroup();
				iterator.remove();
			}
			
		}
	}
	
	public void execute(){
		for (Iterator iterator = groupHistory.iterator(); iterator.hasNext();) {
			GroupMod gm = (GroupMod) iterator.next();
			if(!gm.isMarked()){
				gm.deleteGroup();
				iterator.remove();
			}
		}
	}

	public GroupMod getGroupMod(GroupMod gmod) {
		return groupHistory.get(groupHistory.indexOf(gmod));
	}

	public void setGroupMod(GroupMod gmod) {
		groupHistory.set(groupHistory.indexOf(gmod), gmod);
	}

}
