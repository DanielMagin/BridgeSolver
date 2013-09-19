/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bridgesolver;

import com.sun.media.sound.AiffFileReader;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Martin Hoelzel
 */
class HashiPanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

        private final int NORTH = 0;
        private final int EAST = 1;
        private final int SOUTH = 2;
        private final int WEST = 3;
        boolean isWin = false;
        boolean lineDraw = false;
        private int scale = 20;
        private int dimX = 10;
        private int dimY = 10;
        private int curNodeX = -1;
        private int curNodeY = -1;
        private Hashimaker maker;
        private Hashi h;
        private JFrame f;
        private JButton newButton;
        private JButton solution;
        private JTextField xField;
        private JTextField yField;
        private Stroke normal;
        private Stroke bold;
        private Timer t = null;

        public HashiPanel() {
                this.setBackground(Color.white);

                maker = new Hashimaker();
                h = maker.newHashi(dimX, dimY);

                normal = new BasicStroke(1);
                bold = new BasicStroke(2);
        }

        public void init() {
                this.addMouseMotionListener(this);
                this.addMouseListener(this);

                newButton = new JButton("New");
                newButton.addActionListener(this);

                solution = new JButton("Solution");
                solution.addActionListener(this);

                xField = new JTextField("" + dimX);
                yField = new JTextField("" + dimY);

                JPanel pp = new JPanel();
                pp.setBackground(new Color(225, 225, 225));
                pp.setLayout(new FlowLayout());
                pp.add(newButton);
                pp.add(solution);
                pp.add(xField);
                pp.add(yField);

                Toolkit tk = this.getToolkit().getDefaultToolkit();
                Dimension d = tk.getScreenSize();

                f = new JFrame("Hashi");
                f.setLayout(new BorderLayout());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(300, 350);
                f.setLocation(d.width / 2 - (f.getWidth()/2), d.height / 2 - (f.getHeight()/2));

                JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setViewportView(this);

                f.add(scrollPane, BorderLayout.CENTER);
                f.add(pp, BorderLayout.SOUTH);
                f.setVisible(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isWin) {
                        return;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.setFont(new Font("sansserif", Font.BOLD, 15));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setStroke(normal);

                Map<String, Line> lines = null;

                if (lineDraw) {
                        //lines = h.getSolLines();
                        AI ai = new AI();
                        ai.solveGame();
                        lines = h.getCurLines();
                } else {
                        lines = h.getCurLines();
                }

                Iterator<Line> it = lines.values().iterator();

                while (it.hasNext()) {
                        Line l = it.next();
                        this.drawLine(g2d, l, true);
                }

                Map<String, Node> nodes = h.getNodes();
                Iterator<Node> it2 = nodes.values().iterator();

                while (it2.hasNext()) {
                        Node n = it2.next();

                        if (n != null) {
                                this.drawNode(g2d, n.x, n.y, n.value);
                        }
                }
        }


        public void drawLine(Graphics2D g, Line l, boolean drawFirst) {
                boolean hasTwo = l.hasTwo;

                if (l.x1 == l.x2) {
                        if (drawFirst) {
                                g.drawLine(l.x1 * scale + 8, l.y1 * scale + 20, l.x2 * scale + 8, l.y2 * scale);
                        }
                        if (hasTwo) {
                                g.drawLine(l.x1 * scale + 12, l.y1 * scale + 20, l.x2 * scale + 12, l.y2 * scale);
                        }
                } else {
                        if (drawFirst) {
                                g.drawLine(l.x1 * scale + 20, l.y1 * scale + 8, l.x2 * scale, l.y2 * scale + 8);
                        }
                        if (hasTwo) {
                                g.drawLine(l.x1 * scale + 20, l.y1 * scale + 12, l.x2 * scale, l.y2 * scale + 12);
                        }
                }
        }

        public void drawNode(Graphics2D g, int x, int y, int value) {
                g.setColor(Color.red);
                if (x == curNodeX && y == curNodeY) {
                        g.setStroke(bold);
                        g.fillOval(x * scale, y * scale, 20, 20);

                        //draw possible lines
                        if (!this.lineDraw) {
                                //this.drawHelpLines(g);
                        }
                        g.setStroke(normal);
                } else {
                        if (value == 0) {
                                g.setColor(Color.gray);
                                g.fillOval(x * scale, y * scale, 20, 20);
                        } else {
                                g.drawOval(x * scale, y * scale, 20, 20);
                        }
                }

                g.setColor(Color.black);
                g.drawString("" + value, x * scale + 7, y * scale + 15);
        }

        public final void newHashi(Hashi h) {
                this.h = h;
                this.setPreferredSize(new Dimension(dimX*scale+10, dimY*scale+10));
                this.revalidate();
                lineDraw = false;
                this.repaint();
        }

        void solve() {
                lineDraw = true;
                this.repaint();
        }

        public void actionPerformed(ActionEvent e) {
                if (e.getSource() == newButton) {
                        isWin = false;
                        if (t != null) {
                                t.cancel();
                        }
                        int a = Integer.valueOf(xField.getText());
                        int b = Integer.valueOf(yField.getText());
                        if (a < 5 || b < 5) {
                                xField.setText("5");
                                yField.setText("5");
                                this.actionPerformed(e);
                        } else {
                                dimX = a;
                                dimY = b;
                                this.newHashi(maker.newHashi(dimX, dimY));
                        }
                } else {
                        this.solve();
                }
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
                if (isWin) {
                        return;
                }
                Point p = e.getPoint();
                int x = p.x / scale;
                int y = p.y / scale;

                Map<String, Node> nodes = h.getNodes();

                String format = h.formatInt(x, y);

                if (x != curNodeX || y != curNodeY) {
                        if (nodes.containsKey(format)) {
                                Node n = nodes.get(format);
                                if (n != null) {
                                        curNodeX = x;
                                        curNodeY = y;
                                        this.repaint();
                                }
                        }
                }
        }

        private void drawHelpLines(Graphics2D g) {
                for (int i = NORTH; i <= WEST; i++) {
                        Point p = this.getNextNode(i);
                        if (p != null) {
                                Line l = new Line(curNodeX, curNodeY, p.x, p.y, true);
                                Map<String, Line> curLines = h.getCurLines();

                                Line cur = curLines.get(l.toString());
                                if (cur == null || !cur.hasTwo) {
                                        int start = h.getNodeValue(l.x1, l.y1);
                                        int end = h.getNodeValue(l.x2, l.y2);
                                        if (start > 0 && end > 0) {
                                                this.drawLine(g, l, false);
                                        }
                                }
                        }
                }
        }

        private Point getNextNode(int direction) {
                Point p;
                Map<String, Node> nodes = h.getNodes();
                switch (direction) {
                        case NORTH:
                                for (int i = curNodeY - 1; i >= 0; i--) {
                                        String format = h.formatInt(curNodeX, i);
                                        if (nodes.containsKey(format)) {
                                                Node n = nodes.get(format);
                                                if (n != null) {
                                                        return new Point(curNodeX, i);
                                                }
                                                break;
                                        }
                                }
                                break;
                        case EAST:
                                for (int i = curNodeX + 1; i < h.dimX; i++) {
                                        String format = h.formatInt(i, curNodeY);
                                        if (nodes.containsKey(format)) {
                                                Node n = nodes.get(format);
                                                if (n != null) {
                                                        return new Point(i, curNodeY);
                                                }
                                                break;
                                        }
                                }
                                break;
                        case WEST:
                                for (int i = curNodeX - 1; i >= 0; i--) {
                                        String format = h.formatInt(i, curNodeY);
                                        if (nodes.containsKey(format)) {
                                                Node n = nodes.get(format);
                                                if (n != null) {
                                                        return new Point(i, curNodeY);
                                                }
                                                break;
                                        }
                                }
                                break;
                        case SOUTH:
                                for (int i = curNodeY + 1; i < h.dimY; i++) {
                                        String format = h.formatInt(curNodeX, i);
                                        if (nodes.containsKey(format)) {
                                                Node n = nodes.get(format);
                                                if (n != null) {
                                                        return new Point(curNodeX, i);
                                                }
                                                break;
                                        }
                                }
                                break;
                }
                return null;
        }

        public void mouseClicked(MouseEvent e) {
                if (isWin) {
                        return;
                }
                if (e.getButton() == MouseEvent.BUTTON1 && !this.lineDraw) {
                        Point p = e.getPoint();
                        int direction = getDirectionFormCurrentNode(p.x, p.y);

                        Point nextNode = this.getNextNode(direction);
                        if (nextNode != null) {
                                Line l = new Line(curNodeX, curNodeY, nextNode.x, nextNode.y, false);
                                h.addCurLine(l);
                                if (h.isWin()) {
                                        this.playWinAnimation();
                                }
                                this.repaint();
                        }
                }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        private int getDirectionFormCurrentNode(int x, int y) {
                int cX = curNodeX * scale + 10;
                int cY = curNodeY * scale + 10;

                int relX = x - cX;
                int relY = -y + cY;

                if (relX >= 0 && relY >= 0) {
                        if (relY >= relX) {
                                return NORTH;
                        } else {
                                return EAST;
                        }
                } else if (relX < 0 && relY >= 0) {
                        if (relY >= -relX) {
                                return NORTH;
                        } else {
                                return WEST;
                        }
                } else if (relX < 0 && relY < 0) {
                        if (-relY >= -relX) {
                                return SOUTH;
                        } else {
                                return WEST;
                        }
                } else {
                        if (-relY >= relX) {
                                return SOUTH;
                        } else {
                                return EAST;
                        }
                }
        }

        public int getRandom(int max) {
                return (int) Math.round(Math.random() * max);
        }

        private void playWinAnimation() {
                isWin = true;
                Graphics2D g = (Graphics2D) this.getGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

                int height = this.getHeight();
                int width = this.getWidth();

                t = new Timer();
                t.schedule(new WinTask(g, width, height), 0, 20);
        }
}
