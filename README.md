1. Introduction
	This project implements the link-state algorithm OSPF in java for a sample network with 5 routers. 

2. How to install
Install java - OpenJDK 1.8.0_232
	Run make in the directory that contains packet.java, circuit_DB.java, link_cost.java, router.java
	chmod u+x on router.sh
	chmod +x nse-linux386

3. Example Usage
	3.1 Executables
		./nse-linux386 <routers_host> <nse_port>
		<routers_host> -  host where routers are running - must be the same for all 5
		<nse_port> - the port number of the NSE

		./router.sh <router_id> <nse_host> <nse_port> <router_port>
		<router_id> - integer that represent the router id - unique
		<nse_host> host where nse is running
		<nse_port> port number of the nse
		<router_port> router port

	3.2 Execution order:
		./nse-linux386 <routers_host> <nse_port>
		./router.sh 1 <nse_host> <nse_port> <router_port>
		./router.sh 2 <nse_host> <nse_port> <router_port>
		./router.sh 3 <nse_host> <nse_port> <router_port>
		./router.sh 4 <nse_host> <nse_port> <router_port>
		./router.sh 5 <nse_host> <nse_port> <router_port>


	3.3 Output:
		Each router terminates when it has received all 14 links and has a complete topology database
		router1.log
		router2.log 
		router3.log
		router4.log
		router5.log

		Each log file contains each of the messages sent and received at the router and the topology database
		and routing information base each time there is a change to the topology database. 

5. Author:
	Amanda Prestidge
