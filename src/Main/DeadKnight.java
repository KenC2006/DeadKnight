package Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class DeadKnight {
    public static void main(String []args) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        GamePanel game = new GamePanel(screenSize);
        frame.add(game);
        frame.setTitle("Main.DeadKnight");
        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(true);
        frame.setVisible(true);

        game.start();
    }
}
