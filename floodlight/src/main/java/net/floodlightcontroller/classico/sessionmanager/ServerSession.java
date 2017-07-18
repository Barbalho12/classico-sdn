package net.floodlightcontroller.classico.sessionmanager;

import org.projectfloodlight.openflow.types.DatapathId;

public class ServerSession {

	private int port;
	private String ip;
	private DatapathId datapathId;
	
	public ServerSession(String ip, int port, DatapathId datapathId) {
		this.port = port;
		this.ip = ip;
		this.setDatapathId(datapathId);
	}
	
	public ServerSession(String ip, int port) {
		this.port = port;
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public DatapathId getDatapathId() {
		return datapathId;
	}

	public void setDatapathId(DatapathId datapathId) {
		this.datapathId = datapathId;
	}



	

}
