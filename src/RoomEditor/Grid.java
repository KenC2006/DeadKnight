package RoomEditor;

import Structure.Hitbox;
import Structure.Vector2F;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;


/**
 * The Grid class represents a graphical editor for creating and editing game rooms or levels.
 * It provides functionalities to add walls, hazards, entrances, spawns, and manage them interactively.
 * Users can load existing configurations from files and manipulate entities spawns as well as properties of rooms
 */
public class Grid extends JPanel implements MouseListener, MouseMotionListener {
    private final int TILES_PER_HEIGHT = 100 * 1000;
    private Vector2F p1 = null, p2 = null, highlighted = new Vector2F();
    private double boxSize;
    private int scaledBoxSize;
    private final ArrayList<Rectangle> walls = new ArrayList<>();
    private final ArrayList<Rectangle> hazards = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final ArrayList<Spawn> playerSpawns = new ArrayList<>();
    private final ArrayList<Spawn> enemySpawns = new ArrayList<>();
    private final ArrayList<Spawn> itemSpawns = new ArrayList<>();
    private final ArrayList<Spawn> chestSpawns = new ArrayList<>();
    private final ArrayList<Spawn> bossSpawns = new ArrayList<>();
    private final Stack<Integer> stack = new Stack<>();
    private Vector2F topLeftPoint = null;
    private final RoomObject selected = new RoomObject();
    private final JComboBox<File> dropDown;
    private File fileToSave;

    public Grid(File roomStorage) {

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        dropDown = new JComboBox<>(Objects.requireNonNull(roomStorage.listFiles())); //Loads the dropDown menu for loading created rooms
        dropDown.setFocusable(false);
        this.add(dropDown);
        dropDown.setVisible(true);
        dropDown.setLayout(null);


        loadFiles();
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                boxSize = (double) Math.min(getHeight(), getWidth()) / (TILES_PER_HEIGHT);
                scaledBoxSize = (int) (boxSize * 1000);
                repaint();
            }
        });
    }

    public void addHazard(){
        if (selected !=null && walls.contains(selected.getObject())) {
            hazards.add((Rectangle)selected.getObject());
            walls.remove(selected.getObject());
            stack.remove(stack.size()-1);
            stack.add(6);
            selected.reset();
        }
    }


    /**
     * Shifts all grid elements by a specified vector.
     *
     * @param change Vector2F representing the change in position.
     */
    public void shift(Vector2F change) {
        // Shift walls
        for (Rectangle wall : walls) {
            wall.setLocation((int) (wall.getX() + change.getX()), (int) (wall.getY() + change.getY()));
        }

        // Shift hazards
        for (Rectangle hazard : hazards) {
            hazard.setLocation((int) (hazard.getX() + change.getX()), (int) (hazard.getY() + change.getY()));
        }

        // Shift entrances
        for (Entrance entrance : entrances) {
            entrance.setRelativeLocation(entrance.getLocation().getTranslated(change));
        }

        // Shift player spawns
        for (Spawn playerSpawn : playerSpawns) {
            playerSpawn.translateInPlace(change);
        }

        // Shift item spawns
        for (Spawn itemSpawn : itemSpawns) {
            itemSpawn.translateInPlace(change);
        }

        // Shift chest spawns
        for (Spawn chestSpawn: chestSpawns) {
            chestSpawn.translateInPlace(change);
        }

        // Shift boss spawns
        for (Spawn bossSpawn: bossSpawns) {
            bossSpawn.translateInPlace(change);
        }

        // Shift enemy spawns
        for (Spawn enemySpawn : enemySpawns) {
            enemySpawn.translateInPlace(change);
        }

        updateTopLeft();
        repaint();
    }

    /**
     * @return the list of walls in the grid.
     */
    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    /**
     * @return the stack of recent actions.
     */
    public Stack<Integer> getStack() {
        return stack;
    }

    /**
     * @return the list of entrances in the grid.
     */
    public ArrayList<Entrance> getEntrances() {
        return entrances;
    }

    /**
     * Paints the grid and its elements.
     *
     * @param g the Graphics object used for painting.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY.brighter().brighter());
        g.fillRect( (highlighted.getX() * scaledBoxSize / 1000), 0,  (scaledBoxSize), getHeight());
        g.fillRect(0,  (highlighted.getY() * scaledBoxSize / 1000), getWidth(),  (scaledBoxSize));

        g.setColor(Color.PINK);
        g.fillRect( ((highlighted.getX() - 15000) * scaledBoxSize / 1000),  ((highlighted.getY() - 14000) * scaledBoxSize / 1000),  (scaledBoxSize * 31),  (scaledBoxSize * 31));


        g.setColor(Color.LIGHT_GRAY);
        g.fillRect( ((highlighted.getX() - 10000) * scaledBoxSize / 1000),  ((highlighted.getY() - 10000) * scaledBoxSize / 1000),  (scaledBoxSize * 21),  (scaledBoxSize * 21));

        g.setColor(Color.LIGHT_GRAY.darker());
        g.fillRect( (highlighted.getX() * scaledBoxSize / 1000),  ((highlighted.getY() - 20000) * scaledBoxSize / 1000),  (scaledBoxSize),  (scaledBoxSize * 41));
        g.fillRect( ((highlighted.getX() - 20000) * scaledBoxSize / 1000),  (highlighted.getY() * scaledBoxSize / 1000),  (scaledBoxSize * 41),  (scaledBoxSize));

        g.setColor(Color.DARK_GRAY.brighter());
        g.fillRect( (highlighted.getX() * scaledBoxSize / 1000),  ((highlighted.getY() - 5000) * scaledBoxSize / 1000),  (scaledBoxSize),  (scaledBoxSize * 11));
        g.fillRect( ((highlighted.getX() - 5000) * scaledBoxSize / 1000),  (highlighted.getY() * scaledBoxSize / 1000),  (scaledBoxSize* 11),  (scaledBoxSize));


        g.setColor(Color.DARK_GRAY);
        g.fillRect( (highlighted.getX() * scaledBoxSize / 1000),  ((highlighted.getY() - 3000) * scaledBoxSize / 1000),  (scaledBoxSize),  (scaledBoxSize * 7));
        g.fillRect( ((highlighted.getX() - 2000) * scaledBoxSize / 1000),  (highlighted.getY() * scaledBoxSize / 1000),  (scaledBoxSize * 5),  (scaledBoxSize));


        for (Rectangle wall : walls) {
            if (selected.getObject() == wall) g.setColor(Color.GREEN);
            else g.setColor(Color.RED);
            g.fillRect( (wall.x * scaledBoxSize / 1000),  (wall.y * scaledBoxSize / 1000),  (wall.width * scaledBoxSize / 1000),  (wall.height * scaledBoxSize / 1000));
        }
        for (Rectangle hazard : hazards) {
            if (selected.getObject() == hazard) g.setColor(Color.GREEN);
            else g.setColor(Color.ORANGE);
            g.fillRect((hazard.x * scaledBoxSize / 1000),  (hazard.y * scaledBoxSize / 1000),  (hazard.width * scaledBoxSize / 1000),  (hazard.height * scaledBoxSize / 1000));
        }
        for (Entrance entrance : entrances) {
            if (selected.getObject() == entrance) g.setColor(Color.GREEN);
            else g.setColor(Color.BLUE);
            entrance.draw(g, scaledBoxSize);
        }
        for (Spawn playerSpawn : playerSpawns) {
            if (selected.getObject() == playerSpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.YELLOW);
            g.fillRect(playerSpawn.getX() * scaledBoxSize / 1000, playerSpawn.getY() * scaledBoxSize / 1000, scaledBoxSize, scaledBoxSize);
        }
        for (Spawn itemSpawn : itemSpawns) {
            if (selected.getObject() == itemSpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.MAGENTA);
            g.fillRect(itemSpawn.getX()* scaledBoxSize / 1000, itemSpawn.getY() * scaledBoxSize / 1000, scaledBoxSize, scaledBoxSize);
        }
        for (Spawn enemySpawn : enemySpawns) {
            if (selected.getObject() == enemySpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.BLACK);
            g.fillRect(enemySpawn.getX() * scaledBoxSize / 1000, enemySpawn.getY() * scaledBoxSize / 1000, scaledBoxSize, scaledBoxSize);
        }
        for (Spawn chestSpawn: chestSpawns) {
            if (selected.getObject() == chestSpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.PINK);
            g.fillRect(chestSpawn.getX() * scaledBoxSize / 1000, chestSpawn.getY() * scaledBoxSize / 1000, scaledBoxSize * 3, scaledBoxSize * 2);
        }

        for (Spawn bossSpawn: bossSpawns) {
            if (selected.getObject() == bossSpawn) g.setColor(Color.GREEN);
            else g.setColor(Color.ORANGE);
            g.fillRect(bossSpawn.getX() * scaledBoxSize / 1000, bossSpawn.getY() * scaledBoxSize / 1000, scaledBoxSize * 3, scaledBoxSize * 3);

        }

        g.setColor(Color.GREEN);
        if (p2 == null && p1 != null) {
            g.fillRect( (p1.getX() * scaledBoxSize / 1000),  (p1.getY() * scaledBoxSize / 1000),  (scaledBoxSize),  (scaledBoxSize));
        }

        g.setColor(Color.BLACK);
        if (boxSize <= 0) return;
        for (int i = 0; i < getWidth(); i += scaledBoxSize) {
            g.drawLine( i, 0,  i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += scaledBoxSize) {
            g.drawLine(0,  i, getWidth(),  i);
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
        for (Rectangle hazard : hazards) {
            if (hazard.intersects(new Rectangle(p1.getX(), p1.getY(), 1000, 1000))) {
                p1 = null;
                return hazard;
            }
        }
        for (Entrance entrance : entrances) {
            if (entrance.getHitbox().intersects(new Hitbox(p1.getX(), p1.getY() , p1.getX() + 1000, p1.getY() + 1000))){
                selected.setObject(entrance);
                p1 = null;
                return entrance;
            }
        }

        for (Spawn playerSpawn : playerSpawns) {
            if (playerSpawn.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(playerSpawn);
                p1 = null;
                return playerSpawn;
            }
        }

        for (Spawn itemSpawns : itemSpawns) {
            if (itemSpawns.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(itemSpawns);
                p1 = null;
                return itemSpawns;
            }
        }

        for (Spawn enemySpawn : enemySpawns) {
            if (enemySpawn.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(enemySpawn);
                p1 = null;
                return enemySpawn;
            }
        }

        for (Spawn chestSpawn : chestSpawns) {
            if (chestSpawn.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(chestSpawn);
                p1 = null;
                return chestSpawn;
            }
        }

        for (Spawn bossSpawn : bossSpawns) {
            if (bossSpawn.getLocation().getManhattanDistance(p1) == 0){
                selected.setObject(bossSpawn);
                p1 = null;
                return bossSpawn;
            }
        }
        return null;
    }

    /**
     * Undoes the last move by removing the last added element from the corresponding list.
     * It checks the type of the last added element using the stack and removes it from the appropriate list.
     * Updates the top-left point and repaints the component.
     */
    public void undoLastMove() {
        if (stack.isEmpty()) return;
        if (stack.get(stack.size() - 1) == 1) {
            getEntrances().remove(stack.size() - 1);
        } else if (stack.get(stack.size() - 1) == 2) {
            getWalls().remove(stack.size() - 1);
        } else if (stack.get(stack.size() - 1) == 3) {
            getPlayerSpawns().remove(stack.size() - 1);
        } else if (stack.get(stack.size() - 1) == 4) {
            getItemSpawns().remove(stack.size() - 1);
        } else if (stack.get(stack.size() - 1) == 5) {
            getEnemySpawns().remove(stack.size() - 1);
        } else if (stack.get(stack.size() - 1) == 6) {
            getHazards().remove(stack.size() - 1);
        } else if (stack.get(stack.size() - 1) == 7) {
            chestSpawns.remove(stack.size() - 1);
        } else if (stack.get(stack.size() - 1) == 8) {
            bossSpawns.remove(stack.size() - 1);
        }

        stack.pop();
        p1 = null;
        updateTopLeft();
        repaint();
    }

    /**
     * Retrieves the list of hazards.
     *
     * @return an ArrayList of Rectangle objects representing hazards.
     */
    public ArrayList<Rectangle> getHazards() {
        return hazards;
    }

    /**
     * Deletes the selected object from the appropriate list.
     * It checks the type of the selected object and removes it from the corresponding list.
     * Updates the top-left point and resets the selected object.
     */
    public void delete() {
        if (selected.getObject() instanceof Entrance) {
            entrances.remove((Entrance) selected.getObject());
        } else if (selected.getObject() instanceof Spawn) {
            switch (((Spawn) selected.getObject()).getType()) {
                case PLAYER:
                    playerSpawns.remove((Spawn) selected.getObject());
                    break;
                case ITEM:
                    itemSpawns.remove((Spawn) selected.getObject());
                    break;
                case ENEMY:
                    enemySpawns.remove((Spawn) selected.getObject());
                    break;
                case CHEST:
                    chestSpawns.remove((Spawn) selected.getObject());
                    break;
                case BOSS:
                    bossSpawns.remove((Spawn) selected.getObject());
                    break;
            }
        } else if (hazards.contains(selected.getObject())) {
            hazards.remove((Rectangle) selected.getObject());
        } else {
            walls.remove((Rectangle) selected.getObject());
        }
        stack.remove(selected.getObject());
        selected.reset();
        updateTopLeft();
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
        playerSpawns.add(new Spawn(p1.getX(),p1.getY(), Spawn.SpawnType.PLAYER));
        stack.add(3);
        p1=null;
        repaint();
    }

    public void addItemSpawn(){
        if (p1 == null) return;
        itemSpawns.add(new Spawn(p1.getX(),p1.getY(), Spawn.SpawnType.ITEM));
        stack.add(4);
        p1=null;
        repaint();
    }

    public void addEnemySpawn(){
        if (p1 == null) return;
        enemySpawns.add(new Spawn(p1.getX(),p1.getY(), Spawn.SpawnType.ENEMY));
        stack.add(5);
        p1=null;
        repaint();
    }

    public void addChestSpawn(){
        if (p1 == null) return;
        chestSpawns.add(new Spawn(p1.getX(),p1.getY(), Spawn.SpawnType.CHEST));
        stack.add(7);
        p1=null;
        repaint();
    }

    public void addBossSpawn(){
        if (p1 == null) return;
        bossSpawns.add(new Spawn(p1.getX(),p1.getY(), Spawn.SpawnType.BOSS));
        stack.add(8);
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
        if (p2 !=null){
            System.out.println((p2.getX() - topLeftPoint.getX())/1000 + " " + (p2.getY() - topLeftPoint.getY())/1000);
        }

        walls.add(new Rectangle(p1.getX(), p1.getY(), p1.getXDistance(p2) + 1000, p1.getYDistance(p2) + 1000));
        stack.add(2);
        p1 = null;
        p2 = null;
        repaint();
    }

    /**
     * Retrieves the list of player spawn points.
     *
     * @return an ArrayList of Spawn objects representing player spawn points.
     */
    public ArrayList<Spawn> getPlayerSpawns() {
        return playerSpawns;
    }

    /**
     * Retrieves the list of enemy spawn points.
     *
     * @return an ArrayList of Spawn objects representing enemy spawn points.
     */
    public ArrayList<Spawn> getEnemySpawns() {
        return enemySpawns;
    }

    /**
     * Retrieves the list of item spawn points.
     *
     * @return an ArrayList of Spawn objects representing item spawn points.
     */
    public ArrayList<Spawn> getItemSpawns() {
        return itemSpawns;
    }

    /**
     * Retrieves the list of chest spawn points.
     *
     * @return an ArrayList of Spawn objects representing chest spawn points.
     */
    public ArrayList<Spawn> getChestSpawns() {
        return chestSpawns;
    }

    /**
     * Retrieves the list of boss spawn points.
     *
     * @return an ArrayList of Spawn objects representing boss spawn points.
     */
    public ArrayList<Spawn> getBossSpawns() {
        return bossSpawns;
    }


    /**
     * Resets the current setup by clearing all walls, entrances, spawn points, and hazards.
     * Also resets the file to be saved, the selected object, the top-left point, and the selected points.
     */
    public void reset() {
        getWalls().clear();
        getEntrances().clear();
        getPlayerSpawns().clear();
        getItemSpawns().clear();
        getEnemySpawns().clear();
        getHazards().clear();
        getChestSpawns().clear();
        getBossSpawns().clear();
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

    private void updateTopLeft() {
        topLeftPoint = null;
        for (Rectangle wall: walls) {
            topLeftPoint = new Vector2F((int) wall.getX(), (int) wall.getY()).getMin(topLeftPoint);
        }
        for (Entrance e: entrances) {
            topLeftPoint = e.getLocation().getMin(topLeftPoint);
        }
        for (Rectangle h: hazards) {
            topLeftPoint = new Vector2F((int) h.getX(), (int) h.getY()).getMin(topLeftPoint);
        }
    }

    /**
     * Initializes the dropdown menu to load files. When a file is selected from the dropdown,
     * this method reads the contents of the file and sets up various elements (walls, entrances,
     * spawns, hazards) in the application based on the data from the file.
     */
    public void loadFiles() {
        // Add an action listener to the dropdown menu
        dropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the selected file from the dropdown menu
                    File file = (File) Objects.requireNonNull(dropDown.getSelectedItem());
                    // Create a scanner to read the file
                    Scanner in = new Scanner(file);
                    // Reset the current setup
                    reset();
                    // Set the file to be saved later
                    fileToSave = file;
                    // Read the number of walls from the file
                    int wallNum = in.nextInt();
                    for (int i = 0; i < wallNum; i++) {
                        // Read wall coordinates and create wall rectangles
                        int x = in.nextInt();
                        int y = in.nextInt();
                        topLeftPoint = new Vector2F(x, y).getMin(topLeftPoint);

                        walls.add(new Rectangle(x, y, (in.nextInt()) - x, (in.nextInt()) - y));
                    }
                    // Read the number of entrances from the file
                    int entranceNum = in.nextInt();
                    for (int i = 0; i < entranceNum; i++) {
                        // Read entrance coordinates and create entrance objects
                        Vector2F v1 = new Vector2F(in.nextInt(), in.nextInt()), v2 = new Vector2F(in.nextInt(), in.nextInt());
                        topLeftPoint = v1.getMin(topLeftPoint);

                        entrances.add(new Entrance(v1, v2));
                    }
                    // Read the number of player spawns from the file
                    int playerSpawnNum = in.nextInt();
                    for (int i = 0; i < playerSpawnNum; i++) {
                        // Read player spawn coordinates and create spawn objects
                        int x = in.nextInt();
                        int y = in.nextInt();
                        playerSpawns.add(new Spawn(x, y, Spawn.SpawnType.PLAYER));
                    }

                    // Read the number of item spawns from the file
                    int itemSpawnNum = in.nextInt();
                    for (int i = 0; i < itemSpawnNum; i++) {
                        // Read item spawn coordinates and create spawn objects
                        int x = in.nextInt();
                        int y = in.nextInt();
                        itemSpawns.add(new Spawn(x, y, Spawn.SpawnType.ITEM));
                    }

                    // Read the number of enemy spawns from the file
                    int enemySpawnNum = in.nextInt();
                    for (int i = 0; i < enemySpawnNum; i++) {
                        // Read enemy spawn coordinates and create spawn objects
                        int x = in.nextInt();
                        int y = in.nextInt();
                        enemySpawns.add(new Spawn(x, y, Spawn.SpawnType.ENEMY));
                    }

                    // Read the number of hazards from the file
                    int hazardNum = in.nextInt();
                    for (int i = 0; i < hazardNum; i++) {
                        // Read hazard coordinates and create hazard rectangles
                        int x = in.nextInt();
                        int y = in.nextInt();
                        topLeftPoint = new Vector2F(x, y).getMin(topLeftPoint);
                        hazards.add(new Rectangle(x, y, (in.nextInt()) - x, (in.nextInt()) - y));
                    }

                    // Read the number of chest spawns from the file
                    int chestSpawnNum = in.nextInt();
                    for (int i = 0; i < chestSpawnNum; i++) {
                        // Read chest spawn coordinates and create spawn objects
                        int x = in.nextInt();
                        int y = in.nextInt();
                        chestSpawns.add(new Spawn(x, y, Spawn.SpawnType.CHEST));
                    }

                    // Read the number of boss spawns from the file
                    int bossSpawnNum = in.nextInt();
                    for (int i = 0; i < bossSpawnNum; i++) {
                        // Read boss spawn coordinates and create spawn objects
                        int x = in.nextInt();
                        int y = in.nextInt();
                        bossSpawns.add(new Spawn(x, y, Spawn.SpawnType.BOSS));
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // Revalidate the component hierarchy
        revalidate();
    }



    /**
     * Handles mouse click events. When the mouse is clicked, this method determines the position
     * of the click relative to the grid and performs actions based on the current state of the points.
     * It sets the first point (`p1`) on the first click and the second point (`p2`) on the second click,
     * then calls the `addRect()` method to add a rectangle between these points.
     *
     * @param e the mouse event that occurred when the mouse was clicked
     */
    public void mouseClicked(MouseEvent e) {
        // Calculate the x and y coordinates of the mouse click, scaled to the grid
        int mouseX = ((e.getX() / scaledBoxSize) * 1000), mouseY = ((e.getY() / scaledBoxSize) * 1000);

        if (p1 == null) {
            // If p1 is not set, set it to the current mouse position and set the selected object
            p1 = new Vector2F(mouseX, mouseY);
            selected.setObject(returnSelected());
        } else if (p2 == null) {
            // If p1 is set but p2 is not, set p2 to the current mouse position and add the rectangle
            p2 = new Vector2F(mouseX, mouseY);
            addRect();
        }
        // Repaint the grid to reflect any changes
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
            selected.setLocation( (e.getX() / scaledBoxSize) * 1000,  (e.getY() / scaledBoxSize) * 1000);
            repaint();
        }
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (boxSize == 0) return;
        int mouseX =  (e.getX() / scaledBoxSize) * 1000, mouseY =  (e.getY() / scaledBoxSize) * 1000;
        highlighted = new Vector2F(mouseX, mouseY);
        repaint();
    }
}



