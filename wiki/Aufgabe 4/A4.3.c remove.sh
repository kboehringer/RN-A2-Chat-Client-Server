#!/bin/bash
sudo iptables -D INPUT -s 172.16.1.0/24 -p tcp --tcp-flags ACK,FIN,RST,URG,PSH -j ACCEPT
sudo iptables -D INPUT -s 172.16.1.0/24 -p tcp --tcp-flags SYN -j DROP