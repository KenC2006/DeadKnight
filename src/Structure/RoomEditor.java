package Structure;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RoomEditor extends JFrame {
    private int row=100;
    private int col=100;
    public static File roomFile = new File("room.txt");
    public static FileWriter fwriter;
    static {
        try {
            fwriter = new FileWriter(roomFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RoomEditor() {
        add(new Grid(row,col));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    fwriter.close();
                    System.out.println("Closing");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}