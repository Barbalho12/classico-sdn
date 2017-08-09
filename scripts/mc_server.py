import socket
import struct
import sys
import subprocess
import shlex
import os

message = 'very important data'
multicast_group = ('224.3.29.71', 10000)

# Create the datagram socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Set a timeout so the socket does not block indefinitely when trying
# to receive data.
sock.settimeout(0.2)


# Set the time-to-live for messages to 1 so they do not go past the
# local network segment.
ttl = struct.pack('b', 1)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl)


try:

    # Send data to the multicast group
    print >>sys.stderr, 'sending "%s"' % message
    sent = sock.sendto(message, multicast_group)
    # subprocess.call(['cd ../evalvid && ./mp4trace -f -s 224.3.29.71 10000 sample.mp4 > files/st01'])
    # subprocess.call(shlex.split('cd ../evalvid && ls'))
    os.system("cd ../evalvid && ./mp4trace -f -s 224.3.29.71 10000 sample.mp4 > files/st01")

    # Look for responses from all recipients
    # while True:
    #     print >>sys.stderr, 'waiting to receive'
    #     try:
    #         data, server = sock.recvfrom(16)
    #     except socket.timeout:
    #         print >>sys.stderr, 'timed out, no more responses'
    #         break
    #     else:
    #         print >>sys.stderr, 'received "%s" from %s' % (data, server)

finally:
    print >>sys.stderr, 'closing socket'
    sock.close()