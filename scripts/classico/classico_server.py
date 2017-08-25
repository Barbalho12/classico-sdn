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

try :
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    print 'Socket created'
    s.bind((HOST, PORT))
    print 'Socket bind complete'
    newRequest = s.recvfrom(1024)
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

def timeAlert():
    s1 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s1.sendto("A", ("192.168.2.23", 10001)) # IPERF
    time.sleep(5)
    time.sleep(5)
    s1.sendto("A", ("192.168.2.4", 10001))
    print "55"
    time.sleep(5)
    s1.sendto("A", ("192.168.2.5", 10001))
    print "45"
    time.sleep(5)
    s1.sendto("A", ("192.168.2.6", 10001))
    s1.sendto("A", ("192.168.2.24", 10001)) # IPERF
    print "40"
    time.sleep(5)
    s1.sendto("A", ("192.168.2.7", 10001))
    print "35"
    time.sleep(5)
    s1.sendto("A", ("192.168.2.8", 10001))
    print "30"
    time.sleep(5)
    print "25"
    time.sleep(5)
    print "20"
    time.sleep(5)
    print "15"
    time.sleep(5)
    print "10"
    time.sleep(5)
    print "5"
    time.sleep(1)
    print "4"
    time.sleep(1)
    print "3"
    time.sleep(1)
    print "2"
    time.sleep(1)
    print "1"
    s1.close()


raw_input("Press Enter to continue...")
os.system("cd ../../evalvid && ./client.sh &")
time.sleep(1)
thread.start_new_thread(timeAlert, ())
time.sleep(5)
os.system("cd ../../evalvid && ./mp4trace -f -s "+ipAddr+" 10000 sample.mp4 > files/st01")
time.sleep(1)
os.system("sudo kill -1 $(ps -C 'tcpdump' -o pid=)")



