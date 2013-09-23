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
