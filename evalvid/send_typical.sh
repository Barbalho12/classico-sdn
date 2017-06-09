
sudo rm files/rd01 
sudo ufw enable
sudo tcpdump -i any -n -tt -v udp port 10020 > files/rd01 &
sudo tcpdump -i any -n -tt -v udp port 10015 > files/rd01 &

sleep 2

sudo ./mp4trace -f -s 192.168.2.120 10020 sample.mp4  > files/st01 &
sudo ./mp4trace -f -s 192.168.2.115 10015 sample.mp4  > files/st01 

sleep 2

sudo killall tcpdump
./get_data.sh

sleep 2

zip test-typical-2-flows files/*
