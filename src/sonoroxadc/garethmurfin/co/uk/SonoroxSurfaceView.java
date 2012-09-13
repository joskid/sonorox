package sonoroxadc.garethmurfin.co.uk;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

//import fullsonorox.garethmurfin.co.uk.R;

//import sonorox.garethmurfin.co.uk.R;
import sonoroxadc.boids.Boid;
import sonoroxadc.boids.Flock;
import sonoroxadc.boids.Vector3D;

//import sonoroxfull.garethmurfin.co.uk.R;


//import sonoroxadc.garethmurfin.co.uk.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * View that draws, takes keystrokes, etc. for a simple LunarLander game.
 * 
 * Has a mode which RUNNING, PAUSED, etc. Has a x, y, dx, dy, ... capturing the
 * current ship physics. All x/y etc. are measured with (0,0) at the lower left.
 * updatePhysics() advances the physics based on realtime. draw() renders the
 * ship, and does an invalidate() to prompt another draw() as soon as possible
 * by the system.
 */
class SonoroxSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    
	class GameThread extends Thread {
        /*
         * State-tracking constants
         */
        public static final int STATE_LOSE 			 = 1;
        public static final int STATE_CLEAR_GRID	 = 2;
        public static final int STATE_READY 		 = 3;
        public static final int STATE_RUNNING 		 = 4;
        public static final int STATE_WIN 			 = 5;
		public static final int STATE_KILL 		 = 6;
		public static final int STATE_PAUSE 		 = 7;
        private float x;
        private float y;
        private static final int SPEED = 100;
        private boolean dRight;
        private boolean dLeft;
        private boolean dUp;
        private boolean dDown;
        private int mCanvasWidth;
        private int mCanvasHeight;
        private long mLastTime;
        private Bitmap mSnowflake,back2;
        private Bitmap grid, feroxhex,background,title,feroxcircle, fingerprint,splash,startscreen;
        public int WIDTH=getWidth();
        public int HEIGHT=getHeight();
        public static final String VERSION="1.0";
         /** Message handler used by thread to post stuff back to the SonoroxSurfaceView */
        private Handler mHandler;
        /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
        public int mMode;
        /** Indicate whether the surface has been created & is ready to draw */
        private boolean mRun = false;
        /** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;
        Context mContext;
        public Water water;
		
        public GameThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;
 
        	x = 10;
            y = 10;
              if (feroxhex==null)
            	 feroxhex=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.hex);
              if (background==null)
            	  background=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test);
              if (title==null)
            	  title=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.title);
              if (feroxcircle==null)
            	  feroxcircle=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.circle);
              if (fingerprint==null)
            	  fingerprint=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fprint);
              if (splash==null)
            	  splash=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.alphasoftwarecs);
              if (startscreen==null)
            	  startscreen=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.startscreen);
              if (back2==null)
            	  back2=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.back2);
            
              
              
                    }
        Flock flock;
        public int getHeightVal()
        {
        	return HEIGHT;	
        }
        public int getWidthVal()
        {
        	return WIDTH;	
        }
        ///////////////
        public static final int SINE=1;
        public static final int DRUMS=2;
        public static final int GUITAR=3;

       long trackballlastpressed;
        public boolean doTrackballEvent(MotionEvent event) {
        	
        	switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	_("Pressed in track ball");
            	if (System.currentTimeMillis()-trackballlastpressed<300)
            	{
            		_("DOUBLE PRESSED TRACK BALL");
            		trackball=!trackball;
            		return true;
            	}
            	trackballlastpressed=System.currentTimeMillis();
            	if (trackball)
            	{
	            	Cell c = new Cell(myX,myY,MODE);
		            addOrRemoveCell(c);
            	}
            	else
            	{
            		cycleToNextInstrument();
            		
            	}
            	return true;
            case MotionEvent.ACTION_UP:
            	_("released track ball");
            return true;
            case MotionEvent.ACTION_CANCEL:
            	_("cancel track ball");
            return true;
            case MotionEvent.ACTION_MOVE:
            	if (doADelay(2))
            	{
            		
            	}
            	else
            	{
            		return true;
            	}
            	boolean TRACK_BALL_CAN_BE_JOYPAD=true;
					if (TRACK_BALL_CAN_BE_JOYPAD)
            	{
            	
	                if (mContext != null) {
	                   float x = event.getX() * event.getXPrecision();
	                   float y = event.getY() * event.getYPrecision();
	                 _(x+" x "+y);
	                   if (x==-1 && y==-0.0)
	                   {
	                	   //LEFT
	                	   myX--;
	                	   if (myX<0)
	                		   myX=0;
	                   
	                	   	                	   
	                   }
	                   if (x==1 && y==-0.0)
	                   {
	                	   myX++;
	                	   if (myX>WIDTH/TILEWIDTH)
	                		   myX=(WIDTH/TILEWIDTH)-1;
	                	   //RIGHT
	                	  
	                	   
	                   }
	                   if (y==-1 && x==0.0)
	                   {
	                	   //UP
	                	   myY--;
	                	   if (myY<0)
	                		   myY=0;
	                   }
	                   if (y==1.0 && x==0.0)
	                   {
	                	   //DOWN
	                	   myY++;
	                	   if (myY>HEIGHT/TILEHEIGHT)
	                		   myY=(HEIGHT/TILEHEIGHT)-1;
	                   }
	                }
                    return true;
                }
            }
        	return true;
        }
        public void cycleToNextInstrument() {
        	_("CYCLE INSTRUMENTS");
			// TODO Auto-generated method stub
        	SonoroxSurfaceView.MODE++;
			if (SonoroxSurfaceView.MODE>3)
				SonoroxSurfaceView.MODE=1;
			
			modechange=50;
			
		}
		/**
         * Starts the game, setting parameters for the current difficulty.
         */
        public void doStart() {
            synchronized (mSurfaceHolder) {
            	// Initialize game here!
            	_("######################### GAME THREAD DO START");
                x = 10;
                y = 10;
            	
                mLastTime = System.currentTimeMillis() + 100;
                setState(STATE_RUNNING);
            }
        }

        /**
         * Pauses the physics update & animation.
         */
        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING) 
                {
                	setState(STATE_PAUSE);
                    _("PAUSE");
                }
            }
        }
        
        public void restartStuff()
        {
        	mMode = STATE_RUNNING;
        }

        Typeface font;
        @Override
        public void run() {
        	
        	if (paint==null)
        		paint = new Paint();
        		
        	
        	 font = Typeface.createFromAsset(getContext().getAssets(), "spaceage.ttf");
        	 paint.setTypeface(font);
        	 
       
        		
        		
        	   WIDTH=getWidth();
               HEIGHT=getHeight();
               _("size: "+WIDTH+" X "+ HEIGHT);
               
            while (mRun) {
            	
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    
                    if (flock==null && SonoroxActivity.BOIDS)
                    {
                    	  //flock
                        flock = new Flock();
                        // Add an initial set of boids into the system
                        for (int i = 0; i < 15; i++) {
                        	Vector3D v3d  = new Vector3D(getWidth()/2,getHeight()/2);
                        	Boid b = new Boid(v3d,2.0f,0.05f,c,paint);
                        	//_("ADDED BOID...");
                          flock.addBoid(b);
                        }

                    }
                    
                  //  paint.setTypeface(font);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING) 
                        {
                        	/*if (c==null)
                        	{
                        		//c = mSurfaceHolder.lockCanvas(null);
                        		_("NEW CANVAS MADE.");
                        	}*/
                        	updateGame(c);
                        	
                        }
                        else if (mMode == STATE_KILL)
                        {
                        	_("good bye cruel world.");
                        	mRun=false;
                        }
                        else
                        {
                        	_("not running "+mMode);
                        	mMode=STATE_RUNNING;
                        	//Thread.sleep(20);
                        }
                        Thread.sleep(1);
                    }
                } catch (Exception e) {
					// TODO Auto-generated catch block
                	_("##### massive fuck up!!    "+e.getMessage());
					e.printStackTrace();
				} finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            _("######################### GAME THREAD KILLED");
        }

        /**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         * 
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @see #setState(int, CharSequence)
         * @param mode one of the STATE_* constants
         */
        public void setState(int mode) {
            synchronized (mSurfaceHolder) {
                setState(mode, null);
            }
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @param mode one of the STATE_* constants
         * @param message string to add to screen or null
         */
        public void setState(int mode, CharSequence message) {
            synchronized (mSurfaceHolder) {
                mMode = mode;
            }
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
        	_("--setSurfaceSize");
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
            }
        }

        /**
         * Resumes from a pause.
         */
        public void unpause() {
            // Move the real time clock up to now
            synchronized (mSurfaceHolder) {
                mLastTime = System.currentTimeMillis() + 100;
            }
            _("BACK FROM PAUSE");
            setState(STATE_RUNNING);
        }

        /**
         * Handles a key-down event.
         * 
         * @param keyCode the key that was pressed
         * @param msg the original event object
         * @return true
         */
        
        boolean doKeyDown(int keyCode, KeyEvent msg) {
        	boolean handled = false;
            synchronized (mSurfaceHolder) {
            	if (keyCode == KeyEvent.KEYCODE_CAMERA)
            	{
            		
            		
            		cycleToNextInstrument();
					_("CAMERA KEY PRESSED."+SonoroxSurfaceView.MODE);
					return true;
            	}
            	if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            		dRight = true;
            		handled = true;
            	}
            	if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
            		dLeft = true;
            		handled = true;
            	}
            	if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
            		dUp = true;
            		handled = true;
            	}
            	if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
            		dDown = true;
            		handled = true;
            	}
                return handled;
            }
        }

        /**
         * Handles a key-up event.
         * 
         * @param keyCode the key that was pressed
         * @param msg the original event object
         * @return true if the key was handled and consumed, or else false
         */
        boolean doKeyUp(int keyCode, KeyEvent msg) {
        	boolean handled = false;
            synchronized (mSurfaceHolder) {
            	if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            		dRight = false;
            		handled = true;
            	}
            	if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
            		dLeft = false;
            		handled = true;
            	}
            	if (keyCode == KeyEvent.KEYCODE_DPAD_UP){
            		dUp = false;
            		handled = true;
            	}
            	if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
            		dDown = false;
            		handled = true;
            	}
                return handled;
            }
        }

        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        Paint paint;
    
    	boolean drawgrid=true;
    	private void drawGrid(Canvas canvas)
    	{
    		/*for (int y=0; y<getHeight(); y++)
        	{
        		for (int x=0; x<getWidth(); x++)
            	{
            		canvas.drawRect(x*TILEWIDTH, y*TILEWIDTH, (x*TILEWIDTH)+TILEWIDTH, (y*TILEHEIGHT)+TILEHEIGHT, paint);
            	}
        	}*/
    		for (int i=0; i<10; i++) 
    		{
    			canvas.drawLine(0, TILEHEIGHT*i, WIDTH, TILEHEIGHT*i, paint);
    		}
    		for (int i=0; i<16; i++) 
    		{
    			canvas.drawLine(TILEWIDTH*i, 0, TILEWIDTH*i, HEIGHT, paint);
    		}
    		//drawgrid=false;
    	}
    	int line;
    	String data;
    	boolean trackball;
    	int myX=8;//so it starts hidden
    	int myY=4;
    	//boolean debug=false;
		private int modechange=0;
		Graphics g;
		boolean disturbed=true;
		public  boolean waterOn=false;
		boolean repaintBackdrop=false;
		private int pauseMessage;
		
		public SonoroxActivity sonoroxActivity;
		private boolean doWORDS;
		//int lineCounter;
        private void doDraw(Canvas canvas) {
        	
        	if (SonoroxActivity.state==SonoroxActivity.EXTRA_SCREEN)
        	{
        		_("do no painting");
        		return;
        	}
        		
        	
            
        	if (canvas!=null)
        	{
        		paint.setTextSize(20);
	         //   if (1==1)
	           // 	return;
        		
        		if (SonoroxActivity.introscreen)
        		{
        			_("INTRO SCREEN.");
        			SonoroxActivity.splashCounter++;
        			
        			if (SonoroxActivity.splashCounter>3)
        			{
        				
        				// load sounds now so that our logo is on during the delay
        				sonoroxActivity.loadSoundStuff();
        				SonoroxActivity.doScroller=true;
        			}
        			else
        			{
        				_("DRAWING SPLASH (alpha)");
        				canvas.drawBitmap(splash,0,(HEIGHT/2)-splash.getHeight()/2, paint);
        			}
        			if (SonoroxActivity.splashCounter>5)
        			{
        				canvas.drawARGB(255, 0, 0, 0);
        				
        			}
        			if (SonoroxActivity.splashCounter>6)
        			{
        				
        				SonoroxActivity.introscreen=false;
        			}
        			return;
        		}
        		
        		if (SonoroxActivity.lineCounter>1 && !SonoroxActivity.BOIDS)
	        	{
        			// empty canvas
	        		canvas.drawARGB(255, 0, 0, 0);
	        		//drawWater(canvas);
	        	}
        		
	        	
	        	
	        	if (line==0 && SonoroxActivity.lineCounter==0)
	        	{
	        		_("logo");
	        		canvas.drawBitmap(title,0,(HEIGHT/2)-title.getHeight()/2, paint);
	        	}
	        	
	        	//data="";
	        	//canvas.drawBitmap(grid, 0, 0, paint);
	        	//canvas.drawBitmap(mSnowflake, x, y++, paint);
	        	//if (y>getHeight())
	        	//	y=0;
	        	paint.setColor(0xFFFFFF00);
	        	paint.setStyle(Paint.Style.FILL);
	        	//paint.setColor(0xffff0000 | 255);
	        	Enumeration e=listOfSelected.elements();
	        	//paint.setColor(0xFFFFFFFF);    
	        	while (e.hasMoreElements())
	        	{
	        		Cell c = (Cell)e.nextElement();
	        		//paint.setColor(0xf6ff00);
	        		
	        		switch (c.MODE)
	        		{
	        		case SINE:
	        			if (line==c.X*TILEWIDTH)// && line<=c.X*TILEWIDTH)
	            		{
	            			SonoroxActivity.playSound(c.Y);
	            		}
	        			canvas.drawRect(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT, paint);
	            		break;
	        		case DRUMS:
	        			if (line==c.X*TILEWIDTH)// && line<=c.X*TILEWIDTH)
	            		{
	            			SonoroxActivity.playSound(c.Y+10);
	            		}
	        		//	paint.setColor(0xFF0000);
	        			//canvas.drawArc(new RectF(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT),0, 360, false, paint);
	        			//paint.setColor(0xFFFF0000);
	        			canvas.drawBitmap(feroxcircle, c.X*TILEWIDTH, c.Y*TILEHEIGHT, paint);
	            		break;
	        		case GUITAR:
	        			if (line==c.X*TILEWIDTH)// && line<=c.X*TILEWIDTH)
	            		{
	            			SonoroxActivity.playSound(c.Y+20);
	            		}
	        			canvas.drawBitmap(feroxhex, c.X*TILEWIDTH, c.Y*TILEHEIGHT, paint);
	        			//canvas.drawPath(new Path(), paint);
	        			break;
	        		}
	        		//canvas.drawBitmap(feroxhex, c.X*TILEWIDTH, c.Y*TILEHEIGHT, paint);
	        		//canvas.drawArc(new RectF(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT),0, 360, false, paint);// .drawRect(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT, paint);
	        		//canvas.drawRect(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT, paint);
	        		//if (debug)
	        		//	data+=","+c.X+"x"+c.Y+"["+c.MODE+"]";
	        	}
	        	if (!SonoroxActivity.BOIDS) {
		        	//line
		        	canvas.drawRect(line,0,line+1,HEIGHT, paint);
		        	line+=5;   
	        	}
	        	if (SonoroxActivity.PARTY_MODE)
	        	{
	        		//SonoroxActivity.BOIDS=true;
	        		//line+=5;// dont draw line but keep it moving
	        		paint.setColor(0xFFff26e3);//48FF00);
	        		paint.setTextSize(13);
	        		//canvas.drawRect(0,WIDTH,HEIGHT-10,HEIGHT, paint);
	        		canvas.drawText(DownloadFilesTask.partyTracksInfo.elementAt(SonoroxActivity.partyModeIndex).toString(), 5,HEIGHT-7 , paint);
	        	}
	        	/*if (SonoroxActivity.DEMO_EXPIRED)
	        	{
	        		paint.setColor(0xFFff26e3);//48FF00);
	        		//canvas.drawRect(0,WIDTH,HEIGHT-10,HEIGHT, paint);
	        		canvas.drawText("DEMO EXPIRED. THANKS FOR SONOROXING", 5,HEIGHT-7 , paint);
	        	
	        	}*/
	        	paint.setColor(0xFFff26e3);//48FF00);
	        	//paint.setColor(0xFFFF0000);
	        	paint.setStyle(Paint.Style.STROKE);
	        	if (trackball)
	        	{
	        		drawGrid(canvas);
	        		paint.setColor(0xFFFFFF00);
	        		canvas.drawRect(myX*TILEWIDTH, myY*TILEHEIGHT, (myX*TILEWIDTH)+TILEWIDTH, (myY*TILEHEIGHT)+TILEHEIGHT, paint);
	        	}
	        	
	        	if (line>WIDTH)
	        	{
	        		line=0;
	        		SonoroxActivity.lineCounter++;
	        		
	        		if (SonoroxActivity.PARTY_MODE)
	        		{
	        			if (SonoroxActivity.lineCounter>3)
	        			{
	        				SonoroxActivity.lineCounter=2;
	        				_("new party track time");
	        			
	        				SonoroxActivity.partyModeIndex++;
	        				if (SonoroxActivity.partyModeIndex>=DownloadFilesTask.partyTracks.size())
	        				{
	        					_("PARTY MODE COMPLETE.");
	        					SonoroxActivity.PARTY_MODE=false;
	        					SonoroxActivity.clearMap();
	        					SonoroxActivity.BOIDS=true;
	        				}
	        				else
	        				{
		        				 String loadme = DownloadFilesTask.partyTracks.elementAt(SonoroxActivity.partyModeIndex).toString();
		        				 _("party mode will now load : "+loadme);
		        				 sonoroxActivity.loadData(loadme,true);
	        				}
	        			}
	        			
	        		}
	        		
	        		if (SonoroxActivity.lineCounter>100)//000)
	        		{
	        			SonoroxActivity.lineCounter=2;
	        			/*if (!touched)
	        			{
	        				// if the app is left idle for AGES it will say
	        				// touch me! one in every ten times it will say "gone for a cheesecake?"
	        				
	        				cheeseCakeX=WIDTH;//WIDTH*9;
	        				notTouchedInAGES=true;
	        				pauseMessage=getRand(1,10);
	        				_("NOT TOUCHED IN AGES.."+pauseMessage);
	        			}*/
	        			
	        		}
	        	}
	        	//
	        	if (SonoroxActivity.debug && SonoroxActivity.lineCounter>1)
	        	{
	        		//canvas.drawText(""+(line/TILEWIDTH)+":"+data, 5,10 , paint);// drawRect(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT, paint);
	        		//canvas.drawText(""+(line/TILEWIDTH)+":"+data+": "+fps()+" fps", 5,10 , paint);// drawRect(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT, paint);
	        		if (FPS_RUNNING_SLOW)
	        		{
	        			paint.setColor(0xffff0000);
	        			canvas.drawText(fps(), 5,HEIGHT-7 , paint);// drawRect(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT, paint);
	        		} 
	        		else
	        		{
	        			paint.setColor(0xffffe200);
	        			canvas.drawText(fps(), 5,HEIGHT-7 , paint);// drawRect(c.X*TILEWIDTH, c.Y*TILEHEIGHT, (c.X*TILEWIDTH)+TILEWIDTH, (c.Y*TILEHEIGHT)+TILEHEIGHT, paint);
	        		}
	        	}
	        	if (modechange>0)
	        	{
	        		modechange--;
		        	switch (MODE)
		        	{
		        	case SINE:
		        		canvas.drawText("SYNTH", 5,15 , paint);
		        		break;
		        	case GUITAR:
		        		canvas.drawText("GUITAR", 5,15 , paint);
		        		break;
		        	case DRUMS:
		        		canvas.drawText("DRUMS", 5,15 , paint);
		        		break;
		        	
		        	}
	        	}
	        	if (notTouchedInAGES)
	        	{
	        		
	        		
	        		if (cheeseCakeX>-2000)
	        		{
	        			paint.setTextSize(100);
		        		paint.setColor(0xFFff26e3);
		        		if (pauseMessage==5)
		        			canvas.drawText("Gone for a cheesecake?", (cheeseCakeX-=2),(HEIGHT/2)+50 , paint);
		        		else
		        			canvas.drawText("Touch me!", cheeseCakeX--,(HEIGHT/2)+50 , paint);
		        		paint.setTextSize(20);
	        		}
	        	}
	        	if (introCounter>0)
	        	{
	        		paint.setColor(0xFFFFFF00);
	        		introCounter--;
	        		
	        		//canvas.drawText("Sonorox", 5,10 , paint);
	        		//canvas.drawText(introtext, (WIDTH/2)-paint.measureText(introtext)/2,10 , paint);
	        	}
	        	
	        	//canvas.drawRect(cellx*TILEWIDTH, celly*TILEHEIGHT, (cellx*TILEWIDTH)+TILEWIDTH, (celly*TILEHEIGHT)+TILEHEIGHT, paint);
	        	//if (drawgrid)
	        	//	drawGrid(canvas);
	        	
		        if (SonoroxActivity.debug)
		        {
		        	 times[times_idx % NUM_HISTORY] = now;
		             ++ times_idx;
		        }
		        
		        if (SonoroxActivity.DEMOMODE && SonoroxActivity.lineCounter>1)
		        {
		        	//_("SONOROX DEMO.");
		        	drawDemo(canvas);
		        }
		       // if (!SonoroxActivity.DEMOMODE && RECORD_DEMO)
		       // {
		        //	drawDemo(canvas);
		       // 	//canvas.drawText(demoCounter+"",5,25 , paint);
		       // }
        	}
        	else
        	{
        		_("CANVAS IS "+canvas);
        	}
	        
        	if (/*line==0 &&*/ SonoroxActivity.lineCounter>1)
        	{
        		//
        		//_("SS");
        		//canvas.drawBitmap(startscreen,(WIDTH/2)-startscreen.getWidth()/2,(HEIGHT/2)-startscreen.getHeight()/2, paint);
        		
        		if (SonoroxActivity.doScroller)
        		{
	        		xTextScroller--;
	        		//_("xTextScroller:"+xTextScroller);
	        		//paint.setAntiAlias(true);
	        		paint.setColor(0xFFFFFF00);
	        		paint.setTextSize(25);
	        		//canvas.drawText("Sonorox v"+VERSION+" - www.alphasoftware.org/sonorox ", xTextScroller,HEIGHT-9,paint);
	        		//_("DRAWING TEXT");
	        			canvas.drawRect(-1, HEIGHT-25, WIDTH+2, HEIGHT+1, paint) ;//.drawARGB(255, 0, 0, 0);
	        			canvas.drawText("TO BEGIN TOUCH THE SCREEN.", xTextScroller,HEIGHT-25,paint);
	        			canvas.drawText("                                 TO SEE DEMO PRESS MENU KEY", xTextScroller,HEIGHT-7,paint);
	        		 
	        		if (xTextScroller<-WIDTH*2)
	        		{
	        			xTextScroller=WIDTH+200;
	        			SonoroxActivity.scrollerCount++;
	        			_("loop SCROLLER "+SonoroxActivity.scrollerCount);
	        			if (SonoroxActivity.scrollerCount> 2)//2)////2)
	        			{
	        				_("STOP SCROLLER "+SonoroxActivity.scrollerCount);
	        				SonoroxActivity.doScroller=false; //loop help twice then vanish
	        				doWORDS=true;
	        				wordsTimer=0;
	        				sonorox_demo_code=0;
	        				
	        			}
	        		}
        		}
        		else
        		{
        			//_("doScroller is false");
        		}
        	}
        	
        	// when its been idel it cycles thru the letters SONOROX
        	if (doWORDS)
        	{
        		wordsTimer++;
        		if (wordsTimer>100)
        		{
        			wordsTimer=0;
	        		switch (sonorox_demo_code)
					{
					case 0:
						sonoroxActivity.loadData(SonoroxActivity.S_,true); sonorox_demo_code++;
						break;
					case 1:
						sonoroxActivity.loadData(SonoroxActivity.O_,true); sonorox_demo_code++;
						break;
					case 2:
						sonoroxActivity.loadData(SonoroxActivity.N_,true); sonorox_demo_code++;
						break;
					case 3:
						sonoroxActivity.loadData(SonoroxActivity.O_,true); sonorox_demo_code++;
						break;
					case 4:
						sonoroxActivity.loadData(SonoroxActivity.R_,true); sonorox_demo_code++;
						break;
					case 5:
						sonoroxActivity.loadData(SonoroxActivity.O_,true); sonorox_demo_code++;
						break;
					case 6:
						sonoroxActivity.loadData(SonoroxActivity.X_,true); sonorox_demo_code++;
						break;
					case 7:
						sonoroxActivity.clearMap(); doWORDS=false;
						// display sonorox logo briefly then it will be left on screen
						//as repaitnign ifs turned off in this state
						//canvas.drawARGB(255, 0, 0, 0);
						SonoroxActivity.BOIDS=true;
						
						
						break;
					
					}
        		}
        	}
        	if (SonoroxActivity.BOIDS && boidsCounter<3  )
        	{
        		boidsCounter++;
        		canvas.drawARGB(255, 0, 0, 0);
        		//canvas.drawBitmap(back2,0,(HEIGHT/2)-back2.getHeight()/2, paint);
        		// so when it doesnt repaint last thign we see is a sonorox logo,
        		// not the end X of the sonorox tune logo 
        	}
        }
        int boidsCounter=0;
        int xTextScroller=WIDTH+200;
        int sonorox_demo_code=0;
        int sonorox_demo_code_counter=0;
        int wordsTimer=0;
       
        
        private void drawWater(Canvas canvas)
        {
        	if (waterOn)
        	{
	        	if (water==null)
	        	{
	        		g= new Graphics(canvas);
	        		water = new Water(background,g);
	        		
	        	}
           		water.newframe();
        	
	        	canvas.save(); 
	            canvas.scale(30f, 32f); 
	           
	            canvas.drawBitmap(water.ripple, 0, water.width, 0, 0, water.width, water.height, true, paint);
	            
	            canvas.restore(); 
        	}
        }
        private final Random randomizer = new Random();
        
        /**
         * A method to obtain a random number between a miniumum and maximum
         * range.
         * @param min The minimum number of the range
         * @param max The maximum number of the range
         * @return
         */
        public final int getRand(int min, int max) {
            int r = Math.abs(randomizer.nextInt());
            return (r % ((max - min + 1))) + min;
        }
        
        boolean notTouchedInAGES;
        int cheeseCakeX=WIDTH*9;
        public int demoCounter;
        String demotext;
        int yval=0;
        int fingerprintx=-100;
        public static final int DEMO_DELAY_TIME=300;
        int fingerprinty=HEIGHT/2;
        private void drawDemo(Canvas canvas) {
        	///////////////////////////////canvas.drawText(demoCounter+"",5,25 , paint);
			// TODO Auto-generated method stub
        	demoCounter++;
        	
        	//paint.setAntiAlias(true);
        	paint.setColor(0xFF00FFFC);
        	    
        	//PASTED FROM RECORD MODE
        	if (thread.demoCounter==324) { addOrRemoveCell(new Cell(7,8,1)); }
        	if (thread.demoCounter==404) { addOrRemoveCell(new Cell(11,8,1)); }
        	if (thread.demoCounter==486) { addOrRemoveCell(new Cell(13,6,1)); }
        	if (thread.demoCounter==556) { addOrRemoveCell(new Cell(15,3,1)); }
        	if (thread.demoCounter==699) { addOrRemoveCell(new Cell(3,7,1)); }
        	
        	if (thread.demoCounter==1077) { addOrRemoveCell(new Cell(14,1,2)); }
        	if (thread.demoCounter==1086) { addOrRemoveCell(new Cell(12,1,2)); }
        	if (thread.demoCounter==1095) { addOrRemoveCell(new Cell(10,2,2)); }
        	if (thread.demoCounter==1105) { addOrRemoveCell(new Cell(6,2,2)); }
        	if (thread.demoCounter==1113) { addOrRemoveCell(new Cell(2,2,2)); }
        	
        	if (thread.demoCounter==1493) { addOrRemoveCell(new Cell(8,1,3)); }
        	if (thread.demoCounter==1501) { addOrRemoveCell(new Cell(4,1,3)); }
        	if (thread.demoCounter==1637) { addOrRemoveCell(new Cell(12,2,3)); }
        	if (thread.demoCounter==1769) { addOrRemoveCell(new Cell(15,6,3)); }
        	 
        	
        	if (demoCounter<DEMO_DELAY_TIME)
        	{ //          XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        		demotext="SONOROX DEMONSTRATION"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="How to use Sonorox."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME && demoCounter<DEMO_DELAY_TIME*2)
        	{
        		demotext="Sonorox is a musical toy."; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="By touching the screen you can"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="create music."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*2 && demoCounter<DEMO_DELAY_TIME*3)
        	{
        		//        XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        		demotext="Touch anywhere and a symbol"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="appears. When the line hits it,"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="a sample plays."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*3 && demoCounter<DEMO_DELAY_TIME*4)
        	{
        		demotext="You can also swap instruments."; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="Pressing the trackball / menu"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="item cycles through them."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*4 && demoCounter<DEMO_DELAY_TIME*5)
        	{
        		//        XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        		demotext="It's a short loop, but you will"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="soon be making some great"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="little tunes!"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*5 && demoCounter<DEMO_DELAY_TIME*6)
        	{
        		demotext="Using the MENU key you can"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="access extra features like:"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		
        	      
        	}
        	if (demoCounter>DEMO_DELAY_TIME*6 && demoCounter<DEMO_DELAY_TIME*7)
        	{
        		yval=(HEIGHT/3);
        		//        XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        		demotext="Load/Save from SD Card."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="Up/Download from Sonorox site"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="View this demo."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="Start Sonorox Party Mode."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*7 && demoCounter<DEMO_DELAY_TIME*8)
        	{
        		demotext="Also double tap trackball for "; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="more precise controls."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*8 && demoCounter<DEMO_DELAY_TIME*9)
        	{
        		//        XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        		demotext="Now it's time for you to compose"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="some of your own tracks and "; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="upload them!"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*9 && demoCounter<DEMO_DELAY_TIME*10)
        	{
        		demotext="Also don't forget when you"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="download a tune you can"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="vote for it!"; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*10 && demoCounter<DEMO_DELAY_TIME*11)
        	{
        		demotext="Winning tunes will ship with"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="the next release of Sonorox."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*11 && demoCounter<DEMO_DELAY_TIME*12)
        	{
        		demotext="Have fun!"; yval=HEIGHT/2;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        		demotext="DEMO NOW COMPLETE."; yval+=25;
        		canvas.drawText(demotext, (WIDTH/2)-paint.measureText(demotext)/2,yval , paint);
        	}
        	if (demoCounter>DEMO_DELAY_TIME*12)
        	{
        		SonoroxActivity.DEMOMODE=false;
        		SonoroxActivity.clearMap();//clear demo tune
        		MODE=SINE;//reset to synth
        		_("----DEMO DONE----");
        	}
        	
        	
        	
        	
        	
        	
        	
        	
		}
        
        private void hackToSpeedUpAgain()
        {
        	_("INVOKIGN SPEED UP HACK----");
        	sonoroxActivity.restartActivity();
        	slowCounter=0;
        }
        
		// generates fps for debugging/optimising purposes..
        int NUM_HISTORY = 8;
         long times[] = new long[NUM_HISTORY];
         int times_idx;
         long now;
         String str;
         String msg;
         long fps;
         boolean FPS_RUNNING_SLOW;
         int slowCounter;
        public String fps() {
            
            now = System.currentTimeMillis();
            str = null;
            if (times_idx >= NUM_HISTORY) {
                long oldTime = times[times_idx % NUM_HISTORY];
                if (oldTime == now) {
                    // in case of divide-by-zero
                    oldTime = now - 1;
                }
                fps = ((long)1000 * (long)NUM_HISTORY) / (now - oldTime);
                if (times_idx % 20 == 0) {
                    str = fps + "";
                    if (fps<45)
                    {
                    	//_("XSLOW "+slowCounter);
                    	FPS_RUNNING_SLOW=true;
                    	/*if (!touched)
                    	{
                    		slowCounter++;
                    		_("SLOW "+slowCounter);
                    		if (slowCounter>10)
                    		{
                    			hackToSpeedUpAgain();	
                    		}
                    	}*/
                    }
                    else
                    {
                    	FPS_RUNNING_SLOW=false;
                    }
                }
            } else {
                if (times_idx % 20 == 0) {
                    str =  " wee";
                }
            }
            
            if (str==null)
                str=fps+"~";
            
            return str;
            
            
        }
        
        String introtext="T  O  U  C  H    M  E";
int introCounter=100;
        /**
         * Updates the game.
         */
        private void updateGame(Canvas c) {
        	//// <DoNotRemove>
            long now = System.currentTimeMillis();
            // Do nothing if mLastTime is in the future.
            // This allows the game-start to delay the start of the physics
            // by 100ms or whatever.
            if (mLastTime > now) 
            	return;
            double elapsed = (now - mLastTime) / 1000.0;
            mLastTime = now;
            //// </DoNotRemove>
            
            /*
             * Why use mLastTime, now and elapsed?
             * Well, because the frame rate isn't always constant, it could happen your normal frame rate is 25fps
             * then your char will walk at a steady pace, but when your frame rate drops to say 12fps, without elapsed
             * your character will only walk half as fast as at the 25fps frame rate. Elapsed lets you manage the slowdowns
             * and speedups!
             */

            /*if (dUp)
            	y -= elapsed * SPEED;
            if (dDown)
            	y += elapsed * SPEED;
            if (y < 0)
            	y = 0;
            else if (y >= mCanvasHeight - mSnowflake.getHeight())
            	y = mCanvasHeight - mSnowflake.getHeight();
            if (dLeft)
            	x -= elapsed * SPEED;
            if (dRight)
            	x += elapsed * SPEED;
            if (x < 0)
            	x = 0;
            else if (x >= mCanvasWidth - mSnowflake.getWidth())
            	x = mCanvasWidth - mSnowflake.getWidth();
            
            */
         ////////   playSamples();
            doDraw(c);
            if (SonoroxActivity.BOIDS && flock!=null)
            	flock.run();
        }
		public void stopRunning() {
			// TODO Auto-generated method stub
			mRun=false;
		}
    }
    
    /*public void playSamples()
    {
    	Enumeration e = listOfSelected.elements();
    	while (e.hasMoreElements())
    	{
    		Cell c = (Cell) e.nextElement();
    		
    	}
	}*/
    
 
    GameThread thread;
    SurfaceHolder holder;
    public SonoroxSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        _("SonoroxSurfaceView CREATED.");
     
       //if (holder==null)
       // {
         //register our interest in hearing about changes to our surface
         holder = getHolder();
        holder.addCallback(this);
       // }
        // create thread only; it's started in surfaceCreated()
        if (thread==null)
        {
	        _("MAKE A THREAD");
	        	thread = new GameThread(holder, context, new Handler() {
	        		
	            @Override
	            public void handleMessage(Message m) {
	            	// Use for pushing back messages.
	            }
	        });
        	//   setFocusable(true); // make sure we get key events
               
             //  setFocusableInTouchMode(true);//fixes bug of needing one keypress after a touch
               SonoroxActivity.state=SonoroxActivity.NORMAL_SCREEN;
        }

       // setFocusable(true); // make sure we get key events
        
      //  setFocusableInTouchMode(true);//fixes bug of needing one keypress after a touch
        setFocusable(true); // make sure we get key events
        
        setFocusableInTouchMode(true);//fixes bug of needing one keypress after a touch
        
         }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     * 
     * @return the animation thread
     */
    public GameThread getThread() {
        return thread;
    }

    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    }

    /**
     * Standard override for key-up. We actually care about these, so we can
     * turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        return thread.doKeyUp(keyCode, msg);
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
    	_("FOCUS CHANGE.");
        /*if (!hasWindowFocus)
        {
        	_("PAUSE");
        	thread.pause();
        }*/
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    
    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
    	//if (SonoroxActivity.notRunning)
    	//{
	        thread.setRunning(true);
	        thread.start();
	        SonoroxActivity.notRunning=false;
    	//}
    	//else
    	//{
    	//	_("NOT STARTING A NEW THREAD SINCE ONE EXISTS");
    	//	thread.restartStuff();
    	//}
    }
float touchX ;
public static final void _(String s)
{
	//System.out.println(s);
}
int delaytime;
private boolean doADelay(int delayfor)
{
	delaytime++;
	if (delaytime>delayfor)
	{
		delaytime=0;
		return true;
	}
	else
	{
		return false;
	}
}
	float touchY ;
	int action;
    int TILEWIDTH=30;
	int TILEHEIGHT=32;
	boolean touched;
	int cellx,celly;
	public static int MODE=1;
	boolean RECORD_DEMO=false;
	long whenPressed;
	
	@Override 
    public boolean onTouchEvent(MotionEvent ev) 
    { 
		if (SonoroxActivity.DEMOMODE)
		{
			// dont let them touch in demo mode.
			return true;
		}
		if (SonoroxActivity.introscreen)
		{
			_("INTROSCREEN FINISHED.");
			SonoroxActivity.introscreen=false;
			return true;
		}
		if (thread.waterOn)
		{
			thread.water.disturb( (int)(ev.getX()/30f), (int)(ev.getY()/32f));
			thread.disturbed=true;
		}
		if ( !(System.currentTimeMillis()-whenPressed >200))
		{
			return true;
		}
		whenPressed=System.currentTimeMillis();
		thread.notTouchedInAGES=false;
    	/*if (doADelay(6))
    	{
    		
    	}
    	else
    	{
    		return true;
    	}*/
    	
		
		touched=true;
		SonoroxActivity.doScroller=false;//so scroller goes off when they touch
		SonoroxActivity.BOIDS=false;
    	action = ev.getAction();
    	touchX = ev.getX();
    	touchY = ev.getY();
    	//_("JUST TOUCHED @ "+touchX+","+touchY);
    	
    	//work out which cell
    	cellx = (int)touchX/TILEWIDTH;
    	celly = (int)touchY/TILEHEIGHT;
    	//_("CELL "+cellx+" x "+celly);
    	
    	if (RECORD_DEMO)
    	{
    		_("if (thread.demoCounter=="+thread.demoCounter+") { addOrRemoveCell(new Cell("+cellx+","+celly+","+MODE+")); }");
    	}
    	Cell c = new Cell(cellx,celly,MODE);
    	addOrRemoveCell(c);
    	
    	
    	
    	return true;
    	//if (action==MotionEvent.ACTION_UP)
    }
	
	public void addOrRemoveCell(Cell c)
	{
		
		//_("CELL mode:"+MODE);
		Enumeration e = listOfSelected.elements();
    	boolean removedone=false;
    	while (e.hasMoreElements())
    	{
    		Cell tmp = (Cell) e.nextElement();
    		if (tmp.X==c.X && tmp.Y==c.Y)
    		{
    			//_("removed one!");
    			listOfSelected.remove(tmp);
    			removedone=true;
    		}
    		else
    		{
    			//_("no match:"+tmp.X+" x "+tmp.Y);
    		}
    		if (removedone)
    			break;
    	}	
    	if (!removedone)
    		listOfSelected.add(c);
	}
	
	public static Vector listOfSelected = new Vector();
    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        
    	_("surface destroyed");
    	
    	
    	boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        
    }
    
    
    
   
    
    
    
}



