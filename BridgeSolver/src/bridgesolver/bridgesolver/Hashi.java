/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgesolver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Martin Hoelzel
 */
public class Hashi {

        private Map<String, Node> nodes;
        private Map<String, Line> solLines;
        private Map<String, Line> curLines;
        public int dimX;
        public int dimY;

        public Hashi(int dimX, int dimY) {
                this.dimX = dimX;
                this.dimY = dimY;

                solLines = new HashMap<>();
                curLines = new HashMap<>();

                nodes = new HashMap<>();
        }

        public String formatInt(int x, int y) {
                String result = "";
                if (x < 10) {
                        result += "0" + x;
                } else {
                        result += x;
                }
                if (y < 10) {
                        result += "0" + y;
                } else {
                        result += y;
                }
                return result;
        }

        void addSolLine(Line line) {
                solLines.put(line.toString(), line);
                int lineStrength = 1;
                if (line.hasTwo) {
                        lineStrength = 2;
                }

                this.increaseNode(line.x1, line.y1, lineStrength);
                this.increaseNode(line.x2, line.y2, lineStrength);
        }

        void addCurLine(Line line) {
                String format = line.toString();
                Line l = curLines.get(format);
                if (l == null) {
                        int start = this.getNodeValue(line.x1, line.y1);
                        int end = this.getNodeValue(line.x2, line.y2);

                        if (start > 0 && end > 0) {
                                //add line
                                line.hasTwo = false;
                                curLines.put(format, line);

                                this.decreaseNode(line.x1, line.y1, 1);
                                this.decreaseNode(line.x2, line.y2, 1);
                        }
                } else if (!l.hasTwo) {
                        int start = this.getNodeValue(l.x1, l.y1);
                        int end = this.getNodeValue(l.x2, l.y2);
                        if (start > 0 && end > 0) {
                                //add line to existing
                                l.hasTwo = true;

                                this.decreaseNode(l.x1, l.y1, 1);
                                this.decreaseNode(l.x2, l.y2, 1);
                        } else {
                                //remove both lines
                                curLines.remove(format);

                                this.increaseNode(line.x1, line.y1, 1);
                                this.increaseNode(line.x2, line.y2, 1);
                        }
                } else {
                        //remove both lines
                        curLines.remove(format);

                        this.increaseNode(line.x1, line.y1, 2);
                        this.increaseNode(line.x2, line.y2, 2);
                }

        }

        public synchronized int getNodeValue(int x, int y) {
                String format = this.formatInt(x, y);
                Node n = nodes.get(format);
                if (n != null) {
                        return n.value;
                }
                return 0;
        }

        public void increaseNode(int x, int y, int amount) {
                String format = this.formatInt(x, y);
                if (nodes.containsKey(format)) {
                        Node n = nodes.get(format);
                        n.value += amount;
                        if(n.isCreated() && n.value > n.getInitialValue()){
                            n.value = n.getInitialValue();
                        }
                } else {
                        Node n = new Node(x, y, amount);
                        nodes.put(format, n);
                }
        }

        public void decreaseNode(int x, int y, int amount) {
                String format = this.formatInt(x, y);
                if (nodes.containsKey(format)) {
                        Node n = nodes.get(format);
                        if (n.value >= amount) {
                                n.value -= amount;
                        }
                } else {
                        Node n = new Node(x, y, amount);
                        nodes.put(format, n);
                }
        }

        public boolean isWin(){
                Iterator<Node> it = nodes.values().iterator();
                while(it.hasNext()){
                        Node n = it.next();
                        if(n.value!=0) return false;
                }
                return true;
        }

        public Map<String, Line> getSolLines() {
                return solLines;
        }

        public Map<String, Line> getCurLines() {
                return curLines;
        }

        public Map<String, Node> getNodes() {
                return nodes;
        }
        
        public void created(){
            Iterator<Node> it = nodes.values().iterator();
                while(it.hasNext()){
                        Node n = it.next();
                        n.created();
                }
        }
}