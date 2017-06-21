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
	
//	private String ipSource; /*IP do host*/
//	private String processPortSource; /*Porta do processo do host*/
//	
//	private String ipDestination; /*Talvez seja necessário*/
//	private String processPortDestination; /*Talvez seja necessário*/
//
//	private String idEdgeSwitch; /*Switch de Borda*/
//	private String inPortEdgeSwitch; /*porta de Entrada do switch*/
//	private String outPortEdgeSwitch; /*porta de Saída do switch*/
//	
	private Date timeUserInSession; /*Tempo em que entrou na sessão*/
	
	
	public UserSession(IPv4Address srcIp, TransportPort srcPort, IPv4Address dstIp, TransportPort dstPort, DatapathId datapathId) {
		timeUserInSession = new Date();
		this.srcIp = srcIp;
		this.srcPort = srcPort;
		this.dstIp = dstIp;
		this.dstPort = dstPort;
		this.datapathId = datapathId;
	}


//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((datapathId == null) ? 0 : datapathId.hashCode());
//		result = prime * result + ((dstIp == null) ? 0 : dstIp.hashCode());
//		result = prime * result + ((dstPort == null) ? 0 : dstPort.hashCode());
//		result = prime * result + idUser;
//		result = prime * result + ((srcIp == null) ? 0 : srcIp.hashCode());
//		result = prime * result + ((srcPort == null) ? 0 : srcPort.hashCode());
//		return result;
//	}


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

//	public UserSession(int idUser, String ipSource, String processPortSource, String idEdgeSwitch,
//			String inPortEdgeSwitch) {
//		timeUserInSession = new Date();
//		this.idUser = idUser;
//		this.ipSource = ipSource;
//		this.processPortSource = processPortSource;
//		this.idEdgeSwitch = idEdgeSwitch;
//		this.inPortEdgeSwitch = inPortEdgeSwitch;
//	}
	@Override
	public String toString() {
		String texto = idUser+" - ";
		texto += timeUserInSession.getHours() +"h"+timeUserInSession.getMinutes()+"m"+timeUserInSession.getSeconds()+"s - ";
		texto += srcIp.toString()+":"+srcPort.getPort()+" - ";
		texto += "Switch: "+datapathId.toString()+" ";
		return texto;
	}

	
}
