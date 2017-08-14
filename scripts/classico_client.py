import socket
import struct
import sys
import subprocess
import os

# create dgram udp socket
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
except socket.error:
    print 'Failed to create socket'
    sys.exit()
 
host = '192.168.2.1'
port = 10000;
videofile = "sample.mp4"


s.sendto(videofile, (host, port))

# s.close()
# Receive/respond loop
# while True:
# try:
script = "cd ../evalvid &&"
script += "rm -rf files/sd"+sys.argv[1]+" &&"
script += "ufw enable &&"
script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]
os.system(script)

   
    # data, address = sock.recvfrom(1024)
    
    # print >>sys.stderr, 'received %s bytes from %s' % (len(data), address)
    # print >>sys.stderr, data

    # print >>sys.stderr, 'sending acknowledgement to', address
    # sock.sendto('ack', address)

# finally:
#     # print >> sys.stderr, 'closing socket'
#     # sock.close()