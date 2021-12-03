# Concurrent Image Processing

The objective of the program is to pixelate image by finding the average color for the 
(square size) x (square size) boxes and set the color of the whole square to this average color. <br><br>
It will be done both by using single thread and multiple threads.
The amount of threads that will be used is determined by the number of CPU cores times 2 or by optional argument.
<br>
<br>
The application takes 4 arguments (first 3 are required, and the last one is optional), filename, square size, the processing mode and number of threads to use (optional). 
If number of threads won't be given as argument, then it will be equal to twice the number of CPU cores. 
<br>
<ul>
<li>file name: the name of the graphic file of jpg format (no size constraints)</li>
<li>square size: the side of the square for the averaging</li>
<li>processing mode: 's' - single threaded and 'm' - multi threaded</li>
<li>number of threads (optional)</li>
</ul>

The process of pixelation will be shown in real time and final result will be saved to **result.jpg** file.<br>

The language of choice is **Java** for the support for physical level concurrency despite no previous experience.

## How to run the application
<ul>
<li> Compile the application: 
</ul>

```
javac processImage.java
``` 

<ul>
<li> Run the application
</ul>

```
java processImage mona.jpg 40 s
```
or
```
java processImage mona.jpg 5 m 8
```

## Original image
![Original image](https://github.com/ADA-GWU/concurrency-Dashdamirli/blob/main/mona.jpg?raw=true)

## Result of the process
![Image of result](https://github.com/ADA-GWU/concurrency-Dashdamirli/blob/main/result.jpg?raw=true)
