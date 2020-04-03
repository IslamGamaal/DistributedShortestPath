import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ServerGraph extends UnicastRemoteObject implements Graph {

    HashMap<Integer, Node> nodes;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    ServerGraph() throws RemoteException {
        super();
        nodes = new HashMap<>();
    }

    public void removeEdge(int id1, int id2) {
        if (!nodes.containsKey(id1) || !nodes.containsKey(id2))
            return;
        Node fromNode = nodes.get(id1);
        if (fromNode.isNeighbour(id2)) {
            fromNode.removeNeighbour(id2);
        }
    }

    public void addEdge(int id1, int id2) {
        Node fromNode = nodes.get(id1);
        Node toNode = nodes.get(id2);

        if (fromNode == null) {
            fromNode = new Node(id1);
            nodes.put(fromNode.getNodeId(), fromNode);
        }

        if (toNode == null) {
            toNode = new Node(id2);
            nodes.put(toNode.getNodeId(), toNode);
        }

        fromNode.addNext(toNode);
    }

    @Override
    public String exectue(String batch) throws RemoteException{
        String[] batchLines = batch.split("\n");
        StringBuilder collectedResults = new StringBuilder();

        for (String command: batchLines){

            if (command.contains("F")){
                break;
            } else {
                String[] splittedCommand = command.split(" ");
                if (splittedCommand.length<3){
                    collectedResults.append("Wrong Format!").append("\n");
                    continue;
                }
                String operand = splittedCommand[0];
                int id1 = Integer.parseInt(splittedCommand[1]);
                int id2 = Integer.parseInt(splittedCommand[2]);

                if (command.contains("Q")){
                    this.lock.readLock().lock();
                    collectedResults.append(performQuery(id1, id2)).append("\n");
                    this.lock.readLock().unlock();
                } else if (command.contains("A")){
                    this.lock.writeLock().lock();
                    addEdge(id1, id2);
                    this.lock.writeLock().unlock();
                } else if (command.contains("D")){
                    this.lock.writeLock().lock();
                    removeEdge(id1, id2);
                    this.lock.writeLock().unlock();
                }
            }
        }
        return collectedResults.toString();
    }

    @Override
    public int getGraphSize() throws RemoteException {
        return nodes.size();
    }

    public String performQuery (int id1, int id2){
        if (!nodes.containsKey(id1) || !nodes.containsKey(id2)){
            return "-1";
        }

        if(id1 == id2) {
            return "0";
        }

        Node startNode = nodes.get(id1);
        Node targetNode = nodes.get(id2);
        HashSet<Integer> visitedNodes = new HashSet<>();
        HashMap<Integer, Integer> shortestCountFromStartNode = new HashMap<>();
        shortestCountFromStartNode.put(startNode.getNodeId(), 0);
        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);

        while (!queue.isEmpty()){
            Node parentNode = queue.poll();
            visitedNodes.add(parentNode.getNodeId());
            for (Map.Entry<Integer, Node> neighbour : parentNode.getNeighbours().entrySet()){
                if (neighbour.getKey()== targetNode.getNodeId()){
                    return String.valueOf(shortestCountFromStartNode.get(parentNode.getNodeId())+1);
                }
                if (!visitedNodes.contains(neighbour.getKey())){
                    shortestCountFromStartNode.put(neighbour.getKey(), shortestCountFromStartNode.get(parentNode.getNodeId())+1);
                    queue.add(neighbour.getValue());
                }
            }
        }
        return "-1";
    }


    @Override
    public String initGraph(String edgesString) throws RemoteException {
        String[] edges = edgesString.split("\n");
        for (String edge : edges){
            String[] splittedEdge = edge.split(" ");
            if (splittedEdge.length == 2){
                int id1 = Integer.parseInt(splittedEdge[0]);
                int id2 = Integer.parseInt(splittedEdge[1]);
                addEdge(id1, id2);
            }

        }

        return "R";
    }

}
