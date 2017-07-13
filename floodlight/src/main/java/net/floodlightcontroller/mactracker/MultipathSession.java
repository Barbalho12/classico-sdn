package net.floodlightcontroller.mactracker;

import java.util.List;

public class MultipathSession {
	
	private List<CandidatePath> paths;
	private ServerSession serverSession;
	private UserSession userSession;
	private SessionMultiUser sessionMultiUser;
	
	public MultipathSession(List<CandidatePath> paths, UserSession userSession, ServerSession serverSession, SessionMultiUser sessionMultiUser) {
		this.paths = paths;
		this.userSession = userSession;
		this.sessionMultiUser = sessionMultiUser;
		this.serverSession = serverSession;
	}
	
	public List<CandidatePath> getPaths() {
		return paths;
	}
	public void setPaths(List<CandidatePath> paths) {
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

//	@Override
//	public String toString() {
//		return "MultipathSession [paths=" + paths.toString() + ", serverSession=" + userSession.getDstIp() + ", userSession=" + userSession.getSrcIp()
//				+ ", sessionMultiUser=" + sessionMultiUser.getDescription() + "]";
//	}
	
	@Override
	public String toString() {
		return "MultipathSession [Size Candidate Paths = " + paths.size() + ", "
				+ "Server = " + userSession.getDstIp() + ":"+userSession.getDstPort()+", "
				+ "User = " + userSession.getSrcIp() + ":"+userSession.getSrcPort()+", "
				+ "Session Context = " + sessionMultiUser.getDescription() + " ]";
	}
	
	public String getPathIndex(){
		return serverSession.getDatapathId().toString() +
		userSession.getDatapathId().toString();

	}

}
