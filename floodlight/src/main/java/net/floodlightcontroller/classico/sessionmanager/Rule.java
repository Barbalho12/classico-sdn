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

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((eth_type == null) ? 0 : eth_type.hashCode());
//		result = prime * result + ((inPort == null) ? 0 : inPort.hashCode());
//		result = prime * result + ((ip_proto == null) ? 0 : ip_proto.hashCode());
//		result = prime * result + ((ipv4Dst == null) ? 0 : ipv4Dst.hashCode());
//		result = prime * result + ((ipv4Src == null) ? 0 : ipv4Src.hashCode());
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
		Rule other = (Rule) obj;
//		if (eth_type == null) {
//			if (other.eth_type != null)
//				return false;
//		} else if (!eth_type.equals(other.eth_type))
//			return false;
//		if (inPort == null) {
//			if (other.inPort != null)
//				return false;
//		} else if (!inPort.equals(other.inPort))
//			return false;
//		if (ip_proto == null) {
//			if (other.ip_proto != null)
//				return false;
//		} else if (!ip_proto.equals(other.ip_proto))
//			return false;
		if (ipv4Dst == null) {
			if (other.ipv4Dst != null)
				return false;
		} else if (!ipv4Dst.equals(other.ipv4Dst))
			return false;
		if (ipv4Src == null) {
			if (other.ipv4Src != null)
				return false;
		} else if (!ipv4Src.equals(other.ipv4Src))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rule [ipv4Src=" + ipv4Src + ", ipv4Dst=" + ipv4Dst + "]";
	}
	
	

}
