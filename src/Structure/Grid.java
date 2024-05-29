package Structure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;


public class Grid extends JPanel implements MouseListener, MouseMotionListener {
    private Vector2F p1 = null, p2 = null, highlighted = new Vector2F();
    private int boxSize;
    private final ArrayList<Rectangle> walls = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final Stack<Integer> stack = new Stack<>();
    private Vector2F topLeftPoint = new Vector2F(999, 999);

    public Grid() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                boxSize = Math.min(getHeight(), getWidth()) / 20;
                repaint();
            }
        });
    }

    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    public Stack<Integer> getStack() {
        return stack;
    }

    public ArrayList<Entrance> getEntrances() {
        return entrances;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY.brighter());
        g.fillRect((int) highlighted.getX() * boxSize, 0, boxSize, getHeight());
        g.fillRect(0, (int) highlighted.getY() * boxSize, getWidth(), boxSize);

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) highlighted.getX() * boxSize, ((int) highlighted.getY() - 5) * boxSize, boxSize, boxSize * 11);
        g.fillRect(((int) highlighted.getX() - 5) * boxSize, (int) highlighted.getY() * boxSize, boxSize * 11, boxSize);


        g.setColor(Color.LIGHT_GRAY.darker());
        g.fillRect((int) highlighted.getX() * boxSize, ((int) highlighted.getY() - 3) * boxSize, boxSize, boxSize * 7);
        g.fillRect(((int) highlighted.getX() - 2) * boxSize, (int) highlighted.getY() * boxSize, boxSize * 5, boxSize);


        g.setColor(Color.RED);
        for (Rectangle wall : walls) {
            g.fillRect(wall.x * boxSize, wall.y * boxSize, wall.width * boxSize, wall.height * boxSize);
        }
        g.setColor(Color.BLUE);
        for (Entrance entrance : entrances) {
            entrance.draw(g, boxSize);
        }
        g.setColor(Color.GREEN);
        if (p2 == null && p1 != null) {
            g.fillRect((int) p1.getX() * boxSize, (int) p1.getY() * boxSize, boxSize, boxSize);
        }

        g.setColor(Color.BLACK);
        if (boxSize <= 0) return;
        for (int i = 0; i < getWidth(); i += boxSize) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += boxSize) {
            g.drawLine(0, i, getWidth(), i);
        }
    }

    public void undoLastMove() {
        if (stack.isEmpty()) return;
        if (stack.getLast() == 1){
            getEntrances().removeLast();
        } else {
            getWalls().removeLast();
        }

        stack.pop();
        p1 = null;
        repaint();
    }


    public void addEntrance(int xOffset, int yOffset) {
        if (p1 == null) return;
        entrances.add(new Entrance(p1, p1.getTranslated(new Vector2F(xOffset, yOffset))));
        stack.add(1);
        p1 = null;
        repaint();
    }

    public void addRect() {
        if (p1 == null || p2 == null) {
            System.out.println("BUG FOUND, ADD RECT CALLED WHEN NO P1 SET");
            return;
        }

        if (p1.getX() > p2.getX()) {
            double temp = p1.getX();
            p1.setX(p2.getX());
            p2.setX(temp);
        }

        if (p1.getY() > p2.getY()) {
            double temp = p1.getY();
            p1.setY(p2.getY());
            p2.setY(temp);
        }

        if (topLeftPoint.getX() > p1.getX()) {
            topLeftPoint.setX(p1.getX());
        }
        if (topLeftPoint.getY() > p1.getY()) {
            topLeftPoint.setY(p1.getY());
        }

        walls.add(new Rectangle((int) p1.getX(), (int) p1.getY(), (int) p1.getXDistance(p2) + 1, (int) p1.getYDistance(p2) + 1));
        stack.add(2);

        p1 = null;
        p2 = null;
        repaint();
    }

    public void reset() {
        getWalls().clear();
        getEntrances().clear();
        getStack().clear();
        p1 = null;
        p2 = null;
        repaint();
    }

    public Vector2F getLeftMostPoint() {
        return topLeftPoint;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() / boxSize, mouseY = e.getY() / boxSize;
        if (p1 == null) {
            p1 = new Vector2F(mouseX, mouseY);
        } else if (p2 == null) {
            p2 = new Vector2F(mouseX, mouseY);
            addRect();
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (boxSize == 0) return;
        int mouseX = e.getX() / boxSize, mouseY = e.getY() / boxSize;
        highlighted = new Vector2F(mouseX, mouseY);
        repaint();
    }
}



