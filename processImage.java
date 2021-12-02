import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

// I never code in Java and I hope you will take that into consideration.
// Thank you.

public class processImage {

    public static void main(String[] args) throws IOException {
        // the application requires 3 arguments and all are needed to be provided
        if(args.length!=3){
            System.out.println("Please provide three arguments: filename, square size and" +
                    " the processing mode (s/m)");
            return;
        }

        int i;

        // get values from arguments
        String fileName = args[0];
        int squareSize = Integer.parseInt(args[1]);

        // read image
        BufferedImage img =  ImageIO.read(new File(java.nio.file.Paths.get(System.getProperty("user.dir"),fileName).toString()));

        // "m" for multi-thread, "s" for single-thread
        if("m".equals(args[2])){

            // get number of cores
            int cores = Runtime.getRuntime().availableProcessors();
            System.out.println("Number of cores: " + cores);

            // cores/2 number of threads will be used
            System.out.println("Number of threads: " + cores/2);

            JFrame frame=new JFrame();

            // create cores/2 threads
            ArrayList<Parallel> t = new ArrayList<>();
            for(i=0;i<cores/2;i++){
                Parallel parallel = new Parallel(i,cores/2,img,squareSize,frame);
                t.add(parallel);
            }

            // start threads
            try{
                for(i=0;i<cores/2;i++){
                    t.get(i).start();
                }
            }
            catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }


            // join threads
            try{
                for(i=0;i<cores/2;i++){
                    t.get(i).join();
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

        }else if("s".equals(args[2])){
            Sequential s = new Sequential(img,squareSize);

            // call calculate method that modifies image
            s.calculate();
        }

        // save the result in "result.jpg" file
        try {
            File outputfile = new File(java.nio.file.Paths.get(System.getProperty("user.dir"),"result.jpg").toString());
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Sequential {
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

class Parallel extends Thread{
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

    // method to modify image by changing pixel values
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