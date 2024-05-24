package Structure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Grid extends JPanel implements MouseListener {
    public static int sR=-1, sC, eR, eC;
    private int boxWidth, boxHeight;
    private int set=0;
    private final ArrayList<Rectangle>walls=new ArrayList<>();

    public Grid(int row, int col) {
        this.addMouseListener(this);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                boxWidth = getWidth() / col;
                boxHeight = getHeight() / row;
                repaint();
            }
        });
    }
    public ArrayList<Rectangle> getWalls(){
        return walls;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        if (set == 1){
            g.fillRect(sC*boxWidth,sR*boxHeight,boxWidth,boxHeight);
        }
        g.setColor(Color.RED);
        for (Rectangle wall : walls) {
            g.fillRect(wall.x * boxWidth, wall.y * boxHeight, boxWidth * (wall.width), boxHeight * (wall.height));
        }

        g.setColor(Color.BLACK);
        if (boxWidth > 0 && boxHeight > 0) {
            for (int i = 0; i < getWidth(); i += boxWidth) {
                g.drawLine(i, 0, i, getHeight());
            }
            for (int i = 0; i < getHeight(); i += boxHeight) {
                g.drawLine(0, i, getWidth(), i);
            }

        }
    }
    public void addRect(int x, int y){
        set++;
        if (set==1){
            sR=y;
            sC=x;
        }
        if (set==2){
            eR=y;
            eC=x;
            if (Grid.eR < Grid.sR) {
                int temp = Grid.sR;
                Grid.sR = Grid.eR;
                Grid.eR = temp;
            }
            if (Grid.eC < Grid.sC) {
                int temp = Grid.sC;
                Grid.sC = Grid.eC;
                Grid.eC = temp;
            }
            set=0;
            walls.add(new Rectangle(sC,sR,eC-sC+1,eR-sR+1));
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        addRect( mouseX/boxWidth, mouseY/boxHeight);

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
}



