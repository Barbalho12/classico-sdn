#!/bin/bash

sudo apt-get install openssh-server
sudo apt-get install openssh-client

sudo iptables -A INPUT -p tcp --dport 22 -m conntrack --ctstate NEW,ESTABLISHED -j ACCEPT
sudo iptables -A OUTPUT -p tcp --sport 22 -m conntrack --ctstate ESTABLISHED -j ACCEPT

sudo mv /etc/ssh/sshd_config /etc/ssh/sshd_config_BACKUP

sudo cp ssh_config_RASPBERRY.txt /etc/ssh/sshd_config

sudo /etc/init.d/ssh stop
sudo /etc/init.d/ssh start
