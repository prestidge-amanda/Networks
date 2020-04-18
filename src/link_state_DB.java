import java.util.ArrayList;

public class link_state_DB {
    private ArrayList<ArrayList<packet>> data;
    private ArrayList<ArrayList<Integer>> rib;
    private ArrayList<ArrayList <Integer>> links;
    private int currentRouterId;
    private final int num_routers=5;

    link_state_DB(circuit_DB circuit_db,int currentRouterId){
        this.data = new ArrayList<ArrayList<packet>>();
        this.links=new ArrayList<ArrayList<Integer>>();
        this.currentRouterId=currentRouterId;
        int sender;
        int router_id;
        int link_id;
        int cost;
        int via;
        packet p;
        for(int j=0;j<num_routers;j++){
            data.add(new ArrayList<packet>());
        }

        for(int i=0;i<7;i++){
            links.add(new ArrayList<Integer>());
        }

        for(int i= 0; i<circuit_db.getNumLinks();i++){
            ArrayList<Integer> entry= new ArrayList<>();
            sender = currentRouterId;
            router_id = currentRouterId;
            link_id = circuit_db.getLink(i);
            cost=circuit_db.getLinkCost(i);
            entry.add(cost);
            entry.add(currentRouterId);
            entry.add(-1);
            via = -1;
            p = new packet(sender,router_id,link_id,cost,via);
            data.get(currentRouterId-1).add(p);
            links.set(link_id-1,entry);
        }
        rib_DB();
    }

    private void rib_DB(){
        this.rib=new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> entry;
        for (int i=0;i<num_routers;i++){
            entry = new ArrayList<Integer>();
            if (i==currentRouterId-1){
                    entry.add(-1);
                    entry.add(0);
            }else{
                    entry.add(Integer.MAX_VALUE);
                    entry.add(Integer.MAX_VALUE);
            }
            this.rib.add(entry);
        }
    }

    public int getSize(int i){
        return data.get(i).size();
    }

    public packet getData(int router, int index){
        return data.get(router).get(index);
    }

    public int sizeDB(){
        int size=0;
        for (int i = 0;i<num_routers;i++){
            size+=data.get(i).size();
        }
        return size;
    }

    public boolean addPacket(packet p){
        packet currentPacket;
        // Add this info to link state db
        String msg="receives a LSP DU: sender " + p.getSender() + " router_id "+p.getRouter_id() + " link_id " +
                p.getLink_id() + " cost "+ p.getCost() + " via " + p.getVia() + "\n";
      //  System.out.print(msg);
        if(data.get((p.getRouter_id()-1)).size()>0){
            for(int i=0;i<data.get((p.getRouter_id()-1)).size();i++){
                currentPacket=data.get((p.getRouter_id()-1)).get(i);
                if(currentPacket.getLink_id()==p.getLink_id()){
                    return false;
                }
            }
        }
        data.get((p.getRouter_id()-1)).add(p);
        if(links.get(p.getLink_id()-1).size()>0){
            ArrayList<Integer> entry = new ArrayList<>(links.get(p.getLink_id()-1));
            entry.set(2,p.getRouter_id());
            links.set(p.getLink_id()-1,entry);
        }else{
            ArrayList<Integer> entry = new ArrayList<>();
            entry.add(p.getCost());
            entry.add(p.getRouter_id());
            entry.add(-1);
            links.set(p.getLink_id()-1,entry);
        }
        updateRIB();
        return true;
    }

    private void updateRIB(){
        int distance=0;
        int prevNode=currentRouterId-1;
        ArrayList<Boolean> notVisited=new ArrayList<Boolean>();

        this.rib=new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> entry;
        for (int i=0;i<num_routers;i++){
            entry = new ArrayList<Integer>();
            if (i==prevNode){
                entry.add(-1);
                entry.add(0);
            }else{
                entry.add(Integer.MAX_VALUE);
                entry.add(Integer.MAX_VALUE);
            }
            this.rib.add(entry);
        }

        for(int i=0;i<num_routers;i++){
            notVisited.add(false);
        }

      //  notVisited.remove(currentRouterId);

        for(int i=0; i<num_routers;i++){
            System.out.println("start");
            int minDistance=Integer.MAX_VALUE;
            int index =-1;

            // find smallest cost router edge - source initially
            for(int j=0;j<num_routers;j++){
                System.out.println("new: "+rib.get(j).get(1)+ " min: "+minDistance);
                if(rib.get(j).get(1)<minDistance&&notVisited.get(j)==false){
                    index=j;
                    minDistance=rib.get(j).get(1);
                }
            }

            System.out.println(index);
            if(index!=-1){
                notVisited.set(index,true);

                for(int k=0;k<data.get(index).size();k++){
                    System.out.println(index + " in loop" + links.get(data.get(index).get(k).getLink_id()-1).size());
                    if(links.get(data.get(index).get(k).getLink_id()-1).get(2)!=-1){
                        int router = links.get(data.get(index).get(k).getLink_id()-1).get(1)-1;
                        if(router==index){
                            router=links.get(data.get(index).get(k).getLink_id()-1).get(2)-1;
                        }
                        System.out.println("neighbour"+ router);
                        if(rib.get(router).get(1)>minDistance+data.get(index).get(k).getCost()){
                            rib.get(router).set(0, index+1);
                            rib.get(router).set(1,minDistance+data.get(index).get(k).getCost());
                        }
                    }
                }
            }else{
                break;
            }
        }
    }

    public String printDB(){
        String msg ="# Topology Database \n";
        for(int i=0;i<data.size();i++){
            if(data.get(i).size()>0){
                int index = i+1;
                msg+="R" + currentRouterId+ " -> R"+index+" nbr link " + data.get(i).size() + "\n";
                for (int j=0;j<data.get(i).size();j++){
                    msg+="R" + currentRouterId+ " -> R"+index+ " link "+ data.get(i).get(j).getLink_id()+ " cost "+data.get(i).get(j).getCost()+ "\n";
                }
            }
        }
        return msg;
    }

    public String printRIB(){
        String msg = "#RIB\n";
        int index;
        for(int i=0;i<num_routers;i++){
            index=i+1;
            if(rib.get(i).get(0)==-1){
                msg+= "R"+currentRouterId+" -> R"+index +" -> Local, 0\n";
            }else if(rib.get(i).get(0)==Integer.MAX_VALUE){
                msg+= "R"+currentRouterId+" -> R"+index +" -> INF, INF\n";
            }else{
                msg+= "R"+currentRouterId+" -> R"+index +" -> R"+rib.get(i).get(0)+ ", " +rib.get(i).get(1)+"\n";
            }
        }
        return msg;
    }
}
