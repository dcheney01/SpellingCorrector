package spell;

public class Node implements INode{
    int count;
    INode[] children;

    public Node() {
        children = new Node[26];
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
