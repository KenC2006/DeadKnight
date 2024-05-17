import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel{
    private ActionManager manager = new ActionManager();
    private Player player = new Player();

    public GamePanel() {
        this.setLayout(null);
        this.setFocusable(true);
        this.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
    }
}
