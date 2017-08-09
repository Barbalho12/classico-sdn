import socket
import struct
import sys
import subprocess
import os

multicast_group = '224.3.29.71'
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


# Receive/respond loop
# while True:


    # print >>sys.stderr, '\nwaiting to receive message'
script = "cd ../evalvid &&"
script += "rm -rf files/sd"+sys.argv[1]+" &&"
script += "ufw enable &&"
script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]
os.system(script)
# os.system("cd ../evalvid &&")
# os.system("rm -rf files/sd"+sys.argv[1]+"")
# os.system("ufw enable"+"")
# os.system("tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1])
    # os.system("cd ../evalvid && ./mp4trace -f -s 224.3.29.71 10000 sample.mp4 > files/st01")
    # data, address = sock.recvfrom(1024)
    
    # print >>sys.stderr, 'received %s bytes from %s' % (len(data), address)
    # print >>sys.stderr, data

    # print >>sys.stderr, 'sending acknowledgement to', address
    # sock.sendto('ack', address)