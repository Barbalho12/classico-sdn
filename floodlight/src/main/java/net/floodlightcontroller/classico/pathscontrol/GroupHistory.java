package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.projectfloodlight.openflow.protocol.OFBucket;
import org.projectfloodlight.openflow.types.DatapathId;

public class GroupHistory {
	List<GroupMod> groupHistory;
	
	public GroupHistory(){
		groupHistory = new ArrayList<GroupMod>();
	}
	
	void add(GroupMod groupMod){
		groupMod.setMarked(true);
		groupHistory.add(groupMod);
	}
	
	void set(int i, GroupMod groupMod){
		
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
		for (Iterator iterator = groupHistory.iterator(); iterator.hasNext();) {
			GroupMod groupMod = (GroupMod) iterator.next();
			
			if(!groupMod.isMarked()){
				groupMod.deleteGroup();
				iterator.remove();
			}
			
		}
	}

}
