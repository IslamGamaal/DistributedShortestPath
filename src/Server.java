import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {
    public static void main(String[] args) {
        Graph stub = new ServerGraph();
        try {
            Naming.rebind("rmi://localhost:5000/root", stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
