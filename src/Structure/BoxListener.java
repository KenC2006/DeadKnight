package Structure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class BoxListener extends MouseAdapter {

    public BoxListener() {
    }

    public void mouseClicked(MouseEvent me) {
        JPanel clickedBox =(JPanel)me.getSource();
        Grid.select++;
        if (clickedBox.getBackground() == Color.GREEN){
            clickedBox.setBackground(Color.WHITE);
            Grid.select++;
        }
        else {
            clickedBox.setBackground(Color.GREEN);
            if (Grid.select==1){
                Grid.sR=Integer.parseInt(clickedBox.getName())/100;
                Grid.sC=Integer.parseInt(clickedBox.getName())%100;
            }
            else if (Grid.select==2){
                Grid.eR=Integer.parseInt(clickedBox.getName())/100;
                Grid.eC=Integer.parseInt(clickedBox.getName())%100;
                if (Grid.eR<Grid.sR){
                    int temp=Grid.sR;
                    Grid.sR=Grid.eR;
                    Grid.eR=temp;
                }
                if (Grid.eC<Grid.sC){
                    int temp=Grid.sC;
                    Grid.sC=Grid.eC;
                    Grid.eC=temp;
                }
                for (int i=0;i<=Math.abs(Grid.sR-Grid.eR);i++){
                    for (int j=0;j<=Math.abs(Grid.sC-Grid.eC);j++){
                        Grid.panels.get((Grid.sR+i)*100+j+Grid.sC).setBackground(Color.RED);
                    }
                }
                Grid.select=0;
                try {
                    RoomEditor.fwriter.write(Grid.sR+" "+Grid.sC+" "+(Grid.eC-Grid.sC+1)+" "+(Grid.eR-Grid.sR+1)+"\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
