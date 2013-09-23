/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgesolver;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.TimerTask;

/**
 *
 * @author Martin Hoelzel
 */
public class WinTask extends TimerTask {

        private Graphics2D g;
        private int width;
        private int height;

        public WinTask(Graphics2D g, int width, int height) {
                this.g = g;
                this.width = width;
                this.height = height;
        }

        public int getRandom(int max) {
                return (int) Math.round(Math.random() * max);
        }

        @Override
        public void run() {
                Color c = new Color(this.getRandom(255), this.getRandom(255), this.getRandom(255));

                Stroke s = new BasicStroke(this.getRandom(15));

                Font f = new Font("sansserif", Font.BOLD, this.getRandom(100));

                g.setStroke(s);
                g.setColor(c);
                g.setFont(f);

                int x1 = this.getRandom(width);
                int y1 = this.getRandom(height);

                int x2 = this.getRandom(width);
                int y2 = this.getRandom(height);

                int selection = this.getRandom(2);

                switch (selection) {
                        case 0: g.drawLine(x1, y1, x2, y2);
                        break;
                        case 1: g.drawString("WIN", x1, y1);
                        break;
                        case 2: g.drawOval(x1, y1, this.getRandom(300), this.getRandom(300));
                        break;
                }

                
        }
}