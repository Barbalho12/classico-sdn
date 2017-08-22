import socket
import struct
import sys
import subprocess
import os

try:
	script = "cd ../evalvid &&"
	script += "rm -rf files/sd"+sys.argv[1]+" &&"
	script += "ufw enable &&"
	script += "tcpdump -n -tt -v udp port 10000 >  files/sd"+sys.argv[1]
	os.system(script)

finally:
    print >> sys.stderr, 'closing socket'
    sock.close()