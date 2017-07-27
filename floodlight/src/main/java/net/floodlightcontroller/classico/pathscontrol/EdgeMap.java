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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clients == null) ? 0 : clients.hashCode());
		result = prime * result + ((nextNodePath == null) ? 0 : nextNodePath.hashCode());
		result = prime * result + ((ofPort == null) ? 0 : ofPort.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgeMap other = (EdgeMap) obj;
		if (clients == null) {
			if (other.clients != null)
				return false;
		} else if (!clients.equals(other.clients))
			return false;
		if (nextNodePath == null) {
			if (other.nextNodePath != null)
				return false;
		} else if (!nextNodePath.equals(other.nextNodePath))
			return false;
		if (ofPort == null) {
			if (other.ofPort != null)
				return false;
		} else if (!ofPort.equals(other.ofPort))
			return false;
		return true;
	}
	
	

}
