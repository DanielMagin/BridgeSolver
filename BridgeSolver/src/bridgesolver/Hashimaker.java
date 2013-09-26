/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bridgesolver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Hoelzel
 */
public class Hashimaker {

        private Hashi hashi;

        private boolean[][] blocked;

        public Hashi newHashi(int dimX, int dimY){
                hashi = new Hashi(dimX, dimY);
                blocked = new boolean[dimX][dimY];
                for (int i = 0; i < blocked.length; i++) {
                        for (int j = 0; j < blocked[i].length; j++) {
                                blocked[i][j] = false;
                        }
                }

                //start
                int startX = this.getRandom(dimX-1);
                int startY = this.getRandom(dimY-1);

                List<Node> remaining = new ArrayList<>();
                remaining.add(new Node(startX, startY, 0));

                while(remaining.size()>0){
                        int selection = this.getRandom(remaining.size()-1);

                        Node n = remaining.get(selection);

                        List<Node> possible = this.getPossibleNodes(n.x, n.y);
                        if(possible.isEmpty()) remaining.remove(n);
                        else {
                                int newSelection = this.getRandom(possible.size()-1);

                                Node newNode = possible.get(newSelection);
                                remaining.add(newNode);

                                int lineBold = this.getRandom(1);
                                Line line;
                                if(lineBold==0) line = new Line(n, newNode, false);
                                else line = new Line(n, newNode, true);

                                hashi.addSolLine(line);

                                blockWay(line);
                        }
                }

                this.getPossibleNodes(3, 4);
                hashi.created();
                return hashi;
        }

        public int getRandom(int max){
                return (int)Math.round(Math.random()*max);
        }

        public List<Node> getPossibleNodes(int x, int y){
                List<Node> result = new ArrayList<>();
                int cap;
                int dimX = hashi.dimX;
                int dimY = hashi.dimY;


                //north direction
                if(y>=2){
                        cap = -1;
                        for(int i = y-1; i>=0; i--){
                                if(blocked[x][i]) {
                                        cap = i;
                                        break;
                                }
                        }

                        for(int i = y-2; i>cap; i--){
                                if(isUnblocked(x,i)) result.add(new Node(x, i, 0));
                        }
                }

                //east direction
                if(x+2<dimX){
                        cap = dimX;
                        for(int i = x+1; i<dimX; i++){
                                if(blocked[i][y]) {
                                        cap = i;
                                        break;
                                }
                        }

                        for(int i = x+2; i<cap; i++){
                                if(isUnblocked(i,y)) result.add(new Node(i, y, 0));
                        }
                }

                //south direction
                if(y+2<dimY){
                        cap = dimY;
                        for(int i = y+1; i<dimY; i++){
                                if(blocked[x][i]) {
                                        cap = i;
                                        break;
                                }
                        }

                        for(int i = y+2; i<cap; i++){
                                if(isUnblocked(x,i)) result.add(new Node(x, i, 0));
                        }
                }

                //west direction
                if(x>=2){
                        cap = -1;
                        for(int i = x-1; i>=0; i--){
                                if(blocked[i][y]) {
                                        cap = i;
                                        break;
                                }
                        }

                        for(int i = x-2; i>cap; i--){
                                if(isUnblocked(i,y)) result.add(new Node(i, y, 0));
                        }
                }

                return result;
        }

        private boolean isUnblocked(int x, int y) {
                //north
                if(y-1>=0&&hashi.getNodeValue(x, y-1)!=0) return false;
                //east
                if(x+1<hashi.dimX&&hashi.getNodeValue(x+1, y)!=0) return false;
                //south
                if(y+1<hashi.dimY&&hashi.getNodeValue(x, y+1)!=0) return false;
                //west
                if(x-1>=0&&hashi.getNodeValue(x-1, y)!=0) return false;

                return true;
        }

        private void blockWay(Line line) {
                int x1;
                int x2;
                if(line.x1<=line.x2) {
                        x1 = line.x1;
                        x2 = line.x2;
                }else{
                        x1 = line.x2;
                        x2 = line.x1;
                }

                for(int i = x1; i<=x2; i++) blocked[i][line.y1]=true;

                int y1;
                int y2;
                if(line.y1<=line.y2) {
                        y1 = line.y1;
                        y2 = line.y2;
                }else{
                        y1 = line.y2;
                        y2 = line.y1;
                }

                for(int i = y1; i<=y2; i++) blocked[line.x1][i]=true;
        }

}