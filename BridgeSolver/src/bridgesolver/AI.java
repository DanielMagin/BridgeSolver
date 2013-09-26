/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgesolver;

/**
 *
 * @author Stazzer
 */
public abstract class AI {
    
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    
    
    protected Hashi hashi;
    
    public AI(Hashi hashi){
        this.hashi = hashi;
    }
    
    public void solveGame(){
    }
    
    protected void drawLine(Node n1, Node n2){
        Line line = new Line(n1, n2, false);
        hashi.addCurLine(line);
    }
}
