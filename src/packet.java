import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class packet {
    private String packet_type;
    private int router_id;
    private int link_id;
    private int sender;
    private int cost;
    private int via;

    packet(packet p){
        this.packet_type=p.packet_type;
        this.router_id=p.router_id;
        this.link_id=p.link_id;
        this.sender=p.sender;
        this.cost=p.cost;
        this.via=p.via;
    }

    packet(int router_id, int link_id){
        this.router_id=router_id;
        this.link_id=link_id;
        this.packet_type="HELLO";
        this.sender=-1;
        this.cost=-1;
        this.via=-1;
    }

    packet(int router_id){
        this.router_id=router_id;
        this.packet_type="INIT";
        this.link_id=-1;
        this.sender=-1;
        this.cost=-1;
        this.via=-1;
    }

    packet(int sender, int router_id,int link_id, int cost, int via){
        this.sender=sender;
        this.packet_type="LSPDU";
        this.router_id=router_id;
        this.link_id=link_id;
        this.cost=cost;
        this.via=via;
    }

    public int getCost() {
        return cost;
    }

    public int getRouter_id(){
        return router_id;
    }

    public int getLink_id(){
        return link_id;
    }

    public int getVia(){
        return via;
    }

    public void setVia(int via) {
        this.via=via;
    }

    public int getSender(){
        return sender;
    }

    public String getPacket_type(){
        return packet_type;
    }

    public byte[] getUDPdata() {
        ByteBuffer buffer;

       if(packet_type=="INIT"){
            buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(router_id);
        }else if (packet_type=="HELLO"){
            buffer = ByteBuffer.allocate(8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(router_id);
            buffer.putInt(link_id);
        }else{
            buffer = ByteBuffer.allocate(20);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(sender);
            buffer.putInt(router_id);
            buffer.putInt(link_id);
            buffer.putInt(cost);
            buffer.putInt(via);
        }
        return buffer.array();
    }

    public static packet parseUDPdata(byte[] UDPdata) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(UDPdata);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        IntBuffer iBuffer = buffer.asIntBuffer();
        int count = iBuffer.remaining();
        int[] results = new int[count];
        iBuffer.get(results);
        if (results[3]==0){
            return new packet(results[0],results[1]);
        }else{
            return new packet(results[0],results[1],results[2],results[3],results[4]);
        }
    }


}
