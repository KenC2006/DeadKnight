package UI;

import Entities.Player;
import Managers.ActionManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Menu extends UI implements ActionListener {
    private final ArrayList<JButton> uiButtons = new ArrayList<>();
    private final ArrayList<JButton> controlButtons = new ArrayList<>();
    private final JButton start = new JButton("Start");
    private final JButton controls = new JButton("Controls");

    private JButton currentButton;
    private final Player player;
    private boolean gameOn = false;
    private boolean controlsOn = false;

    private final JPanel panel;

    public Menu(JPanel panel, Player player) {
        this.panel = panel;
        this.player = player;
        addButton(start, uiButtons);
        addButton(controls, uiButtons);


        JButton jumpButton = new JButton("Jump: " + KeyEvent.getKeyText(player.getControls().get(0)));
        JButton rightButton = new JButton("Move Right: " + KeyEvent.getKeyText(player.getControls().get(1)));
        JButton leftButton = new JButton("Move Left: " + KeyEvent.getKeyText(player.getControls().get(2)));
        JButton switchButton = new JButton("Switch Weapons: " + KeyEvent.getKeyText(player.getControls().get(3)));
        JButton primaryButton = new JButton("Set Primary: " + KeyEvent.getKeyText(player.getControls().get(4)));
        JButton cameraRight = new JButton("Shift Camera Right: " + KeyEvent.getKeyText(player.getControls().get(5)));
        JButton cameraLeft = new JButton("Shift Camera Left: " + KeyEvent.getKeyText(player.getControls().get(6)));
        JButton rollButton = new JButton("Roll: " + KeyEvent.getKeyText(player.getControls().get(7)));
        addButton(jumpButton, controlButtons);
        addButton(rightButton, controlButtons);
        addButton(leftButton, controlButtons);
        addButton(switchButton, controlButtons);
        addButton(primaryButton, controlButtons);
        addButton(cameraRight, controlButtons);
        addButton(cameraLeft, controlButtons);
        addButton(rollButton, controlButtons);
        resize();
    }

    public void addButton(JButton button, ArrayList<JButton> buttons) {
        if (buttons == controlButtons) button.setVisible(false);
        button.setFocusable(false);
        panel.add(button);
        buttons.add(button);
        button.addActionListener(this);
    }

    public void resize() {
        resizeUIButtons();
        resizeControlButtons();
    }

    private void resizeUIButtons() {
        for (int i = 0; i < uiButtons.size(); i++) {
            uiButtons.get(i).setSize(panel.getWidth() / 8, panel.getHeight() / 15);
            uiButtons.get(i).setLocation((panel.getWidth() - uiButtons.get(i).getWidth() * (uiButtons.size() - i) - uiButtons.get(i).getWidth() / 6 * (uiButtons.size() - i)) / 2 + (uiButtons.get(i).getWidth() / 2 + uiButtons.get(i).getWidth() / 6) * i, panel.getHeight() - uiButtons.get(i).getHeight() * 3);
        }
    }

    private void resizeControlButtons() {
        for (int i = 0; i < controlButtons.size(); i++) {
            controlButtons.get(i).setSize(panel.getWidth() / 8, panel.getHeight() / 15);
            controlButtons.get(i).setLocation((panel.getWidth() - controlButtons.get(i).getWidth()) / 2, i * controlButtons.get(i).getHeight() + panel.getHeight() / 15 + (i - 1) * panel.getHeight() / 20);
        }
    }

    public void draw() {
        Graphics2D g = getGraphics();
        if (!gameOn) {
            //g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            gameOn = true;
            for (JButton uiButton : uiButtons) {
                uiButton.setVisible(false);
            }

        }

        if (controlsOn) {
            if (currentButton != null) currentButton.setBackground(null);
            currentButton = (JButton) e.getSource();
            currentButton.setBackground(Color.YELLOW);

        }

        else if (e.getSource() == controls) {
            controlsOn = true;
            for (JButton uiButton : uiButtons) {
                uiButton.setVisible(false);
            }
            for (JButton controlButton : controlButtons) {
                controlButton.setVisible(true);
            }
        }
    }

    public void updateKeyPresses(ActionManager manager) {
        if (manager.isPressed() && currentButton != null) {
            currentButton.setBackground(Color.YELLOW);
            player.getControls().set(controlButtons.indexOf(currentButton),manager.getKeyCode());
            currentButton.setBackground(null);
            currentButton = null;

        }
    }
}


