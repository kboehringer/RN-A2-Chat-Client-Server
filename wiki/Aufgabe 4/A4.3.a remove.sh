#!/bin/bash
sudo iptables -D INPUT -s 172.16.1.0/24 -j DROP
sudo iptables -D OUTPUT -s 172.16.1.0/24 -j DROP