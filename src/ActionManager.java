import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ActionManager extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {
    private Timer timer = new Timer(10,this);
    private final Map<Integer, Boolean> pressed = new HashMap<>();

    public ActionManager(){
        timer.start();
    }

    /**
     *
     * @param panel
     */
    public void addPanel(JPanel panel) {
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    public boolean getPressed(int code) {
        return pressed.getOrDefault(code, false);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed.put(e.getKeyCode(), false);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
