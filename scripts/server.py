import time
import socket
import sys
import threading
import thread
 
HOST = ''   # Symbolic name meaning all available interfaces
PORT = 8888 # Arbitrary non-privileged port
 
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
 
#now keep talking with the client



#Receve nome do arquivo video
# d = s.recvfrom(1024)
# data = d[0] #nome do arquivo video
# addr = d[1] #Endereco do cliente

# #Ler arquivo
# bytes = open(data).read()

#Envia para o cliente o tamanho do arquivo
#s.sendto(str(len(bytes)), (addr[0], addr[1]))

def clientRequest(nameFile, ipAddr, portAddr):
    bytesFile = open(nameFile).read()
    s.sendto(str(len(bytesFile)), (ipAddr, portAddr))
    INIT = 0
    END = 1024
    while(True):
        s.sendto(bytesFile[INIT:END] , (ipAddr, portAddr))
        
        INIT += END - INIT

        if(len(bytesFile)-INIT > 1024):
            END += 1024
        else:
            END += len(bytesFile)-INIT
        print(INIT, END, len(bytesFile))

        if(INIT == END):
            break
        time.sleep(0.01)
    


while(True):
    newRequest = s.recvfrom(1024)
    nameFile = newRequest[0] #nome do arquivo video
    addr = newRequest[1] #Endereco do cliente
    ipAddr = addr[0]
    portAddr = addr[1]
    thread.start_new_thread( clientRequest, (nameFile, ipAddr, portAddr))

s.close()


