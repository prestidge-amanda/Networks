import java.util.ArrayList;

public class rib_DB {
    private ArrayList<ArrayList<Integer>> rib;
    private final int  num_routers = 5;
    private int currentRouter;
    rib_DB(int currentRouter){
        this.rib=new ArrayList<ArrayList<Integer>>();
        this.currentRouter=currentRouter;
        ArrayList<Integer> entry;
        for (int i=0;i<num_routers;i++){
            entry = new ArrayList<Integer>();
            for(int j=0;j<num_routers;j++){
                if (i==currentRouter-1&&j==currentRouter-1){
                    entry.add(0);
                }else{
                    entry.add(Integer.MAX_VALUE);
                }
            }
            this.rib.add(entry);
        }
    }

    public void setRib(int i,ArrayList<Integer> entry){
        rib.set(i, entry);
    }

    public ArrayList<Integer> getRib(int i){
        return rib.get(i);
    }
}
