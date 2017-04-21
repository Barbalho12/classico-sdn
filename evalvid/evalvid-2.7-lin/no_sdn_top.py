#!/usr/bin/python

"""
This example shows how to work with WiFi on Mininet.
"""

from mininet.net import Mininet
from mininet.node import  OVSController, OVSKernelSwitch
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.link import TCLink

def topology():
    "Create a network."
    net = Mininet( controller=OVSController, link=TCLink, switch=OVSKernelSwitch )
	
    _bw=10
    _latency='5ms'
    _max_queue_size=100
    _use_htb=True
    # _ip_remote_control='127.0.0.1'
    # _port_remote_control=6653

    print "*** Creating nodes"
    h1 = net.addHost( 'h1', mac="00:00:00:00:00:01", ip='10.0.0.1' )
    h2 = net.addHost( 'h2', mac="00:00:00:00:00:02", ip='10.0.0.2' )
    h3 = net.addHost( 'h3', mac="00:00:00:00:00:03", ip='10.0.0.3' )
    h4 = net.addHost( 'h4', mac="00:00:00:00:00:04", ip='10.0.0.4' )
    h5 = net.addHost( 'h5', mac="00:00:00:00:00:05", ip='10.0.0.5' )
    h6 = net.addHost( 'h6', mac="00:00:00:00:00:06", ip='10.0.0.6' )
    h7 = net.addHost( 'h7', mac="00:00:00:00:00:07", ip='10.0.0.7' )
    h8 = net.addHost( 'h8', mac="00:00:00:00:00:08", ip='10.0.0.8' )
    h9 = net.addHost( 'h9', mac="00:00:00:00:00:09", ip='10.0.0.9' )
    h10 = net.addHost( 'h10', mac="00:00:00:00:00:10", ip='10.0.0.10' )
    h11 = net.addHost( 'h11', mac="00:00:00:00:00:11", ip='10.0.0.11' )
    h12 = net.addHost( 'h12', mac="00:00:00:00:00:12", ip='10.0.0.12' )
    s1 = net.addSwitch( 's1' )
    s2 = net.addSwitch( 's2' )
    s3 = net.addSwitch( 's3' )
    c0 = net.addController('c0', controller=OVSController)

    

    print "*** Associating Stations"
    net.addLink(h1, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h2, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h3, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h4, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h5, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h6, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h7, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h8, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h9, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h10, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h11, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(h12, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s1, s2, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    net.addLink(s1, s3, bw=_bw,latency=_latency,max_queue_size=_max_queue_size,use_htb=_use_htb)
    
    print "*** Starting network"
    net.build()
    c0.start()
    s1.start( [c0] )
    s2.start( [c0] )
    s3.start( [c0] )

    print "*** Running CLI"
    CLI( net )

    print "*** Stopping network"
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    topology()


