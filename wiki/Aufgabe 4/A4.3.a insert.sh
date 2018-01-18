#!/bin/bash
sudo iptables -A INPUT -s 172.16.1.0/24 -j DROP
sudo iptables -A OUTPUT -s 172.16.1.0/24 -j DROP