package net.floodlightcontroller.classico;

import java.util.ArrayList;

import java.util.Collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;

import net.floodlightcontroller.classico.pathscontrol.CandidatePath;
import net.floodlightcontroller.classico.pathscontrol.ExecutorPathFlowSDN;
import net.floodlightcontroller.classico.pathscontrol.Monitor;
import net.floodlightcontroller.classico.pathscontrol.MulticriteriaPathSelection;
import net.floodlightcontroller.classico.pathscontrol.MultipathSession;
import net.floodlightcontroller.classico.sessionmanager.ServerSession;
import net.floodlightcontroller.classico.sessionmanager.MultiuserSessionControl;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.ICMP;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.LLDP;
import net.floodlightcontroller.packet.UDP;
import net.floodlightcontroller.routing.IRoutingService;
import net.floodlightcontroller.statistics.IStatisticsService;
import net.floodlightcontroller.topology.ITopologyService;
import net.floodlightcontroller.util.OFMessageUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLASSICOModule implements IOFMessageListener, IFloodlightModule/*, IStatisticsService*/ {
	
	private static final String SERVER_IP = "192.168.2.1";
	private static final int SERVER_PORT = 10000;
	private static final String SERVER_EDGE_SWITCH_ID = "00:00:00:00:aa:bb:cc:01";
	
	/*H3 NO MOMENTO*/
//	private final String ADPUSB_MAC = "00:13:3B:85:05:05";
	
	/*H3*/
//	private final String NOTEBOOK_FELIPE_MAC = "00:22:19:fd:65:77";
//	private final String NOTEBOOK_FELIPE_IP = "192.168.2.110";
//	private final String NOTEBOOK_FELIPE_INTERFACE = "eth0.3";
//	private final String NOTEBOOK_FELIPE_INTERFACE = "s32-eth3";
	
	/*H2*/
//	private final String NOTEBOOK_PROBOOK_MAC = "c8:cb:b8:c3:fc:3e";
//	private final String NOTEBOOK_PROBOOK_IP = "192.168.2.120";
//	private final String NOTEBOOK_PROBOOK_INTERFACE = "eth1.5";
//	private final String NOTEBOOK_PROBOOK_INTERFACE = "s38-eth4";
	
	/*H4*/
//	private final String PC_FELIPE_MAC = "fc:15:b4:d9:51:40";
//	private final String PC_FELIPE_IP = "192.168.2.115";
//	private final String PC_FELIPE_INTERFACE = "eth1.1";
//	private final String PC_FELIPE_INTERFACE = "s38-eth3";
	
//	private final String PC_THALYSON_MAC = "10:60:4b:ea:b9:01";
//	private final String PC_THALYSON_IP = "192.168.2.100";
//	private final String PC_THALYSON_INTERFACE = "eth0.1";

	protected IOFSwitchService switchService;
	protected IFloodlightProviderService floodlightProvider;
	protected Set<Long> macAddresses;
	protected static Logger logger;
	
	protected ILinkDiscoveryService linkDiscoveryService;
	protected IStatisticsService statisticsService;
	
	protected IRoutingService routingService;
	protected ITopologyService topologyService;
	
	private ServerSession serverSession;
	private MultiuserSessionControl tableSessionMultiuser;
	
	private ExecutorPathFlowSDN executorSDN;
	private Monitor monitor;
	private MulticriteriaPathSelection multicriteriaPathSelection;
	
	
	@Override
	public String getName() {
		return CLASSICOModule.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		return true;
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
		linkDiscoveryService = context.getServiceImpl(ILinkDiscoveryService.class);
		statisticsService = context.getServiceImpl(IStatisticsService.class);
		
		routingService = context.getServiceImpl(IRoutingService.class);
		topologyService = context.getServiceImpl(ITopologyService .class);
		
		macAddresses = new ConcurrentSkipListSet<Long>();
		logger = LoggerFactory.getLogger(CLASSICOModule.class);
		
		serverSession = new ServerSession(SERVER_IP, SERVER_PORT, DatapathId.of(SERVER_EDGE_SWITCH_ID));
		tableSessionMultiuser = new MultiuserSessionControl(serverSession);

		executorSDN = new ExecutorPathFlowSDN(switchService);
		
		monitor = new Monitor(this, tableSessionMultiuser, routingService, switchService, linkDiscoveryService, statisticsService);
		monitor.start();
		
		tableSessionMultiuser.setMonitor(monitor);
		multicriteriaPathSelection = new MulticriteriaPathSelection();
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		/*floodlightProvider.addOFMessageListener(OFType.FLOW_REMOVED, this);
		floodlightProvider.addOFMessageListener(OFType.FLOW_MOD, this);
		floodlightProvider.addOFMessageListener(OFType.GROUP_MOD, this);
		floodlightProvider.addOFMessageListener(OFType.TABLE_STATUS, this);*/
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch iof_switch, OFMessage msg, FloodlightContext cntx) {
		
		switch (msg.getType()) {
		
			case PACKET_IN:

				Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
				
				if (eth.getEtherType() == EthType.IPv4) {	
					IPv4 ipv4 = (IPv4) eth.getPayload();

					if(ipv4.getProtocol() == IpProtocol.UDP) {

						OFPort packetInPort = OFMessageUtils.getInPort((OFPacketIn) msg);
						boolean newUserSaved = verifySession(iof_switch, packetInPort, ipv4, eth.getSourceMACAddress());
						if(!newUserSaved){
							return Command.STOP;
						}
					}
					
			
				}/*else if (eth.getEtherType() == EthType.LLDP) {
					LLDP lldp = (LLDP) eth.getPayload();
					System.out.println(lldp.toString());
			
				}*/
				
				break;

			default:
				System.out.println(msg.getType());
				break;
		}
		
		return Command.CONTINUE;
	}
	
	private synchronized boolean verifySession(IOFSwitch iof_switch, OFPort packetInPort, IPv4 ipv4, MacAddress macAddress){
		UDP udp = (UDP) ipv4.getPayload();
		
		/*Endereço do Host Fonte*/
		IPv4Address srcIp = ipv4.getSourceAddress();
		TransportPort srcPort = udp.getSourcePort();
		
		/*Endereço do Host Destino*/
		IPv4Address dstIp = ipv4.getDestinationAddress();
		TransportPort dstPort = udp.getDestinationPort();
		
		/*Verifica se Host destino não é o servidor cadastrado, se sim é retornado*/
		if((!tableSessionMultiuser.getServerSession().getIp().equals(dstIp.toInetAddress().getHostAddress())) &&
					tableSessionMultiuser.getServerSession().getPort() != dstPort.getPort()){
			return true;
		}
		
		/*Verifica se Host Fonte é o servidor cadastrado, se sim é retornado*/
		if(tableSessionMultiuser.getServerSession().getIp().equals(srcIp.toInetAddress().getHostAddress())){
//				&& tableSessionMultiuser.getServerSession().getPort() == srcPort.getPort()){
			return true;
		}

		//Pega o conteudo da mensagem
		byte payload[] = udp.getPayload().serialize();
		String service = "";
		for(int i = 0; i < payload.length; i++){
			service += (char) payload[i];
		}

		/*Tenta criar o usuário/sessão com o conteúdo passado. O retorno será falso se for adicionado um usuário a uma sessão já existente.
		Se o usuário já existia em sessão ou se ele foi o primeiro de uma nova sessão, o retorno é verdadeiro*/
		boolean newUserSaved = tableSessionMultiuser.addClientRequest(
				srcIp, 
				srcPort, 
				dstIp, 
				dstPort,
				macAddress,
				service,
				iof_switch.getId(),
				packetInPort
		);

		return newUserSaved;
	}
	
	public void notifyUpdates(List<MultipathSession> multipathSessions){
		HashMap<String, CandidatePath> bestPaths = multicriteriaPathSelection.calculateBestPaths(multipathSessions);
		executorSDN.updateFlowPaths(tableSessionMultiuser.getMultipathSessions(), bestPaths);
	}

	
}