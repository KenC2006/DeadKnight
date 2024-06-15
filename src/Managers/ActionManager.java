package Managers;

import Structure.Vector2F;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class
ActionManager implements KeyListener, MouseListener, MouseMotionListener {
    private final Map<Integer, Boolean> pressed = new HashMap<>();
    private int lastPressed = 0;
    private Vector2F mouseLocation = new Vector2F();
    private boolean mousePressed;

    public void addPanel(JPanel panel) {
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    public Vector2F getMouseLocation() {
        return mouseLocation;
    }

    public boolean getPressed(int code) {
        return pressed.getOrDefault(code, false);
    }

    public boolean isPressed() {
        return pressed.containsValue(true);
    }

    public int getKeyCode() {
        return lastPressed;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed.put(e.getKeyCode(), true);
        lastPressed=e.getKeyCode();
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
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
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
        mouseLocation.setX(e.getX());
        mouseLocation.setY(e.getY());
    }
}
