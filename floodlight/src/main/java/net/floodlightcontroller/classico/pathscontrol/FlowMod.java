package net.floodlightcontroller.classico.pathscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.projectfloodlight.openflow.protocol.OFBucket;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowDelete;
import org.projectfloodlight.openflow.protocol.OFFlowModify;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.OFGroup;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.classico.sessionmanager.Rule;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.OFSwitch;
import net.floodlightcontroller.util.FlowModUtils;

public class FlowMod {
	
	private OFFactory factory;
	private IOFSwitch iofs;

	private int sessionID;
	private boolean marked;
	private Rule rule;
	
	
	public FlowMod(IOFSwitch iofs, int sessionid, Rule rule) {
		this.iofs = iofs;
		this.sessionID = sessionid;
		this.rule = rule;
		this.factory = iofs.getOFFactory();
		marked = true;
	}

	public void deleteFlow() {

		OFFlowDelete f = factory.buildFlowDelete().setHardTimeout(0).setIdleTimeout(0)
				.setPriority(FlowModUtils.PRIORITY_MAX)
				.setMatch(factory.buildMatch()
						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
						.setExact(MatchField.IP_PROTO, IpProtocol.UDP)
						.build())
				.build();
		iofs.write(f);

		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD DELETE: Switch: " + iofs.getId().toString() + ", Reference: "
				+ rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
	}
	
	public void createFlow(OFGroup group) {
		OFFlowAdd flowAdd = factory.buildFlowAdd().setHardTimeout(0).setIdleTimeout(0)
				.setPriority(FlowModUtils.PRIORITY_MAX)
				.setMatch(factory.buildMatch()
						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
				.setActions(Collections.singletonList(factory.actions().buildGroup().setGroup(group).build())).build();
		iofs.write(flowAdd);
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Switch: " + iofs.getId().toString() + ", Port: "
				+ group.getGroupNumber() + ", Reference: " + rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
	}
	
	public void createFlow(OFPort ofPort) {
		OFFlowAdd flowAdd = factory.buildFlowAdd().setHardTimeout(0).setIdleTimeout(0)
				.setPriority(FlowModUtils.PRIORITY_MAX)
				.setMatch(factory.buildMatch()
						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
				.setActions(Collections
						.singletonList(factory.actions().buildOutput().setMaxLen(0xffFFffFF).setPort(ofPort).build()))
				.build();
		iofs.write(flowAdd);
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD ADD: Switch: " + iofs.getId().toString() + ", Port: "
				+ ofPort.getPortNumber() + ", Reference: " + rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
	}
	
	public void modifyFlow(OFPort ofPort) {

		OFFlowModify flowmodify = factory.buildFlowModify().setHardTimeout(0).setIdleTimeout(0)
				.setPriority(FlowModUtils.PRIORITY_MAX)
				.setMatch(factory.buildMatch()
						.setExact(MatchField.ETH_TYPE, EthType.IPv4)
						.setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
						.setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
						.setExact(MatchField.IP_PROTO, IpProtocol.UDP).build())
				.setActions(Collections
						.singletonList(factory.actions().buildOutput().setMaxLen(0xffFFffFF).setPort(ofPort).build()))
				.build();
		iofs.write(flowmodify);
		System.out.println("[ExecutorPathFlowSDN] FLOW_MOD MODIFY: Switch: " + iofs.getId().toString() + ", Port: "
				+ ofPort.getPortNumber() + ", Reference: " + rule.getIpv4Src() + " -> " + rule.getIpv4Dst());
	}

	public IOFSwitch getIofs() {
		return iofs;
	}

	public void setIofs(IOFSwitch iofs) {
		this.iofs = iofs;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	
	public Object getDatapathId() {
		return iofs.getId();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iofs == null) ? 0 : iofs.hashCode());
		result = prime * result + (marked ? 1231 : 1237);
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		result = prime * result + sessionID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlowMod other = (FlowMod) obj;
		if (iofs == null) {
			if (other.iofs != null)
				return false;
		} else if (!iofs.getId().equals(other.iofs.getId()))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		if (sessionID != other.sessionID)
			return false;
		return true;
	}
	

}
