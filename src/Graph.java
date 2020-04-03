import java.rmi.*;

interface Graph extends Remote {
    String initGraph(String[] edges) throws RemoteException;
    String apply (String[] batchLines) throws RemoteException;
    int getGraphSize();
}
