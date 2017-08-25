import socket
import struct
import sys
import subprocess
import os

multicast_group = '224.3.29.72'
os.system("route add -host 224.3.29.72 "+sys.argv[1]+"-eth0")
server_address = ('', 10000)


if len(sys.argv) > 2:
	try:   
		s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		s1.bind(('', 10001))
		data = s1.recvfrom(1024)
	finally:
	    s1.close()


# Create the socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Bind to the server address
sock.bind(server_address)

# Tell the operating system to add the socket to the multicast group
# on all interfaces.
group = socket.inet_aton(multicast_group)
mreq = struct.pack('4sL', group, socket.INADDR_ANY)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

try:
	script = "cd ../../evalvid &&"
	script += "rm -rf files/sd"+sys.argv[1]+" &&"
	if len(sys.argv) <= 2:
		script += "ufw enable &&"
	script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]
	os.system(script)

finally:
    print >> sys.stderr, 'closing socket'
    sock.close()