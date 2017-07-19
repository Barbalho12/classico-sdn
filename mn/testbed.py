#!/usr/bin/python

from mininet.net import Mininet
from mininet.node import  OVSController, OVSKernelSwitch, RemoteController
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.link import TCLink

def topology():

    "Create a network."
    net = Mininet( controller=RemoteController, link=TCLink, switch=OVSKernelSwitch )
	
    _bw=100
    _latency='5ms'
    _max_queue_size=100
    _use_htb=True
    _ip_remote_control='127.0.0.1'
    _port_remote_control=6653

    adp_USB100MB_MAC = "00:13:3B:85:05:05"

    print "*** Creating Hosts"
    h1 = net.addHost( 'h1', mac="10:60:4b:ea:b9:01", ip='192.168.2.100' )
    h2 = net.addHost( 'h2', mac="c8:cb:b8:c3:fc:3e", ip='192.168.2.120' )
    # h3 = net.addHost( 'h3', mac="00:22:19:fd:65:77", ip='192.168.2.110' )
    h3 = net.addHost( 'h3', mac=adp_USB100MB_MAC, ip='192.168.2.110' )
    h4 = net.addHost( 'h4', mac="fc:15:b4:d9:51:40", ip='192.168.2.115' )


    


    print "*** Creating Switchs"
    s35 = net.addSwitch( 's35', dpid='00:00:00:00:aa:bb:cc:35' )
    s32 = net.addSwitch( 's32', dpid='00:00:00:00:aa:bb:cc:32' )
    s15 = net.addSwitch(  's15', dpid='00:00:00:00:aa:bb:cc:15' )
    s14 = net.addSwitch(  's14', dpid='00:00:00:00:aa:bb:cc:14' )
    s02 = net.addSwitch(  's02', dpid='00:00:00:00:aa:bb:cc:02' )
    s17 = net.addSwitch(  's17', dpid='00:00:00:00:aa:bb:cc:17' )
    s38 = net.addSwitch(  's38', dpid='00:00:00:00:aa:bb:cc:38' )
    
    print "*** Creating Controller Openflow"
    c0 = RemoteController( 'c0', ip=_ip_remote_control, port=_port_remote_control )

    print "*** Connecting hosts"
    net.addLink(h1, s35, bw=_bw, use_htb=_use_htb)
    net.addLink(h2, s38, bw=_bw, use_htb=_use_htb)
    net.addLink(h3, s32, bw=_bw, use_htb=_use_htb)
    net.addLink(h4, s38, bw=_bw, use_htb=_use_htb)
    # net.addLink(h1, s38, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)

    # TESTE ADICIONAL
    h15 = net.addHost( 'h15', mac="00:00:00:00:00:15", ip='192.168.2.15' )
    h17 = net.addHost( 'h17', mac="00:00:00:00:00:17", ip='192.168.2.17' )
    net.addLink(h15, s15, bw=_bw, use_htb=_use_htb)
    net.addLink(h17, s17, bw=_bw, use_htb=_use_htb)
    # ---------------

    print "*** Creating connection between switches"
    net.addLink(s35, s15, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s35, s14, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s32, s15, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s32, s14, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)

    net.addLink(s15, s17, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s15, s02, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s14, s17, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s14, s02, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)

    net.addLink(s02, s38, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s17, s38, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)

    
    print "*** Starting network"
    net.build()
    c0.start()
    s35.start( [c0] )
    s15.start( [c0] )
    s17.start( [c0] )
    s14.start( [c0] )
    s32.start( [c0] )
    s02.start( [c0] )
    s38.start( [c0] )

    print "*** Running CLI"
    CLI( net )

    print "*** Stopping network"
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    topology()


