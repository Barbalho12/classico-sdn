#!/usr/bin/python

"""
This example shows how to work with WiFi on Mininet.
"""

from mininet.net import Mininet
from mininet.node import  OVSController, OVSKernelSwitch, RemoteController
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.link import TCLink

def topology():

    "Create a network."
    net = Mininet( controller=RemoteController, link=TCLink, switch=OVSKernelSwitch )
	
    _bw=10
    _latency='5ms'
    _max_queue_size=100
    _use_htb=True
    _ip_remote_control='127.0.0.1'
    _port_remote_control=6653

    print "*** Creating Hosts"
    h1 = net.addHost( 'h1', mac="00:00:00:00:00:01", ip='10.0.0.1' )
    h2 = net.addHost( 'h2', mac="00:00:00:00:00:02", ip='10.0.0.2' )
    h3 = net.addHost( 'h3', mac="00:00:00:00:00:03", ip='10.0.0.3' )
    h4 = net.addHost( 'h4', mac="00:00:00:00:00:04", ip='10.0.0.4' )
    h5 = net.addHost( 'h5', mac="00:00:00:00:00:05", ip='10.0.0.5' )
    h6 = net.addHost( 'h6', mac="00:00:00:00:00:06", ip='10.0.0.6' )
    h7 = net.addHost( 'h7', mac="00:00:00:00:00:07", ip='10.0.0.7' )
    h8 = net.addHost( 'h8', mac="00:00:00:00:00:08", ip='10.0.0.8' )


    print "*** Creating Switchs"
    s1 = net.addSwitch( 's1', dpid='00:00:00:00:aa:bb:cc:35' )
    s2 = net.addSwitch( 's2', dpid='00:00:00:00:aa:bb:cc:15' )
    s3 = net.addSwitch( 's3', dpid='00:00:00:00:aa:bb:cc:17' )
    s4 = net.addSwitch( 's4', dpid='00:00:00:00:aa:bb:cc:10' )
    s5 = net.addSwitch( 's5', dpid='00:00:00:00:aa:bb:cc:14' )
    s6 = net.addSwitch( 's6', dpid='00:00:00:00:aa:bb:cc:16' )
    s7 = net.addSwitch( 's7', dpid='00:00:00:00:aa:bb:cc:13' )
    s8 = net.addSwitch( 's8', dpid='00:00:00:00:aa:bb:cc:36' )
    

    print "*** Creating Controller Openflow"
    c0 = RemoteController( 'c0', ip=_ip_remote_control, port=_port_remote_control )

    

    print "*** Creating connection between switches"
    net.addLink(s1, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s1, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s2, s4, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s2, s5, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s3, s4, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s3, s5, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s4, s6, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s4, s7, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s5, s6, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s5, s7, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s6, s8, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s7, s8, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)

    print "*** Connecting hosts"
    net.addLink(h1, s1, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h2, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h3, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h4, s4, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h5, s5, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h6, s6, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h7, s7, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h8, s8, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
   

    
    print "*** Starting network"
    net.build()
    c0.start()
    s1.start( [c0] )
    s2.start( [c0] )
    s3.start( [c0] )
    s4.start( [c0] )
    s5.start( [c0] )
    s6.start( [c0] )
    s7.start( [c0] )
    s8.start( [c0] )

    print "*** Running CLI"
    CLI( net )

    print "*** Stopping network"
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    topology()


