package Managers;

import Structure.Vector2F;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Manages user input actions, including keyboard and mouse events.
 *
 * <p>
 * The ActionManager class handles the registration of key and mouse listeners to a JPanel.
 * It tracks the state of key presses, mouse clicks, and mouse movements, providing methods
 * to query the current state of these inputs.
 * </p>
 */
public class ActionManager implements KeyListener, MouseListener, MouseMotionListener {
    private final Map<Integer, Boolean> pressed = new HashMap<>();
    private int lastPressed = 0;
    private Vector2F absoluteMouseLocation = new Vector2F();
    private boolean mousePressed;

    /**
     * Adds the necessary listeners to the provided panel.
     *
     * <p>
     * This method attaches key, mouse, and mouse motion listeners to the specified JPanel, enabling it to handle
     * keyboard and mouse events.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The panel will be able to respond to key presses, mouse clicks, and mouse movement.
     * </p>
     *
     * @param panel the JPanel to which the listeners will be added
     */
    public void addPanel(JPanel panel) {
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    /**
     * Retrieves the current absolute mouse location.
     *
     * <p>
     * This method returns the current mouse coordinates within the component that received the event.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The absolute mouse location is returned.
     * </p>
     *
     * @return the current absolute mouse location as a Vector2F
     */
    public Vector2F getAbsoluteMouseLocation() {
        return absoluteMouseLocation;
    }

    /**
     * Checks if a specific key is currently pressed.
     *
     * <p>
     * This method returns a boolean indicating whether the key corresponding to the given keycode is currently pressed.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> A boolean value representing the key's pressed state is returned.
     * </p>
     *
     * @param code the keycode of the key to check
     * @return true if the key is pressed, false otherwise
     */
    public boolean getPressed(int code) {
        return pressed.getOrDefault(code, false);
    }

    /**
     * Checks if any key is currently pressed.
     *
     * <p>
     * This method returns a boolean indicating whether any key is currently pressed.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> A boolean value indicating if any key is pressed is returned.
     * </p>
     *
     * @return true if any key is pressed, false otherwise
     */
    public boolean isPressed() {
        return pressed.containsValue(true);
    }

    /**
     * Retrieves the keycode of the last pressed key.
     *
     * <p>
     * This method returns the keycode of the most recently pressed key.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The keycode of the last pressed key is returned.
     * </p>
     *
     * @return the keycode of the last pressed key
     */
    public int getKeyCode() {
        return lastPressed;
    }

    /**
     * Checks if the mouse is currently pressed.
     *
     * <p>
     * This method returns a boolean indicating whether the mouse is currently pressed.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> A boolean value indicating if the mouse is pressed is returned.
     * </p>
     *
     * @return true if the mouse is pressed, false otherwise
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed.put(e.getKeyCode(), true);
        lastPressed = e.getKeyCode();
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
     * Updates the absolute mouse location when the mouse is dragged.
     *
     * <p>
     * This method sets the absolute mouse location based on the coordinates of the mouse event.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The absolute mouse location is updated to the current coordinates of the mouse drag event.
     * </p>
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        absoluteMouseLocation.setX(e.getX());
        absoluteMouseLocation.setY(e.getY());
    }

    /**
     * Updates the absolute mouse location when the mouse is moved.
     *
     * <p>
     * This method sets the absolute mouse location based on the coordinates of the mouse event.
     * </p>
     *
     * <p>
     * <strong>Post-condition:</strong> The absolute mouse location is updated to the current coordinates of the mouse move event.
     * </p>
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        absoluteMouseLocation.setX(e.getX());
        absoluteMouseLocation.setY(e.getY());
    }
}
