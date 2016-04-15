Homework 3: Networking
CSE 534, Spring 2016
Instructor: Aruna Balasubramanian
Due date: 4/13/2016, 9.00pm

************************************************
Submitted By: Alpit Kumar Gupta
Solar Id: 110451714
************************************************

************************************************
Part 0
************************************************
Followed the specified setup procedures by installing
a. Mininet
b. MiniNext
c. Quagga

************************************************
Part A
************************************************

A1:
	#copied /home/mininet/USC-NSL-miniNExT-75c2781/examples/quagga-ixpto example project into myfolder
	#Modified topo.py to add Host H1 H2, Router R1 R2 R3 R4 with their eth0 port addresses and added links in between them
	#Additionally i have used below miniNext command to assign ip addresses to other eth1/eth2 ports
		R1 ip address add 173.0.0.1/24 dev R1-eth1
		R1 ip address add 174.0.0.1/24 dev R1-eth2
		R2 ip address add 175.0.0.1/24 dev R2-eth1
		R3 ip address add 176.0.0.1/24 dev R3-eth1
		R4 ip address add 175.0.0.2/24 dev R4-eth1
		R4 ip address add 176.0.0.2/24 dev R4-eth2


	mininext> pingall
	*** Ping: testing ping reachability
	H1 -> X R1 X X X
	H2 -> X X X X R4
	R1 -> H1 X R2 R3 X
	R2 -> X X X X X
	R3 -> X X X X X
	R4 -> X H2 X X X
	
	
	mininext> nodes
	available nodes are:
	H1 H2 R1 R2 R3 R4 c0
	
	mininext> H1 ip route
	172.0.0.0/24 dev H1-eth0  proto kernel  scope link  src 172.0.0.1
	
	mininext> H2 ip route
	177.0.0.0/24 dev H2-eth0  proto kernel  scope link  src 177.0.0.2
	
	mininext> R1 ip route
	172.0.0.0/24 dev R1-eth0  proto kernel  scope link  src 172.0.0.2
	173.0.0.0/24 dev R1-eth1  proto kernel  scope link  src 173.0.0.1
	174.0.0.0/24 dev R1-eth2  proto kernel  scope link  src 174.0.0.1
	
	mininext> R2 ip route
	173.0.0.0/24 dev R2-eth0  proto kernel  scope link  src 173.0.0.2
	175.0.0.0/24 dev R2-eth1  proto kernel  scope link  src 175.0.0.1
	
	mininext> R3 ip route
	174.0.0.0/24 dev R3-eth0  proto kernel  scope link  src 174.0.0.2
	176.0.0.0/24 dev R3-eth1  proto kernel  scope link  src 176.0.0.1

	mininext> R4 ip route
	175.0.0.0/24 dev R4-eth1  proto kernel  scope link  src 175.0.0.2
	176.0.0.0/24 dev R4-eth2  proto kernel  scope link  src 176.0.0.2
	177.0.0.0/24 dev R4-eth0  proto kernel  scope link  src 177.0.0.1
	
	a). topo.py along with assignIP.sh present in \HW3_110451714_FCN\PartA\A1
	
	b). Network topology figure, NetworkTopology.jpg/topology.docs present in \HW3_110451714_FCN\PartA\A1
	   I have added the complete network diagram including port, IP and subnet information.
	
	
A2:
	a). Steps explained for static routing

	#Enabling IP forwarding variable to 1
		H1 echo 1 > /proc/sys/net/ipv4/ip_forward
		R1 echo 1 > /proc/sys/net/ipv4/ip_forward
		R2 echo 1 > /proc/sys/net/ipv4/ip_forward
		R3 echo 1 > /proc/sys/net/ipv4/ip_forward
		R4 echo 1 > /proc/sys/net/ipv4/ip_forward
		H2 echo 1 > /proc/sys/net/ipv4/ip_forward


	Adding static routes:

	H1 ip route add 177.0.0.0/24 via 172.0.0.2 dev H1-eth0
	H1 ip route add 174.0.0.0/24 via 172.0.0.2 dev H1-eth0
	R1 ip route add 177.0.0.0/24 via 173.0.0.2 dev R1-eth1
	R1 ip route add 177.0.0.0/24 via 174.0.0.2 dev R1-eth2
	R2 ip route add 177.0.0.0/24 via 175.0.0.2 dev R2-eth1
	R2 ip route add 172.0.0.0/24 via 173.0.0.1 dev R2-eth0
	R3 ip route add 177.0.0.0/24 via 176.0.0.2 dev R3-eth1
	R3 ip route add 172.0.0.0/24 via 174.0.0.1 dev R3-eth0
	R4 ip route add 177.0.0.0/24 via 177.0.0.2 dev R4-eth0
	R4 ip route add 172.0.0.0/24 via 176.0.0.1 dev R4-eth2
	H2 ip route add 172.0.0.0/24 via 177.0.0.2 dev H2-eth0
	H2 ip route add 175.0.0.0/24 via 177.0.0.2 dev H2-eth0
	H2 ip route add 176.0.0.0/24 via 177.0.0.2 dev H2-eth0

	FORWARD RELAYING: H1 to H2
	R1 iptables -t nat -A POSTROUTING -o R1-eth0 -j MASQUERADE
	R1 iptables -A FORWARD -i R1-eth0 -o R1-eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT
	R1 iptables -A FORWARD -i R1-eth0 -o R1-eth1 -j ACCEPT

	R2 iptables -t nat -A POSTROUTING -o R2-eth1 -j MASQUERADE
	R2 iptables -A FORWARD -i R2-eth0 -o R2-eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT
	R2 iptables -A FORWARD -i R2-eth0 -o R2-eth1 -j ACCEPT

	R4 iptables -t nat -A POSTROUTING -o R4-eth1 -j MASQUERADE
	R4 iptables -A FORWARD -i R4-eth1 -o R4-eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
	R4 iptables -A FORWARD -i R4-eth1 -o R4-eth0 -j ACCEPT


	REVERSE REALYING:- H2 to H1

	R4 iptables -A FORWARD -i R4-eth0 -o R4-eth2 -j ACCEPT
	R4 iptables -A FORWARD -i R4-eth0 -o R4-eth2 -m state --state RELATED,ESTABLISHED -j ACCEPT
	R4 iptables -t nat -A POSTROUTING -o R4-eth2 -j MASQUERADE

	R3 iptables -A FORWARD -i R3-eth1 -o R3-eth0 -j ACCEPT
	R3 iptables -A FORWARD -i R3-eth1 -o R3-eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
	R3 iptables -t nat -A POSTROUTING -o R3-eth0 -j MASQUERADE
	
	
	Please find the routing table screenshot for every node in the folder:  
	To establish static routes, i have route every source to destination via another HOP.

	For example: 
		H1 ip route add 177.0.0.0/24 via 172.0.0.2 dev H1-eth0
		H1 will send data packets to subnet 177.0.0.0/24 via HOP 172.0.0.2


			mininext> H1 ip route
			172.0.0.0/24 dev H1-eth0  proto kernel  scope link  src 172.0.0.1
			174.0.0.0/24 via 172.0.0.2 dev H1-eth0
			177.0.0.0/24 via 172.0.0.2 dev H1-eth0
			mininext> H2 ip route
			172.0.0.0/24 via 177.0.0.1 dev H2-eth0
			175.0.0.0/24 via 177.0.0.1 dev H2-eth0
			176.0.0.0/24 via 177.0.0.1 dev H2-eth0
			177.0.0.0/24 dev H2-eth0  proto kernel  scope link  src 177.0.0.2
			mininext> R1 ip route
			172.0.0.0/24 dev R1-eth0  proto kernel  scope link  src 172.0.0.2
			173.0.0.0/24 dev R1-eth1  proto kernel  scope link  src 173.0.0.1
			174.0.0.0/24 dev R1-eth2  proto kernel  scope link  src 174.0.0.1
			177.0.0.0/24 via 173.0.0.2 dev R1-eth1
			mininext> R2 ip route
			172.0.0.0/24 via 173.0.0.1 dev R2-eth0
			173.0.0.0/24 dev R2-eth0  proto kernel  scope link  src 173.0.0.2
			175.0.0.0/24 dev R2-eth1  proto kernel  scope link  src 175.0.0.1
			177.0.0.0/24 via 175.0.0.2 dev R2-eth1
			mininext> R3 ip route
			172.0.0.0/24 via 174.0.0.1 dev R3-eth0
			174.0.0.0/24 dev R3-eth0  proto kernel  scope link  src 174.0.0.2
			176.0.0.0/24 dev R3-eth1  proto kernel  scope link  src 176.0.0.1
			177.0.0.0/24 via 176.0.0.2 dev R3-eth1
			mininext> R4 ip route
			172.0.0.0/24 via 176.0.0.1 dev R4-eth2
			175.0.0.0/24 dev R4-eth1  proto kernel  scope link  src 175.0.0.2
			176.0.0.0/24 dev R4-eth2  proto kernel  scope link  src 176.0.0.2
			177.0.0.0/24 dev R4-eth0  proto kernel  scope link  src 177.0.0.1
	
	
	mininext> H1 ping H2
	PING 177.0.0.2 (177.0.0.2) 56(84) bytes of data.
	64 bytes from 177.0.0.2: icmp_seq=1 ttl=61 time=0.650 ms
	64 bytes from 177.0.0.2: icmp_seq=2 ttl=61 time=0.336 ms
	64 bytes from 177.0.0.2: icmp_seq=3 ttl=61 time=0.337 ms
	64 bytes from 177.0.0.2: icmp_seq=4 ttl=61 time=0.346 ms
	64 bytes from 177.0.0.2: icmp_seq=5 ttl=61 time=0.339 ms
	64 bytes from 177.0.0.2: icmp_seq=6 ttl=61 time=0.380 ms
	64 bytes from 177.0.0.2: icmp_seq=7 ttl=61 time=0.338 ms

	--- 177.0.0.2 ping statistics ---
	7 packets transmitted, 7 received, 0% packet loss, time 5998ms
	rtt min/avg/max/mdev = 0.336/0.389/0.650/0.108 ms
	

	b). mininext> H1 traceroute H2

	traceroute to 177.0.0.2 (177.0.0.2), 30 hops max, 60 byte packets
	 1  172.0.0.2 (172.0.0.2)  0.082 ms  0.031 ms  0.030 ms
	 2  175.0.0.1 (175.0.0.1)  0.088 ms  0.043 ms  0.036 ms
	 3  * * *
	 4  177.0.0.2 (177.0.0.2)  0.109 ms  0.088 ms  0.086 ms
	
	mininext> H2 traceroute H1
	traceroute to 172.0.0.1 (172.0.0.1), 30 hops max, 60 byte packets
	 1  177.0.0.1 (177.0.0.1)  0.222 ms  0.137 ms  0.134 ms
	 2  176.0.0.1 (176.0.0.1)  0.384 ms  0.236 ms  0.225 ms
	 3  * * *
	 4  172.0.0.1 (172.0.0.1)  0.407 ms  0.384 ms  0.378 ms


****************************************************************************************


**********************************************	 
Part B:
**********************************************

B1:

	#Enable zebra=yes and ripd=yes in /etc/quagga/daemons file
	#cp /usr/share/doc/quagga/examples/zebra.conf.sample /etc/quagga/zebra.conf
	#cp /usr/share/doc/quagga/examples/ripd.conf.sample /etc/quagga/ripd.conf

	Now, copy conf files from /etc/quagga/*.conf to /myfolder/configs/X
	X would be every node H1, H2, R1, R2, R3, R4
	This would enable the zebra and ripd capabilities.
	
	restart the quagga service: /etc/init.d/quagga restart
		root@wings-GB-BXBT-2807:/home/wings/Alpit/fcn/myfolder/configs/R4# /etc/init.d/quagga restart
		Stopping Quagga monitor daemon: (waiting) .. watchquagga.
		Stopping Quagga daemons (prio:0): (waiting) .. ripd zebra (bgpd) (ripngd) (ospfd) (ospf6d) (isisd) (babeld).
		Removing all routes made by zebra.
		Loading capability module if not yet done.
		Starting Quagga daemons (prio:10): zebra ripd.
		Starting Quagga monitor daemon: watchquagga.

		
	#Enabling IP forwarding variable to 1
		H1 echo 1 > /proc/sys/net/ipv4/ip_forward
		R1 echo 1 > /proc/sys/net/ipv4/ip_forward
		R2 echo 1 > /proc/sys/net/ipv4/ip_forward
		R3 echo 1 > /proc/sys/net/ipv4/ip_forward
		R4 echo 1 > /proc/sys/net/ipv4/ip_forward
		H2 echo 1 > /proc/sys/net/ipv4/ip_forward
		
	Now to configure every router/host with Quagga ripd daemons, i followed the below configurations
	
	login to any router/host
		cd /miniNExT/util
		./mx R1-> launch any host/router
		netstat -na-> find the port of running ripd
		telnet localhost 2602-> remote access for localhost/ripd process
		en-> enable
		configure terminal-> to configure the ripd terminal
		router rip-> Enables RIP as a routing protocol
		network X.X.X.X/24-> X.X.X.X is the network number of the directly connected network you want to advertise.
		write-> save the config
		exit-> come out of the terminal


B2:

	a)	
		mininext> H1 ip route
		172.0.0.0/24 dev H1-eth0  proto kernel  scope link  src 172.0.0.1
		173.0.0.0/24 via 172.0.0.2 dev H1-eth0  proto zebra  metric 2
		174.0.0.0/24 via 172.0.0.2 dev H1-eth0  proto zebra  metric 2
		175.0.0.0/24 via 172.0.0.2 dev H1-eth0  proto zebra  metric 3
		176.0.0.0/24 via 172.0.0.2 dev H1-eth0  proto zebra  metric 3
		177.0.0.0/24 via 172.0.0.2 dev H1-eth0  proto zebra  metric 4
		mininext> H2 ip route
		172.0.0.0/24 via 177.0.0.1 dev H2-eth0  proto zebra  metric 4
		173.0.0.0/24 via 177.0.0.1 dev H2-eth0  proto zebra  metric 3
		174.0.0.0/24 via 177.0.0.1 dev H2-eth0  proto zebra  metric 3
		175.0.0.0/24 via 177.0.0.1 dev H2-eth0  proto zebra  metric 2
		176.0.0.0/24 via 177.0.0.1 dev H2-eth0  proto zebra  metric 2
		177.0.0.0/24 dev H2-eth0  proto kernel  scope link  src 177.0.0.2
		mininext> R1 ip route
		172.0.0.0/24 dev R1-eth0  proto kernel  scope link  src 172.0.0.2
		173.0.0.0/24 dev R1-eth1  proto kernel  scope link  src 173.0.0.1
		174.0.0.0/24 dev R1-eth2  proto kernel  scope link  src 174.0.0.1
		175.0.0.0/24 via 173.0.0.2 dev R1-eth1  proto zebra  metric 2
		176.0.0.0/24 via 174.0.0.2 dev R1-eth2  proto zebra  metric 2
		177.0.0.0/24 via 173.0.0.2 dev R1-eth1  proto zebra  metric 3
		mininext> R2 ip route
		172.0.0.0/24 via 173.0.0.1 dev R2-eth0  proto zebra  metric 2
		173.0.0.0/24 dev R2-eth0  proto kernel  scope link  src 173.0.0.2
		174.0.0.0/24 via 173.0.0.1 dev R2-eth0  proto zebra  metric 2
		175.0.0.0/24 dev R2-eth1  proto kernel  scope link  src 175.0.0.1
		176.0.0.0/24 via 175.0.0.2 dev R2-eth1  proto zebra  metric 2
		177.0.0.0/24 via 175.0.0.2 dev R2-eth1  proto zebra  metric 2
		mininext> R3 ip route
		172.0.0.0/24 via 174.0.0.1 dev R3-eth0  proto zebra  metric 2
		173.0.0.0/24 via 174.0.0.1 dev R3-eth0  proto zebra  metric 2
		174.0.0.0/24 dev R3-eth0  proto kernel  scope link  src 174.0.0.2
		175.0.0.0/24 via 176.0.0.2 dev R3-eth1  proto zebra  metric 2
		176.0.0.0/24 dev R3-eth1  proto kernel  scope link  src 176.0.0.1
		177.0.0.0/24 via 176.0.0.2 dev R3-eth1  proto zebra  metric 2
		mininext> R4 ip route
		172.0.0.0/24 via 175.0.0.1 dev R4-eth1  proto zebra  metric 3
		173.0.0.0/24 via 175.0.0.1 dev R4-eth1  proto zebra  metric 2
		174.0.0.0/24 via 176.0.0.1 dev R4-eth2  proto zebra  metric 2
		175.0.0.0/24 dev R4-eth1  proto kernel  scope link  src 175.0.0.2
		176.0.0.0/24 dev R4-eth2  proto kernel  scope link  src 176.0.0.2
		177.0.0.0/24 dev R4-eth0  proto kernel  scope link  src 177.0.0.1
	
		
		mininext> pingall
		*** Ping: testing ping reachability
		H1 -> H2 R1 R2 R3 R4
		H2 -> H1 R1 R2 R3 R4
		R1 -> H1 H2 R2 R3 R4
		R2 -> H1 H2 R1 R3 R4
		R3 -> H1 H2 R1 R2 R4
		R4 -> H1 H2 R1 R2 R3
		*** Results: 0% dropped (30/30 received)
		
	
	b)	mininext> H1 traceroute H2
		traceroute to 177.0.0.2 (177.0.0.2), 30 hops max, 60 byte packets
		 1  172.0.0.2 (172.0.0.2)  0.210 ms  0.184 ms  0.138 ms
		 2  173.0.0.2 (173.0.0.2)  0.236 ms  0.215 ms  0.212 ms
		 3  175.0.0.2 (175.0.0.2)  0.299 ms  0.281 ms  0.277 ms
		 4  177.0.0.2 (177.0.0.2)  0.363 ms  0.344 ms  0.343 ms
	
	c)	mininext> H1 ping -c 1 H2
		PING 177.0.0.2 (177.0.0.2) 56(84) bytes of data.
		64 bytes from 177.0.0.2: icmp_seq=1 ttl=61 time=0.453 ms

		--- 177.0.0.2 ping statistics ---
		1 packets transmitted, 1 received, 0% packet loss, time 0ms
		rtt min/avg/max/mdev = 0.453/0.453/0.453/0.000 ms


B3: 

	a)	
	Enter R2:
	root@wings-GB-BXBT-2807:/# telnet localhost 2602
	Trying 127.0.0.1...
	Connected to localhost.
	Escape character is '^]'.

	Hello, this is Quagga (version 0.99.22.4).
	Copyright 1996-2005 Kunihiro Ishiguro, et al.


	User Access Verification

	Password:
	ripd> en
	ripd# configure terminal
	ripd(config)# router rip
	ripd(config-router)# no network 173.0.0.0/24
	ripd(config-router)# write
	Configuration saved to /etc/quagga/ripd.conf

	b)
	Down time= Wed Apr 13 02:42:24 EDT 2016
	Re-establishment time= Wed Apr 13 02:43:12 EDT 2016
	Time to re-establish= 48 seconds

	
	c)
	mininext> H1 traceroute H2
	traceroute to 177.0.0.2 (177.0.0.2), 30 hops max, 60 byte packets
	 1  172.0.0.2 (172.0.0.2)  0.213 ms  0.179 ms  0.169 ms
	 2  174.0.0.2 (174.0.0.2)  0.257 ms  0.217 ms  0.210 ms
	 3  176.0.0.2 (176.0.0.2)  0.308 ms  0.276 ms  0.276 ms
	 4  177.0.0.2 (177.0.0.2)  0.418 ms  0.402 ms  0.299 ms



****************************************************************************************


**********************************************	 
Part C:
**********************************************
 

	C1:
	
	a) All the four source files are present in folder: \HW3_110451714_FCN\PartC\C1
	
		Source files:
		Advertisement.java  ClientSocketConn.java  DistanceVector.java  ServerSocketConn.java  topology.txt
		
		Program flow-> Every host will execute below command to start the host/router's algorthm
			
			command: java <filename> serverport client1-host port client2-host port 
		
		Program execution needs to be setup from left to right nodes, H1-->H2
		First it open the given server port and then connect as a client to mentioned required servers,
		<client1-host port client2-host port>.
		The reason for these number of arguments is that in our network a host can have at max two incoming
		connections (servers) or two outgoing connections (clients).
		As for the very first execution, all the required incoming/outgoing connections would be established
		as clients/servers. These sockets would be maintain to advertise the routing informations.
		
		Program is a daemon process which will be executed after every 20 seconds and follow below
		procedures:
			#Check if there is any network info on the socket from any of the client updates
			#if yes, read the node route info and then compare with the map loaded from own file info
			#if there is any update, map will incorporate the changes and update it to the file of that host
			#Once after updaing own file, it will write the data to the socket connections to its neighbours
			#Run the Bellman Ford algorithm with the updated route file copy
			#Repeat the same process periodically, 20 seconds in my scenario.
			
		Here, All the route level advertisment are done using socket connection among neighbours and
		generate the routing table of the host/router gradually till it converges.
		
		
	b)
	Time take to find shortest path= 26 seconds
	
	Time calculation Methodolgy:
	long startTime = System.currentTimeMillis();
	
	****** Bellman Ford Algorithm*******
	
	long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    System.out.println(elapsedTime);
	
	c)
	Vertex H1 distance to other nodes
		H1 0 H1
		H2 10 R1
		R1 2 R1
		R2 12 R1
		R3 8 R1
		R4 8 R1
	
	
	Vertex H2 distance from other nodes
		H1 -1 null
		H2 0 H2
		R1 -1 null
		R2 -1 null
		R3 -1 null
		R4 -1 null
	
	
	Vertex R1 distance from other nodes
		H1 -1 null
		H2 8 R2
		R1 0 R1
		R2 10 R2
		R3 6 R3
		R4 6 R2
	
	Vertex R2 distance from other nodes
		H1 -1 null
		H2 -2 null
		R1 -1 null
		R2 0 R2
		R3 -1 null
		R4 -4 null

	Vertex R3 distance from other nodes
		H1 -1 null
		H2 7 R4
		R1 -1 null
		R2 -1 null
		R3 0 R3
		R4 5 R4
		

	Vertex R4 distance from other nodes
		H1 -1 null
		H2 2 H2
		R1 -1 null
		R2 -1 null
		R3 -1 null
		R4 0 R4
		
	
	Note: I am displaying -1 as node unreachable

	
	C2:
	
	a). time taken for the protocol to converge= 35 Seconds
	
	b).
	Vertex H1 distance from other nodes
	H1 0 H1
	H2 10 R1
	R1 2 R1
	R2 12 R1
	R3 3 R1
	R4 8 R1


	Vertex H2 distance from other nodes
	H1 -1 H2
	H2 0 H2
	R1 -1 H2
	R2 -1 H2
	R3 -1 H2
	R4 -1 H2


	Vertex R1 distance from other nodes
	H1 -1 R1
	H2 8 R2
	R1 0 R1
	R2 10 R2
	R3 1 R3
	R4 6 R2


	Vertex R2 distance from other nodes
	H1 -1 R2
	H2 -2 R4
	R1 -1 R2
	R2 0 R2
	R3 -1 R2
	R4 -4 R4


	Vertex R3 distance from other nodes
	H1 -1 R3
	H2 7 R4
	R1 -1 R3
	R2 -1 R3
	R3 0 R3
	R4 5 R4


	Vertex R4 distance from other nodes
	H1 -1 R4
	H2 2 H2
	R1 -1 R4
	R2 -1 R4
	R3 -1 R4
	R4 0 R4


*************************************************************************************************
References:
	http://openmaniak.com/quagga_tutorial.php
	http://computernetworkingnotes.com/routing-static-dynamics-rip-ospf-igrp-eigrp/rip-routing-configurations.html
	http://www.nongnu.org/quagga/docs/quagga.html
	http://mininet.org/walkthrough/
	http://mininet.org/
	