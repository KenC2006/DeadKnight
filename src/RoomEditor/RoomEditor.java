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
    private static File roomStorage; // CHANGE SET NUMBER
    public RoomEditor() {
    	for (File f:new File(".").listFiles()) {
    		System.out.println(f.getName());
    	}
    	System.out.println("------------");
//		System.out.println(new File(".").getParent());

//    	for (File f:new File("/bin").listFiles()) {
//    		System.out.println(f.getName());
//    	}
    	System.out.println("------------");
    	for (File f:new File("./bin").listFiles()) {
    		System.out.println(f.getName());
    	}
//    	System.out.println("------------");
//    	for (File f:new File("bin").listFiles()) {
//    		System.out.println(f.getName());
//    	}
//    	System.out.println(new File("/").listFiles());

    	roomStorage = new File("Rooms/Set3");
        grid = new Grid(roomStorage);
        add(grid);
        setLayout(new BorderLayout());
        add(grid,BorderLayout.CENTER);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_RIGHT) grid.addEntrance(1000, 0);
                if (e.getKeyCode() == KeyEvent.VK_LEFT) grid.addEntrance(-1000, 0);
                if (e.getKeyCode() == KeyEvent.VK_UP) grid.addEntrance(0, -1000);
                if (e.getKeyCode() == KeyEvent.VK_DOWN) grid.addEntrance(0, 1000);

                if (e.getKeyCode() == KeyEvent.VK_P) grid.addPlayerSpawn();
                if (e.getKeyCode() == KeyEvent.VK_I) grid.addItemSpawn();
                if (e.getKeyCode() == KeyEvent.VK_E) grid.addEnemySpawn();

                if (e.getKeyCode() == KeyEvent.VK_U) grid.shift(new Vector2F(0, -1000));
                if (e.getKeyCode() == KeyEvent.VK_H) grid.shift(new Vector2F(-1000, 0));
                if (e.getKeyCode() == KeyEvent.VK_J) grid.shift(new Vector2F(0, 1000));
                if (e.getKeyCode() == KeyEvent.VK_K) grid.shift(new Vector2F(1000, 0));


                if (e.getKeyCode() == KeyEvent.VK_H) grid.addHazard();


                if (e.getKeyCode() == KeyEvent.VK_R) grid.reset();
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && grid.getSelected()!=null) grid.delete();

                if (grid.getWalls().isEmpty() && grid.getEntrances().isEmpty() && grid.getEnemySpawns().isEmpty() && grid.getPlayerSpawns().isEmpty() && grid.getItemSpawns().isEmpty()) return;
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    grid.undoLastMove();
                }

                if (e.getKeyCode() == KeyEvent.VK_S) {
                    int fileNum = (Objects.requireNonNull(roomStorage.list()).length);
                    System.out.println("saved");
                    File file;
                    if (grid.getFileToSave()==null) {
                        file = new File(roomStorage.getPath() + "/room" + (fileNum + 1) + ".txt");
                        grid.getDropDown().addItem(file);
                    }
                    else file = grid.getFileToSave();
                    try {
                        FileWriter fw = new FileWriter(file);
                        int ox =  grid.getLeftMostPoint().getX(), oy =  grid.getLeftMostPoint().getY();

                        fw.write(grid.getWalls().size() + "\n");
                        for (Rectangle r: grid.getWalls()) {
                            fw.write((r.x - ox) + " " + (r.y - oy) + " " + (r.width + r.x - ox) + " " + (r.height + r.y - oy) + "\n");
                        }

                        fw.write(grid.getEntrances().size() + "\n");
                        for (Entrance entrance: grid.getEntrances()) {
                            fw.write((entrance.getLocation().getX() - ox) + " " + ( entrance.getLocation().getY() - oy) + " " + ( entrance.getConnection().getX() - ox) + " " + ( entrance.getConnection().getY() - oy) + "\n");
                        }

                        fw.write(grid.getPlayerSpawns().size() + "\n");
                        for (PlayerSpawn r: grid.getPlayerSpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }

                        fw.write(grid.getItemSpawns().size() + "\n");
                        for (ItemSpawn r: grid.getItemSpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }

                        fw.write(grid.getEnemySpawns().size() + "\n");
                        for (EnemySpawn r: grid.getEnemySpawns()) {
                            fw.write((r.getX() - ox) + " " + (r.getY() - oy) + "\n");
                        }
                        fw.write(grid.getHazards().size() + "\n");
                        for (Rectangle r: grid.getHazards()) {
                            fw.write((r.x - ox) + " " + (r.y - oy) + " " + (r.width + r.x - ox) + " " + (r.height + r.y - oy) + "\n");
                        }

                        fw.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }


}
