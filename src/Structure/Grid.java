package Structure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;


public class Grid extends JPanel implements MouseListener {
    private Vector2F p1 = null, p2 = null;
    private int boxSize;
    private final ArrayList<Rectangle> walls = new ArrayList<>();
    private final ArrayList<Rectangle> entrances = new ArrayList<>();
    private final Stack<Rectangle> stack = new Stack<>();
    private final int VERTICAL_ENTRANCE_LENGTH = 7;
    private final int HORIZONTAL_ENTRANCE_LENGTH = 5;
    private Vector2F topLeftPoint = new Vector2F(999, 999);

    public Grid() {
        this.addMouseListener(this);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                boxSize = Math.min(getHeight(), getWidth()) / 100;
                repaint();
            }
        });
    }

    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    public Stack<Rectangle> getStack() {
        return stack;
    }

    public ArrayList<Rectangle> getEntrances() {
        return entrances;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        for (Rectangle wall : walls) {
            g.fillRect(wall.x * boxSize, wall.y * boxSize, wall.width * boxSize, wall.height * boxSize);
        }
        g.setColor(Color.BLUE);
        for (Rectangle entrance : entrances) {
            g.fillRect(entrance.x * boxSize, entrance.y * boxSize, entrance.width * boxSize, entrance.height * boxSize);
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
        if (!getEntrances().isEmpty() && stack.getLast().equals(getEntrances().getLast())){
            getEntrances().removeLast();
        } else {
            getWalls().removeLast();
        }

        stack.pop();
        repaint();
    }

    public void addVerticalEntrance() {
        if (p1 == null) return;
        entrances.add(new Rectangle((int) p1.getX() - (HORIZONTAL_ENTRANCE_LENGTH / 2), (int) p1.getY(), HORIZONTAL_ENTRANCE_LENGTH, 1));
        stack.add(entrances.getLast());
        p1 = null;
        repaint();
    }

    public void addHorizontalEntrance() {
        if (p1 == null) return;
        entrances.add(new Rectangle((int) p1.getX(), (int) p1.getY() - (VERTICAL_ENTRANCE_LENGTH / 2), 1, VERTICAL_ENTRANCE_LENGTH));
        stack.add(entrances.getLast());
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
        stack.add(walls.getLast());

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
}



