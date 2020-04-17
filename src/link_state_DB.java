import java.util.ArrayList;

public class link_state_DB {
    private ArrayList<ArrayList<packet>> data;
    private ArrayList<ArrayList<packet>> link;
    private ArrayList<ArrayList<Integer>> rib;
    private int currentRouterId;
    private final int num_routers=5;

    link_state_DB(circuit_DB circuit_db,int currentRouterId){
        this.data = new ArrayList<ArrayList<packet>>();
        this.link = new ArrayList<ArrayList<packet>>();
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
        for(int k=0; k<7;k++){
            link.add(new ArrayList<packet>());
        }
        for(int i= 0; i<circuit_db.getNumLinks();i++){
            sender = currentRouterId;
            router_id = currentRouterId;
            link_id = circuit_db.getLink(i);
            cost=circuit_db.getLinkCost(i);
            via = -1;
            p = new packet(sender,router_id,link_id,cost,via);
            data.get(currentRouterId-1).add(p);
            link.get(link_id-1).add(p);
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
        link.get(p.getLink_id()-1).add(p);
        updateRIB();
        return true;
    }

    private void updateRIB(){
      int distance=0;
        int currIndex=currentRouterId-1;;
        int currShort=0;
        int nextIndex=currentRouterId-1;
        ArrayList<Boolean> visited = new ArrayList<>();

        this.rib=new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> entry;
        for (int i=0;i<num_routers;i++){
            entry = new ArrayList<Integer>();
            if (i==currIndex){
                entry.add(-1);
                entry.add(0);
            }else{
                entry.add(Integer.MAX_VALUE);
                entry.add(Integer.MAX_VALUE);
            }
            this.rib.add(entry);
        }

        int connected=0;
      for(int k=0;k<link.size();k++){
          if(link.get(k).size()>1){
              visited.add(false);
              connected++;
          }else{
              visited.add(true);
          }
      }

      int check;
      visited.set(currIndex,true);
       for(int i=0;i<connected-1;i++){

            //find shortest link
           currShort=Integer.MAX_VALUE;
            for(int j=0;j<data.get(currIndex).size();j++) {
                System.out.print("curr short:" + currShort+" curr index:"+ currIndex +"\n");
                if(link.get(data.get(currIndex).get(j).getLink_id()-1).size()>1){
                    if(link.get(data.get(currIndex).get(j).getLink_id()-1).get(0).getRouter_id()-1!=currIndex){
                        check=link.get(data.get(currIndex).get(j).getLink_id()-1).get(0).getRouter_id()-1;
                    }else{
                        check=link.get(data.get(currIndex).get(j).getLink_id()-1).get(1).getRouter_id()-1;
                    }
                    if (currShort > data.get(currIndex).get(j).getCost() && visited.get(check)==false) {
                        currShort = data.get(currIndex).get(j).getCost();
                        nextIndex=check;
                    }
                }
            }

            System.out.print("curr short:" + currShort+" curr index:"+ currIndex +"\n");
            distance+=currShort;
            currIndex=nextIndex;
            visited.set(currIndex,true);

            // update neighbours of shortest
            for(int j=0;j<data.get(currIndex).size();j++){
                int calc = distance+data.get(currIndex).get(j).getCost();
                System.out.println("r"+data.get(currIndex).get(j).getRouter_id()+" distance "+ calc + "rib" + rib.get(data.get(currIndex).get(j).getRouter_id()-1).get(1) );
                if((rib.get(data.get(currIndex).get(j).getRouter_id()-1).get(1) > (distance+data.get(currIndex).get(j).getCost()))&& visited.get(data.get(currIndex).get(j).getRouter_id()-1)==false){
                    rib.get(data.get(currIndex).get(j).getRouter_id()-1).set(0,data.get(currIndex).get(j).getVia());
                    rib.get(data.get(currIndex).get(j).getRouter_id()-1).set(1,data.get(currIndex).get(j).getCost()+distance);
                }
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
