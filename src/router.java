import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class router {
    final int num_routers = 5;
    public static void main(String[] args) throws Exception {
        int router_id = Integer.parseInt(args[0]);
        String nse_host = args[1];
        int nse_port = Integer.parseInt(args[2]);
        int router_port = Integer.parseInt(args[3]);
        new router(router_id,nse_host,nse_port,router_port);
    }

    router(int router_id, String nse_host, int nse_port, int router_port) throws Exception {

        // connect to NSE
        DatagramSocket socketServer;
        DatagramSocket socketCleint;
        socketServer = new DatagramSocket(router_port);
        socketCleint = new DatagramSocket();
        byte[] sendBuffer;
        byte[] recieveBuffer = new byte[44];
        circuit_DB db = new circuit_DB(1);

        // send init packet
        packet init_packet = new packet(router_id);
        sendBuffer= init_packet.getUDPdata();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer,sendBuffer.length, InetAddress.getByName(nse_host), nse_port);
        socketServer.send(sendPacket);

        // should i use the same socket to listen?


        // receive circuit db from nse
        DatagramPacket receivePacket = new DatagramPacket(recieveBuffer,recieveBuffer.length);
        socketServer.receive(receivePacket);
        circuit_DB circuit_db = db.parseUDPdata(receivePacket.getData());

        // while loop to receive pckts
        while (true) {

            // send hello to all neighbours new neighours ?


            // receive hello send set of LS_PDU to neighbour (link state db)

            // Receive LS_PDU

            // Add this info to link state db

            // send LS_PDU update sender and via fields to appropriate values - send only once, don't fwd duplicates

            // Run SPF on Link State Databse  - converted to RIB
            break;
            // Print LSD and RIB in log file
        }

    }
}