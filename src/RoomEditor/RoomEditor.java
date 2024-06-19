package RoomEditor;

import Structure.Vector2F;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class RoomEditor extends JFrame {
    private final Grid grid;
    private static File roomStorage; // Directory to store rooms

    public RoomEditor() {
        roomStorage = new File("res/Rooms/Set3"); // Default storage directory for rooms

        grid = new Grid(roomStorage); // Initialize the grid for room editing with specified storage directory
        add(grid); // Add grid to the frame
        setLayout(new BorderLayout()); // Set layout for the frame
        add(grid, BorderLayout.CENTER); // Add grid to the center of the frame

        setVisible(true); // Make the frame visible
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation
        setSize(1280, 720); // Set initial size of the frame
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Keyboard controls for the room editor

                // Adding entrances in different directions
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) grid.addEntrance(1000, 0);
                if (e.getKeyCode() == KeyEvent.VK_LEFT) grid.addEntrance(-1000, 0);
                if (e.getKeyCode() == KeyEvent.VK_UP) grid.addEntrance(0, -1000);
                if (e.getKeyCode() == KeyEvent.VK_DOWN) grid.addEntrance(0, 1000);

                // Adding spawns for player, items, enemies, chests, and boss
                if (e.getKeyCode() == KeyEvent.VK_P) grid.addPlayerSpawn();
                if (e.getKeyCode() == KeyEvent.VK_I) grid.addItemSpawn();
                if (e.getKeyCode() == KeyEvent.VK_E) grid.addEnemySpawn();
                if (e.getKeyCode() == KeyEvent.VK_C) grid.addChestSpawn();
                if (e.getKeyCode() == KeyEvent.VK_B) grid.addBossSpawn();

                // Shifting the grid in different directions
                if (e.getKeyCode() == KeyEvent.VK_U) grid.shift(new Vector2F(0, -1000));
                if (e.getKeyCode() == KeyEvent.VK_H) grid.shift(new Vector2F(-1000, 0));
                if (e.getKeyCode() == KeyEvent.VK_J) grid.shift(new Vector2F(0, 1000));
                if (e.getKeyCode() == KeyEvent.VK_K) grid.shift(new Vector2F(1000, 0));

                // Adding hazards
                if (e.getKeyCode() == KeyEvent.VK_H) grid.addHazard();

                // Resetting the grid
                if (e.getKeyCode() == KeyEvent.VK_R) grid.reset();

                // Deleting selected object
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && grid.getSelected() != null) grid.delete();

                if (grid.getWalls().isEmpty() && grid.getEntrances().isEmpty() && grid.getEnemySpawns().isEmpty() && grid.getPlayerSpawns().isEmpty() && grid.getItemSpawns().isEmpty()) return;
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    grid.undoLastMove();
                }

                // Saving the room layout
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    // Determine the next available file number for saving
                    int fileNum = Objects.requireNonNull(roomStorage.list()).length;
                    System.out.println("saved");
                    File file;
                    if (grid.getFileToSave() == null) {
                        file = new File(roomStorage.getPath() + "/room" + (fileNum + 1) + ".txt");
                        grid.getDropDown().addItem(file);
                    } else {
                        file = grid.getFileToSave();
                    }
                    try {
                        FileWriter fw = new FileWriter(file);
                        int ox = grid.getLeftMostPoint().getX(), oy = grid.getLeftMostPoint().getY();

                        // Save walls
                        fw.write(grid.getWalls().size() + "\n");
                        for (Rectangle r : grid.getWalls()) {
                            fw.write((r.x - ox) + " " + (r.y - oy) + " " + (r.width + r.x - ox) + " " + (r.height + r.y - oy) + "\n");
                        }

                        // Save entrances
                        fw.write(grid.getEntrances().size() + "\n");
                        for (Entrance entrance: grid.getEntrances()) {
                            fw.write((entrance.getLocation().getX() - ox) + " " + ( entrance.getLocation().getY() - oy) + " " + ( entrance.getConnection().getX() - ox) + " " + ( entrance.getConnection().getY() - oy) + "\n");
                        }

                        // Save player spawns
                        fw.write(grid.getPlayerSpawns().size() + "\n");
                        for (Spawn r : grid.getPlayerSpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }

                        // Save item spawns
                        fw.write(grid.getItemSpawns().size() + "\n");
                        for (Spawn r : grid.getItemSpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }

                        // Save enemy spawns
                        fw.write(grid.getEnemySpawns().size() + "\n");
                        for (Spawn r : grid.getEnemySpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }

                        // Save hazards
                        fw.write(grid.getHazards().size() + "\n");
                        for (Rectangle r : grid.getHazards()) {
                            fw.write((r.x - ox) + " " + (r.y - oy) + " " + (r.width + r.x - ox) + " " + (r.height + r.y - oy) + "\n");
                        }

                        // Save chest spawns
                        fw.write(grid.getChestSpawns().size() + "\n");
                        for (Spawn r : grid.getChestSpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }

                        // Save boss spawns
                        fw.write(grid.getBossSpawns().size() + "\n");
                        for (Spawn r : grid.getBossSpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }

                        fw.close(); // Close the FileWriter
                    } catch (IOException ex) {
                        throw new RuntimeException(ex); // Throw runtime exception if IO error occurs
                    }
                }
            }
        });
    }
}
