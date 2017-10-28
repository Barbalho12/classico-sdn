#!/usr/bin/python

from mininet.net import Mininet
from mininet.node import  OVSController, OVSKernelSwitch, RemoteController
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.link import TCLink

def topology():

    "Create a network."
    net = Mininet( controller=RemoteController, link=TCLink, switch=OVSKernelSwitch )
	
    _bw=10
    _latency='100ms'
    _max_queue_size=100
    _use_htb=True
    _ip_remote_control='127.0.0.1'
    _port_remote_control=6653

    adp_USB100MB_MAC = "00:13:3B:85:05:05"

    print "*** Creating Hosts"
    h1 = net.addHost( 'h1', mac="00:00:00:00:00:01", ip='192.168.2.1' )

    h2 = net.addHost( 'h2', mac="00:00:00:00:00:02", ip='192.168.2.2' )
    h3 = net.addHost( 'h3', mac="00:00:00:00:00:03", ip='192.168.2.3' )
    
    h4 = net.addHost( 'h4', mac="00:00:00:00:00:04", ip='192.168.2.4' )
    h5 = net.addHost( 'h5', mac="00:00:00:00:00:05", ip='192.168.2.5' )
    
    
    print "*** Creating Switchs"
    s1 = net.addSwitch( 's1', dpid='00:00:00:00:aa:bb:cc:01' )
    s2 = net.addSwitch( 's2', dpid='00:00:00:00:aa:bb:cc:02' )
    s3 = net.addSwitch( 's3', dpid='00:00:00:00:aa:bb:cc:03' )
    s4 = net.addSwitch( 's4', dpid='00:00:00:00:aa:bb:cc:04' )
    
    
    print "*** Creating Controller Openflow"
    c0 = RemoteController( 'c0', ip=_ip_remote_control, port=_port_remote_control )

    print "*** Connecting hosts"
    net.addLink(h1, s1, bw=_bw, use_htb=_use_htb)

    net.addLink(h2, s2, bw=_bw, use_htb=_use_htb)
    net.addLink(h3, s3, bw=_bw, use_htb=_use_htb)

    net.addLink(h4, s4, bw=_bw, use_htb=_use_htb)
    net.addLink(h5, s4, bw=_bw, use_htb=_use_htb)
  

    print "*** Creating connection between switches"
    net.addLink(s1, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s1, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s2, s4, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s3, s4, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    

    print "*** Starting network"
    net.build()
    c0.start()
    s1.start( [c0] )
    s2.start( [c0] )
    s3.start( [c0] )
    s4.start( [c0] )
   

    print "*** Running CLI"
    CLI( net )

    print "*** Stopping network"
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    topology()


