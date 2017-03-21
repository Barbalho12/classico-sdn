#!/bin/bash

IFACE=`iwconfig 2>&1 | grep IEEE | awk '{print $1}'`

iperf -s -u &
