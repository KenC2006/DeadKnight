import javax.swing.*;

public class DeadKnight {
    public static void main(String []args){
        JFrame frame = new JFrame();
        GamePanel game = new GamePanel();

        frame.add(game);
        frame.setTitle("DeadKnight");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(true);
        frame.setVisible(true);

        game.start();
    }
}
