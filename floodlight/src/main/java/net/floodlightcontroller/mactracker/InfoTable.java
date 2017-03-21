package net.floodlightcontroller.mactracker;

import java.util.ArrayList;
import java.util.List;

public class InfoTable {
	private String ip_fonte;
	private String ip_destino;
	private String id_switch;
	private String porta_entrada_switch;
	private String porta_saida_switch;
	private String port_tcp_fonte;
	private String port_tcp_destino;
	private String fullData;
	
	private String subscriptionAttribute;
	private String idSensor;
	private List<InfoTable> sessions;
	
	
	
	public InfoTable(String ip_fonte, String ip_destino, String id_switch, String porta_entrada_switch,
			String porta_saida_switch, String port_tcp_fonte, String port_tcp_destino, String data) {
		super();
		this.ip_fonte = ip_fonte;
		this.ip_destino = ip_destino;
		this.id_switch = id_switch;
		this.porta_entrada_switch = porta_entrada_switch;
		this.porta_saida_switch = porta_saida_switch;
		this.port_tcp_fonte = port_tcp_fonte;
		this.port_tcp_destino = port_tcp_destino;
		this.fullData = data;
		
		this.sessions = new ArrayList<InfoTable>();
	}
	
	
	
	public InfoTable(String ip_fonte, String ip_destino, String id_switch, String porta_entrada_switch,
			String porta_saida_switch, String port_tcp_fonte, String port_tcp_destino, String fullData,
			String subscriptionAttribute, String idSensor) {
		super();
		this.ip_fonte = ip_fonte;
		this.ip_destino = ip_destino;
		this.id_switch = id_switch;
		this.porta_entrada_switch = porta_entrada_switch;
		this.porta_saida_switch = porta_saida_switch;
		this.port_tcp_fonte = port_tcp_fonte;
		this.port_tcp_destino = port_tcp_destino;
		this.fullData = fullData;
		this.subscriptionAttribute = subscriptionAttribute;
		this.idSensor = idSensor;
		
		this.sessions = new ArrayList<InfoTable>();
	}



	public String getIp_fonte() {
		return ip_fonte;
	}
	public void setIp_fonte(String ip_fonte) {
		this.ip_fonte = ip_fonte;
	}
	public String getIp_destino() {
		return ip_destino;
	}
	public void setIp_destino(String ip_destino) {
		this.ip_destino = ip_destino;
	}
	public String getId_switch() {
		return id_switch;
	}
	public void setId_switch(String id_switch) {
		this.id_switch = id_switch;
	}
	public String getPorta_saida_switch() {
		return porta_saida_switch;
	}
	public void setPorta_saida_switch(String porta_saida_switch) {
		this.porta_saida_switch = porta_saida_switch;
	}
	public String getPorta_entrada_switch() {
		return porta_entrada_switch;
	}
	public void setPorta_entrada_switch(String porta_entrada_switch) {
		this.porta_entrada_switch = porta_entrada_switch;
	}

	public String getPort_tcp_fonte() {
		return port_tcp_fonte;
	}

	public void setPort_tcp_fonte(String port_tcp_fonte) {
		this.port_tcp_fonte = port_tcp_fonte;
	}

	public String getPort_tcp_destino() {
		return port_tcp_destino;
	}

	public void setPort_tcp_destino(String port_tcp_destino) {
		this.port_tcp_destino = port_tcp_destino;
	}

	public String getData() {
		return fullData;
	}

	public void setData(String data) {
		this.fullData = data;
	}
	public String getSubscriptionAttribute() {
		return subscriptionAttribute;
	}
	public void setSubscriptionAttribute(String subscriptionAttribute) {
		this.subscriptionAttribute = subscriptionAttribute;
	}
	public String getIdSensor() {
		return idSensor;
	}
	public void setIdSensor(String idSensor) {
		this.idSensor = idSensor;
	}


	public List<InfoTable> getSessions() {
		return sessions;
	}


	public void addSession(InfoTable session) {
		sessions.add(session);
	}
	
	public void setSessions(List<InfoTable> sessions) {
		this.sessions = sessions;
	}



	@Override
	public String toString() {
		return "InfoTable [ip_fonte=" + ip_fonte + ", ip_destino=" + ip_destino + ", id_switch=" + id_switch
				+ ", porta_entrada_switch=" + porta_entrada_switch + ", porta_saida_switch=" + porta_saida_switch
				+ ", port_tcp_fonte=" + port_tcp_fonte + ", port_tcp_destino=" + port_tcp_destino + ", fullData="
				+ fullData + ", subscriptionAttribute=" + subscriptionAttribute + ", idSensor=" + idSensor
				+ ", sessions=" + sessions + "]";
	}
	
	
	
}
