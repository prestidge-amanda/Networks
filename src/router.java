import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class router {
    final int num_routers = 5;

    public static void main(String[] args) throws Exception {
        int router_id = Integer.parseInt(args[0]);
        String nse_host = args[1];
        int nse_port = Integer.parseInt(args[2]);
        int router_port = Integer.parseInt(args[3]);
        new router(router_id, nse_host, nse_port, router_port);
    }

    router(int router_id, String nse_host, int nse_port, int router_port) throws Exception {

        // Variables
        DatagramSocket socketServer;
        DatagramSocket socketCleint;
        socketServer = new DatagramSocket(router_port);
        socketCleint = new DatagramSocket();
        int recvHello=0;
        byte[] sendBuffer;
        byte[] recieveBuffer = new byte[44];
        circuit_DB db = new circuit_DB(1);
        packet hello;
        String msg;
        BufferedWriter logWriter;
        rib_DB rib_db = new rib_DB(router_id);
        link_state_DB LSDB;
        packet packetL;

        // Create log file
        String fileName = "router"+router_id+".log";
        File logFile=new File(System.getProperty("user.dir"),fileName);
        if(logFile.exists()){
                logFile.delete();
        }
        logFile.createNewFile();
        logWriter = new BufferedWriter(new FileWriter(logFile,true));


            // send init packet
        packet init_packet = new packet(router_id);
        sendBuffer = init_packet.getUDPdata();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(nse_host), nse_port);
        socketServer.send(sendPacket);

        // log init sent
        msg = "R" + router_id + " sends an INIT: router_id "+ router_id + "\n";
        logWriter.write(msg);


        // receive circuit db from nse
        DatagramPacket receivePacket = new DatagramPacket(recieveBuffer, recieveBuffer.length);
        socketServer.receive(receivePacket);
        circuit_DB circuit_db = db.parseUDPdata(receivePacket.getData());
        System.out.println(circuit_db.getNumLinks());

        // log circuitDB received
        msg = "R" + router_id+ " receives a CIRCUIT_DB: nbr_link " + circuit_db.getNumLinks()+"\n";
        logWriter.write(msg);

        // create topology from circuit db
        LSDB = new link_state_DB(circuit_db,router_id);

        // send hello to neighbours
        for (int i =0;i<circuit_db.getNumLinks();i++){
            hello = new packet(router_id,circuit_db.getLink(i));
            sendBuffer = hello.getUDPdata();
            sendPacket=new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(nse_host), nse_port);
            socketServer.send(sendPacket);
            msg="R1 sends a HELLO: router_id "+router_id+" link_id " +circuit_db.getLink(i) +"\n";
            logWriter.write(msg);
        }

        // while loop to receive pckts
       while (true) {
            // receive packets from neighbours
            receivePacket = new DatagramPacket(recieveBuffer, recieveBuffer.length);
            socketServer.receive(receivePacket);
            packet p = packet.parseUDPdata(receivePacket.getData());
          //  System.out.print(p.getRouter_id());

            // handle hello packets received
            if (p.getPacket_type()=="HELLO"){
                recvHello++;
                // log hello received
                msg="R1 receives a HELLO: router_id "+p.getRouter_id()+" link_id " +p.getLink_id() +"\n";
                // check if hello recv twice?
                for (int k=0;k<num_routers;k++){
                        if (k+1!=p.getRouter_id()){
                            for (int i=0;i<LSDB.getSize(k);i++){
                                packetL=LSDB.getData(k,i);
                                packetL.setVia(p.getLink_id());
                                sendBuffer=packetL.getUDPdata();
                                sendPacket=new DatagramPacket(sendBuffer,sendBuffer.length,InetAddress.getByName(nse_host),nse_port);
                                socketServer.send(sendPacket);
                                // Log sent LSPDU
                                msg="R"+router_id+" sends a LSPDU: sender "+ packetL.getSender()+ " router_id "+ packetL.getRouter_id()+ " link_id "+ packetL.getLink_id() +
                                        "cost" + packetL.getCost() + " via " + packetL.getVia() +"\n";
                                logWriter.write(msg);
                            }
                        }
                }
            }

            // handle LSPDU packets received
            else{
              //  System.out.print("something else");
                // Add this info to link state db

                // send LS_PDU update sender and via fields to appropriate values - send only once, don't fwd duplicates

                // Run SPF on Link State Databse  - converted to RIB

                // Print LSD and RIB in log file
            }
            if(recvHello==circuit_db.getNumLinks()){
                break;
            }
        }
        logWriter.close();
    }
}