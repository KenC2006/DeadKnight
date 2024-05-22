package Managers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventsManager implements ActionListener {
    private int TICKS = 10;
    private final Timer timer = new Timer(TICKS,this);

    public EventsManager() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

