package Managers;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ActionManager implements KeyListener, MouseListener, MouseMotionListener {
    private final Map<Integer, Boolean> pressed = new HashMap<>();
    private int pressedCount = 0;

    public void addPanel(JPanel panel) {
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    public boolean getPressed(int code) {
        return pressed.getOrDefault(code, false);
    }

    public boolean anyKeyPressed() {
        return pressedCount > 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedCount += 1;
        pressed.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedCount -= 1;
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
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
