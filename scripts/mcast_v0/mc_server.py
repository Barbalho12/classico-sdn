import socket
import struct
import sys
# import subprocess
# import shlex
import os
# import threading
import thread
import time

multicast_group = ('224.3.29.71', 10000)
os.system("route add -host 224.3.29.71 "+sys.argv[1]+"-eth0")

# Create the datagram socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Set a timeout so the socket does not block indefinitely when trying
# to receive data.
sock.settimeout(0.2)

# Set the time-to-live for messages to 1 so they do not go past the
# local network segment.
ttl = struct.pack('b', 1)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl)

# def startClient():
#     os.system("cd ../evalvid && ./client.sh")


# def timeAlert():
#     passos = 60
#     while(passos > 0):
#         time.sleep(10)
#         passos -= 10
#         print passos,'seconds'
def timeAlert():
	s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	s1.sendto("A", ("192.168.2.23", 10001)) # IPERF
	time.sleep(5)
	time.sleep(5)
	s1.sendto("A", ("192.168.2.4", 10001))
	print "55"
	time.sleep(5)
	s1.sendto("A", ("192.168.2.5", 10001))
	print "45"
	time.sleep(5)
	s1.sendto("A", ("192.168.2.6", 10001))
	s1.sendto("A", ("192.168.2.24", 10001)) # IPERF
	print "40"
	time.sleep(5)
	s1.sendto("A", ("192.168.2.7", 10001))
	print "35"
	time.sleep(5)
	s1.sendto("A", ("192.168.2.8", 10001))
	print "30"
	time.sleep(5)
	print "25"
	time.sleep(5)
	print "20"
	time.sleep(5)
	print "15"
	time.sleep(5)
	print "10"
	time.sleep(5)
	print "5"
	time.sleep(1)
	print "4"
	time.sleep(1)
	print "3"
	time.sleep(1)
	print "2"
	time.sleep(1)
	print "1"
	s1.close()
	

try:
	
    # thread.start_new_thread(startClient, ())
    os.system("cd ../../evalvid && ./client.sh &")
    time.sleep(1)
    # thread.start_new_thread(timeAlert, ())
    # time.sleep(5)
    os.system("cd ../../evalvid && ./mp4trace -f -s 224.3.29.71 10000 sample.mp4 > files/st01")
    time.sleep(1)
    os.system("sudo kill -2 $(ps -C 'tcpdump' -o pid=)")

finally:
    print >> sys.stderr, 'closing socket'
    sock.close()

