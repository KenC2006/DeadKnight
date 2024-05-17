import javax.swing.*;

public class DeadKnight {
    public static void main(String []args){
        JFrame frame=new JFrame();
        frame.add(new GamePanel());
        frame.setTitle("DeadKnight");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(true);
        frame.setVisible(true);

    }
}
