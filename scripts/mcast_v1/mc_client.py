import socket
import struct
import sys
import subprocess
import os
import time

multicast_group = '224.3.29.72'
os.system("route add -host 224.3.29.72 "+sys.argv[1]+"-eth0")
server_address = ('', 10000)

ip_server = '192.168.2.1'
port_server = 10000
content = "sample.mp4"

# Script for evaluation of evalvid
script_evalvid = "cd ../../evalvid &&"
script_evalvid += "rm -rf files/sd"+sys.argv[1]+" &&"
script_evalvid += "ufw enable  > /dev/null &&"
script_evalvid += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]

# Takes the request time 
os.system("cd ../../evalvid && tcpdump ether multicast -n -tt -c 1 > files/ts"+sys.argv[1]+" &") 

# Do not interfere in anything
time.sleep(2)

try:
	# Create the socket
	sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

	# Bind to the server address
	sock.bind(server_address)

	# Tell the operating system to add the socket to the multicast group
	# on all interfaces.
	group = socket.inet_aton(multicast_group)
	mreq = struct.pack('4sL', group, socket.INADDR_ANY)
	sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)
finally:
    # print >> sys.stderr, 'closing socket'
    sock.close()
    # sys.exit()

# Close socket
# s.close()

#Run script for evaluation of evalvid
os.system(script_evalvid)

# Show end evaluation time
os.system("echo CLIENT "+sys.argv[1]+" END $(date +'%F %T,%3N') ")

# show packets number captured by tcpdump 
os.system("awk 'END{print \"CLIENT "+sys.argv[1]+" PACKETS\", (NR-1)/2}' ../../evalvid/files/sd"+sys.argv[1])

# Show setup time
os.system("a=$(awk 'NR==1 { print $1 }' ../../evalvid/files/ts"+sys.argv[1]+") && b=$(awk 'NR==1 { print $1}' ../../evalvid/files/sd"+sys.argv[1]+") && tempo=$(echo \"scale=5 ; $b - $a\" | bc) && echo CLIENT "+sys.argv[1]+" SETUP TIME = $tempo")

