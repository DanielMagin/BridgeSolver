/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bridgesolver;

/**
 *
 * @author Martin Hoelzel
 */
public class Line {

        public int x1;
        public int y1;

        public int x2;
        public int y2;

        public boolean hasTwo;

        public Line(int x1, int y1, int x2, int y2, boolean hasTwo) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
                this.hasTwo = hasTwo;

                this.flip();
        }

        public final void flip(){
                if(x1>x2) {
                        int old = x1;
                        x1=x2;
                        x2=old;
                }
                if(y1>y2){
                        int old = y1;
                        y1=y2;
                        y2=old;
                }
        }

        @Override
        public String toString() {
                String result = "";
                result+=this.formatInt(x1);
                result+=this.formatInt(y1);
                result+=this.formatInt(x2);
                result+=this.formatInt(y2);
                return result;
        }

        public Line(Node start, Node end, boolean hasTwo) {
                this.x1 = start.x;
                this.y1 = start.y;
                this.x2 = end.x;
                this.y2 = end.y;
                this.hasTwo = hasTwo;

                this.flip();
        }

        public String formatInt(int i){
                String result = "";
                if(i<10) result += "0"+i;
                else result+= i;
                return result;
        }

}
