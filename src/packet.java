import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class packet {
    private String packet_type;
    private int router_id;
    private int link_id;
    private int sender;
    private int cost;
    private int via;

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

    public void setSender(int sender){
        this.sender=sender;
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
        int num1 = buffer.getInt();
        int num2=buffer.getInt();
        int  num3;
        try{
             num3 = buffer.getInt();
        }catch(BufferUnderflowException e){
                return new packet(num1,num2);
        }
        int cost=buffer.getInt();
        int via=buffer.getInt();
        return new packet(num1,num2,num3,cost,via);
    }


}
