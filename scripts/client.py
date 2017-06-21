import socket   #for sockets
import sys  #for exit
 
# create dgram udp socket
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
except socket.error:
    print 'Failed to create socket'
    sys.exit()
 
host = 'localhost';
port = 8888;
videofile = "sample.mp4"

try:
    host = sys.argv[1]
    port = int(sys.argv[2])
    videofile = sys.argv[3]
except:
    print "[Usage: cliente.py host port videofile]\n"
    host = 'localhost';
    port = 8888;
    videofile = "sample.mp4"


s.sendto(videofile, (host, port))
d = s.recvfrom(1024)
size_data = int(d[0])

# FileNameIn = 'Hexdata.mp4'

# # create a file object: open it with "write" mode
# HexFile = open(FileNameIn,"w")

while(size_data > 0) :

    try :
        d = s.recvfrom(1024)

        data = d[0]
        addr = d[1]
        size_data -= len(data);
        print(size_data,len(data))


        # HexFile.write(data)
    except socket.error, msg:
        print 'Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
        # HexFile.close()
        sys.exit()

# HexFile.close()