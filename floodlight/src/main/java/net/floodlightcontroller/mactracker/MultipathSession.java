package net.floodlightcontroller.mactracker;

import java.util.List;

import net.floodlightcontroller.routing.Path;

public class MultipathSession {
	
	private List<Path> paths;
	private ServerSession serverSession;
	private UserSession userSession;
	private SessionMultiUser sessionMultiUser;
	
	public MultipathSession(List<Path> paths, UserSession userSession, SessionMultiUser sessionMultiUser) {
		this.paths = paths;
		this.userSession = userSession;
		this.sessionMultiUser = sessionMultiUser;

	}
	
	public List<Path> getPaths() {
		return paths;
	}
	public void setPaths(List<Path> paths) {
		this.paths = paths;
	}
	public UserSession getUserSession() {
		return userSession;
	}
	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}
	public SessionMultiUser getSessionMultiUser() {
		return sessionMultiUser;
	}
	public void setSessionMultiUser(SessionMultiUser sessionMultiUser) {
		this.sessionMultiUser = sessionMultiUser;
	}

	public ServerSession getServerSession() {
		return serverSession;
	}

	public void setServerSession(ServerSession serverSession) {
		this.serverSession = serverSession;
	}

	@Override
	public String toString() {
		return "MultipathSession [paths=" + paths.toString() + ", serverSession=" + userSession.getDstIp() + ", userSession=" + userSession.getSrcIp()
				+ ", sessionMultiUser=" + sessionMultiUser.getDescription() + "]";
	}
	
	

}
