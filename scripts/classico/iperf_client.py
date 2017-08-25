import socket
import struct
import sys
import subprocess
import os

try:   
	s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	s1.bind(('', 10001))
	data = s1.recvfrom(1024)
finally:
    s1.close()

try:
	script = "iperf -c 192.168.2."+sys.argv[1]+" -b "+sys.argv[2]+"m -t "+sys.argv[3]
	os.system(script)
finally:
    print 'END'