import java.awt.image.BufferedImage;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Sequential {
    BufferedImage img;
    int squareSize;
    public Sequential(BufferedImage image, int size){
        img = image;
        squareSize = size;
    }

    public void calculate(){

        // prepare frame and label
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img.getWidth() ,img.getHeight());
        JLabel lbl=new JLabel();

        // loop through image to change pixel values
        for (int y = 0; y <= img.getHeight()-squareSize; y+=squareSize) {
            // methods are doing the same thing, so Sequential class wil use Parallel class' modifyImage method
            Parallel.modifyImage(y, lbl, img, squareSize, frame);
        }
    }
}