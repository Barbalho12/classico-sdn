package net.floodlightcontroller.mactracker;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.projectfloodlight.openflow.protocol.OFBucket;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFGroupAdd;
import org.projectfloodlight.openflow.protocol.OFGroupType;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionSetField;
import org.projectfloodlight.openflow.protocol.action.OFActions;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.protocol.oxm.OFOxms;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFGroup;
import org.projectfloodlight.openflow.types.OFPort;

import org.projectfloodlight.openflow.types.TransportPort;


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

	Map<String,String> map = new HashMap<String,String>();
	boolean flag = true;
	boolean flag2 = true;
	
	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {

		switch (msg.getType()) {
		
			case PACKET_IN:
				
				Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
	
				if (eth.getEtherType() == EthType.IPv4) {
						
					IPv4 ipv4 = (IPv4) eth.getPayload();
	
					if(ipv4.getProtocol() == IpProtocol.UDP) {

						//Switch de entrada para nova regra de fluxo
						IOFSwitch iofs = switchService.getSwitch(DatapathId.of("00:00:00:00:aa:bb:cc:38"));

						if(flag && iofs.equals(sw)){

							//Regra de fluxo
							//ARGS: Interface de entrada - IP fonte - IP Destino
							Rule rule1 = new Rule("eth1.1", "192.168.2.150", "192.168.2.185");

							//Ação sobre o pacote que será replicado
							//ARGS: Interface de Saída do pacote- Novo IP destino - Novo MAC destino - Nova Porta destino
							ArrayList<OFAction> actionList2 = createListActions(iofs, "eth1.4", "192.168.2.100", "10:60:4b:ea:b9:01", 12345);
							ArrayList<OFBucket> buckets = createBuckets(iofs, actionList2, "eth1.5");

							//Criação do grupo
							group(iofs, rule1, buckets);
							System.out.println("Create Group...");
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

	public void group(IOFSwitch mySwitch, Rule rule, ArrayList<OFBucket> buckets){
		OFFactory myFactory = mySwitch.getOFFactory();

//		ArrayList<OFBucket> buckets = createBuckets(mySwitch, actionList);

		/* Crio o grupo com a lista de buckets (cada bucket com uma lista de ações)*/
		OFGroupAdd groupAdd = myFactory.buildGroupAdd()
			    .setGroup(OFGroup.of(1))
			    .setGroupType(OFGroupType.ALL)
			    .setBuckets(buckets)
			    .build();

		/*Escrevo no switch*/
		mySwitch.write(groupAdd);

		System.out.println("--------------------GroupMod--------------------");



		/*Crio um fluxo que irá direcionar os pacotes que obedecem as restrições para o grupo criado antes*/

		OFFlowAdd flowAdd = myFactory.buildFlowAdd()
			    .setHardTimeout(0)
			    .setIdleTimeout(0)
			    .setPriority(FlowModUtils.PRIORITY_MAX)
			    .setMatch(myFactory.buildMatch()
			    	.setExact(MatchField.IN_PORT, mySwitch.getPort(rule.getInPort()).getPortNo())
			        .setExact(MatchField.ETH_TYPE, EthType.IPv4)
			        .setExact(MatchField.IPV4_SRC, IPv4Address.of(rule.getIpv4Src()))
			        .setExact(MatchField.IPV4_DST, IPv4Address.of(rule.getIpv4Dst()))
			        .setExact(MatchField.IP_PROTO, IpProtocol.UDP)
			        .build())
			    .setActions(Collections.singletonList((OFAction) myFactory.actions().buildGroup()
			        .setGroup(OFGroup.of(1))
			        .build()))
			    .build();
	
		
		mySwitch.write(flowAdd);
		
		System.out.println("--------------------FlowMod--------------------");
	}
	
	private ArrayList<OFBucket> createBuckets(IOFSwitch mySwitch, ArrayList<OFAction> actionList, String normalFlowPort) {
		OFFactory myFactory = mySwitch.getOFFactory();
				
		ArrayList<OFBucket> buckets = new ArrayList<OFBucket>(2);
		
		//Primeiro bucket segue o percurso normal
		buckets.add(mySwitch.getOFFactory().buildBucket()
			.setWatchPort(OFPort.ANY)
		    .setWatchGroup(OFGroup.ANY)
		    .setActions(Collections.singletonList((OFAction) myFactory.actions().buildOutput()
		        .setMaxLen(0xffFFffFF)
		        .setPort(mySwitch.getPort(normalFlowPort).getPortNo())
		        .build()))
		    .build());
		/* Adiciona as ações no segundo bucket*/
		buckets.add(myFactory.buildBucket()
			.setWatchPort(OFPort.ANY)
		    .setWatchGroup(OFGroup.ANY)
		    .setActions(actionList)
		    .build());
		
		return buckets;
	}
	
	private OFBucket newBucket(IOFSwitch mySwitch, ArrayList<OFAction> actionList) {
		OFFactory myFactory = mySwitch.getOFFactory();
		
		OFBucket newBucket = myFactory.buildBucket()
				.setWatchPort(OFPort.ANY)
			    .setWatchGroup(OFGroup.ANY)
			    .setActions(actionList)
			    .build();
		
		return newBucket;
	}


	private ArrayList<OFAction> createListActions(IOFSwitch mySwitch, String switchPortOutName, String ipHost, String macHost, int port) {
		//Fábrica pra criar os objetos
		OFFactory myFactory = mySwitch.getOFFactory();
		
		//Ações de modificação de pacotes
		ArrayList<OFAction> actionList = new ArrayList<OFAction>();
		
		OFOxms oxms = myFactory.oxms();
		OFActions actions = myFactory.actions();
		
		/* Cria a ação de modificar o destino, e adiciona a lista*/
		OFActionSetField setIpv4Dst = actions.buildSetField()
		    .setField(
		        oxms.buildIpv4Dst()
		        .setValue(IPv4Address.of(ipHost))
		        .build()
		    )
		    .build();
		actionList.add(setIpv4Dst);
		
		/* Cria a ação de modificar o MAc destino, e adiciona a lista*/
		OFActionSetField setEthDst = actions.buildSetField()
		    .setField(
		        oxms.buildEthDst()
		        .setValue(MacAddress.of(macHost))
		        .build()
		    )
		    .build();
		actionList.add(setEthDst);
		
		/* Cria a ação de modificar o MAc destino, e adiciona a lista*/
		OFActionSetField setTcpPortDst = actions.buildSetField()
		    .setField(
		        oxms.buildTcpDst()
		        .setValue(TransportPort.of(port)) 
		        .build()
		    )
		    .build();
		actionList.add(setTcpPortDst);
		
		/* Cria a ação de porta de saída do pacote, e adiciona a lista*/
		actionList.add(myFactory.actions().buildOutput()
		        .setMaxLen(0xffFFffFF)
		        .setPort(mySwitch.getPort(switchPortOutName).getPortNo())
		        .build());
		
		return actionList;
	}

}