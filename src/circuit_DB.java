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

    public void setLinkCost(int link, int cost){
        linkcost.set(link-1,new link_cost(link,cost));
    }

    public int getLinkCost(int link){
        return linkcost.get(link-1).getCost();
    }
}
