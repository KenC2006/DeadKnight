import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ActionManager extends JPanel implements KeyListener, ActionListener, MouseListener {
        Timer timer=new Timer(10,this);

    public ActionManager(){
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.print("f");

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.print("kc");
        if (e.getKeyCode()==KeyEvent.VK_W){
            System.out.print("k");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //  ASIODASIOJDIOASJDIOJASIODJSA
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
}
