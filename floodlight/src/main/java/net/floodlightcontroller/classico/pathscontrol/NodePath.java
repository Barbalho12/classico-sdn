package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.List;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.classico.sessionmanager.Rule;

public class NodePath {
	
	private int idSession;
	private DatapathId dataPathId;
//	private boolean isBranch;
	private List<EdgeMap> conections;
	Rule rule;
	
	public NodePath(int idSession) {
		conections = new ArrayList<>();
	}
	
	public NodePath(DatapathId dataPathId, int idSession) {
		this.dataPathId = dataPathId;
		this.idSession = idSession;
		conections = new ArrayList<>();
	}
	
	public int getIdSession() {
		return idSession;
	}

	public void setIdSession(int idSession) {
		this.idSession = idSession;
	}

	public DatapathId getDataPathId() {
		return dataPathId;
	}
	public void setDataPathId(DatapathId dataPathId) {
		this.dataPathId = dataPathId;
	}
	public boolean isBranch() {
		if(getConections().size() > 1) return true;
		return false;
	}
//	public void setBranch(boolean isBranch) {
//		this.isBranch = isBranch;
//	}
	public List<EdgeMap> getConections() {
		return conections;
	}
	public void setConections(List<EdgeMap> conections) {
		this.conections = conections;
	}

	
	
	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void addConnectionIfNotExists(EdgeMap edgeMap) {
		if(!conections.contains(edgeMap))
			conections.add(edgeMap);	
	}
	
	public EdgeMap getLastConnection(){
		if(conections.isEmpty()){
			return null;
		}
		
		return conections.get(conections.size()-1);
	}

	public boolean containsConnection(OFPort portId) {
		for (EdgeMap edgeMap : conections) {
			if(edgeMap.getOfPort().equals(portId)){
				return true;
			}
		}
		return false;
	}

	public EdgeMap getConnection(OFPort portId) {
		for (EdgeMap edgeMap : conections) {
			if(edgeMap.getOfPort().equals(portId)){
				return edgeMap;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "NodePath [dataPathId=" + dataPathId + ", conections=" + conections + "]";
	}

	public boolean isBridge() {
		if( getConections().size() == 1 && getConections().get(0).getNextNodePath() != null){
			return true;
		}
		return false;
	}

	public EdgeMap getFirstConnection() {
		if(!conections.isEmpty()){
			return conections.get(0);
		}
		return null;
	}
	
	

}
