#!/bin/bash


sudo ufw enable
rm -rf files/sdh*

PORT1=12000
PORT=12000

for i in `seq 1 100`
do
	sudo tcpdump -n -tt -v udp port $PORT >  files/sdh$i &
	PORT=$(( PORT + 1 ))
	echo $PORT
done


sleep 1

clear


COUNT=`ps -C tcpdump --no-headers | wc -l`

echo "$COUNT processos Criados" 
echo "Escutando nas portas: $PORT1 ~ $PORT"
echo "Esperando finalizar... "
wait

sudo killall tcpdump

