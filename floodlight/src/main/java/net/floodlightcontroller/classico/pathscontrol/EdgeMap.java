package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.List;

import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.classico.sessionmanager.UserSession;

public class EdgeMap {
	
	private OFPort ofPort;
	private List<UserSession> clients;
	private NodePath nextNodePath;
	
	public EdgeMap() {
		clients = new ArrayList<>();
	}

	public OFPort getOfPort() {
		return ofPort;
	}

	public void setOfPort(OFPort ofPort) {
		this.ofPort = ofPort;
	}

	public List<UserSession> getClients() {
		return clients;
	}

	public void setClients(List<UserSession> clients) {
		this.clients = clients;
	}

	public NodePath getNextNodePath() {
		return nextNodePath;
	}

	public void setNextNodePath(NodePath nextNodePath) {
		this.nextNodePath = nextNodePath;
	}

	public void addClient(UserSession userSession) {
		clients.add(userSession);
		
	}

	public void addClientIfNotExists(UserSession userSession) {
		if(!clients.contains(userSession))
			clients.add(userSession);
	}

	@Override
	public String toString() {
		return "EdgeMap [ofPort=" + ofPort + ", clients=" + clients + ", nextNodePath=" + nextNodePath + "]";
	}
	
	

}
