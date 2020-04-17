import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class circuit_DB {
    private int nbr_link;
    private ArrayList<link_cost> linkcost;
    private ArrayList<Boolean> hellos;
    private final int number_router = 5;

    circuit_DB(int nbr_link){
        linkcost=new ArrayList<>();
        for (int i=0;i<number_router;i++){
            // 6535 big int
            linkcost.add( new link_cost(i+1, 6535));
        }
    }

    circuit_DB(circuit_DB c){
        this.nbr_link=c.nbr_link;
        this.linkcost=new ArrayList<link_cost>(c.linkcost);
        this.hellos=new ArrayList<Boolean>(hellos);
    }

    circuit_DB(int nbr_link, ArrayList<link_cost> linkcost){
        this.nbr_link=nbr_link;
        this.linkcost=linkcost;
        this.hellos= new ArrayList<Boolean>();
        for (int i =0;i<nbr_link;i++){
            hellos.add(false);
        }
    }

    public void setHelloReceived(int i){
        hellos.set(i,true);
    }

    public boolean getHello(int i){
        return hellos.get(i);
    }

    public int getNumLinks(){
        return nbr_link;
    }

    public static circuit_DB parseUDPdata(byte[] UDPdata) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(UDPdata);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int nbr_link = buffer.getInt();
        ArrayList<link_cost> a = new ArrayList<link_cost>();
        for (int i=0;i<nbr_link;i++){
            int link = buffer.getInt();
            int cost = buffer.getInt();
            a.add(new link_cost(link,cost));
        }
        return new circuit_DB(nbr_link,new ArrayList<link_cost>(a));
    }

    public boolean getHelloStatus(int i){
        return hellos.get(i);
    }

    public int getLink(int i){
        return linkcost.get(i).getLink();
    }

    public void setLinkCost(int link, int cost){
        linkcost.set(link-1,new link_cost(link,cost));
    }

    public int getLinkCost(int link){
        return linkcost.get(link).getCost();
    }
}
