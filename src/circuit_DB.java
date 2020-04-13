import java.nio.ByteBuffer;
import java.util.ArrayList;

public class circuit_DB {
    private int nbr_link;
    private ArrayList<link_cost> linkcost;
    private final int number_router = 5;

    circuit_DB(int nbr_link){
        linkcost=new ArrayList<>();
        for (int i=0;i<number_router;i++){
            // 6535 big int
            linkcost.add( new link_cost(i+1, 6535));
        }
    }

    circuit_DB(int nbr_link, ArrayList<link_cost> linkcost){
        this.nbr_link=nbr_link;
        this.linkcost=linkcost;
    }

    public static circuit_DB parseUDPdata(byte[] UDPdata) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(UDPdata);
        int nbr_link = buffer.getInt();
        ArrayList<link_cost> a = new ArrayList<link_cost>();
        for (int i=0;i<nbr_link;i++){
            int link = buffer.getInt();
            int cost = buffer.getInt();
            a.add(new link_cost(link,cost));
        }
        return new circuit_DB(nbr_link,new ArrayList<link_cost>(a));
    }


    public void setLinkCost(int link, int cost){
        linkcost.set(link-1,new link_cost(link,cost));
    }

    public int getLinkCost(int link){
        return linkcost.get(link-1).getCost();
    }
}
