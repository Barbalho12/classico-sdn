import socket
import struct
import sys
import subprocess
import os
import time

# try:   
# 	s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
# 	s1.bind(('', 10001))
# 	data = s1.recvfrom(1024)
# finally:
#     s1.close()

try:
	adress = ('192.168.2.51','192.168.2.52')
	x = 0
	while(10):
    	
	# script = "iperf -c 192.168.2."+sys.argv[1]+" -b "+sys.argv[2]+"m -t "+sys.argv[3]
		script = "iperf -c "+adress[x%2]+" -b "+sys.argv[1]+"m -t "+sys.argv[2]
		print script
		os.system(script)
		# time.sleep(int(sys.argv[2]))
		x+=1

finally:
    print 'END'