#!/usr/bin/env python
import os
from mininet.net import Mininet
from mininet.node import Controller, RemoteController
from mininet.cli import CLI
from mininet.link import TCLink
from mininet.link import Intf
from mininet.log import setLogLevel, info
 
def myNetwork():
  net = Mininet( topo=None, build=False)
 
  info( '*** Adding controller\n' )
  net.addController( 'c0', controller=RemoteController, ip='192.168.0.103', port=6653 )
  # net.addController(name='c0')
 
  info( '*** Add switches\n')
  s1 = net.addSwitch('s1')
  # Intf( 'eth0', node=s1 ) #Some Problem
 
  info( '*** Add hosts\n')
  h1 = net.addHost('h1', ip='0.0.0.0')
  h2 = net.addHost('h2', ip='0.0.0.0')
 
  info( '*** Add links\n')
  net.addLink(h1, s1, cls=TCLink, bw=10, delay='1ms', loss=0)
  net.addLink(h2, s1, cls=TCLink, bw=10, delay='1ms', loss=5)
  info( '*** Starting network\n')
  net.start()
  os.popen('ovs-vsctl add-port s1 eth0')
  h1.cmdPrint('dhclient '+h1.defaultIntf().name)
  h2.cmdPrint('dhclient '+h2.defaultIntf().name)
  CLI(net)
  net.stop()
 
if __name__ == '__main__':
  setLogLevel( 'info' )
  myNetwork()