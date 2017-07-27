package net.floodlightcontroller.classico.sessionmanager;

import java.util.Date;

import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;

public class UserSession {
	
	private int idUser; /*ID do Host*/
	
	private IPv4Address ip;
	private TransportPort port;
	private IPv4Address dstIp;
	private TransportPort dstPort;
	private DatapathId datapathId;
	private MacAddress MACadreess;
	private OFPort switchInPort;
	
	private Date timeUserInSession; /*Tempo em que entrou na sessão*/
	
	
	public UserSession(IPv4Address ip, TransportPort port, IPv4Address dstIp, TransportPort dstPort, MacAddress macAddress, DatapathId datapathId,OFPort switchInPort) {
		timeUserInSession = new Date();
		this.ip = ip;
		this.port = port;
		this.dstIp = dstIp;
		this.dstPort = dstPort;
		this.datapathId = datapathId;
		this.MACadreess = macAddress;
		this.setSwitchInPort(switchInPort);
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
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
//		if (port == null) {
//			if (other.port != null)
//				return false;
//		} else if (!port.equals(other.port))
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
		String texto = "ID = "+idUser+", ";
		texto += "Time Init = "+timeUserInSession.getHours() +"h"+timeUserInSession.getMinutes()+"m"+timeUserInSession.getSeconds()+"s, ";
		texto += "Address = " + ip.toString()+":"+port.getPort()+", ";
		texto += "Edge switch = "+datapathId.toString()+"]";
		return "UserSession ["+texto;
	}

	public IPv4Address getIp() {
		return ip;
	}


	public void setIp(IPv4Address ip) {
		this.ip = ip;
	}


	public TransportPort getPort() {
		return port;
	}


	public void setPort(TransportPort port) {
		this.port = port;
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


	public MacAddress getMACadreess() {
		return MACadreess;
	}


	public void setMACadreess(MacAddress mACadreess) {
		MACadreess = mACadreess;
	}


	public OFPort getSwitchInPort() {
		return switchInPort;
	}


	public void setSwitchInPort(OFPort switchInPort) {
		this.switchInPort = switchInPort;
	}

	
}
