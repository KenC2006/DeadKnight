package Structure;

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
    private int scaledBoxSize;
    private final ArrayList<Rectangle> walls = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final ArrayList<PlayerSpawn> playerSpawns = new ArrayList<>();
    private final ArrayList<EnemySpawn> enemySpawns = new ArrayList<>();
    private final ArrayList<ItemSpawn> itemSpawns = new ArrayList<>();
    private final Stack<Integer> stack = new Stack<>();
    private Vector2F topLeftPoint = null;
    private final RoomObject selected = new RoomObject();
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
                scaledBoxSize = (int) (boxSize * 1000);
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
        g.fillRect((int) (highlighted.getX() * scaledBoxSize / 1000), 0, (int) (scaledBoxSize), getHeight());
        g.fillRect(0, (int) (highlighted.getY() * scaledBoxSize / 1000), getWidth(), (int) (scaledBoxSize));

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int) ((highlighted.getX() - 10000) * scaledBoxSize / 1000), (int) ((highlighted.getY() - 10000) * scaledBoxSize / 1000), (int) (scaledBoxSize * 21), (int) (scaledBoxSize * 21));

        g.setColor(Color.LIGHT_GRAY.darker());
        g.fillRect((int) (highlighted.getX() * scaledBoxSize / 1000), (int) ((highlighted.getY() - 20000) * scaledBoxSize / 1000), (int) (scaledBoxSize), (int) (scaledBoxSize * 41));
        g.fillRect((int) ((highlighted.getX() - 20000) * scaledBoxSize / 1000), (int) (highlighted.getY() * scaledBoxSize / 1000), (int) (scaledBoxSize * 41), (int) (scaledBoxSize));

        g.setColor(Color.DARK_GRAY.brighter());
        g.fillRect((int) (highlighted.getX() * scaledBoxSize / 1000), (int) ((highlighted.getY() - 5000) * scaledBoxSize / 1000), (int) (scaledBoxSize), (int) (scaledBoxSize * 11));
        g.fillRect((int) ((highlighted.getX() - 5000) * scaledBoxSize / 1000), (int) (highlighted.getY() * scaledBoxSize / 1000), (int) (scaledBoxSize* 11), (int) (scaledBoxSize));


        g.setColor(Color.DARK_GRAY);
        g.fillRect((int) (highlighted.getX() * scaledBoxSize / 1000), (int) ((highlighted.getY() - 3000) * scaledBoxSize / 1000), (int) (scaledBoxSize), (int) (scaledBoxSize * 7));
        g.fillRect((int) ((highlighted.getX() - 2000) * scaledBoxSize / 1000), (int) (highlighted.getY() * scaledBoxSize / 1000), (int) (scaledBoxSize * 5), (int) (scaledBoxSize));


        for (Rectangle wall : walls) {
            if (selected.getObject() == wall) g.setColor(Color.GREEN);
            else g.setColor(Color.RED);
            g.fillRect((int) (wall.x * scaledBoxSize / 1000), (int) (wall.y * scaledBoxSize / 1000), (int) (wall.width * scaledBoxSize / 1000), (int) (wall.height * scaledBoxSize / 1000));
        }
        for (Entrance entrance : entrances) {
            if (selected.getObject() == entrance) g.setColor(Color.GREEN);
            else g.setColor(Color.BLUE);
            entrance.draw(g, scaledBoxSize);
        }
        for (PlayerSpawn playerSpawn : playerSpawns) {
            if (selected.getObject() == playerSpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.YELLOW);
            g.fillRect(playerSpawn.getX() * scaledBoxSize / 1000, playerSpawn.getY() * scaledBoxSize / 1000, scaledBoxSize, scaledBoxSize);
        }
        for (ItemSpawn itemSpawn : itemSpawns) {
            if (selected.getObject() == itemSpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.MAGENTA);
            g.fillRect(itemSpawn.getX()* scaledBoxSize / 1000, itemSpawn.getY() * scaledBoxSize / 1000, scaledBoxSize, scaledBoxSize);
        }
        for (EnemySpawn enemySpawn : enemySpawns) {
            if (selected.getObject() == enemySpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.BLACK);
            g.fillRect(enemySpawn.getX() * scaledBoxSize / 1000, enemySpawn.getY() * scaledBoxSize / 1000, scaledBoxSize, scaledBoxSize);
        }

        g.setColor(Color.GREEN);
        if (p2 == null && p1 != null) {
            g.fillRect((int) (p1.getX() * scaledBoxSize / 1000), (int) (p1.getY() * scaledBoxSize / 1000), (int) (scaledBoxSize), (int) (scaledBoxSize));
        }

        g.setColor(Color.BLACK);
//        System.out.printf("%f %d\n", boxSize, getWidth());
        if (boxSize <= 0) return;
        int last = 0;
        for (int i = 0; i < getWidth(); i += scaledBoxSize) {
//            System.out.println((int) i - last);
            last = (int) i;
            g.drawLine((int) i, 0, (int) i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += scaledBoxSize) {
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
        for (PlayerSpawn playerSpawn : playerSpawns) {
            if (playerSpawn.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(playerSpawn);
                p1 = null;
                return playerSpawn;
            }
        }
        for (ItemSpawn itemSpawns : itemSpawns) {
            if (itemSpawns.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(itemSpawns);
                p1 = null;
                return itemSpawns;
            }
        }
        for (EnemySpawn enemySpawn : enemySpawns) {
            if (enemySpawn.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(enemySpawn);
                p1 = null;
                return enemySpawn;
            }
        }
        return null;
    }

    public void undoLastMove() {
        if (stack.isEmpty()) return;
        if (stack.getLast() == 1) {
            getEntrances().removeLast();
        } else if (stack.getLast() == 2) {
            getWalls().removeLast();
        } else if (stack.getLast() == 3) {
            getPlayerSpawns().removeLast();
        } else if (stack.getLast() == 4) {
            getItemSpawns().removeLast();
        } else if (stack.getLast() == 5) {
            getEnemySpawns().removeLast();
        }
        stack.pop();
        p1 = null;
        repaint();
    }

    public void delete() {
        if (selected.getObject() instanceof Entrance) entrances.remove((Entrance) selected.getObject());
        else if (selected.getObject() instanceof PlayerSpawn) playerSpawns.remove((PlayerSpawn) selected.getObject());
        else if (selected.getObject() instanceof ItemSpawn) itemSpawns.remove((ItemSpawn) selected.getObject());
        else if (selected.getObject() instanceof EnemySpawn) enemySpawns.remove((EnemySpawn) selected.getObject());
        else walls.remove((Rectangle) selected.getObject());
        stack.remove(selected.getObject());
        selected.reset();
    }

    public RoomObject getSelected() {
        return selected;
    }

    public void addEntrance(int xOffset, int yOffset) {
        if (p1 == null) return;
        entrances.add(new Entrance(p1, p1.getTranslated(new Vector2F(xOffset, yOffset))));
        stack.add(1);
        p1 = null;
        repaint();
    }

    public void addPlayerSpawn(){
        if (p1 == null) return;
        playerSpawns.add(new PlayerSpawn(p1.getX(),p1.getY()));
        stack.add(3);
        p1=null;
        repaint();
    }

    public void addItemSpawn(){
        if (p1 == null) return;
        itemSpawns.add(new ItemSpawn(p1.getX(),p1.getY()));
        stack.add(4);
        p1=null;
        repaint();
    }

    public void addEnemySpawn(){
        if (p1 == null) return;
        enemySpawns.add(new EnemySpawn(p1.getX(),p1.getY()));
        stack.add(5);
        p1=null;
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

    public ArrayList<PlayerSpawn> getPlayerSpawns() {
        return playerSpawns;
    }

    public ArrayList<EnemySpawn> getEnemySpawns() {
        return enemySpawns;
    }

    public ArrayList<ItemSpawn> getItemSpawns() {
        return itemSpawns;
    }

    public void reset() {
        getWalls().clear();
        getEntrances().clear();
        getPlayerSpawns().clear();
        getItemSpawns().clear();
        getEnemySpawns().clear();
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
                        int x=in.nextInt();
                        int y=in.nextInt();
                        topLeftPoint = new Vector2F(x, y).getMin(topLeftPoint);

                        walls.add(new Rectangle(x, y, (in.nextInt()) - x, (in.nextInt()) - y));
                        stack.add(2);
                    }
                    int entranceNum = in.nextInt();
                    for (int i = 0; i < entranceNum; i++) {
                        Vector2F v1 = new Vector2F(in.nextInt(), in.nextInt()), v2 = new Vector2F(in.nextInt(), in.nextInt());
                        topLeftPoint = v1.getMin(v2).getMin(topLeftPoint);

                        entrances.add(new Entrance(v1, v2));
                    }
                    int playerSpawnNum=in.nextInt();
                    for (int i = 0; i < playerSpawnNum; i++) {
                        int x=in.nextInt();
                        int y=in.nextInt();
                        playerSpawns.add(new PlayerSpawn(x,y));
                    }

                    int itemSpawnNum=in.nextInt();
                    for (int i = 0; i < itemSpawnNum; i++) {
                        int x=in.nextInt();
                        int y=in.nextInt();
                        itemSpawns.add(new ItemSpawn(x,y));
                    }

                    int enemySpawnNum=in.nextInt();
                    for (int i = 0; i < enemySpawnNum; i++) {
                        int x=in.nextInt();
                        int y=in.nextInt();
                        enemySpawns.add(new EnemySpawn(x,y));
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
        int mouseX = (int) ((e.getX() / scaledBoxSize) * 1000), mouseY = (int) ((e.getY() / scaledBoxSize) * 1000);
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
            selected.setLocation((int) (e.getX() / scaledBoxSize) * 1000, (int) (e.getY() / scaledBoxSize) * 1000);
            repaint();
        }
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (boxSize == 0) return;
        int mouseX = (int) (e.getX() / scaledBoxSize) * 1000, mouseY = (int) (e.getY() / scaledBoxSize) * 1000;
        highlighted = new Vector2F(mouseX, mouseY);
        repaint();
    }
}



