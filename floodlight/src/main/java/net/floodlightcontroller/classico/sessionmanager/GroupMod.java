package net.floodlightcontroller.classico.sessionmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.projectfloodlight.openflow.protocol.OFBucket;
import org.projectfloodlight.openflow.protocol.OFFactory;
import org.projectfloodlight.openflow.protocol.OFGroupAdd;
import org.projectfloodlight.openflow.protocol.OFGroupType;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.action.OFActionSetField;
import org.projectfloodlight.openflow.protocol.action.OFActions;
import org.projectfloodlight.openflow.protocol.oxm.OFOxms;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFGroup;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;

import net.floodlightcontroller.core.IOFSwitch;

public class GroupMod {
	
//	private static int GROUP_NUMBER = 1;
	private int id;
	private OFFactory factory;
	private OFGroupAdd groupAdd;
	private IOFSwitch iof_switch;
	ArrayList<OFBucket> buckets;
	
	public GroupMod(IOFSwitch iof_switch) {
		this.setIof_switch(iof_switch);
		this.factory = iof_switch.getOFFactory();
		
		buckets = new ArrayList<>();
	}
	
	public GroupMod(IOFSwitch iofs, int id) {
		this.setIof_switch(iofs);
		this.factory = iofs.getOFFactory();
		this.setId(id);
		buckets = new ArrayList<>();
	}

	public void writeGroup(){
		groupAdd = factory.buildGroupAdd()
//				.setGroup(OFGroup.of(1))
				.setGroup(OFGroup.of(getId()))
//			    .setGroup(OFGroup.of(GROUP_NUMBER++))
			    .setGroupType(OFGroupType.ALL)
			    .setBuckets(buckets)
			    .build();
		
		iof_switch.write(groupAdd);
		System.out.println("[GROUP] Group Created");
	}
	

	public OFBucket createBucketNormalFlow(IOFSwitch iofSwitch, String normalFlowPort) {
		
		List<OFAction> actions = Collections.singletonList((OFAction) factory.actions().buildOutput()
		        .setMaxLen(0xffFFffFF)
		        .setPort(iofSwitch.getPort(normalFlowPort).getPortNo())
//		        .setPort(OFPort.NORMAL)
		        .build());
		
		OFBucket newBucket = createBucket(actions);

		//buckets.add(newBucket);
		
//		System.out.println("[GROUP] BucketNormalFlow Created");
		
		return newBucket;
	}
	
	public OFBucket createBucket(List<OFAction> actionList) {
		
		OFBucket newBucket = factory.buildBucket()
				.setWatchPort(OFPort.ANY)
			    .setWatchGroup(OFGroup.ANY)
			    .setActions(actionList)
			    .build();
		
		buckets.add(newBucket);
		
		System.out.println("[GROUP] Bucket Created");
		
		return newBucket;
	}
	
	public OFBucket createBucket(IOFSwitch iofSwitch, String switchOutputPort, String newIPHostDest, String newMacHostDest, int newPortDest) {
		ArrayList<OFAction> actionList = createListActions(iofSwitch, switchOutputPort, newIPHostDest, newMacHostDest, newPortDest);
		
		OFBucket newBucket = createBucket(actionList);
		
		return newBucket;
	}
	
	//ARGS: Interface de Saída do pacote- Novo IP destino - Novo MAC destino - Nova Porta destino
	public ArrayList<OFAction> createListActions(IOFSwitch iofSwitch, String switchOutputPort, String newIPHostDest, String newMacHostDest, int newPortDest) {

		OFOxms oxms = factory.oxms();
		OFActions actions = factory.actions();
		
		//Ações de modificação de pacotes
		ArrayList<OFAction> actionList = new ArrayList<OFAction>();
		
		/* Cria a ação de modificar o destino, e adiciona a lista*/
		OFActionSetField setNewIpv4Dst = actions.buildSetField()
		    .setField(
		        oxms.buildIpv4Dst()
		        .setValue(IPv4Address.of(newIPHostDest))
		        .build()
		    )
		    .build();
		actionList.add(setNewIpv4Dst);
		
		/* Cria a ação de modificar o MAc destino, e adiciona a lista*/
		OFActionSetField setNewEthDst = actions.buildSetField()
		    .setField(
		        oxms.buildEthDst()
		        .setValue(MacAddress.of(newMacHostDest))
		        .build()
		    ).build();
		actionList.add(setNewEthDst);
		
		/* Cria a ação de modificar o MAc destino, e adiciona a lista*/
		OFActionSetField setNewTcpDst = actions.buildSetField()
		    .setField(
		        oxms.buildUdpDst()
		        .setValue(TransportPort.of(newPortDest)) 
		        .build()
		    ).build();
		
		actionList.add(setNewTcpDst);
		
		/* Cria a ação de porta de saída do pacote, e adiciona a lista*/
		actionList.add(factory.actions().buildOutput()
		        .setMaxLen(0xffFFffFF)
		        .setPort(iofSwitch.getPort(switchOutputPort).getPortNo())
//		        .setPort(OFPort.NORMAL)
		        .build());
		
		System.out.println("[GROUP] List Actions Created");
		
		return actionList;
	}
	
	public ArrayList<OFAction> createListActions(IOFSwitch iofSwitch, OFPort switchOutputPort, IPv4Address newIPHostDest, TransportPort newPortDest, MacAddress newMacHostDest) {

		OFOxms oxms = factory.oxms();
		OFActions actions = factory.actions();
		
		//Ações de modificação de pacotes
		ArrayList<OFAction> actionList = new ArrayList<OFAction>();
		
		/* Cria a ação de modificar o destino, e adiciona a lista*/
		OFActionSetField setNewIpv4Dst = actions.buildSetField()
		    .setField(
		        oxms.buildIpv4Dst()
		        .setValue(newIPHostDest)
		        .build()
		    )
		    .build();
		actionList.add(setNewIpv4Dst);
		
		/* Cria a ação de modificar o MAc destino, e adiciona a lista*/
		OFActionSetField setNewEthDst = actions.buildSetField()
		    .setField(
		        oxms.buildEthDst()
		        .setValue(newMacHostDest)
		        .build()
		    ).build();
		actionList.add(setNewEthDst);
		
		/* Cria a ação de modificar o MAc destino, e adiciona a lista*/
		OFActionSetField setNewTcpDst = actions.buildSetField()
		    .setField(
		        oxms.buildUdpDst()
		        .setValue(newPortDest) 
		        .build()
		    ).build();
		
		actionList.add(setNewTcpDst);
		
		/* Cria a ação de porta de saída do pacote, e adiciona a lista*/
		actionList.add(factory.actions().buildOutput()
		        .setMaxLen(0xffFFffFF)
		        .setPort(switchOutputPort)
//		        .setPort(OFPort.NORMAL)
		        .build());
		
		System.out.println("[GROUP] List Actions Created");
		
		return actionList;
	}

	public IOFSwitch getIof_switch() {
		return iof_switch;
	}

	public void setIof_switch(IOFSwitch iof_switch) {
		this.iof_switch = iof_switch;
	}
	
	public OFGroup getGroup() {
		return groupAdd.getGroup();
	}

	public void createBucket(IOFSwitch iofs, OFPort switchOutputPort, IPv4Address srcIp, TransportPort srcPort,
			MacAddress maCadreess) {
		ArrayList<OFAction> actionList = createListActions(iofs, switchOutputPort, srcIp, srcPort, maCadreess );

		OFBucket newBucket = createBucket(actionList);
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
