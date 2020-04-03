import java.rmi.Naming;

public class Server {
    public static void main(String[] args) {
        GraphInterface stub = new GraphServer();
        Naming.rebind("rmi://localhost:5000/root", stub);
    }
}
