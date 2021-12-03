import java.util.*;
import java.awt.image.BufferedImage;

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

        // read image
        BufferedImage img =  ImageIO.read(new File(fileName));

        // "m" for multi-thread, "s" for single-thread
        if("m".equals(args[2])){

            // get number of cores
            int cores = Runtime.getRuntime().availableProcessors();
            System.out.println("Number of cores: " + cores);

            
            // number of threads that will be used
            int threadCount = args.length!=4 ? cores*2 : Integer.parseInt(args[3]);
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