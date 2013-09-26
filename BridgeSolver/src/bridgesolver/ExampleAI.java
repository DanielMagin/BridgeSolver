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
            applyJustEnoughNeighbourTechnique(n);
        }

    }

    private void applyJustEnoughNeighbourTechnique(Node n) {
        int possibleLines = 0;
        for (int i = 0; i < 4; i++) {
            if (n.neighbours[i] != null && (int) n.neighbours[i].value > 0) {
                possibleLines += (n.neighbours[i].value == 1) ? 1 : 2;
            } else {
                n.neighbours[i] = null;
            }
        }
        if (n.value == possibleLines) {
            for (int i = 0; i < 4; i++) {
                if (n.neighbours[i] != null) {
                    drawLine(n, i, n.neighbours[i].value > 1);
                }
            }
        }

    }

    private void drawLine(Node sourceNode, int indexOfDestNode, boolean doubleLine) {
        Node destNode = sourceNode.neighbours[indexOfDestNode];
        //draw the line
        super.hashi.addCurLine(new Line(sourceNode, destNode, false));
        if(doubleLine){
            super.hashi.addCurLine(new Line(sourceNode, destNode, doubleLine));
        }
        this.hashiPanel.repaint();
        //check for need
        sourceNode.lines[indexOfDestNode] += (doubleLine) ? 2 : 1;
        //check for disrupted neighbour relations
        for (Node nextNode : super.hashi.getNodes().values()) {
            if (nextNode == sourceNode || nextNode == destNode) {
                continue;
            }
            if ((indexOfDestNode == NORTH && nextNode.y < sourceNode.y && nextNode.y > destNode.y) || (indexOfDestNode == SOUTH && nextNode.y > sourceNode.y && nextNode.y < destNode.y)) {
                if (nextNode.neighbours[EAST] != null && nextNode.x < sourceNode.x && nextNode.neighbours[EAST].x > sourceNode.x) {
                    nextNode.neighbours[EAST] = null;
                } else if (nextNode.neighbours[WEST] != null && nextNode.x > sourceNode.x && nextNode.neighbours[WEST].x < sourceNode.x) {
                    nextNode.neighbours[WEST] = null;
                }
            }
            if ((indexOfDestNode == EAST && nextNode.x > sourceNode.x && nextNode.x < destNode.x) || (indexOfDestNode == WEST && nextNode.x < sourceNode.x && nextNode.x > destNode.x)) {
                if (nextNode.neighbours[NORTH] != null && nextNode.y > sourceNode.y && nextNode.neighbours[NORTH].y < sourceNode.y) {
                    nextNode.neighbours[NORTH] = null;
                } else if (nextNode.neighbours[SOUTH] != null && nextNode.y < sourceNode.y && nextNode.neighbours[SOUTH].y > sourceNode.y) {
                    nextNode.neighbours[SOUTH] = null;
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
