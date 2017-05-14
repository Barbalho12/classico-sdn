package net.floodlightcontroller.mactracker;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.OFGroup;


import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.util.FlowModUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MACTracker implements IOFMessageListener, IFloodlightModule {

	protected IOFSwitchService switchService;
	protected IFloodlightProviderService floodlightProvider;
	protected Set<Long> macAddresses;
	protected static Logger logger;
	

//	static List<InfoTable> sessions = new ArrayList<InfoTable>();
	
	@Override
	public String getName() {
		return MACTracker.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
//		if(type.equals(OFType.PACKET_IN) && name.equals("forwarding")){
//			return true;
//		}else{
			return false;
//		}
		
		
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
//		if(type.equals(OFType.PACKET_IN) && name.equals("forwarding")){
			return true;
//		}else{
//			return false;
//		}
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		switchService = context.getServiceImpl(IOFSwitchService.class);
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
		macAddresses = new ConcurrentSkipListSet<Long>();
		logger = LoggerFactory.getLogger(MACTracker.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
	}

//	Map<String,String> map = new HashMap<String,String>();
	boolean flag = true;
//	boolean flag2 = true;
	
	@Override
	public synchronized net.floodlightcontroller.core.IListener.Command receive(IOFSwitch iof_switch, OFMessage msg, FloodlightContext cntx) {

		switch (msg.getType()) {
		
			case PACKET_IN:
				
				Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
	
				if (eth.getEtherType() == EthType.IPv4) {
						
					IPv4 ipv4 = (IPv4) eth.getPayload();
	
					if(ipv4.getProtocol() == IpProtocol.UDP) {


						if(flag){
							
							//Switch de entrada para nova regra de fluxo
							IOFSwitch  iofs = switchService.getSwitch(DatapathId.of("00:00:00:00:aa:bb:cc:35"));

							GroupMod gmod = new GroupMod(iofs);
							
							/*Cria um bucket com fluxo normal (pacote segue caminho original)*/
							try{
								gmod.createBucketNormalFlow(iofs, "s35-eth3");
							}catch (Exception e) {
								System.err.println("ERROR1");
							}
							
							
							/*Cria um bucket com fluxo diferente (altera ip, mac e porta do pacote destino)*/
							try{
								gmod.createBucket(iofs, "s35-eth4", "10.7.227.230", "00:00:00:00:00:04", 12345);
							}catch (Exception e) {
								System.err.println("ERROR2");
							}
							
							
							/*Grava no switch*/
							try{
								gmod.writeGroup();
							}catch (Exception e) {
								System.err.println("ERROR3");
							}
							
							
							/*Regra de fluxo: Interface de entrada - IP fonte - IP Destino*/
							try{
								Rule rule1 = new Rule("s35-eth1", "10.7.227.200", "10.7.227.215");
								Rule rule2 = new Rule("s35-eth2", "10.7.227.200", "10.7.227.215");
								
								createFluxo(iofs, rule1, gmod.getGroup());
								createFluxo(iofs, rule2, gmod.getGroup());
							}catch (Exception e) {
								System.err.println("ERROR4");
							}
							
							
							
							flag = false;
						}
					}
				}else{
					return Command.CONTINUE;
				}
				break;
			default:
				break;
		}
		return Command.CONTINUE;
	}

	public void createFluxo(IOFSwitch iof_switch, Rule rule, OFGroup group){
		OFFactory factory = iof_switch.getOFFactory();

		/*Crio um fluxo que irá direcionar os pacotes que obedecem as restrições para o grupo criado antes*/
		OFFlowAdd flowAdd = factory.buildFlowAdd()
			    .setHardTimeout(0)
			    .setIdleTimeout(0)
			    .setPriority(FlowModUtils.PRIORITY_MAX)
			    .setMatch(factory.buildMatch()
			    	.setExact(MatchField.IN_PORT, iof_switch.getPort(rule.getInPort()).getPortNo())
			        .setExact(MatchField.ETH_TYPE, EthType.IPv4)
			        .setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
			        .setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
			        .setExact(MatchField.IP_PROTO, IpProtocol.UDP)
			        .build())
			    .setActions(Collections.singletonList((OFAction) factory.actions().buildGroup()
			    	.setGroup(group)
			        .build()))
			    .build();
	
		
		iof_switch.write(flowAdd);
		System.out.println("[FLOW_MOD] ...");
	}
	
}