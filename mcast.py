#!/usr/bin/python

from mininet.net import Mininet
from mininet.node import  OVSController, OVSKernelSwitch, RemoteController
from mininet.cli import CLI
from mininet.log import setLogLevel
from mininet.link import TCLink
import time
import os
import thread

def link_down(net):
    time.sleep(31)
    print "*** Link Down"
    net.configLinkStatus('s15','s17','down')
    os.system("echo \"$(date +'%F %T,%3N') Link s15 - s17 Down\"")
    # os.system("echo \"$(date +'%F %T,%3N') Link s15 - s17 Down\" >> scripts/mcast_v1/log.txt")

def topology():

    # "Create a network."
    net = Mininet( controller=RemoteController, link=TCLink, switch=OVSKernelSwitch )
	
    _bw=10
    _latency='5ms'
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
    h6 = net.addHost( 'h6', mac="00:00:00:00:00:06", ip='192.168.2.6' )
    h7 = net.addHost( 'h7', mac="00:00:00:00:00:07", ip='192.168.2.7' )

    h8 = net.addHost( 'h8', mac="00:00:00:00:00:08", ip='192.168.2.8' )
    h9 = net.addHost( 'h9', mac="00:00:00:00:00:09", ip='192.168.2.9' )
    h10 = net.addHost( 'h10', mac="00:00:00:00:00:10", ip='192.168.2.10' )

    h11 = net.addHost( 'h11', mac="00:00:00:00:00:11", ip='192.168.2.11' )
    # h12 = net.addHost( 'h12', mac="00:00:00:00:00:12", ip='192.168.2.12' )
    # h13 = net.addHost( 'h13', mac="00:00:00:00:00:13", ip='192.168.2.13' )

    # hc = net.addHost( 'hc', mac="00:00:00:00:00:50", ip='192.168.2.50' )
    # hs1 = net.addHost( 'hs1', mac="00:00:00:00:00:51", ip='192.168.2.51' )
    # hs2 = net.addHost( 'hs2', mac="00:00:00:00:00:52", ip='192.168.2.52' )
    
    print "*** Creating Switchs"
    s35 = net.addSwitch( 's35', dpid='00:00:00:00:aa:bb:cc:35' )
    s32 = net.addSwitch( 's32', dpid='00:00:00:00:aa:bb:cc:32' )
    s15 = net.addSwitch( 's15', dpid='00:00:00:00:aa:bb:cc:15' )
    s14 = net.addSwitch( 's14', dpid='00:00:00:00:aa:bb:cc:14' )
    s02 = net.addSwitch( 's02', dpid='00:00:00:00:aa:bb:cc:02' )
    s17 = net.addSwitch( 's17', dpid='00:00:00:00:aa:bb:cc:17' )
    s38 = net.addSwitch( 's38', dpid='00:00:00:00:aa:bb:cc:38' )
    
    print "*** Creating Controller Openflow"
    c0 = RemoteController( 'c0', ip=_ip_remote_control, port=_port_remote_control )

    print "*** Connecting hosts"
    net.addLink(h1, s32, bw=_bw, use_htb=_use_htb)

    net.addLink(h2, s38, bw=_bw, use_htb=_use_htb)
    net.addLink(h3, s38, bw=_bw, use_htb=_use_htb)
    net.addLink(h4, s38, bw=_bw, use_htb=_use_htb)
    
    net.addLink(h5, s02, bw=_bw, use_htb=_use_htb)
    net.addLink(h6, s02, bw=_bw, use_htb=_use_htb)
    net.addLink(h7, s02, bw=_bw, use_htb=_use_htb)

    net.addLink(h8, s17, bw=_bw, use_htb=_use_htb)
    net.addLink(h9, s17, bw=_bw, use_htb=_use_htb)
    net.addLink(h10, s17, bw=_bw, use_htb=_use_htb)

    net.addLink(h11, s35, bw=_bw, use_htb=_use_htb)
    # net.addLink(h12, s35, bw=_bw, use_htb=_use_htb)
    # net.addLink(h13, s35, bw=_bw, use_htb=_use_htb)

    # net.addLink(hc, s32, bw=_bw, use_htb=_use_htb)
    # net.addLink(hs1, s14, bw=_bw, use_htb=_use_htb)
    # net.addLink(hs2, s15, bw=_bw, use_htb=_use_htb)

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

    try:

        #Run Floodlight in another terminal
        exec_floodlight="cd ../floodlight && ant && java -jar target/floodlight.jar > mcast_log.txt && exit"
        os.system("gnome-terminal -x sh -c '"+exec_floodlight+" ; bash'")

        #wait time for compile and run Floodlight
        time.sleep(40)

        # net.pingAll()

        #Arquivo de log dos hosts
        os.system("rm -f scripts/mcast_v1/log.txt")
        # os.system("cd scripts/mcast_v1 && echo '' > log.txt")
        print "\n"

        #List of hots
        hosts = [h2, h3, h4, h5, h6, h7, h8, h9, h10, h11]#, h12, h13]

        #Start Server
        h1.cmd('cd scripts/mcast_v1 && python mc_server.py h1 >> log.txt &')
        print "H1 START "
        time.sleep(2)

        thread.start_new_thread( link_down, (net,) )

        #Starts a host every 5 seconds
        for i in range(2, (len(hosts)+2)):
            
            hosts[i-2].cmd('cd scripts/mcast_v1 && python mc_client.py h'+str(i)+' >> log.txt &')
            if(i==2):
                os.system("sudo tcpdump -i any -n -tt -v tcp port 6653 > evalvid/files/open_flow_mcast.txt &")
                os.system("sudo tcpdump -i any -n -tt -v igmp > evalvid/files/igmp_mcast.txt &")
            print("H"+str(i)+" START")
            time.sleep(5)

    except:
        print 'Failed '


    try:
        time.sleep(15)
        os.system("sudo kill -1 $(ps -C 'java -jar target/floodlight.jar' -o pid=)")
        time.sleep(1)
        os.system("mv scripts/mcast_v1/log.txt evalvid/files && mv ../floodlight/mcast_log.txt evalvid/files")
        time.sleep(1)
        os.system("cd evalvid/files && sort -n -k1 log.txt -o log.txt")
        time.sleep(1)
        os.system("sudo kill -1 $(ps -C 'sh -c cd ../floodlight && ant && java -jar target/floodlight.jar > mcast_log.txt' -o pid=)")
        
        # raw_input("\nPress Enter to continue...\n")
        # os.system("cd evalvid && ./evaluation_complete.sh h2 mcast_10h_5s_linkdown")
    finally:
        print "*** Stopping network"
        net.stop()

if __name__ == '__main__':
    setLogLevel( 'info' )
    topology()


