package net.floodlightcontroller.mactracker;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFFlowAdd;
import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFFlowRemoved;
import org.projectfloodlight.openflow.protocol.OFFlowRemoved.Builder;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPortDesc;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.MatchField;
import org.projectfloodlight.openflow.types.DatapathId;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.Masked;
import org.projectfloodlight.openflow.types.OFGroup;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;
import org.projectfloodlight.openflow.types.U64;
import org.python.modules.synchronize;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Route;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFConnection;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.internal.IOFSwitchService;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.types.NodePortTuple;
import net.floodlightcontroller.linkdiscovery.ILinkDiscoveryService;
import net.floodlightcontroller.linkdiscovery.Link;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.UDP;
import net.floodlightcontroller.routing.IRoutingDecisionChangedListener;
import net.floodlightcontroller.routing.IRoutingService;
import net.floodlightcontroller.routing.IRoutingService.PATH_METRIC;
import net.floodlightcontroller.statistics.IStatisticsService;
import net.floodlightcontroller.statistics.SwitchPortBandwidth;
import net.floodlightcontroller.topology.ITopologyService;
import net.floodlightcontroller.topology.TopologyManager;
import net.floodlightcontroller.util.FlowModUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.UnmodifiableIterator;
import net.floodlightcontroller.routing.Path;


public class MACTracker implements IOFMessageListener, IFloodlightModule, IStatisticsService {

	protected IOFSwitchService switchService;
	protected IFloodlightProviderService floodlightProvider;
	protected Set<Long> macAddresses;
	protected static Logger logger;
	
	protected ILinkDiscoveryService linkDiscoveryService;
	protected IStatisticsService statisticsService;
	boolean firstTimeFlag = true;
	
	protected IRoutingService routingService;
	protected ITopologyService topologyService;
	
	/*H3 NO MOMENTO*/
	private final String ADPUSB_MAC = "00:13:3B:85:05:05";
	
	/*H3*/
	private final String NOTEBOOK_FELIPE_MAC = "00:22:19:fd:65:77";
	private final String NOTEBOOK_FELIPE_IP = "192.168.2.110";
//	private final String NOTEBOOK_FELIPE_INTERFACE = "eth0.3";
//	private final String NOTEBOOK_FELIPE_INTERFACE = "s32-eth3";
	
	/*H2*/
	private final String NOTEBOOK_PROBOOK_MAC = "c8:cb:b8:c3:fc:3e";
	private final String NOTEBOOK_PROBOOK_IP = "192.168.2.120";
//	private final String NOTEBOOK_PROBOOK_INTERFACE = "eth1.5";
	private final String NOTEBOOK_PROBOOK_INTERFACE = "s38-eth4";
	
	/*H4*/
	private final String PC_FELIPE_MAC = "fc:15:b4:d9:51:40";
	private final String PC_FELIPE_IP = "192.168.2.115";
//	private final String PC_FELIPE_INTERFACE = "eth1.1";
	private final String PC_FELIPE_INTERFACE = "s38-eth3";
	
	
	
//	private final String PC_THALYSON_MAC = "10:60:4b:ea:b9:01";
//	private final String PC_THALYSON_IP = "192.168.2.100";
//	private final String PC_THALYSON_INTERFACE = "eth0.1"; //VERIFICAR
	
	@Override
	public String getName() {
		return MACTracker.class.getSimpleName();
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
		logger = LoggerFactory.getLogger(MACTracker.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
//		floodlightProvider.addOFMessageListener(OFType.FLOW_REMOVED, this);
//		floodlightProvider.addOFMessageListener(OFType.METER_MOD, this);
//		floodlightProvider.addOFMessageListener(OFType.FLOW_MOD, this);
		
	}

	boolean flag = true;
	
	ServerSession serverSession = new ServerSession("192.168.2.110", 8888);
	TableSessionMultiuser tableSessionMultiuser = new TableSessionMultiuser(serverSession);
	
//	public class Node{
//		private DatapathId id;
//		private OFPort ofPort;
//		public Node(DatapathId id){
//			this.id = id;
//		}
//		public Node(DatapathId id, OFPort ofPort){
//			this.id = id;
//			this.ofPort = ofPort;
//		}
//	}
//	
//	public class Path{
//		private Node src;
//		private Node dst;
//		private List<Link> pathNodes;
//
//		public Path(DatapathId idSrc, DatapathId idDst){
//			this.pathNodes = new ArrayList<>();
//			this.src = new Node(idSrc);
//			this.dst = new Node(idDst);
//		}
//		
//		public Path(Node src, Node dst, List<Link> pathNodes){
//			this.pathNodes = pathNodes;
//			this.src = src;
//			this.dst = dst;
//		}
//		
//		public void addLink(Link link){
//			pathNodes.add(link);
//		}
//		
//		public Node getSrc(){
//			return src;
//		}
//		
//		public Node getDst(){
//			return dst;
//		}
//		
//		public List<Link> getPaths(){
//			return pathNodes;
//		}
//		
////		public boolean existsDstInPath(DatapathId idDst){
////			for(Iterator<Link> iteratorNode = pathNodes.iterator(); iteratorNode.hasNext();){
////				Link link = (Link) iteratorNode;
////				if(link.getDst().equals(idDst)){
////					return true;
////				}
////			}
////			return false;
////		}
//		public boolean equalsLastNode(DatapathId id){
//			return pathNodes.get(pathNodes.size()-1).equals(id);
//		}
//	}
	
//	public void calculatePaths(DatapathId src,DatapathId dst){
//
//		ArrayList<Link> listLinks = new ArrayList<>(linkDiscoveryService.getLinks().keySet());
////		listLinks = (ArrayList<Link>) ((ArrayList<Link>) listLinks).clone();
//		
////		listLinks.addAll(linkDiscoveryService.getLinks().keySet());
//		
//		List<Path> paths = new ArrayList<>();
//		
////		System.out.println(listLinks.size());
//		
//		List<Link> linksRemoved = new ArrayList<>();
//		
//		for (Link link : listLinks) {
//			
//			if(link.getDst().equals(src) || link.getSrc().equals(dst)){
//				linksRemoved.add(link);
//				continue;
//			}
//			if(link.getSrc().equals(src)){
//				Path path = new Path(src, dst);
//				path.addLink(link);
//				paths.add(path);
//			}
//			
//		}
//		
////		for (Iterator<Link> iterator = listLinks.iterator(); iterator.hasNext();) {
////			Link link = (Link) iterator.next();
////			
////			if(link.getDst().equals(src) || link.getSrc().equals(dst)){
////				iterator.remove();
////				continue;
////			}
////			if(link.getSrc().equals(src)){
////				Path path = new Path(src, dst);
////				path.addLink(link);
////				paths.add(path);
////			}
////		}
//
//		
////		while(!listLinks.isEmpty()){
//		while(linksRemoved.size() < listLinks.size()){
//			
//			for(Iterator<Path> iteratorNode = paths.iterator(); iteratorNode.hasNext();){
//				Path pathNode = (Path) iteratorNode.next();
//				
//				
//				for (Link link : listLinks) {
//					if(linksRemoved.contains(link)) continue;
//					
////					if(pathNode.existsDstInPath(link.getDst())){
//					if(pathNode.equalsLastNode(link.getDst())){
//						linksRemoved.add(link);
//						continue;
//					}
//					if(pathNode.equalsLastNode(link.getSrc())){
//						Path path = new Path(pathNode.getSrc(), pathNode.getDst(), pathNode.getPaths());
//						path.addLink(link);
//						paths.add(path);
//					}
//					
//				}
//				iteratorNode.remove();
//				
////				for (Iterator<Link> iterator = listLinks.iterator(); iterator.hasNext();) {
////					Link link = (Link) iterator.next();
////					
//////					if(pathNode.existsDstInPath(link.getDst())){
////					if(pathNode.equalsLastNode(link.getDst())){
////						iterator.remove();
////						continue;
////					}
////					if(pathNode.equalsLastNode(link.getSrc())){
////						Path path = new Path(pathNode.getSrc(), pathNode.getDst(), pathNode.getPaths());
////						path.addLink(link);
////						paths.add(path);
////					}
////					
////				}
////				iteratorNode.remove();
//
//			}
//
//		}
//
//	}
	
	
	
	
	public List<Path> calculatePaths(DatapathId src,DatapathId dst, PATH_METRIC metric){
		if(metric != null){
			routingService.setPathMetric(metric);
		}else{
			routingService.setPathMetric(PATH_METRIC.HOPCOUNT);
		}
		
		
		List<Path> paths = routingService.getPathsSlow(src, dst, 100);
		return paths;
	}
	
	
	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch iof_switch, OFMessage msg, FloodlightContext cntx) {
//		initGroupSettings();

//		/*Get Links topology*/
//		for (Iterator<Link> iterator = linkDiscoveryService.getLinks().keySet().iterator(); iterator.hasNext();) {
//			Link link = (Link) iterator.next();
//			System.out.println(link.toString());
//
////			IOFSwitch swch = switchService.getSwitch(link.getDst());
//			if(statisticsService.getBandwidthConsumption(link.getSrc(), link.getSrcPort()) != null){
//				System.out.println(statisticsService.getBandwidthConsumption(link.getSrc(), link.getSrcPort()).getBitsPerSecondRx().getValue());
//				
//			}
//			
//		}
//		System.out.println("---------------------------");
//
//		
		DatapathId src = DatapathId.of("00:00:00:00:aa:bb:cc:32");
		DatapathId dst = DatapathId.of("00:00:00:00:aa:bb:cc:38");
//		System.out.println("Antes: "+linkDiscoveryService.getLinks().keySet().size());
////		if(linkDiscoveryService.getLinks().keySet().size()==40)
////			calculatePaths(src,dst);
//		System.out.println("Depois: "+linkDiscoveryService.getLinks().keySet().size());
//		System.out.println("---------------------------");
////		iof_switch.getConnections().get(0).getDatapathId()
//		for (UnmodifiableIterator<IOFConnection> iterator = iof_switch.getConnections().iterator(); iterator.hasNext();) {
//			System.out.println(iof_switch.getId() +" - "+ ((IOFConnection) iterator.next()).getDatapathId());
//			
//			System.out.println(topologyService.getAllLinks().get(src));
			
//		}
//			routingService.forceRecompute();
//		routingService.forceRecompute();
		if(routingService != null){
//			routingService.setPathMetric(PATH_METRIC.HOPCOUNT);
//			routingService.setPathMetric(PATH_METRIC.HOPCOUNT);
//			routingService.setPathMetric(PATH_METRIC.HOPCOUNT_AVOID_TUNNELS);
//			routingService.setPathMetric(PATH_METRIC.UTILIZATION);
//			routingService.setPathMetric(PATH_METRIC.LINK_SPEED);
//			System.out.println(routingService.getMaxPathsToCompute());
//			System.out.println(routingService.getPathsFast(src, dst, 300).toString());
//			System.out.println(routingService.getPathsSlow(src, dst, 300).size());
			System.out.println("-----------------------------------");
			System.out.println(calculatePaths(src,dst, PATH_METRIC.HOPCOUNT).toString());
//			System.out.println(routingService.getPathMetric());
//
		}


		switch (msg.getType()) {
		
			case PACKET_IN:
				
				Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
	
				if (eth.getEtherType() == EthType.IPv4) {	
					IPv4 ipv4 = (IPv4) eth.getPayload();

					if(ipv4.getProtocol() == IpProtocol.UDP && flag ) {
//						flag = false;
						verifySession(iof_switch, ipv4);
					}
				}
				break;
			default:
				break;
		}
		
		return Command.CONTINUE;
	}
	
	private synchronized void verifySession(IOFSwitch iof_switch, IPv4 ipv4){
		UDP udp = (UDP) ipv4.getPayload();
		
		TransportPort srcPort = udp.getSourcePort();
		TransportPort dstPort = udp.getDestinationPort();
		
		IPv4Address srcIp = ipv4.getSourceAddress();
		IPv4Address dstIp = ipv4.getDestinationAddress();

		if((!tableSessionMultiuser.getServerSession().getIp().equals(dstIp.toInetAddress().getHostAddress())) ||
				tableSessionMultiuser.getServerSession().getPort() != dstPort.getPort()){
			return;
		}
		
		if(tableSessionMultiuser.getServerSession().getIp().equals(srcIp.toInetAddress().getHostAddress()) ||
				tableSessionMultiuser.getServerSession().getPort() == srcPort.getPort()){
			return;
		}

		byte payload[] = udp.getPayload().serialize();
		String service = "";
		for(int i = 0; i < payload.length; i++){
			service += (char) payload[i];
		}

		tableSessionMultiuser.addClientRequest(
				srcIp, 
				srcPort, 
				dstIp, 
				dstPort,
				service,
				iof_switch.getId()
		);
//		System.out.println(tableSessionMultiuser.toString());
//		System.out.println("Switch: "+iof_switch.getId());
	}

	
	private void initGroupSettings() {
		
		if(firstTimeFlag){
			try{
				
				//Switch de entrada para nova regra de fluxo
				IOFSwitch  iofs = switchService.getSwitch(DatapathId.of("00:00:00:00:aa:bb:cc:38"));
				
				GroupMod gmod = new GroupMod(iofs);
				
				/*Cria um bucket com fluxo normal (pacote segue caminho original)*/
				gmod.createBucketNormalFlow(iofs, NOTEBOOK_PROBOOK_INTERFACE);
				
				/*Cria um bucket com fluxo diferente (altera ip, mac e porta do pacote destino)*/
				gmod.createBucket(iofs, PC_FELIPE_INTERFACE, PC_FELIPE_IP, PC_FELIPE_MAC, 10000);
				
				/*Grava no switch*/
				gmod.writeGroup();
				
				/*Regra de fluxo: IP fonte - IP Destino*/
				Rule rule1 = new Rule(NOTEBOOK_FELIPE_IP, NOTEBOOK_PROBOOK_IP);
				createFluxo(iofs, rule1, gmod.getGroup());

			}catch (Exception e) {
				e.printStackTrace();
				System.err.println("ERROR - Init Group Settings");
			}
			
			firstTimeFlag = false;
		}
	}

	public void createFluxo(IOFSwitch iof_switch, Rule rule, OFGroup group){
		OFFactory factory = iof_switch.getOFFactory();

		/*Crio um fluxo que irá direcionar os pacotes que obedecem as restrições para o grupo criado antes*/
		OFFlowAdd flowAdd = factory.buildFlowAdd()
			    .setHardTimeout(0)
			    .setIdleTimeout(0)
			    .setPriority(FlowModUtils.PRIORITY_MAX)
			    .setMatch(factory.buildMatch()
//			    	.setExact(MatchField.IN_PORT, iof_switch.getPort(rule.getInPort()).getPortNo())
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

	@Override
	public SwitchPortBandwidth getBandwidthConsumption(DatapathId dpid, OFPort p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<NodePortTuple, SwitchPortBandwidth> getBandwidthConsumption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void collectStatistics(boolean collect) {
		// TODO Auto-generated method stub
		
	}
	
}