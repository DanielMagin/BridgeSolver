/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bridgesolver;

/**
 *
 * @author Martin Hoelzel
 */
public class Node {

        public int x;
        public int y;

        public int value;

        public Node(int x, int y, int value) {
                this.x = x;
                this.y = y;
                this.value = value;
        }

        @Override
        public String toString() {
                String result = "";
                result += this.formatInt(x);
                result += this.formatInt(y);
                return result;
        }

        public String formatInt(int i){
                String result = "";
                if(i<10) result += "0"+i;
                else result+= i;
                return result;
        }
}
