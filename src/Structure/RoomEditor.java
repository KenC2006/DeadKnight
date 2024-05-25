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
    public static final int ROW = 100;
    public static final int COL = 150;
    private final Grid grid;
    private static final File roomStorage = new File("src/Rooms");

    public RoomEditor() {
        grid = new Grid(ROW, COL);
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
                if (!grid.getWalls().isEmpty() || !grid.getEntrances().isEmpty()) {
                    if (e.getKeyCode() == KeyEvent.VK_F) {
                        grid.setHorizontalEntranceMode(true);
                        grid.setVerticalEntranceMode(false);
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_G) {
                        grid.setVerticalEntranceMode(true);
                        grid.setHorizontalEntranceMode(false);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_Z) {
                        if (!grid.getEntrances().isEmpty() && grid.getStack().pop().equals(grid.getEntrances().getLast())){
                            grid.getEntrances().removeLast();
                        }
                        else{
                            grid.getWalls().removeLast();
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                        int fileNum = (Objects.requireNonNull(roomStorage.list()).length);
                        File file = new File("src/Rooms/room" + (fileNum + 1) + ".txt");
                        try {
                            FileWriter fw = new FileWriter(file);
                            for (int i = 0; i < grid.getWalls().size(); i++) {
                                fw.write(grid.getWalls().get(i).x/grid.getBoxWidth()-grid.getLeftMostPoint() + " " + grid.getWalls().get(i).y/grid.getBoxHeight() + " " + grid.getWalls().get(i).width/grid.getBoxWidth() + " " + grid.getWalls().get(i).height/grid.getBoxHeight() + "\n");
                            }
                            for (int i=0;i<grid.getEntrances().size();i++){
                                fw.write("Entrance: ");
                                fw.write(grid.getEntrances().get(i).x/grid.getBoxWidth()-grid.getLeftMostPoint() + " " + grid.getEntrances().get(i).y/grid.getBoxHeight() + " " + grid.getEntrances().get(i).width/grid.getBoxWidth() + " " + grid.getEntrances().get(i).height/grid.getBoxHeight() + "\n");
                            }
                            fw.close();
                            grid.getWalls().clear();
                            grid.getEntrances().clear();
                            grid.getStack().clear();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        grid.getWalls().clear();
                        grid.getEntrances().clear();
                        grid.getStack().clear();
                    }
                    grid.repaint();
                }
            }
        });
    }
}
