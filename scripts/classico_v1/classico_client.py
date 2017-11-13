import socket
import struct
import sys
import subprocess
import os
import time

os.system("ufw enable  > /dev/null")

host = '192.168.2.1'
port = 10000
videofile = "sample.mp4"

# Script for evaluation of evalvid
script_evalvid = "cd ../../evalvid &&"
script_evalvid += "rm -rf files/sd"+sys.argv[1]+" &&"
# script_evalvid += "ufw enable  > /dev/null &&"
script_evalvid += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]

# Takes the request time 
os.system("cd ../../evalvid && tcpdump -i any -n -tt -v udp port 10000 -c 1 > files/ts"+sys.argv[1]+" &") 

# Do not interfere in anything
# time.sleep(2)

try:
	# Create socket send by 10000 port 
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind(('', 10000))

    # Show init time (no setup time)
    # os.system("echo CLIENT "+sys.argv[1]+" INIT $(date +'%F %T,%3N') ")

    # print("Send video request..")
    s.sendto(videofile, (host, port))

    # Close socket
    s.close()

except socket.error:
    print 'Failed to create socket'
    sys.exit()

#Run script for evaluation of evalvid
os.system(script_evalvid)

# Show end evaluation time
# os.system("echo CLIENT "+sys.argv[1]+" END $(date +'%F %T,%3N') ")

# show packets number captured by tcpdump 
# os.system("awk 'END{print \"CLIENT "+sys.argv[1]+" PACKETS\", (NR-1)/2}' ../../evalvid/files/sd"+sys.argv[1])

# Show setup time
# os.system("a=$(awk 'NR==1 { print $1 }' ../../evalvid/files/ts"+sys.argv[1]+") && b=$(awk 'NR==1 { print $1}' ../../evalvid/files/sd"+sys.argv[1]+") && tempo=$(echo \"scale=5 ; $b - $a\" | bc) && echo CLIENT "+sys.argv[1]+" SETUP TIME = $tempo")
text=sys.argv[1]
text= text.replace("h", "")
os.system("packets=$(awk 'END{print (NR-1)/2}' ../../evalvid/files/sd"+sys.argv[1]+") && a=$(awk 'NR==1 { print $1 }' ../../evalvid/files/ts"+sys.argv[1]+") && b=$(awk 'NR==1 { print $1}' ../../evalvid/files/sd"+sys.argv[1]+") && tempo=$(echo \"scale=5 ; $b - $a\" | bc) && echo "+text+" 0$tempo $packets")

