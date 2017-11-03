import socket
import struct
import sys
import os
import thread
import time



HOST = ''   # Symbolic name meaning all available interfaces
PORT = 10000 # Arbitrary non-privileged port
portAddr = 10000
ipAddr = ''

# os.system("cd ../../evalvid && ./client.sh &")

try :
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    print 'SERVER Socket created'
    s.bind((HOST, PORT))
    print 'SERVER Socket bind complete'
    newRequest = s.recvfrom(1024)
    os.system("echo SERVER INIT $(date +'%F %T,%3N') ")
    nameFile = newRequest[0] #nome do arquivo video
    addr = newRequest[1] #Endereco do cliente
    ipAddr = addr[0]
    portAddr = addr[1]
except socket.error, msg :
    print 'Failed to create socket. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()
finally:
    print >> sys.stderr, 'closing socket'
    s.close()

time.sleep(1)
# raw_input("Press Enter to continue...")

os.system("cd ../../evalvid && ./client.sh &")
# time.sleep(2)
# thread.start_new_thread(timeAlert, ())
# time.sleep(1)
os.system("cd ../../evalvid && ./mp4trace -f -s "+ipAddr+" 10000 sample.mp4 > files/st01")
os.system("echo SERVER FINISH $(date +'%F %T,%3N') ")
time.sleep(1)

os.system("sudo kill -1 $(ps -C 'tcpdump' -o pid=)")



