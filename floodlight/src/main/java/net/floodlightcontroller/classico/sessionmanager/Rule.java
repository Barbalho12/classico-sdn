package net.floodlightcontroller.classico.sessionmanager;

public class Rule {
	private String inPort;
	private String ipv4Src;
	private String ipv4Dst;
	private String eth_type;
	private String ip_proto;
	
	public Rule(String inPort, String ipv4Src, String ipv4Dst) {
		this.inPort = inPort;
		this.ipv4Src = ipv4Src;
		this.ipv4Dst = ipv4Dst;
	}
	
	public Rule(String ipv4Src, String ipv4Dst) {
		this.ipv4Src = ipv4Src;
		this.ipv4Dst = ipv4Dst;
	}
	
	public String getInPort() {
		return inPort;
	}
	public void setInPort(String inPort) {
		this.inPort = inPort;
	}
	public String getIpv4Src() {
		return ipv4Src;
	}
	public void setIpv4Src(String ipv4Src) {
		this.ipv4Src = ipv4Src;
	}
	public String getIpv4Dst() {
		return ipv4Dst;
	}
	public void setIpv4Dst(String ipv4Dst) {
		this.ipv4Dst = ipv4Dst;
	}
	public String getEth_type() {
		return eth_type;
	}
	public void setEth_type(String eth_type) {
		this.eth_type = eth_type;
	}
	public String getIp_proto() {
		return ip_proto;
	}
	public void setIp_proto(String ip_proto) {
		this.ip_proto = ip_proto;
	}
	
	

}
