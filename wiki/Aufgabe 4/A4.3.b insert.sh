#!/bin/bash
sudo iptables -A INPUT -s 172.16.1.0/24 -p tcp --destination-port 51000 -j ACCEPT
sudo iptables -A INPUT -s 172.16.1.0/24 -p tcp --destination-port ! 51000 -j DROP