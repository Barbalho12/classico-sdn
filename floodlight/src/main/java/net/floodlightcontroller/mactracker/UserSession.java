package net.floodlightcontroller.mactracker;

import java.util.Date;

public class UserSession {

	private int idUser; /*ID do Host*/
	
	private String ipSource; /*IP do host*/
	private String processPortSource; /*Porta do processo do host*/
	
	private String ipDestination; /*Talvez seja necessário*/
	private String processPortDestination; /*Talvez seja necessário*/

	private String idEdgeSwitch; /*Switch de Borda*/
	private String inPortEdgeSwitch; /*porta de Entrada do switch*/
	private String outPortEdgeSwitch; /*porta de Saída do switch*/
	
	private Date timeUserInSession; /*Tempo em que entrou na sessão*/
	
	
	public UserSession() {
		timeUserInSession = new Date();
	}

	public UserSession(int idUser, String ipSource, String processPortSource, String idEdgeSwitch,
			String inPortEdgeSwitch) {
		timeUserInSession = new Date();
		this.idUser = idUser;
		this.ipSource = ipSource;
		this.processPortSource = processPortSource;
		this.idEdgeSwitch = idEdgeSwitch;
		this.inPortEdgeSwitch = inPortEdgeSwitch;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getIpSource() {
		return ipSource;
	}

	public void setIpSource(String ipSource) {
		this.ipSource = ipSource;
	}

	public String getProcessPortSource() {
		return processPortSource;
	}

	public void setProcessPortSource(String processPortSource) {
		this.processPortSource = processPortSource;
	}

	public String getIdEdgeSwitch() {
		return idEdgeSwitch;
	}

	public void setIdEdgeSwitch(String idEdgeSwitch) {
		this.idEdgeSwitch = idEdgeSwitch;
	}

	public String getInPortEdgeSwitch() {
		return inPortEdgeSwitch;
	}

	public void setInPortEdgeSwitch(String inPortEdgeSwitch) {
		this.inPortEdgeSwitch = inPortEdgeSwitch;
	}

	public String getIpDestination() {
		return ipDestination;
	}

	public void setIpDestination(String ipDestination) {
		this.ipDestination = ipDestination;
	}

	public String getProcessPortDestination() {
		return processPortDestination;
	}

	public void setProcessPortDestination(String processPortDestination) {
		this.processPortDestination = processPortDestination;
	}

	public String getOutPortEdgeSwitch() {
		return outPortEdgeSwitch;
	}

	public void setOutPortEdgeSwitch(String outPortEdgeSwitch) {
		this.outPortEdgeSwitch = outPortEdgeSwitch;
	}

	public Date getTimeUserInSession() {
		return timeUserInSession;
	}

	public void setTimeUserInSession(Date timeUserInSession) {
		this.timeUserInSession = timeUserInSession;
	}
	
	
	
}
