package Structure;

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
    private static final File roomStorage = new File("src/Rooms");

    public RoomEditor() {
        grid = new Grid();
        add(grid);
        setLayout(new BorderLayout());
        add(grid,BorderLayout.CENTER);
        pack();

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F) grid.addHorizontalEntrance();
                if (e.getKeyCode() == KeyEvent.VK_G) grid.addVerticalEntrance();
                if (e.getKeyCode() == KeyEvent.VK_R) grid.reset();

                if (grid.getWalls().isEmpty() && grid.getEntrances().isEmpty()) return;
                if (e.getKeyCode() == KeyEvent.VK_Z) grid.undoLastMove();

                if (e.getKeyCode() == KeyEvent.VK_S) {
                    int fileNum = (Objects.requireNonNull(roomStorage.list()).length);
                    File file = new File("src/Rooms/room" + (fileNum + 1) + ".txt");
                    try {
                        FileWriter fw = new FileWriter(file);
                        int ox = (int) grid.getLeftMostPoint().getX(), oy = (int) grid.getLeftMostPoint().getY();

                        fw.write(grid.getWalls().size() + "\n");
                        for (Rectangle r: grid.getWalls()) {
                            fw.write((r.x - ox) + " " + (r.y - oy) + " " + (r.width + r.x - ox) + " " + (r.height + r.y - oy) + "\n");
                        }

                        fw.write(grid.getEntrances().size() + "\n");
                        for (Rectangle r: grid.getEntrances()) {
                            fw.write((r.x - ox) + " " + (r.y - oy) + " " + (r.width + r.x - ox) + " " + (r.height + r.y - oy) + "\n");
                        }
                        fw.close();
                        grid.reset();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }


}
