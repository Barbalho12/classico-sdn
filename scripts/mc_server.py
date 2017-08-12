import socket
import struct
import sys
# import subprocess
# import shlex
import os
# import threading
import thread
import time

multicast_group = ('224.3.29.71', 10000)
os.system("route add -host 224.3.29.71 "+sys.argv[1]+"-eth0")

# Create the datagram socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Set a timeout so the socket does not block indefinitely when trying
# to receive data.
sock.settimeout(0.2)

# Set the time-to-live for messages to 1 so they do not go past the
# local network segment.
ttl = struct.pack('b', 1)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl)

def startClient():
    os.system("cd ../evalvid && ./client.sh")
    # time.sleep(1)
    

try:
	
    thread.start_new_thread(startClient, ())
    time.sleep(1)
    os.system("cd ../evalvid && ./mp4trace -f -s 224.3.29.71 10000 sample.mp4 > files/st01")
    time.sleep(1)
    os.system("sudo kill -2 $(ps -C 'tcpdump' -o pid=)")

finally:
    print >> sys.stderr, 'closing socket'
    sock.close()

