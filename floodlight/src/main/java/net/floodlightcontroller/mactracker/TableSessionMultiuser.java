package net.floodlightcontroller.mactracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.TransportPort;

public class TableSessionMultiuser {
	
	private int id;
	private List<SessionMultiUser> listSessions;
	private Date timeInit; 
	private Date timeUpdate;
	
	
	public TableSessionMultiuser(){
		timeInit = new Date();
		this.listSessions = new ArrayList<>();
	}
	
	public TableSessionMultiuser(int id, List<SessionMultiUser> listSessions) {
		timeInit = new Date();
		this.id = id;
		this.listSessions = listSessions;
	}
	
	public void newUserRequest(){
		
	}

	public int getId() {
		return id;
	}
	
	public void addSession(SessionMultiUser session){
		listSessions.add(session);
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<SessionMultiUser> getListSessions() {
		return listSessions;
	}

	public void setListSessions(List<SessionMultiUser> listSessions) {
		this.listSessions = listSessions;
	}

	public Date getTimeInit() {
		return timeInit;
	}

	public void setTimeInit(Date timeInit) {
		this.timeInit = timeInit;
	}

	public Date getTimeUpdate() {
		return timeUpdate;
	}

	public void setTimeUpdate(Date timeUpdate) {
		this.timeUpdate = timeUpdate;
	}
	
	

	public void addClientRequest(IPv4Address srcIp, TransportPort srcPort, IPv4Address dstIp, TransportPort dstPort,
			String service, DatapathId datapathId) {
		
		ISessionCondition sessionCond = new SessionCondition(service);
		
		UserSession userSession = new UserSession(srcIp, srcPort, dstIp, dstPort, datapathId);
		
		for(SessionMultiUser sm : listSessions){
			//Verifica se sessão existe
			if(sm.getSessionCondition().verify(sessionCond)){
				//verifica se host existe na sessão
				if(sm.userSessionExists(userSession)){
					System.out.println("User exists in session!");
				}else{
					userSession.setIdUser(sm.getListUser().size());
					sm.addUser(userSession);
				}
				return;
			}
		}
		
		SessionMultiUser smu = new SessionMultiUser(listSessions.size(), listSessions.size()+" "+service, sessionCond);
		listSessions.add(smu);

	} 
	
	@Override
	public String toString() {
		String texto = "";
		for(SessionMultiUser sm : listSessions){
			texto += sm.getId() + " - ";
			texto += sm.getDescription()+ " - ";
			texto += sm.getListUser() + "\n";
		}
		return super.toString();
	}
	

}
