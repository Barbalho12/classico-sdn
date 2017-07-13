#!/bin/bash
#
# Server
rm -rf files/sd$1
ufw enable
tcpdump -n -tt -v udp port $2 >  files/sd$1
