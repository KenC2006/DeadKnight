package Structure;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;


public class RoomEditor extends JFrame {
    public static final int ROW = 100;
    public static final int COL = 150;
    private final Grid grid;
    private static final File rooms = new File("C:/workspace/Final2024/src/Rooms");
    public static int fileNum = (Objects.requireNonNull(rooms.list()).length);
    public static ArrayList<FileWriter> fWriters = new ArrayList<>();
    public static int currentFiles = 0;


    public RoomEditor() throws IOException {
        grid = new Grid(ROW, COL);
        add(grid);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        final File[] roomFile = {new File("src/Rooms/room" + (fileNum + 1) + ".txt")};
        fWriters.add(new FileWriter(roomFile[0]));
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    try {
                        fileNum = (Objects.requireNonNull(rooms.list()).length);
                        fWriters.get(currentFiles).close();
                        roomFile[0] = new File("src/Rooms/room" + (fileNum + 1) + ".txt");
                        fWriters.add(new FileWriter(roomFile[0]));
                        System.out.println("saved");
                        grid.reset();
                        currentFiles++;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    System.out.println("reset");
                    grid.reset();
                    File roomFile = new File("src/Rooms/room" + (fileNum + 1) + ".txt");
                    currentFiles++;
                    try {
                        fWriters.add(new FileWriter(roomFile));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    fWriters.get(currentFiles).close();
                    System.out.println(roomFile[0].getName()+" deleted");
                    Files.delete(roomFile[0].toPath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
