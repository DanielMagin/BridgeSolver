/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgesolver;

/**
 *
 * @author maginda
 */
public class ExampleAI extends AI {

    private HashiPanel hashiPanel;

    public ExampleAI(Hashi h, HashiPanel hp) {
        super(h);
        this.hashiPanel = hp;
    }

    public void solve() {
        System.out.println("solved");
        for (Node n : super.hashi.getNodes().values()) {
            exploreNeighbours(n);
        }
        for (Node n : super.hashi.getNodes().values()) {
            applyIsolationTechnique(n);
        }
//        while (true) {
        for (int i = 0; i < 32; i++) {
            for (Node n : super.hashi.getNodes().values()) {
                applyJustEnoughNeighbourTechnique(n);
                applyFewNeighborTechnique(n);
            }
        }
    }
    
    private void applyFewNeighborTechnique(Node n){
        if (n.value == 0) {
            return;
        }
        int sumOfPossibleLines = 0;
        for (int i = 0; i < 4; i++) {
            if (n.neighbours[i] != null) {
                n.neighbours[i].possibleConnections = Math.min(n.neighbours[i].possibleConnections, n.neighbours[i].node.value);
                sumOfPossibleLines += n.neighbours[i].possibleConnections;
                if (n.neighbours[i].possibleConnections == 0) {
                    destroyConnection(n, i);
                }
            }
        }
        if(n.value+1==sumOfPossibleLines){
            for(int i=0; i<4; i++){
                if(n.neighbours[i]!=null && n.neighbours[i].possibleConnections==2){
                    drawLine(n, i, false);
                }
            }
        }
    }

    private void applyIsolationTechnique(Node n) {
        if (n.getInitialValue()==0){
            return;
        }
        if (n.getInitialValue() <= 2) {
            if (n.getInitialValue() == 1) {
                for (int i = 0; i < 4; i++) {
                    if (n.neighbours[i] != null && n.neighbours[i].node.getInitialValue() == 1) {
                        destroyConnection(n, i);
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (n.neighbours[i] != null && n.neighbours[i].node.getInitialValue() <= 2) {
                        n.neighbours[i].possibleConnections = 1;
                    }
                }
            }
        }
    }

    private void applyJustEnoughNeighbourTechnique(Node n) {
        if (n.value == 0) {
            return;
        }
        int sumOfPossibleLines = 0;
        for (int i = 0; i < 4; i++) {
            if (n.neighbours[i] != null) {
                n.neighbours[i].possibleConnections = Math.min(n.neighbours[i].possibleConnections, n.neighbours[i].node.value);
                sumOfPossibleLines += n.neighbours[i].possibleConnections;
                if (n.neighbours[i].possibleConnections == 0) {
                    destroyConnection(n, i);
                }
            }
        }
        if (n.value == sumOfPossibleLines) {
            for (int i = 0; i < 4; i++) {
                if (n.neighbours[i] != null) {
                    drawLine(n, i, n.neighbours[i].possibleConnections > 1);
                }
            }
        }
    }

    /**
     * Removes a connection between two nodes.
     *
     * @param n one of the nodes
     * @param indexOfOtherNode the index of the other node
     */
    private void destroyConnection(Node n, int indexOfOtherNode) {
        if (indexOfOtherNode <= 1) {
            n.neighbours[indexOfOtherNode].node.neighbours[indexOfOtherNode + 2] = null;
        } else {
            n.neighbours[indexOfOtherNode].node.neighbours[indexOfOtherNode - 2] = null;
        }
        n.neighbours[indexOfOtherNode] = null;
    }

    private void drawLine(Node sourceNode, int indexOfDestNode, boolean doubleLine) {
        Node destNode = sourceNode.neighbours[indexOfDestNode].node;
        //draw the line
        super.hashi.addCurLine(new Line(sourceNode, destNode, false));
        sourceNode.neighbours[indexOfDestNode].possibleConnections--;
        if (sourceNode.neighbours[indexOfDestNode].possibleConnections == 0) {
            destroyConnection(sourceNode, indexOfDestNode);
        }
        if (doubleLine) {
            super.hashi.addCurLine(new Line(sourceNode, destNode, doubleLine));
            destroyConnection(sourceNode, indexOfDestNode);
        }
        this.hashiPanel.refresh();
        //check for disrupted neighbour relations
        for (Node nextNode : super.hashi.getNodes().values()) {
            if (nextNode == sourceNode || nextNode == destNode) {
                continue;
            }
            if ((indexOfDestNode == NORTH && nextNode.y < sourceNode.y && nextNode.y > destNode.y) || (indexOfDestNode == SOUTH && nextNode.y > sourceNode.y && nextNode.y < destNode.y)) {
                if (nextNode.neighbours[EAST] != null && nextNode.x < sourceNode.x && nextNode.neighbours[EAST].node.x > sourceNode.x) {
                    destroyConnection(nextNode, EAST);
                } else if (nextNode.neighbours[WEST] != null && nextNode.x > sourceNode.x && nextNode.neighbours[WEST].node.x < sourceNode.x) {
                    destroyConnection(nextNode, WEST);
                }
            }
            if ((indexOfDestNode == EAST && nextNode.x > sourceNode.x && nextNode.x < destNode.x) || (indexOfDestNode == WEST && nextNode.x < sourceNode.x && nextNode.x > destNode.x)) {
                if (nextNode.neighbours[NORTH] != null && nextNode.y > sourceNode.y && nextNode.neighbours[NORTH].node.y < sourceNode.y) {
                    destroyConnection(nextNode, NORTH);
                } else if (nextNode.neighbours[SOUTH] != null && nextNode.y < sourceNode.y && nextNode.neighbours[SOUTH].node.y > sourceNode.y) {
                    destroyConnection(nextNode, SOUTH);
                }
            }
        }
    }

    private void exploreNeighbours(Node n) {
        for (Node nextNode : super.hashi.getNodes().values()) {
            if (nextNode == n) {
                continue;
            }
            if (nextNode.x == n.x) {
                if (nextNode.y < n.y) {
                    if (n.neighbours[NORTH] == null || nextNode.y > n.neighbours[NORTH].node.y) {
                        n.neighbours[NORTH] = new NeighborNode(nextNode, n.value);
                    }
                } else {
                    if (n.neighbours[SOUTH] == null || nextNode.y < n.neighbours[SOUTH].node.y) {
                        n.neighbours[SOUTH] = new NeighborNode(nextNode, n.value);
                    }
                }
            } else if (nextNode.y == n.y) {
                if (nextNode.x < n.x) {
                    if (n.neighbours[WEST] == null || nextNode.x > n.neighbours[WEST].node.x) {
                        n.neighbours[WEST] = new NeighborNode(nextNode, n.value);
                    }
                } else {
                    if (n.neighbours[EAST] == null || nextNode.x < n.neighbours[EAST].node.x) {
                        n.neighbours[EAST] = new NeighborNode(nextNode, n.value);
                    }
                }
            }

        }
    }
}
