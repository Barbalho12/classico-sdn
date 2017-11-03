import socket
import struct
import sys
import subprocess
import os
import time

host = '192.168.2.1'
port = 10000
videofile = "sample.mp4"

# os.system("ufw enable")

if len(sys.argv) > 2:
	videofile = sys.argv[2]
	# try:   
	# 	s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	# 	s1.bind(('', 10001))
	# 	videofile, addr = s1.recvfrom(1024)
	# finally:
	#     s1.close()
os.system("cd ../../evalvid && tcpdump -i any -n -tt -v udp port 10010 -c 1 > files/ts"+sys.argv[1]+" &") 
time.sleep(2)
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind(('', 10010))
    #Get setup time 
    
    s.sendto(videofile, (host, port))
    # os.system("echo "+sys.argv[1]+" $(date +'%F %T,%3N') ")
except socket.error:
    print 'Failed to create socket'
    sys.exit()
 
s.close()

script = "cd ../../evalvid &&"
script += "rm -rf files/sd"+sys.argv[1]+" &&"
# if len(sys.argv) <= 2:
script += "ufw enable &&"
script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]
os.system(script)

x = os.system("a=$(awk 'NR==1 { print $1 }' ../../evalvid/files/tsh2) && b=$(awk 'NR==1 { print $1}' ../../evalvid/files/sdh2) && echo $a $b")
print(x)

