#!/bin/bash


for i in `seq 2 $1`
do
	iperf -c 10.0.0.$i -b 1m -t 1000 &
done

