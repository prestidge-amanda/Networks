public class link_cost {
    private int link;
    private int cost;

    link_cost(int link, int cost){
        this.link=link;
        this.cost=cost;
    }

    link_cost(link_cost l){
        this.link=l.link;
        this.cost=l.cost;
    }

    public int getCost() {
        return cost;
    }

    public int getLink(){
        return link;
    }
}
