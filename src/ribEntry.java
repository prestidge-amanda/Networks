import java.util.ArrayList;

public class ribEntry {
    ArrayList<Integer> pred;
    int cost;
    ribEntry(int cost){
        this.cost=cost;
        this.pred=new ArrayList<Integer>();
    }
}
