import java.awt.image.BufferedImage;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Parallel extends Thread{
    public static BufferedImage img;
    public static JFrame frame;
    public  int squareSize;
    private final int idx;
    private final int threadCount;

    public Parallel(int i,int c,BufferedImage image,int size, JFrame framex){
        super();
        idx =i;
        threadCount = c;
        img = image;
        squareSize = size;
        frame=framex;
    }

    public void run(){

        // prepare frame and label
        frame.setLayout(new FlowLayout());
        frame.setSize(2*img.getWidth() ,2*img.getHeight());
        JLabel lbl=new JLabel();


        // calculate beginning and ending position for threads depending on thread numbers
        int startY = idx*(img.getHeight()/(threadCount));
        int endY;
        if(idx!=threadCount-1)
            endY = startY + (img.getHeight()/(threadCount));
        else
            endY = startY + (img.getHeight()/(threadCount))-squareSize+1;

        // loop through image to change pixel values
        for (int y = startY; y <= endY; y+=squareSize) {
            modifyImage(y, lbl, img, squareSize, frame);
        }
    }

    /**
     * Changes the pixels of image from left to right,
     * top to bottom. Updates label and frame, and 
     * displays it.
     * 
     * An {@code ArrayOutOfBoundsException} may be thrown
     * if the region is not in bounds.
     * However, explicit bounds checking is not guaranteed.
     *
     * @param y           the Y coordinate
     * @param lbl         the label that displays image
     * @param img         image that will be modified
     * @param squareSize  the size of the square for which the average pixel 
     *                     color values will be calculated
     * @param frame       the frame where label is added
     */
    public static void modifyImage(int y, JLabel lbl, BufferedImage img, int squareSize, JFrame frame) {
        int x,sumReds,sumBlues,sumGreens,sumAlphas,avgReds,avgGreens,avgBlues,avgAlphas,i,j,p;

        for (x = 0; x <= img.getWidth()- squareSize; x+= squareSize) {
            sumReds=0; sumBlues=0; sumGreens=0; sumAlphas=0;

            // get pixel values in (square*square) box
            for(i=y; i<y+ squareSize; i++){
                for(j=x; j<x+ squareSize; j++){

                    //get pixel value
                    p = img.getRGB(j,i);

                    //get alpha
                    sumAlphas += (p>>24) & 0xff;

                    //get red
                    sumReds+= (p>>16) & 0xff;

                    //get green
                    sumGreens+= (p>>8) & 0xff;

                    //get blue
                    sumBlues+= p & 0xff;
                }
            }

            // get average of all colors and alpha
            avgReds = sumReds/(squareSize * squareSize);
            avgGreens = sumGreens/(squareSize * squareSize);
            avgBlues = sumBlues/(squareSize * squareSize);
            avgAlphas = sumAlphas/(squareSize * squareSize);

            // get new pixel value
            p = (avgAlphas<<24) | (avgReds<<16) | (avgGreens<<8) | avgBlues;

            // set new pixel value
            for(i=y; i<y+ squareSize; i++){
                for(j=x; j<x+ squareSize; j++){
                    img.setRGB(j, i, p);
                }
            }

            // update frame icon
            ImageIcon icon=new ImageIcon(img);
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
        }
    }
}