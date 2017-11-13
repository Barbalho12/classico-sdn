import socket
import struct
import sys
import os
import thread
import time
import threading

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


# HOST = ''   # Symbolic name meaning all available interfaces
# PORT = 10000 # Arbitrary non-privileged port
# portAddr = 10000
# ipAddr = ''

# Python create mutex  
my_mutex = threading.Lock()  

s_controler = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s_controler.bind(('', 10002))

flag = True

def send_client(ipAddr, portAddr, nameFile):
	try :
		
		ip_group = "224.3.29.72"

		# Send group address to client
		s_controler.sendto(ip_group, (ipAddr, 10001))

		okRequest = s_controler.recvfrom(1024)
		# print("Client "+str(ipAddr)+":"+str(portAddr)+" > "+nameFile+" OK")
		
		# s_controler.close()
		if(flag):
			my_mutex.release()  # global my_mutex
		# print ("++++ Release")
	except :
		x = 2
		# print "Ops: send_client ERRO"
		# sys.exit()


def request_client():

	s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	s.bind(('', 10001))

	try :

		while(True):

			# Receive client request
			newRequest = s.recvfrom(20)
			
			nameFile = newRequest[0] #nome do arquivo video
			addr = newRequest[1] #Endereco do cliente
			ipAddr = addr[0]
			portAddr = addr[1]

			# print("New Client "+str(ipAddr)+":"+str(portAddr)+" > "+nameFile)
			thread.start_new_thread( send_client, (ipAddr, portAddr, nameFile) )

	except socket.error, msg :
		print 'Failed to create socket. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
		sys.exit()

	finally:
		s.close()

# Mutex for concurrent client requests
my_mutex.acquire()
t = thread.start_new_thread( request_client, () )

# os.system("echo SERVER INIT $(date +'%F %T,%3N') ")

# After the first request is processed the mutex is released and the content can be sent
my_mutex.acquire()
flag = False

# Evalvid evaluation
os.system("cd ../../evalvid && ./client.sh &")
os.system("cd ../../evalvid && ./mp4trace -f -s 224.3.29.71 10000 sample.mp4 > files/st01")
# os.system("echo SERVER FINISH $(date +'%F %T,%3N') ")
time.sleep(1)
os.system("sudo kill -2 $(ps -C 'tcpdump' -o pid=)")
