import java.util.ArrayList;

public class link_state_DB {
    private ArrayList<packet> data;
    private int currentRouterId;
    link_state_DB(circuit_DB circuit_db,int currentRouterId){
        data = new ArrayList<>();
        this.currentRouterId=currentRouterId;
        int sender;
        int router_id;
        int link_id;
        int cost;
        int via;
        packet p;
        for(int i= 0; i<circuit_db.getNumLinks();i++){
            sender = currentRouterId;
            router_id = currentRouterId;
            link_id = circuit_db.getLink(i);
            cost=circuit_db.getLinkCost(i);
            via = -1;
            p = new packet(sender,router_id,link_id,cost,via);
            data.add(p);
        }
    }

    public int getSize(){
        return data.size();
    }

    public packet getData(int i){
        return data.get(i);
    }

}
