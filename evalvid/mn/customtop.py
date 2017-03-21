#!/usr/bin/python

"""
This example shows how to work with WiFi on Mininet.
"""

from mininet.net import Mininet
from mininet.node import  RemoteController, OVSController, OVSKernelSwitch
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.link import TCLink

def topology():
    "Create a network."
    net = Mininet( controller=RemoteController, link=TCLink, switch=OVSKernelSwitch )

    print "*** Creating nodes"
    h1 = net.addHost( 'h1' )
    h2 = net.addHost( 'h2' )
    s1 = net.addSwitch( 's1' )
    s2 = net.addSwitch( 's2' )
    c0 = net.addController('c0', controller=RemoteController, ip="127.0.0.1", port=6653)

    print "*** Associating Stations"
    net.addLink(h1, s1)
    net.addLink(h2, s2)
    #linkopts = {'bw':10, 'delay':'5ms', 'loss':1, 'max_queue_size':1000, 'use_htb':True}
    net.addLink(s1,s2,bw=10,latency='10ms',max_queue_size=1000,use_htb=True)
    #net.addLink(s1,s2, **linkopts)

    print "*** Starting network"
    net.build()
    c0.start()
    s1.start( [c0] )
    s2.start( [c0] )

    print "*** Running CLI"
    CLI( net )

    print "*** Stopping network"
    net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    topology()


