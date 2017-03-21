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
	

    print "*** Creating nodes"
    h1 = net.addHost( 'h1', mac="00:00:00:00:00:01", ip='10.0.0.1' )
    h2 = net.addHost( 'h2', mac="00:00:00:00:00:02", ip='10.0.0.2' )
    h3 = net.addHost( 'h3', mac="00:00:00:00:00:03", ip='10.0.0.3' )
    h4 = net.addHost( 'h4', mac="00:00:00:00:00:04", ip='10.0.0.4' )
    s1 = net.addSwitch( 's1' )
    s2 = net.addSwitch( 's2' )
    s3 = net.addSwitch( 's3' )
    #c0 = net.addController('c0', controller=OVSController)
    c0 = RemoteController( 'c0', ip='127.0.0.1', port=6653 )

    print "*** Associating Stations"
    net.addLink(h1, s2)
    net.addLink(h2, s2)
    net.addLink(h3, s3)
    net.addLink(h4, s3)
    net.addLink(s1,s2,bw=1,latency='5ms',max_queue_size=100,use_htb=True)
    net.addLink(s1,s3,bw=1,latency='5ms',max_queue_size=100,use_htb=True)
    
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


