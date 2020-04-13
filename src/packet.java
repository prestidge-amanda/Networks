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

    public int getSender(){
        return sender;
    }

    public String getPacket_type(){
        return packet_type;
    }


}
