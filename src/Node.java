import java.util.HashMap;

public class Node {
    public HashMap<Integer, Node> neighbours;
    private int id;

    Node (int id){
        this.id = id;
        this.neighbours = new HashMap<>();
    }

    public int getNodeId(){
        return this.id;
    }

    public boolean isNeighbour(int id){
        return this.neighbours.containsKey(id);
    }

    public void removeNeighbour(int id){
        this.neighbours.remove(id);
    }

    public HashMap<Integer, Node> getNeighbours(){
        return this.neighbours;
    }

    public void addNext (Node nextNode){
        neighbours.put(nextNode.getNodeId(), nextNode);
    }

}
