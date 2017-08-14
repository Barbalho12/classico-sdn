import socket
import struct
import sys
# import subprocess
# import shlex
import os
# import threading
import thread
import time


HOST = ''   # Symbolic name meaning all available interfaces
PORT = 10000 # Arbitrary non-privileged port
 
# Datagram (udp) socket
try :
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    print 'Socket created'
except socket.error, msg :
    print 'Failed to create socket. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
 
 
# Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error , msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
     
print 'Socket bind complete'


try:
    newRequest = s.recvfrom(1024)
    nameFile = newRequest[0] #nome do arquivo video
    addr = newRequest[1] #Endereco do cliente
    ipAddr = addr[0]
    portAddr = addr[1]
except socket.error , msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
     

s.close()

def startClient():
    os.system("cd ../evalvid && ./client.sh")
    # time.sleep(1)
    

# try:
raw_input("Press Enter to continue...")
thread.start_new_thread(startClient, ())
time.sleep(1)
os.system("cd ../evalvid && ./mp4trace -f -s "+ipAddr+" 10000 sample.mp4 > files/st01")
time.sleep(1)
os.system("sudo kill -2 $(ps -C 'tcpdump' -o pid=)")

# finally:
    # print >> sys.stderr, 'closing socket'
    # sock.close()

