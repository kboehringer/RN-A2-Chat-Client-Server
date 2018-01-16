sudo iptables -A OUTPUT -s 172.16.1.0/24 -p icmp --icmp-type 8 -j ACCEPT
sudo iptables -A INPUT -s 172.16.1.0/24 -p icmp --icmp-type 0 -j ACCEPT
sudo iptables -A INPUT -s 172.16.1.0/24 -p icmp --icmp-type echo-request -j DROP