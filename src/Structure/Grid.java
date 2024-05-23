package Structure;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Grid extends JPanel {
    public static ArrayList<JPanel> panels = new ArrayList<>();
    public static int select = 0;
    public static int sR, sC, eR, eC;


    public Grid(int row, int col) {
        int count = 0;
        setLayout(new GridLayout(row, col));
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));


        for (int i = 1; i <= (row * col); i++) {
            panels.add(new JPanel());
            panels.get(i - 1).setEnabled(true);
            panels.get(i - 1).setBackground(Color.WHITE);
            panels.get(i - 1).setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panels.get(i - 1).addMouseListener(new BoxListener());
            panels.get(i - 1).setName(count + "");
            count++;
            add(panels.get(i - 1));
        }
    }

    public void reset() {
        for (JPanel panel : panels) {
            panel.setBackground(Color.WHITE);
        }
    }
}



