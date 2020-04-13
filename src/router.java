public class router {
    final int num_routers = 5;
    public static void main(String[] args) {
        int router_id = Integer.parseInt(args[0]);
        String nse_host = args[1];
        int nse_port = Integer.parseInt(args[2]);
        int router_port = Integer.parseInt(args[3]);
        new router(router_id,nse_host,nse_port,router_port);
    }

    router(int router_id, String nse_host, int nse_port, int router_port) {

    }
}