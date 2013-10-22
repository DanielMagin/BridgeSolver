/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgesolver;

/**
 *
 * @author maginda
 */
public class AI {

    private HashiPanel hashiPanel;
    private Hashi hashi;
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    public AI(Hashi h, HashiPanel hp) {
        this.hashi = h;
        this.hashiPanel = hp;
        for (Node n : this.hashi.getNodes().values()) {
            exploreNeighbours(n);
        }
        for (Node n : this.hashi.getNodes().values()) {
            applyIsolationTechnique(n);
        }
    }

    public boolean solveNode(Node n) {
        return applyJustEnoughNeighbourTechnique(n) | applyFewNeighborTechnique(n);
    }

    public boolean solveRound() {
        boolean ret = false;
        for (Node n : this.hashi.getNodes().values()) {
            ret = solveNode(n);
        }
        return ret;
    }

    public boolean solveHashi() {
        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = solveRound();
        }
        return hasChanged;
    }

    public boolean solveHashiWithDFS() {
        solveHashi();
        if (this.hashi.isWin()) {
            return true;
        } else {
            Hashi backupHashi = new Hashi(this.hashi);
            for(Node n : backupHashi.getNodes().values()){
                this.exploreNeighbours(n);
            }
            
            for (Node n : backupHashi.getNodes().values()) {
                if (n.value > 0) {
                    for (int i = 0; i < 4; i++) {
                        if (n.neighbours[i] != null && n.neighbours[i].value > 0) {
                            this.hashi.addCurLine(new Line(n, n.neighbours[i], false));
                            System.out.println("added line in DFS");
                            if (this.hashi.isWin()) {
                                return true;
                            } else {
                                this.exploreNeighbours(n);
                                if (solveHashiWithDFS()) {
                                    return true;
                                } else {
                                    this.hashiPanel.setHashi(backupHashi);
                                    this.hashi = backupHashi;
                                    System.out.println("Backtracking");
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean applyFewNeighborTechnique(Node n) {
        if (n.value == 0) {
            return false;
        }
        boolean hasChanged = false;
        int sumOfPossibleLines = 0;
        int nValue = (n.value == 1) ? 1 : 2;
        for (int i = 0; i < 4; i++) {
            if (n.neighbours[i] != null) {
                if (n.neighbours[i].value == 0) {
                    destroyConnection(n, i);
                    continue;
                }
                int neighbourValue = (n.neighbours[i].value == 1) ? 1 : 2;
                sumOfPossibleLines += Math.min(nValue, neighbourValue);
            }
        }
        if (n.value + 1 == sumOfPossibleLines) {
            for (int i = 0; i < 4; i++) {
                if (n.neighbours[i] != null && n.neighbours[i].value >= 2) {
                    drawLine(n, i, false);
                    hasChanged = true;
                }
            }
        }
        return hasChanged;
    }

    private void applyIsolationTechnique(Node n) {
        if (n.getInitialValue() == 0) {
            return;
        }
//        if (n.getInitialValue() <= 2) {
        if (n.getInitialValue() == 1) {
            for (int i = 0; i < 4; i++) {
                if (n.neighbours[i] != null && n.neighbours[i].getInitialValue() == 1) {
                    destroyConnection(n, i);
                }
            }
//            } else {
//                for (int i = 0; i < 4; i++) {
//                    if (n.neighbours[i] != null && n.neighbours[i].getInitialValue() <= 2) {
//                        n.neighbours[i].possibleConnections = 1;
//                    }
//                }
//            }
        }
    }

    private boolean applyJustEnoughNeighbourTechnique(Node n) {
        if (n.value == 0) {
            return false;
        }
        boolean hasChanged = false;
        int sumOfPossibleLines = 0;
        int nValue = (n.value == 1) ? 1 : 2;
        for (int i = 0; i < 4; i++) {
            if (n.neighbours[i] != null) {
                if (n.neighbours[i].value == 0) {
                    destroyConnection(n, i);
                    continue;
                }
                int neighbourValue = (n.neighbours[i].value == 1) ? 1 : 2;
                sumOfPossibleLines += Math.min(nValue, neighbourValue);
            }
        }
        if (n.value == sumOfPossibleLines) {
            for (int i = 0; i < 4; i++) {
                if (n.neighbours[i] != null) {
                    drawLine(n, i, (n.value >= 2 && n.neighbours[i].value >= 2));
                    hasChanged = true;
                }
            }
        }
        return hasChanged;
    }

    /**
     * Removes a connection between two nodes.
     *
     * @param n one of the nodes
     * @param indexOfOtherNode the index of the other node
     */
    private void destroyConnection(Node n, int indexOfOtherNode) {
        if (indexOfOtherNode <= 1) {
            n.neighbours[indexOfOtherNode].neighbours[indexOfOtherNode + 2] = null;
        } else {
            n.neighbours[indexOfOtherNode].neighbours[indexOfOtherNode - 2] = null;
        }
        n.neighbours[indexOfOtherNode] = null;
    }

    private void drawLine(Node sourceNode, int indexOfDestNode, boolean doubleLine) {
        Node destNode = sourceNode.neighbours[indexOfDestNode];
        //draw the line
        this.hashi.addCurLine(new Line(sourceNode, destNode, false));
        if (sourceNode.neighbours[indexOfDestNode].value == 0 || destNode.value == 0) {
            destroyConnection(sourceNode, indexOfDestNode);
        }
        if (doubleLine) {
            this.hashi.addCurLine(new Line(sourceNode, destNode, doubleLine));
            destroyConnection(sourceNode, indexOfDestNode);
        }
        //check for disrupted neighbour relations
        for (Node nextNode : this.hashi.getNodes().values()) {
            if (nextNode == sourceNode || nextNode == destNode) {
                continue;
            }
            if ((indexOfDestNode == NORTH && nextNode.y < sourceNode.y && nextNode.y > destNode.y) || (indexOfDestNode == SOUTH && nextNode.y > sourceNode.y && nextNode.y < destNode.y)) {
                if (nextNode.neighbours[EAST] != null && nextNode.x < sourceNode.x && nextNode.neighbours[EAST].x > sourceNode.x) {
                    destroyConnection(nextNode, EAST);
                } else if (nextNode.neighbours[WEST] != null && nextNode.x > sourceNode.x && nextNode.neighbours[WEST].x < sourceNode.x) {
                    destroyConnection(nextNode, WEST);
                }
            }
            if ((indexOfDestNode == EAST && nextNode.x > sourceNode.x && nextNode.x < destNode.x) || (indexOfDestNode == WEST && nextNode.x < sourceNode.x && nextNode.x > destNode.x)) {
                if (nextNode.neighbours[NORTH] != null && nextNode.y > sourceNode.y && nextNode.neighbours[NORTH].y < sourceNode.y) {
                    destroyConnection(nextNode, NORTH);
                } else if (nextNode.neighbours[SOUTH] != null && nextNode.y < sourceNode.y && nextNode.neighbours[SOUTH].y > sourceNode.y) {
                    destroyConnection(nextNode, SOUTH);
                }
            }
        }
    }

    private void exploreNeighbours(Node n) {
        for (Node nextNode : this.hashi.getNodes().values()) {
            if (nextNode == n) {
                continue;
            }
            if (nextNode.x == n.x) {
                if (nextNode.y < n.y) {
                    if (n.neighbours[NORTH] == null || nextNode.y > n.neighbours[NORTH].y) {
                        n.neighbours[NORTH] = nextNode;
                    }
                } else {
                    if (n.neighbours[SOUTH] == null || nextNode.y < n.neighbours[SOUTH].y) {
                        n.neighbours[SOUTH] = nextNode;
                    }
                }
            } else if (nextNode.y == n.y) {
                if (nextNode.x < n.x) {
                    if (n.neighbours[WEST] == null || nextNode.x > n.neighbours[WEST].x) {
                        n.neighbours[WEST] = nextNode;
                    }
                } else {
                    if (n.neighbours[EAST] == null || nextNode.x < n.neighbours[EAST].x) {
                        n.neighbours[EAST] = nextNode;
                    }
                }
            }

        }
    }
}
