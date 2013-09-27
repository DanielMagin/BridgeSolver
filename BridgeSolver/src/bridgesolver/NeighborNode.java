/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgesolver;

/**
 *
 * @author maginda
 */
public class NeighborNode {

    public Node node;
    public int possibleConnections;

    public NeighborNode(Node node) {
        this(node, (node.value == 1) ? 1 : 2);
    }

    public NeighborNode(Node node, int possibleConnections) {
        this.node = node;
        this.possibleConnections = (possibleConnections == 1) ? 1 : 2;
    }
}
