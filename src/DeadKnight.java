import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * ICS4U Final Project: DeadKnight
 * By Cayden, Beself, and Ken
 * Teacher: Mr. Benum
 * 19/06/2024
 *
 * Grass tileset source: https://www.google.com/search?q=dirt+tileset+tiled&sca_esv=e08f8124576d3ae8&sca_upv=1&rlz=1C1UEAD_enCA1110CA1110&udm=2&biw=2560&bih=1271&ei=1XJzZqDfEuDJp84PheOa4AQ&ved=0ahUKEwjgnd3X8eiGAxXg5MkDHYWxBkwQ4dUDCBA&uact=5&oq=dirt+tileset+tiled&gs_lp=Egxnd3Mtd2l6LXNlcnAiEmRpcnQgdGlsZXNldCB0aWxlZEjWDVDzB1jHC3ACeACQAQCYAZQBoAHgA6oBAzMuMrgBA8gBAPgBAZgCAKACAJgDAIgGAZIHAKAH4QE&sclient=gws-wiz-serp&safe=active&ssui=on#vhid=BgHfqB2j5qYjrM&vssid=mosaic
 * Cloud tileset source: https://www.google.com/search?sca_esv=e08f8124576d3ae8&sca_upv=1&rlz=1C1UEAD_enCA1110CA1110&q=cloud+tileset&udm=2&fbs=AEQNm0AeMNWKf4PpcKMI-eSa16lJoRPMIuyspCxWO6iZW9F1Ns6EVsgc0W_0xN47PHaanAEtg26fpfc9gg2y1-ZsywNNidIzOA0khSyMN51n7r3LlDC9M1NYStuTRDcBUYQ58dKt-Q6SigUS4Yne5yDHLg0vPBr98Nz98twIaNcnWiKaD4QuEh93Q53sB-UkWP9OcfO5KeatY98HR7cDW9ZTjFpZV7kJtA&sa=X&ved=2ahUKEwi8vuGa8uiGAxVRC3kGHQqaDH8QtKgLegQIDRAB&biw=2560&bih=1271&dpr=1&safe=active&ssui=on#vhid=qWz1NYmpMMXkNM&vssid=mosaic
 * Library tileset was created by Cayden's sister
 * Enemies were created by Christina and Ken's sister
 * Game Ideas
 */

public class DeadKnight {
    public static void main(String []args) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        GamePanel game = new GamePanel(screenSize);
        frame.add(game);
        frame.setTitle("DeadKnight");
        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
   //     frame.setUndecorated(true);
        frame.setVisible(true);
        game.start();
    }
}
