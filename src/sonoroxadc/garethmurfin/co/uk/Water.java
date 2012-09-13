package sonoroxadc.garethmurfin.co.uk;
/*
 * Name:     Water
 * Date:     December 2004
 * Author:   Neil Wallis
 * Purpose:  Simulate ripples on Water.
 */

import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.*;
//import java.applet.Applet;
import java.net.URL;

import android.graphics.Bitmap;

public class Water implements  Runnable{
 //// MouseListener, MouseMotionListener {

    String str;
   public static int width;
public static int height;
	int hwidth, hheight;
    Bitmap source;
    Bitmap image, offImage;
    Graphics offGraphics;
    int i,a,b;
    int MouseX,MouseY;
    int fps,delay,size;

    static /*short*/int ripplemap[];
    public static int texture[];
    public static int ripple[];
    static int oldind;
	int newind, mapind;
    static int riprad;
    Image im;

    Thread animatorThread;
    boolean frozen = false;
    //Bitmap image;
   Graphics g;
    public Water(Bitmap image_, Graphics gfx) {
    	image=image_;
    	g=gfx;
    	//T._("WATER MADE.");
      //addMouseListener(this);
      //addMouseMotionListener(this);

      //Retrieve the base image
     /* str = getParameter("image");
      if (str != null) {
        try {
          MediaTracker mt = new MediaTracker(this);
          im = getImage(getDocumentBase(),str);
          mt.addImage(im,0);
          try {
            mt.waitForID(0);
            } catch (InterruptedException e) {
              return;
            }
        } catch(Exception e) {}
      }*/

      //How many milliseconds between frames?
     /* str = getParameter("fps");
      try {
        if (str != null) {
          fps = (int)Integer.parseInt(str);
        }
      } catch (Exception e) {}
      */fps=5;
      delay = (fps > 0) ? (1000 / fps) : 100;

      width = 16;//im.getWidth(this);
      height = 10;//im.getHeight(this);
      hwidth = width>>1;
      hheight = height>>1;
      riprad=1;//6;

      size = width * (height+2) * 2;
      ripplemap = new int[size];// short[size];
      ripple = new int[width*height];
      texture = new int[width*height];
      oldind = width;
      newind = width * (height+3);
      System.out.println("getpixels");
 image.getPixels(texture, 0, width, 0,0,width,height);
     // PixelGrabber pg = new PixelGrabber(im,0,0,width,height,texture,0,width);
      //try {
      //  pg.grabPixels();
       // } catch (InterruptedException e) {}


      //source = new MemoryImageSource(width, height, ripple, 0, width);
      //source.setAnimated(true);
     // source.setFullBufferUpdates(true);
      //  source=Bitmap.createBitmap(ripple,width,height,Bitmap.Config.ARGB_4444);
        
     // image = Bitmap.createBitmap(source);
     // offImage = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_4444);
      //offGraphics = offImage.getGraphics();
    
    }

    public void start() {
        if (frozen) {
            //Do nothing.
        } else {
            //Start animation thread
            if (animatorThread == null) {
                animatorThread = new Thread(this);
            }
            animatorThread.start();
        }
    }

    public void stop() {
      animatorThread = null;
    }

    public void destroy() {
     // removeMouseListener(this);
     // removeMouseMotionListener(this);
    }

  //  public void mouseEntered(MouseEvent e) {}
  //  public void mouseDragged(MouseEvent e) {}
  //  public void mouseReleased(MouseEvent e) {}
  //  public void mouseClicked(MouseEvent e) {}
   // public void mouseExited(MouseEvent e) {}

  /*  public void mousePressed(MouseEvent e) {
      if (frozen) {
        frozen = false;
        start();
      } else {
        frozen = true;
        animatorThread = null;
      }
    }
*/
    public void mouseMoved(int x, int y){///MouseEvent e) {
	disturb(x,y);
    }
int xTEST, yTEST;
long startTime = System.currentTimeMillis();

long lastrun;
private boolean running=true;
long timenow;
    public void run() {
     // Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
   

      //while (Thread.currentThread() == animatorThread) {
    	 // T._("doing stuff "+ripple[0]);
    	  while (running)
    	  {
    		  timenow=(System.currentTimeMillis()-lastrun);
    	if ( timenow>1000 )
    	{
    		System.out.println("new frame "+timenow);
        //newframe();
        lastrun=System.currentTimeMillis();
    	}
    	  }
      //  source=Bitmap.createBitmap(ripple,width,height,Bitmap.Config.ALPHA_8);
        
       /* for (int i=0; i<1000;i++)
        {
        	System.out.print("r"+ripple[i]);
        }
        for (int i=0; i<1000;i++)
        {
        	System.out.print("t"+texture[i]);
        }
        image = Bitmap.createBitmap(image);*/
        
       // source.newPixels();
      //  im,0,0,width,height,texture,0,width
       // image=Bitmap.createBitmap(ripple,width, height, Bitmap.Config.ARGB_4444);
      //  g.drawImage(image,0,0,0);
        //update(g);
        
        /*if (xTEST>width)
        {
        	xTEST=0;
        	yTEST=0;
        }
        disturb(xTEST++,yTEST++);*/
       /* try {
            startTime += delay;
            Thread.sleep(Math.max(0,startTime-System.currentTimeMillis()));
        } catch (InterruptedException e) {
           // break;
        }*/
    	//  }
        //return image;
      }
    //}

    public void paint(Graphics g) {
      //update(g);
    }

    public void update(Graphics g) {
      //.drawImage(offImage,0,0,0);
    }

    public static void disturb(int dx, int dy) {
    	System.out.println("disturb");
      for (int j=dy-riprad;j<dy+riprad;j++) {
        for (int k=dx-riprad;k<dx+riprad;k++) {
          if (j>=0 && j<height && k>=0 && k<width) {
	    ripplemap[oldind+(j*width)+k] += 512;            
          } 
        }
      }
    }

    public static long st,fi;
    public void newframe() {
    	st=System.currentTimeMillis();
      //Toggle maps each frame
      i=oldind;
      oldind=newind;
      newind=i;

      i=0;
      mapind=oldind;
      for (int y=0;y<height;y++) {
        for (int x=0;x<width;x++) {
	  short data = (short)((ripplemap[mapind-width]+ripplemap[mapind+width]+ripplemap[mapind-1]+ripplemap[mapind+1])>>1);
          data -= ripplemap[newind+i];
          data -= data >> 1;//5;//ripple decay
          ripplemap[newind+i]=data;

	  //where data=0 then still, where data>0 then wave
	  data = (short)(1024-data);

          //offsets
  	  a=((x-hwidth)*data/1024)+hwidth;
          b=((y-hheight)*data/1024)+hheight;

 	  //bounds check
          if (a>=width) a=width-1;
          if (a<0) a=0;
          if (b>=height) b=height-1;
          if (b<0) b=0;

          ripple[i]=texture[a+(b*width)];
          mapind++;
	  i++;
        }
      }
      fi=System.currentTimeMillis();
      //T._(""+(fi-st)+"ms");
    }

	

}
