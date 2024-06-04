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
    private final int TILES_PER_HEIGHT = 100 * 1000;
    private Vector2F p1 = null, p2 = null, highlighted = new Vector2F();
    private double boxSize;
    private final ArrayList<Rectangle> walls = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final Stack<Integer> stack = new Stack<>();
    private Vector2F topLeftPoint = null;
    private final GameObject selected = new GameObject();
    private JComboBox<File> dropDown;
    private File fileToSave;

    public Grid() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        loadFiles();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
//                System.out.printf("min %d\n", Math.min(getHeight(), getWidth()));
                boxSize = (double) Math.min(getHeight(), getWidth()) / (TILES_PER_HEIGHT);
//                System.out.printf("Grid box size = %f\n", boxSize);
                repaint();
            }
        });
//        System.out.printf("min %d\n", Math.min(getHeight(), getWidth()));
//        System.out.printf("Grid box size = %f\n", boxSize);
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
        g.fillRect((int) (highlighted.getX() * boxSize), 0, (int) (boxSize * 1000), getHeight());
        g.fillRect(0, (int) (highlighted.getY() * boxSize), getWidth(), (int) (boxSize * 1000));

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) ((highlighted.getX() - 10000) * boxSize), (int) ((highlighted.getY() - 10000) * boxSize), (int) (boxSize * 21000), (int) (boxSize * 21000));

        g.setColor(Color.LIGHT_GRAY.darker());
        g.fillRect((int) (highlighted.getX() * boxSize), (int) ((highlighted.getY() - 20000) * boxSize), (int) (boxSize * 1000), (int) (boxSize * 41000));
        g.fillRect((int) ((highlighted.getX() - 20000) * boxSize), (int) (highlighted.getY() * boxSize), (int) (boxSize * 41000), (int) (boxSize * 1000));

        g.setColor(Color.DARK_GRAY.brighter());
        g.fillRect((int) (highlighted.getX() * boxSize), (int) ((highlighted.getY() - 5000) * boxSize), (int) (boxSize * 1000), (int) (boxSize * 11000));
        g.fillRect((int) ((highlighted.getX() - 5000) * boxSize), (int) (highlighted.getY() * boxSize), (int) (boxSize * 11000), (int) (boxSize * 1000));


        g.setColor(Color.DARK_GRAY);
        g.fillRect((int) (highlighted.getX() * boxSize), (int) ((highlighted.getY() - 3000) * boxSize), (int) (boxSize * 1000), (int) (boxSize * 7000));
        g.fillRect((int) ((highlighted.getX() - 2000) * boxSize), (int) (highlighted.getY() * boxSize), (int) (boxSize * 5000), (int) (boxSize * 1000));


        for (Rectangle wall : walls) {
            if (selected.getObject() == wall) g.setColor(Color.GREEN);
            else g.setColor(Color.RED);
            g.fillRect((int) (wall.x * boxSize), (int) (wall.y * boxSize), (int) (wall.width * boxSize), (int) (wall.height * boxSize));
        }
        for (Entrance entrance : entrances) {
            if (selected.getObject() == entrance) g.setColor(Color.GREEN);
            else g.setColor(Color.BLUE);
            entrance.draw(g, boxSize);
        }

        g.setColor(Color.GREEN);
        if (p2 == null && p1 != null) {
            g.fillRect((int) (p1.getX() * boxSize), (int) (p1.getY() * boxSize), (int) (boxSize * 1000), (int) (boxSize * 1000));
        }

        g.setColor(Color.BLACK);
        if (boxSize <= 0) return;
        for (double i = 0; i < getWidth(); i += boxSize * 1000) {
            g.drawLine((int) i, 0, (int) i, getHeight());
        }
        for (double i = 0; i < getHeight(); i += boxSize * 1000) {
            g.drawLine(0, (int) i, getWidth(), (int) i);
        }
    }

    public File getFileToSave() {
        return fileToSave;
    }

    public Object returnSelected(){
        for (Rectangle wall : walls) {
            if (wall.intersects(new Rectangle(p1.getX(), p1.getY(), 1000, 1000))) {
                p1 = null;
                return wall;
            }
        }
        for (Entrance entrance : entrances) {
            if (entrance.getHitbox().intersects(new Hitbox(p1.getX(), p1.getY() , p1.getX() + 1000, p1.getY() + 1000))){
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
        if (selected.getObject() instanceof Entrance) entrances.remove((Entrance) selected.getObject());
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
        repaint();
    }

    public void addRect() {
        if (p1 == null || p2 == null) {
            return;
        }

        if (p1.getX() > p2.getX()) {
            int temp = p1.getX();
            p1.setX(p2.getX());
            p2.setX(temp);
        }

        if (p1.getY() > p2.getY()) {
            int temp = p1.getY();
            p1.setY(p2.getY());
            p2.setY(temp);
        }

        topLeftPoint = p1.getMin(topLeftPoint);

        walls.add(new Rectangle(p1.getX(), p1.getY(), p1.getXDistance(p2) + 1000, p1.getYDistance(p2) + 1000));
        stack.add(2);
        p1 = null;
        p2 = null;
        repaint();
    }

    public void reset() {
        getWalls().clear();
        getEntrances().clear();
        getStack().clear();
        fileToSave = null;
        selected.reset();
        topLeftPoint = null;
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
        File storage = new File("src/Rooms/Set1");
        dropDown = new JComboBox<>(Objects.requireNonNull(storage.listFiles()));
        dropDown.setFocusable(false);
        this.add(dropDown);
        dropDown.setVisible(true);
        dropDown.setLayout(null);
        dropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file=(File) Objects.requireNonNull(dropDown.getSelectedItem());
                    Scanner in = new Scanner(file);
                    reset();
                    fileToSave = file;
                    int wallNum = in.nextInt();
                    for (int i = 0; i < wallNum; i++) {
                        int x=in.nextInt() * 1000;
                        int y=in.nextInt() * 1000;
                        topLeftPoint = new Vector2F(x, y).getMin(topLeftPoint);
                        walls.add(new Rectangle(x, y, (in.nextInt() * 1000) - x, (in.nextInt() * 1000) - y));
                        stack.add(2);
                    }
                    int entranceNum = in.nextInt();
                    for (int i = 0; i < entranceNum; i++) {
                        Vector2F v1 = new Vector2F(in.nextInt() * 1000, in.nextInt() * 1000), v2 = new Vector2F(in.nextInt() * 1000, in.nextInt() * 1000);
                        topLeftPoint = v1.getMin(v2).getMin(topLeftPoint);
                        entrances.add(new Entrance(v1, v2));
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
        int mouseX = (int) ((e.getX() / boxSize) / 1000) * 1000, mouseY = (int) ((e.getY() / boxSize) / 1000) * 1000;
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
            selected.setLocation((int) (e.getX() / boxSize), (int) (e.getY() / boxSize));

            repaint();
        }
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (boxSize == 0) return;
        int mouseX = (int) ((e.getX() / boxSize) / 1000) * 1000, mouseY = (int) ((e.getY() / boxSize) / 1000) * 1000;
        highlighted = new Vector2F(mouseX, mouseY);
//        System.out.println(highlighted);
        repaint();
    }
}



