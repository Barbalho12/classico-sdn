import socket
import struct
import sys
import os
import thread
import time

def timeAlert():
    time.sleep(10)
    print '10 seconds'

try:
    os.system("cd ../evalvid && ./client.sh &")
    time.sleep(1)
    thread.start_new_thread(timeAlert, ())
    os.system("cd ../evalvid && ./mp4trace -f -s 192.168.2.3 10000 sample.mp4 > files/st01")
    time.sleep(1)
    os.system("sudo kill -2 $(ps -C 'tcpdump' -o pid=)")

finally:
    print >> sys.stderr, 'closing socket'
    sock.close()

