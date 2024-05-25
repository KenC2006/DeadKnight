package Structure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;


public class Grid extends JPanel implements MouseListener {
    public static int sR = -1, sC, eR, eC;
    private int boxWidth, boxHeight;
    private int set = 0;
    private final ArrayList<Rectangle> walls = new ArrayList<>();
    private final ArrayList<Rectangle> entrances = new ArrayList<>();
    private final Stack<Rectangle> stack = new Stack<>();
    private boolean horizontalEntranceMode = false;
    private boolean verticalEntranceMode = false;
    private final int ENTRANCE_LENGTH=5;
    private int leftMostPoint=Integer.MAX_VALUE;

    public Grid(int row, int col) {
        this.addMouseListener(this);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                boxWidth = getWidth() / col;
                boxHeight = getHeight() / row;
                repaint();
            }
        });
    }

    public void setVerticalEntranceMode(boolean verticalEntranceMode) {
        this.verticalEntranceMode = verticalEntranceMode;
    }

    public void setHorizontalEntranceMode(boolean horizontalEntrance) {
        horizontalEntranceMode = horizontalEntrance;
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
            g.fillRect(wall.x, wall.y, wall.width, wall.height);
        }
        g.setColor(Color.BLUE);
        for (Rectangle entrance : entrances) {
            g.fillRect(entrance.x, entrance.y, entrance.width, entrance.height);
        }
        g.setColor(Color.GREEN);
        if (set == 1) {
            g.fillRect(sC * boxWidth, sR * boxHeight, boxWidth, boxHeight);
        }

        g.setColor(Color.BLACK);
        if (boxWidth > 0 && boxHeight > 0) {
            for (int i = 0; i < getWidth(); i += boxWidth) {
                g.drawLine(i, 0, i, getHeight());
            }
            for (int i = 0; i < getHeight(); i += boxHeight) {
                g.drawLine(0, i, getWidth(), i);
            }

        }
    }

    public int getBoxWidth() {
        return boxWidth;
    }


    public int getBoxHeight() {
        return boxHeight;
    }


    public void addRect(int x, int y) {
        if (horizontalEntranceMode) {
            set = 0;
            entrances.add(new Rectangle(x * boxWidth, (y - 2) * boxHeight, boxWidth, boxHeight * ENTRANCE_LENGTH));
            stack.add(entrances.getLast());
            horizontalEntranceMode = false;
            repaint();
            return;
        }
        else if (verticalEntranceMode) {
            set = 0;
            entrances.add(new Rectangle((x-2) * boxWidth, y*boxHeight, boxWidth*ENTRANCE_LENGTH, boxHeight));
            stack.add(entrances.getLast());
            verticalEntranceMode = false;
            repaint();
            return;
        }
        set++;
        if (set == 1) {
            sR = y;
            sC = x;
        }
        if (set == 2) {
            eR = y;
            eC = x;
            if (Grid.eR < Grid.sR) {
                int temp = Grid.sR;
                Grid.sR = Grid.eR;
                Grid.eR = temp;
            }
            if (Grid.eC < Grid.sC) {
                int temp = Grid.sC;
                Grid.sC = Grid.eC;
                Grid.eC = temp;
            }
            leftMostPoint =Math.min(leftMostPoint,sC);
            set = 0;
            walls.add(new Rectangle(sC * boxWidth, sR * boxHeight, (eC - sC + 1) * boxWidth, (eR - sR + 1) * boxHeight));
            stack.add(walls.getLast());
        }
        repaint();
    }

    public int getLeftMostPoint() {
        return leftMostPoint;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() / boxWidth, mouseY = e.getY() / boxHeight;
        addRect(mouseX, mouseY);
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



