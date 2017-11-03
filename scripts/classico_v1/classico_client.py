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
os.system("cd ../../evalvid && tcpdump -i any -n -tt -v udp port 10000 -c 1 > files/ts"+sys.argv[1]+" &") 
time.sleep(2)
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind(('', 10000))
    #Get setup time 
    os.system("echo CLIENT "+sys.argv[1]+" INIT $(date +'%F %T,%3N') ")

    s.sendto(videofile, (host, port))

except socket.error:
    print 'Failed to create socket'
    sys.exit()
 
s.close()

script = "cd ../../evalvid &&"
script += "rm -rf files/sd"+sys.argv[1]+" &&"
# if len(sys.argv) <= 2:
script += "ufw enable  > /dev/null &&"
script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]

os.system(script)
os.system("echo CLIENT "+sys.argv[1]+" END $(date +'%F %T,%3N') ")
os.system("awk 'END{print \"CLIENT "+sys.argv[1]+" PACKETS\", (NR-1)/2}' ../../evalvid/files/sd"+sys.argv[1])
# os.system("wc -l ../../evalvid/files/sd"+sys.argv[1])
os.system("a=$(awk 'NR==1 { print $1 }' ../../evalvid/files/ts"+sys.argv[1]+") && b=$(awk 'NR==1 { print $1}' ../../evalvid/files/sd"+sys.argv[1]+") && tempo=$(echo \"scale=5 ; $b - $a\" | bc) && echo CLIENT "+sys.argv[1]+" SETUP TIME = $tempo")

