import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * <p>
 * ICS4U Final Project: DeadKnight
 * By Cayden, Beself, and Ken
 * Teacher: Mr. Benum
 * 19/06/2024
 * <br>
 * Sources:
 * <ul>
 *     <li>Grass tileset source: <a href="https://global.discourse-cdn.com/standard17/uploads/mapeditor/original/2X/6/66408bbe121f5d6fee647990bc52b8634bb9facb.png">https://global.discourse-cdn.com/standard17/uploads/mapeditor/original/2X/6/66408bbe121f5d6fee647990bc52b8634bb9facb.png</a></li>
 *     <li> Cloud tileset source: <a href="https://opengameart.org/content/cloud-tileset">https://opengameart.org/content/cloud-tileset</a></li>
 *     <li>Library tileset was created by Cayden's sister</li>
 *     <li>Enemies were created by Christina and Ken's sister</li>
 * </ul>
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
        frame.setVisible(true);
        game.start();
    }
}
