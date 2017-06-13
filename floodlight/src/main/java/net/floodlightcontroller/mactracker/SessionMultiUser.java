package net.floodlightcontroller.mactracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionMultiUser {
	private int id; /*ID da sessão*/
	
	private Date timeInit; /*Tempo da sessão*/
	
	private String description; /*Descrição da sessão*/

	private String fullData; /*Conteúdo comum da sessão (talvez seja necessario 
	estar em outra estrutura de dados)*/

	private List<UserSession> listUser; /*Visão da sessão (Pode ser necessário retirar)*/
	
	
	public SessionMultiUser() {
		timeInit = new Date();
		this.listUser = new ArrayList<>();
	}

	public SessionMultiUser(int id, String description) {
		timeInit = new Date();
		this.id = id;
		this.description = description;
		this.listUser = new ArrayList<>();
		
	}
	
	public void addUser(UserSession userSession){
		listUser.add(userSession);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFullData() {
		return fullData;
	}

	public void setFullData(String fullData) {
		this.fullData = fullData;
	}

	public List<UserSession> getListUser() {
		return listUser;
	}

	public void setListUser(List<UserSession> listUser) {
		this.listUser = listUser;
	}

	public Date getTimeInit() {
		return timeInit;
	}

	public void setTimeInit(Date timeInit) {
		this.timeInit = timeInit;
	}
	
	
	

}
