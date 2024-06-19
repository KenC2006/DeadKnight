import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * ICS4U Final Project: DeadKnight
 * By Cayden, Beself, and Ken
 * Teacher: Mr. Benum
 * 19/06/2024
 */

public class DeadKnight {
    public static void main(String []args) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        GamePanel game = new GamePanel(screenSize);
        frame.add(game);
        frame.setTitle("DeadKnight");
        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
  //      frame.setUndecorated(true);
        frame.setVisible(true);
        game.start();
    }
}
