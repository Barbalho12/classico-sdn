#!/bin/bash


for i in `seq 1 $1`
do
	iperf -c 10.0.0.$i -b 1m -t 1000 &
done

