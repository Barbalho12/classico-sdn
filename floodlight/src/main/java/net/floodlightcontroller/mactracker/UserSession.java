package net.floodlightcontroller.mactracker;

import java.util.Date;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.TransportPort;

public class UserSession {
	
	private int idUser; /*ID do Host*/
	
	private IPv4Address srcIp;
	private TransportPort srcPort;
	private IPv4Address dstIp;
	private TransportPort dstPort;
	private DatapathId datapathId;
	
	private Date timeUserInSession; /*Tempo em que entrou na sessão*/
	
	
	public UserSession(IPv4Address srcIp, TransportPort srcPort, IPv4Address dstIp, TransportPort dstPort, DatapathId datapathId) {
		timeUserInSession = new Date();
		this.srcIp = srcIp;
		this.srcPort = srcPort;
		this.dstIp = dstIp;
		this.dstPort = dstPort;
		this.datapathId = datapathId;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSession other = (UserSession) obj;
//		if (datapathId == null) {
//			if (other.datapathId != null)
//				return false;
//		} else if (!datapathId.equals(other.datapathId))
//			return false;
		if (dstIp == null) {
			if (other.dstIp != null)
				return false;
		} else if (!dstIp.equals(other.dstIp))
			return false;
		if (dstPort == null) {
			if (other.dstPort != null)
				return false;
		} else if (!dstPort.equals(other.dstPort))
			return false;
//		if (idUser != other.idUser)
//			return false;
		if (srcIp == null) {
			if (other.srcIp != null)
				return false;
		} else if (!srcIp.equals(other.srcIp))
			return false;
//		if (srcPort == null) {
//			if (other.srcPort != null)
//				return false;
//		} else if (!srcPort.equals(other.srcPort))
//			return false;
		return true;
	}


	public int getIdUser() {
		return idUser;
	}


	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		String texto = idUser+" - ";
		texto += timeUserInSession.getHours() +"h"+timeUserInSession.getMinutes()+"m"+timeUserInSession.getSeconds()+"s - ";
		texto += srcIp.toString()+":"+srcPort.getPort()+" - ";
		texto += "Switch: "+datapathId.toString()+" ";
		return texto;
	}

	public IPv4Address getSrcIp() {
		return srcIp;
	}


	public void setSrcIp(IPv4Address srcIp) {
		this.srcIp = srcIp;
	}


	public TransportPort getSrcPort() {
		return srcPort;
	}


	public void setSrcPort(TransportPort srcPort) {
		this.srcPort = srcPort;
	}


	public IPv4Address getDstIp() {
		return dstIp;
	}


	public void setDstIp(IPv4Address dstIp) {
		this.dstIp = dstIp;
	}


	public TransportPort getDstPort() {
		return dstPort;
	}


	public void setDstPort(TransportPort dstPort) {
		this.dstPort = dstPort;
	}


	public DatapathId getDatapathId() {
		return datapathId;
	}


	public void setDatapathId(DatapathId datapathId) {
		this.datapathId = datapathId;
	}


	public Date getTimeUserInSession() {
		return timeUserInSession;
	}


	public void setTimeUserInSession(Date timeUserInSession) {
		this.timeUserInSession = timeUserInSession;
	}

	
}
