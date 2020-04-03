import java.rmi.*;

interface Graph extends Remote {
    String initGraph(String[] edges) throws RemoteException;
    String exectue(String[] batchLines) throws RemoteException;
    int getGraphSize();
}
