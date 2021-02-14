package spell;

import java.lang.StringBuilder;
import java.util.Locale;

public class Trie implements ITrie {
    public Node root;
    int wordCount = 0, nodeCount = 1;
    boolean equal = true;

    public Trie() {
        root = new Node();
    }

    @Override
    public void add(String word) { //looks good
        INode next = root;
        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (next.getChildren()[index] == null) {
                next.getChildren()[index] = new Node();
                nodeCount++;
            }
            next = next.getChildren()[index];
            if (i == word.length()-1) {
                if (next.getValue() < 1) { wordCount++; }
                next.incrementValue(); //last char means the word is finished
            }
        }
    }

    @Override
    public INode find(String word) {
        INode next = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (next.getChildren()[index] == null) { return null; }
            next = next.getChildren()[index];
        }
        if (next.getValue() < 1) { return null; }
        return next;
    }

    @Override
    public int getWordCount() { return wordCount; }

    @Override
    public int getNodeCount() { return nodeCount; }

    public int hashCode() {
        int result = 3;
        INode[] children = root.getChildren();
        for (int i = 0; i < 26; i++) {
            if (children[i] != null) {
                result = i;
                break;
            }
        }
        return result * wordCount * nodeCount;
    }

    public boolean equals(Object o) {
        if(!(o instanceof Trie)) { return false; }
        if (this.wordCount != ((Trie) o).getWordCount() ||
                    this.nodeCount != ((Trie) o).getNodeCount()) {
            return false;
        }
        //now check that all of the nodes are the same
        INode node1 = root;
        INode node2 = ((Trie) o).root;
        return equalsHelper(node1, node2);
    }
    private boolean equalsHelper(INode node1, INode node2) { //recursively compare the two functions
        INode[] child1 = node1.getChildren();
        INode[] child2 = node2.getChildren();
        for (int i = 0; i < 26; i++) { //iterate through each "letter" node in the Trie
            if(child1[i] != null) { //child1 is not null
                if (child2[i] == null || child1[i].getValue() != child2[i].getValue()) {
                    equal = false; //the tries are no longer equal
                    return false; //end the for loop
                }
                //they are equal nodes with data
                equalsHelper(child1[i], child2[i]);
            }
            else if (child2[i] != null) { //child1 is null, check is child2 is not null
                equal = false; //the tries are no longer equal
                return false; //end the for loop
            }
            //they are equal null nodes
        }
        return equal; //return true if it gets all the way through without returning false
    }



    public String toString() {
        StringBuilder word = new StringBuilder();
        StringBuilder all = new StringBuilder();
        INode node = root;
        return toStringHelper(node, word, all);
    }

    public String toStringHelper(INode node, StringBuilder word, StringBuilder all) {
        for (int i = 0; i < 26; i++) {
            if (node == root) { word = new StringBuilder(); }
            if (node.getChildren()[i] != null) {
                word.append((char) (i + 'a'));
                if (node.getChildren()[i].getValue() > 0) {
                    all.append(word);
                    all.append('\n');
                }
                toStringHelper(node.getChildren()[i], word, all);
            }
        }
        return all.toString();
    }
}
