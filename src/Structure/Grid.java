package Structure;

import Entities.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;


public class Grid extends JPanel implements MouseListener, MouseMotionListener {
    private Vector2F p1 = null, p2 = null, highlighted = new Vector2F();
    private int boxSize;
    private final ArrayList<Rectangle> walls = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final Stack<Integer> stack = new Stack<>();
    private final Vector2F topLeftPoint = new Vector2F(999, 999);
    private final GameObject selected=new GameObject();
    private JComboBox<File> dropDown;

    public Grid() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        loadFiles();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                boxSize = Math.min(getHeight(), getWidth()) / 50;
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
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) highlighted.getX() * boxSize, 0, boxSize, getHeight());
        g.fillRect(0, (int) highlighted.getY() * boxSize, getWidth(), boxSize);


        for (Rectangle wall : walls) {
            if (selected.getObject() == wall) g.setColor(Color.GREEN);
            else g.setColor(Color.RED);
            g.fillRect(wall.x * boxSize, wall.y * boxSize, wall.width * boxSize, wall.height * boxSize);
        }
        for (Entrance entrance : entrances) {
            if (selected.getObject() == entrance) g.setColor(Color.GREEN);
            else g.setColor(Color.BLUE);
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

    public Object returnSelected(){
        for (Rectangle wall : walls) {
            if (wall.intersects(new Rectangle((int) p1.getX(), (int) p1.getY(), 1, 1))) {
                p1 = null;
                return wall;
            }
        }
        for (Entrance entrance : entrances) {
            if (entrance.getHitbox().intersects(new Hitbox(p1.getX(), p1.getY() , p1.getX()+1, p1.getY()+1))){
                selected.setObject(entrance);
                p1 = null;
                return entrance;
            }
        }
        return null;
    }

    public void undoLastMove() {
        if (stack.isEmpty()) return;
        if (stack.getLast() == 1) {
            getEntrances().removeLast();
        } else {
            getWalls().removeLast();
        }
        stack.pop();
        p1 = null;
        repaint();
    }

    public void delete() {
        if (selected.getObject() instanceof Entrance) entrances.remove(selected.getObject());
        else walls.remove(selected.getObject());
        stack.remove(selected.getObject());
        selected.reset();
    }

    public GameObject getSelected() {
        return selected;
    }

    public void addEntrance(int xOffset, int yOffset) {
        if (p1 == null) return;
        entrances.add(new Entrance(p1, p1.getTranslated(new Vector2F(xOffset, yOffset))));
        stack.add(1);
        p1 = null;
        p2=null;
        repaint();
    }

    public void addRect() {
        if (p1 == null || p2 == null) {
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
        selected.reset();
        p1 = null;
        p2 = null;
        repaint();
    }

    public Vector2F getLeftMostPoint() {
        return topLeftPoint;
    }

    public JComboBox<File> getDropDown() {
        return dropDown;
    }

    public void loadFiles() {
        File storage = new File("src/Rooms");
        dropDown = new JComboBox<>(Objects.requireNonNull(storage.listFiles()));
        dropDown.setFocusable(false);
        this.add(dropDown);
        dropDown.setVisible(true);
        dropDown.setLayout(null);
        dropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Scanner in = new Scanner((File) Objects.requireNonNull(dropDown.getSelectedItem()));
                    walls.clear();
                    entrances.clear();
                    selected.reset();
                    int wallNum = in.nextInt();
                    for (int i = 0; i < wallNum; i++) {
                        int x=in.nextInt();
                        int y=in.nextInt();
                        walls.add(new Rectangle(x, y, in.nextInt()-x, in.nextInt()-y));
                        stack.add(2);
                    }
                    int entranceNum = in.nextInt();
                    for (int i = 0; i < entranceNum; i++) {
                        entrances.add(new Entrance(new Vector2F(in.nextInt(),in.nextInt()),new Vector2F(in.nextInt(),in.nextInt())));
                    }

                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX() / boxSize, mouseY = e.getY() / boxSize;
        if (p1 == null) {
            p1 = new Vector2F(mouseX, mouseY);
            selected.setObject(returnSelected());
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
        selected.reset();
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
        if (selected.getObject() != null) {
            selected.setLocation(e.getX() / boxSize, e.getY() / boxSize);

            repaint();
        }
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



