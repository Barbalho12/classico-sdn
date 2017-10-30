import socket
import struct
import sys
import subprocess
import os

multicast_group = '224.3.29.71'
os.system("route add -host 224.3.29.71 "+sys.argv[1]+"-eth0")

os.system("cd ../evalvid && tcpdump -n -tt -i "+sys.argv[1]+"-eth0 -c 1 > files/ts"+sys.argv[1]+" &")

server_address = ('', 10000)

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
	script = "cd ../evalvid &&"
	script += "rm -rf files/sd"+sys.argv[1]+" &&"
	script += "ufw enable &&"
	script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]
	os.system(script)

finally:
    print >> sys.stderr, 'closing socket'
    sock.close()