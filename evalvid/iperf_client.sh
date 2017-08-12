#!/bin/bash

# IFACE=`iwconfig 2>&1 | grep IEEE | awk '{print $1}'`
sudo ufw allow from 10.0.0.0/24 to any port 5001
iperf -s -u &
