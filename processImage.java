import java.util.*;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Graphics2D;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

// I never code in Java and I hope you will take that into consideration.
// Thank you.

public class processImage {

    public static void main(String[] args) throws IOException {
        
        // the application requires 3 arguments and all 3 are needed to be provided
        if(args.length<3){
            System.out.println("Please provide three arguments: filename, square size and" +
                    " the processing mode (s/m)");
            return;
        }

        int i;

        // get values from arguments
        String fileName = args[0];
        int squareSize = Integer.parseInt(args[1]);

        // get window size to scale the image accordingly
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        
        // screenWidth will store the width of the screen
        int screenWidth = (int)size.getWidth();
        
        // screenHeight will store the height of the screen
        int screenHeight = (int)size.getHeight();

        // read image
        BufferedImage fullImage =  ImageIO.read(new File(fileName));

        // calculate the width and hight ratio, and use the smaller one to scale image
        double widthRatio = (float)screenWidth / (float) fullImage.getWidth();
        double heightRatio = (float) screenHeight / (float) fullImage.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        // get the new width and height
        int w = (int) (fullImage.getWidth()* ratio);
        int h = (int) (fullImage.getHeight() * ratio);

        // create new scaled image
        Image temp = fullImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);

        // draw our image on newly created image
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.drawImage(temp, 0, 0, null);
        graphics2D.dispose();

        // "m" for multi-thread, "s" for single-thread
        if("m".equals(args[2])){

            // get number of cores
            int cores = Runtime.getRuntime().availableProcessors();
            System.out.println("Number of cores: " + cores);

            
            // number of threads that will be used
            int threadCount = args.length==4 ? Integer.parseInt(args[3]) : cores;
            System.out.println("Number of threads: " + threadCount);

            JFrame frame=new JFrame();

            // create threadCount threads
            ArrayList<Parallel> t = new ArrayList<>();
            for(i=0;i<threadCount;i++){
                Parallel parallel = new Parallel(i,threadCount,img,squareSize,frame);
                t.add(parallel);
            }

            // start threads
            try{
                for(i=0;i<threadCount;i++){
                    t.get(i).start();
                }
            }
            catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }


            // join threads
            try{
                for(i=0;i<threadCount;i++){
                    t.get(i).join();
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

        }else if("s".equals(args[2])){
            // create new instance of Sequential
            Sequential s = new Sequential(img,squareSize);

            // call calculate method that modifies image
            s.calculate();
        }

        // save the result in "result.jpg" file
        try {
            File outputfile = new File(java.nio.file.Paths.get("result.jpg").toString());
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}