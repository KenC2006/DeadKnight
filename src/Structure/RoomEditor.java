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

    public RoomEditor() throws IOException {
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
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    grid.getWalls().removeLast();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    int fileNum=(Objects.requireNonNull(roomStorage.list()).length);
                    File file=new File("src/Rooms/room" + (fileNum + 1) + ".txt");
                    try {
                        FileWriter fw=new FileWriter(file);
                        for (int i=0;i<grid.getWalls().size();i++) {
                            fw.write(grid.getWalls().get(i).x+" "+grid.getWalls().get(i).y+" "+grid.getWalls().get(i).width+" "+grid.getWalls().get(i).height+"\n");
                        }
                        fw.close();
                        grid.getWalls().clear();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    grid.getWalls().clear();
                }
                grid.repaint();
            }
        });
    }
}
