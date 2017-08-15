import socket
import struct
import sys
import subprocess
import os

host = '192.168.2.1'
port = 10000
videofile = "sample.mp4"

# create dgram udp socket
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind(('', 10000))
    s.sendto(videofile, (host, port))
except socket.error:
    print 'Failed to create socket'
    sys.exit()
 
s.close()

script = "cd ../evalvid &&"
script += "rm -rf files/sd"+sys.argv[1]+" &&"
script += "ufw enable &&"
script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]
os.system(script)

