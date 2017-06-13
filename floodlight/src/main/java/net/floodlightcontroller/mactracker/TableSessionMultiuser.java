package net.floodlightcontroller.mactracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	

}
