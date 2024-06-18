package UI;

import Entities.Player;
import Managers.ActionManager;
import Universal.GameTimer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Menu extends UI implements ActionListener {
    private final ArrayList<JButton> uiButtons = new ArrayList<>();
    private final ArrayList<JButton> controlButtons = new ArrayList<>();
    private final JButton start = new JButton("Start");
    private final JButton controls = new JButton("Controls");
    private BufferedImage gameIcon= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/gameIcon.png")));;

    private final JButton resetControls = new JButton("Reset Default Controls");
    private final JButton returnToMenu = new JButton("Return To Menu");

    private JButton currentButton;
    private final Player player;
    private boolean controlsOn = false;

    private boolean menuOn = true, waitingForPlayer;
    private JPanel panel;

    private String text = "Dead Knight";

    public Menu(JPanel panel, Player player) throws IOException {
        this.player = player;
        this.panel = panel;
        addButton(start, uiButtons);
        addButton(controls, uiButtons);

        JButton jumpButton = new JButton("Jump: " + KeyEvent.getKeyText(player.getControls().get(0)));
        JButton rightButton = new JButton("Move Right: " + KeyEvent.getKeyText(player.getControls().get(1)));
        JButton leftButton = new JButton("Move Left: " + KeyEvent.getKeyText(player.getControls().get(2)));
        JButton switchButton = new JButton("Switch Weapons: " + KeyEvent.getKeyText(player.getControls().get(3)));
        JButton primaryButton = new JButton("Set Primary: " + KeyEvent.getKeyText(player.getControls().get(4)));
        JButton cameraRight = new JButton("Use Weapon Right: " + KeyEvent.getKeyText(player.getControls().get(5)));
        JButton cameraLeft = new JButton("Use Weapon Left: " + KeyEvent.getKeyText(player.getControls().get(6)));
        JButton rollButton = new JButton("Roll / Dash: " + KeyEvent.getKeyText(player.getControls().get(7)));
        JButton openButton = new JButton("Chests / Portal: " + KeyEvent.getKeyText(player.getControls().get(8)));
        addButton(jumpButton, controlButtons);
        addButton(rightButton, controlButtons);
        addButton(leftButton, controlButtons);
        addButton(switchButton, controlButtons);
        addButton(primaryButton, controlButtons);
        addButton(cameraRight, controlButtons);
        addButton(cameraLeft, controlButtons);
        addButton(rollButton, controlButtons);
        addButton(openButton, controlButtons);
        addButton(resetControls, controlButtons);
        addButton(returnToMenu, controlButtons);
        resize();
    }

    public void addButton(JButton button, ArrayList<JButton> buttons) {
        button.setForeground(Color.RED);
        if (buttons == controlButtons) button.setVisible(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getBackground() != Color.YELLOW) button.setBackground(Color.darkGray);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getBackground() != Color.YELLOW) button.setBackground(null);
            }
        });
        button.setBorderPainted(false);
        button.setBackground(null);
        button.setFocusable(false);
        panel.add(button);
        buttons.add(button);
        button.addActionListener(this);
    }

    public void resize() {
        resizeImages();
        resizeUIButtons();
        resizeControlButtons();
    }
    public void resizeImages(){
        gameIcon=resizeImage(gameIcon,panel.getWidth()/7,panel.getWidth()/7);
    }

    private void resizeUIButtons() {
        for (JButton uiButton : uiButtons) {
            uiButton.setFont(new Font("Times New Roman", Font.BOLD, (panel.getWidth()) / 35));
        }
        for (int i = 0; i < uiButtons.size(); i++) {
            uiButtons.get(i).setSize(panel.getWidth() / 5, panel.getHeight() / 10);
            uiButtons.get(i).setLocation((panel.getWidth() - uiButtons.get(i).getWidth() * (uiButtons.size() - i) - uiButtons.get(i).getWidth() / 6 * (uiButtons.size() - i)) / 2 + (uiButtons.get(i).getWidth() / 2 + uiButtons.get(i).getWidth() / 6) * i, panel.getHeight() - uiButtons.get(i).getHeight() * 3);
        }
    }

    private void resizeControlButtons() {
        for (JButton contolButton : controlButtons) {
            contolButton.setFont(new Font("Times New Roman", Font.BOLD, (panel.getWidth()) / 50));
        }
        for (int i = 0; i < controlButtons.size(); i++) {
            controlButtons.get(i).setSize(panel.getWidth() / 4, panel.getHeight() / 12);
            controlButtons.get(i).setLocation(0, i * controlButtons.get(i).getHeight() + panel.getHeight() / 15);
        }
    }

    public void draw() {
        Graphics2D g = getGraphics();
        if (waitingForPlayer && !player.generateRooms()) {
            menuOn = false;
            waitingForPlayer = false;
        }

        if (menuOn) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            Font font = new Font("Times New Roman", Font.BOLD, getPanelWidth() / 20);
            g.setFont(font);
            g.setColor(Color.RED);
            FontMetrics metrics = g.getFontMetrics(font);

            int textX=(getPanelWidth() - metrics.stringWidth(text)) / 2-gameIcon.getWidth()/3;
            g.drawString(text, textX,(getPanelHeight() - metrics.getHeight()) / 2 + metrics.getAscent());
            g.drawImage(gameIcon,textX+metrics.stringWidth(text),(panel.getHeight()-gameIcon.getHeight())/2,null);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            player.reset();
            waitingForPlayer = true;
            controlsOn = false;
            text = "Loading";
            player.setDead(false);
            for (JButton uiButton : uiButtons) {
                uiButton.setVisible(false);
            }
            Graphics2D g = getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            Font font = new Font("Times New Roman", Font.BOLD, getPanelWidth() / 20);
            g.setFont(font);
            g.setColor(Color.RED);
            FontMetrics metrics = g.getFontMetrics(font);

            int textX=(getPanelWidth() - metrics.stringWidth(text)) / 2-gameIcon.getWidth()/3;
            g.drawString(text, textX,(getPanelHeight() - metrics.getHeight()) / 2 + metrics.getAscent());
            g.drawImage(gameIcon,textX+metrics.stringWidth(text),(panel.getHeight()-gameIcon.getHeight())/2,null);
            player.setGenerateRooms(true);
        }
        if (e.getSource() == resetControls) {
            for (int i = 0; i < player.getControls().size(); i++) {
                controlButtons.get(i).setText(controlButtons.get(i).getText().substring(0, controlButtons.get(i).getText().length() - KeyEvent.getKeyText(player.getControls().get(i)).length()));
            }
            player.setDefaultControls();
            for (int i = 0; i < player.getControls().size(); i++) {
                controlButtons.get(i).setText(controlButtons.get(i).getText()+KeyEvent.getKeyText(player.getControls().get(i)));
            }
        }
        if (e.getSource() == returnToMenu) {
            for (JButton button : controlButtons) {
                button.setVisible(false);
            }
            for (JButton button : uiButtons) {
                button.setVisible(true);
                controlsOn=false;
            }
        }

        if (controlsOn) {
            if (currentButton != null) currentButton.setBackground(null);
            if (e.getSource()!=returnToMenu && e.getSource()!=resetControls) {
                currentButton = (JButton) e.getSource();
                currentButton.setBackground(Color.YELLOW);
            }
        } else if (e.getSource() == controls) {
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
            currentButton.setText(currentButton.getText().substring(0,currentButton.getText().length()-KeyEvent.getKeyText(player.getControls().get(controlButtons.indexOf(currentButton))).length())+KeyEvent.getKeyText(manager.getKeyCode()));
            player.getControls().set(controlButtons.indexOf(currentButton), manager.getKeyCode());
            currentButton.setBackground(null);
            currentButton = null;
        }
    }

    public boolean isMenuOn() {
        return menuOn;
    }

    public void setMenuOn(boolean menuOn) {
        this.menuOn = menuOn;
        if (menuOn) {
            for (JButton b: uiButtons) {
                b.setVisible(true);

            }
            text = "Dead Knight";
        }
    }
}


