package sonoroxadc.garethmurfin.co.uk;

//import sonoroxadc.garethmurfin.co.uk.R;
//import sonoroxfull.garethmurfin.co.uk.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//import sonorox.garethmurfin.co.uk.R;
import sonoroxadc.garethmurfin.co.uk.SonoroxSurfaceView.GameThread;

//import fullsonorox.garethmurfin.co.uk.R;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SonoroxActivity extends Activity 
{
	public static boolean DEMO=false;
    private static final int MENU_FILE = Menu.FIRST;
    private static final int MENU_LOAD = Menu.FIRST + 1;
    private static final int MENU_QUIT = Menu.FIRST + 2;
    private static final int MENU_CLEAR = Menu.FIRST + 3;
    private static final int MENU_HELP = Menu.FIRST + 4;
    private static final int MENU_DEMO = Menu.FIRST + 5;
    private static final int MENU_INSTRUMENT = Menu.FIRST + 5;
    private static final int MENU_VOTE = Menu.FIRST + 6;
   
    public static final String S_ =	"0000000000000000" +
    								"0000111111100000" +
    								"0000100000000000" +
    								"0000100000000000" +
    								"0000111111100000" +
    								"0000000000100000" +
    								"0000000000100000" +
    								"0000111111100000" +
    								"0000000000000000" +
    								"0000000000000000";
    public static final String O_ =	"0000000000000000" +
									"0000322222300000" +
									"0000200000200000" +
									"0000200000200000" +
									"0000200000200000" +
									"0000200000200000" +
									"0000200000200000" +
									"0000322222300000" +
									"0000000000000000" +
									"0000000000000000";
    public static final String N_ =	"0000000000000000" +
									"0000333333300000" +
									"0000300000300000" +
									"0000300000300000" +
									"0000300000300000" +
									"0000300000300000" +
									"0000300000300000" +
									"0000300000300000" +
									"0000000000000000" +
									"0000000000000000";
    public static final String R_ =	"0000000000000000" +
									"0000112121200000" +
									"0000200000300000" +
									"0000100000300000" +
									"0000233333000000" +
									"0000100000300000" +
									"0000200000300000" +
									"0000100000300000" +
									"0000000000000000" +
									"0000000000000000";
    public static final String X_ =	"0000000000000000" +
									"0000200000200000" +
									"0000030003000000" +
									"0000001010000000" +
									"0000000100000000" +
									"0000001010000000" +
									"0000030003000000" +
									"0000200000200000" +
									"0000000000000000" +
									"0000000000000000";
    
    boolean MENU_TEST=true;
    public static int lineCounter=0;

    /** A handle to the thread that's actually running the animation. */
    private GameThread mGameThread;
    /** A handle to the View in which the game is running. */
    private SonoroxSurfaceView mGameView;

    /**
     * Invoked during init to give the Activity a chance to set up its Menu.
     * 
     * @param menu the Menu to which entries may be added
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu_) {
        super.onCreateOptionsMenu(menu_);

        _("creat menu");
        menu=menu_;
        menu_.add(0, MENU_QUIT, 0, "QUIT");
        menu_.add(0, MENU_FILE, 0, "FILE");
        menu_.add(0, MENU_CLEAR, 0, "CLEAR");
        menu_.add(0, MENU_INSTRUMENT, 0, "INSTRUMENT");
        if (CAN_WE_VOTE)
    	{
    		_("ADD VOTE TO MENU.");
    		menu_.add(0, MENU_VOTE, 0, "VOTE UP!");
    		CAN_WE_VOTE=false;
    	}
    	else
    	{
    		_("VOTIGN NOT NEEDED");
    		//menu.removeItem(MENU_VOTE);
    	}
        //menu.add(0, MENU_LOAD, 0, "LOAD");
        //menu.add(0, MENU_HELP, 0, "HELP");
       // menu.add(0, MENU_DEMO, 0, "DEMO");
      //  menu.add(0, MENU_LOAD, 0, R.string.menu_resume);

        return true;
    }
    static Menu menu;
    public static final void _(String s)
    {
    	//System.out.println(s);
    }
    public void openMenu(){ 
    	_("OPEN MENU");
    	SonoroxActivity.doScroller=false;//so scroller goes off when they press
    	
    	
        this.getWindow().openPanel(Window.FEATURE_OPTIONS_PANEL, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MENU)); 
   }
   
    final int LOAD_SCREEN=1;
    final int SAVE_SCREEN=2;
    final static int EXTRA_SCREEN=3;
    final static int NORMAL_SCREEN=4;
	private static final long ONE_DAY = 86400000;
	
	private static final long THREE_DAYS = ONE_DAY*3;
    static int state=NORMAL_SCREEN;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        //_("onKeyDown");
      
        if (keyCode==KeyEvent.KEYCODE_MENU )
        {
        	_("OPEN MENU");
        	openMenu();   	//return true;
        }
        
        switch (state)
        {
        case LOAD_SCREEN:
        case SAVE_SCREEN:
	        if (keyCode == KeyEvent.KEYCODE_BACK )
	        {
	        	_("BACK PRESSED "+state);
	        	setContentView(R.layout.extra);
	        	prepareExtraScreen();
	        	
	        	state=EXTRA_SCREEN;
	        }
	        return true;
        case EXTRA_SCREEN:
        	if (keyCode == KeyEvent.KEYCODE_BACK )
	        {
	        	_("BACK PRESSED on extra");
	        	finish(); 
	        	state=NORMAL_SCREEN;
	        	startActivity(getIntent()); //
	        	//startAndViewThread();
	        	
	        	//setContentView(R.layout.main);
	        	
	        	//mGameThread.unpause();
	        	//mGameThread.restartStuff();
	        	//state=NORMAL_SCREEN;
	        	
	        	int threadcount = Thread.getAllStackTraces().size() ;
	        	_("THREADS IN ACTION: "+threadcount);
	        }
        	return true;
        case NORMAL_SCREEN:
        	if (keyCode == KeyEvent.KEYCODE_BACK )
	        {
	        	_("BACK PRESSED QUIT");
	        	finish();
	        	//setContentView(R.layout.main);
	        }
        	return true;
        }
       
		return      super.onKeyDown(keyCode, msg);
    }
   
    private void prepareExtraScreen() {
		// TODO Auto-generated method stub
    	goToLoadScreen();
    	gotoSaveScreen();
    	goToUpload();
    	goToDownloadScreen();
    	goToDemoScreen();
    	goToWebsite();
    	goToPartyModeScreen();
    	
    	BOIDS=false;
	}
	public static void clearMap()
    {
    	SonoroxSurfaceView.listOfSelected = new Vector();
    }
    /**
     * Invoked when the user selects an item from the Menu.
     * 
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     *         otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_QUIT:
                //mGameThread.doStart();
            	//SonoroxSurfaceView.listOfSelected = new Vector();
            	clearMap();
            	SLEEP(100);
            	finishUP();   
                return true;
            case MENU_INSTRUMENT:
            	mGameThread.cycleToNextInstrument();
            	//loadData(S,true);
            	_("CHANGE INSTRUMENT");
                 return true;
            case MENU_CLEAR:
               // mGameThread.setState(GameThread.STATE_LOSE);
            	clearMap();//SonoroxSurfaceView.listOfSelected = new Vector();
                return true;
            case MENU_FILE:
                //mGameThread.pause();
            	//saveData();
            	setContentView(R.layout.extra);
            	prepareExtraScreen();
            	
            	state=EXTRA_SCREEN;
                return true;
            case MENU_LOAD:
            	//loadData();
               // mGameThread.unpause();
                return true;
            case MENU_VOTE:
            	//loadData();
               // mGameThread.unpause();
            	_("VOTE PRESSED.");
            	setContentView(R.layout.extra);
            	prepareExtraScreen();
            	state=EXTRA_SCREEN;
            	//send to this screen to do networing this stops people
            	//constantly voting as the option will vanish til they redownload
            	//
            	  pleaseWait("Voting for "+DownloadFilesTask.lastTune);
	            	 // this is a bit lame as i pass them as urls they arent.
					networking.execute("VOTE",DownloadFilesTask.lastTune,DownloadFilesTask.lastArtist);
					
					_("threaded networking called.B");
					
					
					
					
					
					
                return true;
        }

        return false;
    }
    private String[] listFiles()
    {
    	
    	_("listFiles");
    	File f = new File(Environment.getExternalStorageDirectory(), "sonorox");// this.getFilesDir();
 		if (!f.exists())
 		{
 			f.mkdir();
 			f.setLastModified(System.currentTimeMillis());
 			
 		}
 		else
 		{
 			
 		}
    	File[] files = f.listFiles();
 		
    	String[] allfiles = new String[ files.length ];
 		for (int i=0; i<files.length;i++)
 		{
 			//if (files[i].getName().contains(".srox"))
 			_(""+files[i].getName());
 			allfiles[i]=files[i].getName();
 		}
 		_("found "+files.length+" files.");
 		
 		return allfiles;
    }
    
    public static boolean DEMO_EXPIRED=false;
    public static boolean debug;
    // fake means it reads from a string defined already, we pass in our string instead of a path
    String loadData(String file, boolean fake)
    {
    	clearMap();
    	
    	String returnme="";
    	 try { 
    		 String currentNow ="";
    		 if (!fake)
    		 {
	    		 FileReader fr = new FileReader(new File(Environment.getExternalStorageDirectory() + "/sonorox/" + file));//"sonorox.txt"));
	    		// File file = new File(Environment.getExternalStorageDirectory(), null);
	     		
	     		
	    		 char[] buf = new char[160];
	    		 int read = fr.read(buf);
	    		 fr.close();
	    		 currentNow = new String(buf, 0, read);
	    	 }
    		 else
    		 {
    			 currentNow=file;
    		 }
    		 _("READ: "+currentNow);
    		// SonoroxSurfaceView.listOfSelected = new Vector();
    		 //for (int y=0; y<10; y++)
 			//{
 			//	for (int x=0; x<16; x++)
 		    	//{
    		 int x=0;
    		 int y=0;
    		 if (debug)
    			 _("level:");
 					for (int i=0; i<160;i++)
 		    		 {
 						
 		    			 String s = currentNow.substring(i, i+1);
 		    			 
 		    			 if (s.equals("1"))
 		    			 {
 		    				 putHere(x,y,1);
 		    			 }
 		    			if (s.equals("2"))
		    			 {
		    				 putHere(x,y,2);
		    			 }
 		    			if (s.equals("3"))
		    			 {
		    				 putHere(x,y,3);
		    			 }
 		    			if (x==15)
		    			 {
 		    				//if (debug)
 		    				//	System.out.println(""+s);
		    				x=-1;
		    				y++;
		    			 }
		    			 else
		    			 {
		    				 //if (debug)
		    				//	 System.out.print(""+s);
		    			 }
 		    			 x++;
 		    			 
 		    			returnme+=s;
 		    		 }
 		    	//}
 			//}
 		    	
    		 
    		  //FileInputStream         inFile  = openFileInput("sonorox.txt"); 
    		  //InputStreamReader       isr     = new InputStreamReader(inFile); 
    		  //BufferedReader          in              = new BufferedReader(isr); 
    		  //String inLine = in.readLine(); 
    		 // _("READ: "+inLine);
    		  //inFile.close(); 
    	 }catch (IOException e) { 
    		 _("loading fucked "+e.getMessage());
    		 // showAlert("Warning", "Error Exception " + e.toString(), "ok",true);
    	 } 
    	 return returnme;
    }
    
    public static String FILE_ERROR="";
    private boolean saveData(String song, String artist)
    {
    	//CHEATSSSSS
    	if (artist.equalsIgnoreCase("amiga"))
    	{
    		debug=true;
    	}
    	if (artist.equalsIgnoreCase("amiga"))
    	{
    		BOIDS=true;
    	}
    	if (DEMO)
    	{
    		song="DEMO";
    		artist="DEMO";
    		// demo restriction is that song is always saved as DEMO by DEMO.
    		
    	}
    	
    	boolean success=true;
    	try {
    		
    		
    		 /////////MAKE DIRECTORY
    	    File f = new File(Environment.getExternalStorageDirectory(), "sonorox");// this.getFilesDir();
     		if (!f.exists())
     		{
     			f.mkdir();
     			_("SAVE DIRECTORY MADE");
     			//f.setLastModified(System.currentTimeMillis());
     			
     		}
     		/////////
    		
    	    File root = new File(Environment.getExternalStorageDirectory() + "/" + "sonorox/");//Environment.getExternalStorageDirectory();
    	    _("saving to: "+root.getCanonicalPath());
    	    
    	   
    	    
    	    if (root.canWrite()){
    	    	_("saving commenced");
    	    	song=song.trim();
    	    	artist=artist.trim();
    	    	song=song.replace(" ","_");
    	    	song=song.replace("(","_");
    	    	song=song.replace(")","_");
    	    	song=song.replace(":", "_");
    	    	artist=artist.replace(" ","_");
    	    	artist=artist.replace("(","_");
    	    	artist=artist.replace(")","_");
    	    	artist=artist.replace(":", "_");
    	    	
    	        File gpxfile = new File(root, song+"_(by_"+artist+").srox");
    	        FileWriter gpxwriter = new FileWriter(gpxfile);
    	        BufferedWriter out = new BufferedWriter(gpxwriter);
    	        //out.write("Hello world");
    	         	        
    	        for (int y=0; y<10; y++)
    			{
    				for (int x=0; x<16; x++)
    		    	{
    					int whatsHere=whatsHere(x,y);
    					if (whatsHere>0)
    					{
    						//System.out.print(""+whatsHere);
    						out.write(""+whatsHere);
    					}
    					else
    					{
    						//System.out.print("0");
    						out.write("0");
    					}
    		    		
    		    		if (x==15)
    		    		{
    		    			System.out.println(".");
    		    		}
    					//canvas.drawRect(x*TILEWIDTH, y*TILEWIDTH, (x*TILEWIDTH)+TILEWIDTH, (y*TILEHEIGHT)+TILEHEIGHT, paint);
    		    	}
    			}
    	        
    	        
    	        
    	        out.close();
    	    }
    	} catch (IOException e) {
    	    Log.e("arse", "Could not write file " + e.getMessage());
    	    FILE_ERROR=e.getMessage();
    	    success=false;
    	}
    	
    	
    	
		return success;
		/*for (int y=0; y<10; y++)
		{
			for (int x=0; x<16; x++)
	    	{
				int whatsHere=whatsHere(x,y);
				if (whatsHere>0)
				{
					System.out.print(""+whatsHere);
				}
				else
				{
					System.out.print("0");
				}
	    		
	    		if (x==15)
	    		{
	    			System.out.println(".");
	    		}
				//canvas.drawRect(x*TILEWIDTH, y*TILEWIDTH, (x*TILEWIDTH)+TILEWIDTH, (y*TILEHEIGHT)+TILEHEIGHT, paint);
	    	}
		}*/
		
	}
    
    
    static int whatsHere(int x, int y)
    {
    	Enumeration e = SonoroxSurfaceView.listOfSelected.elements();
		while (e.hasMoreElements())
		{
			Cell c = (Cell) e.nextElement();
			if (c.X==x && c.Y==y)
			{
				return c.MODE;
			}
		}
		return -1;
    }
    public static boolean BOIDS=false;
    public static void putHere(int x, int y, int mode)
    {
    	 SonoroxSurfaceView.listOfSelected.add(new Cell(x,y,mode));
    }
    DownloadFilesTask networking;
    /**
     * Invoked when the Activity is created.
     * 
     * @param savedInstanceState a Bundle containing state saved from a previous
     *        execution, or null if this is a new execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _("SONOROX BORN.");
        // turn off the window's title bar
      //  requestWindowFeature(Window.FEATURE_NO_TITLE); 
      //  int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN; 
      //  getWindow().setFlags(flags, flags); 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        //setContentView(R.layout.main);	
       
        // start tracing to "/sdcard/calc.trace"
       // Debug.startMethodTracing("sonoroxprofile");
        // ...
       
      
      /*  if (soundPool==null)
        {
        	initSounds();
        }
        SOUNDSREADY=true;
        state=NORMAL_SCREEN;
        */
        startAndViewThread();
        
        int threadcount = Thread.getAllStackTraces().size() ;
    	_("THREADS IN ACTION: "+threadcount);
        
     //   TextSwitcher ts =     (TextSwitcher) findViewById(R.id.TextSwitcher01);
        //ts.setText("SONOROX MESSAGE OF THE DAY: ");
        
     //  TextView tv =  (Button) findViewById(R.id.TextView01);
     //  tv.setText("diljbvi jnfbvicij jklmnkimj nui bvkg idfjgj ");
       // b.setText("ARSE");
        // tell system to use the layout defined in our XML file
      
    	
    	  
           //int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN; 
          // getWindow().setFlags(flags, flags); 
    	
    	networking = new DownloadFilesTask(this);
    	
    	
    }
    
    public void loadSoundStuff()
    {
    	 if (soundPool==null)
         {
         	initSounds();
         }
         SOUNDSREADY=true;
         state=NORMAL_SCREEN;
    }
    
    private void startAndViewThread() {
    	_("startAndViewThread");
		// TODO Auto-generated method stub
    	  setContentView(R.layout.main);
          state=NORMAL_SCREEN;
          // get handles to the LunarView from XML, and its LunarThread
          mGameView = (SonoroxSurfaceView) findViewById(R.id.game);
          mGameThread = mGameView.getThread();
 
          mGameThread.sonoroxActivity = this;
          
          // set up a new game
          mGameThread.setState(GameThread.STATE_READY);
          Log.w(this.getClass().getName(), "SIS is null");
         // SLEEP(100);
          // kick off the main paintin thread
          mGameThread.doStart();
          
	}
	private void goToUpload()
    {
    	final Context c=  this;
   	 Button upload = (Button)this.findViewById(R.id.Button04);
   	upload.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) { 
            	_("UPLOAD TUNE");
            	//upload();
            	setContentView(R.layout.uploadscreen);	
            	 
            	
            	
            	
            	 state=LOAD_SCREEN;
            	 
            	 final Spinner sp =  (Spinner)findViewById(R.id.Spinner01);
            	 ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(c,
            		        android.R.layout.simple_spinner_dropdown_item,
            		        listFiles());
            		    sp.setAdapter(spinnerArrayAdapter);
            		    
            		    
            		    
            		   Button loadfile = (Button)findViewById(R.id.loadfilebutton);
            	         loadfile.setOnClickListener(new OnClickListener() { 
            	             public void onClick(View v) { 
            	            	 String file=sp.getSelectedItem().toString();
	            	             _("UPLOAD THIS FILE:"+file);
	            	             //TextView tva = (TextView)findViewById(R.id.TextView01);
	            	            //	tva.setText("UPLOADING ("+file+") TO SONOROX.ALPHASOFTWARE.ORG PLEASE WAIT.");
	            	             //get vals ready to upload:
	            	             String name = file.substring(0,file.indexOf("_(by"));
	            	             _("NAME:"+name);
	            	             String artist = file.substring(file.indexOf("_(by")+5,file.indexOf(").srox"));
	            	             _("ARTIST:"+artist);
	            	             String tune = loadData(file,false);
	            	             _("TUNE:"+tune);
	            	             boolean success = true;
	            	             
	            	             if (DEMO)// && name.equals("DEMO") && artist.equals("DEMO"))
	            	             {
	            	            	 _("DONT BOTHER UPLOADING.");
	            	            	 TextView tv = (TextView)findViewById(R.id.TextView01);
	            	            	  tv.setText("Uploading is not available in the demo.");
	                               	 
	            	             }
	            	             else
	            	             {
	            	             
		            	             pleaseWait("Uploading "+file);
		            	            	 // this is a bit lame as i pass them as urls they arent.
										networking.execute("UPLOAD",name,artist,tune);
										
										_("threaded networking called.B");
	            	             }
									//afterUploading(true);
								///upload(name, artist,tune);
	            	            
            	             }
            	         });
            	
            }
        });
    }
    
	void afterUploading(boolean success) {
		 if (success)
         {
        	 // change the text below to say successfully uploaded.
        	 TextView tv = (TextView)findViewById(R.id.TextView01);
        	 tv.setText("Your tune was successfully uploaded to the community!");
        	 if (DEMO)
        	 {
        		 tv.setText("Sorry, you cannot name or upload tunes in demo version.");
        	 }
         }
         else
         {
        	 TextView tv = (TextView)findViewById(R.id.TextView01);
        	 if (LAST_REPONSE_FROM_SERVER.indexOf("Duplicate entry")!=-1)
        	 {
        		 tv.setText("Sorry you cannot upload an identical song if one already exists.");
    	         
        	 }
        	 else
        	 {
        		 tv.setText("Sorry, there has been a problem uploading your tune, the problem appears to be: "+LAST_REPONSE_FROM_SERVER);
    	         
        	 }
         }
	}
	
	void afterVoting(boolean success) {
		 if (success)
        {
       	 // change the text below to say successfully uploaded.
       	 Button b = (Button)findViewById(R.id.Button07);
       	 if (b!=null)
       		 b.setText("Thanks for voting up "+DownloadFilesTask.lastTune+"!");
        }
        else
        {
       	 _("errm problem voting up");
        }
	}
	
	public static int partyModeIndex;
	public static boolean PARTY_MODE;
	void afterParty(boolean success) {
		 if (success)
       {
			 PARTY_MODE=true;
			 BOIDS=false;
			 clearMap();
			 _("# party tracks: "+DownloadFilesTask.partyTracks.size());
			 String loadme = DownloadFilesTask.partyTracks.elementAt(partyModeIndex).toString();
			 _("party mode will begin with: "+loadme);
			 loadData(loadme,true);
       }
       else
       {
      	 _("errm problem with party");
       }
	}
	
    public static String LAST_REPONSE_FROM_SERVER;
    
    
    static String[] onlineFiles;
	private Progress_Dialog pd;
    
	public static String TASK="";
    Thread thread;
    private void pleaseWait(String message)
    {
    	_("PLEASE WAIT");
    	 pd = new Progress_Dialog (this, message);
    	 thread = new Thread(pd );
	     thread.start();
  
         
    }
    void stopWaiting()
    {
    	_("STOP WAITING");
    	pd.RUNNING=false;
    	networking = new DownloadFilesTask(this);
    	
    }
    
    private void goToPartyModeScreen()
    {
    	
    	final Context c=  this;
    	 Button partyButton = (Button)this.findViewById(R.id.Button06);
    	 partyButton.setOnClickListener(new OnClickListener() { 
             public void onClick(View v) { 
             	_("PARTY MODE PRESSED");
             	
             	  pleaseWait("Downloading tunes.");
	            	 // this is a bit lame as i pass them as urls they arent.
				networking.execute("PARTY","","");
				_("threaded networking called.DD");
             }
             });
         
         
         
        
    }
    
   
    
    private void goToLoadScreen()
    {
    	
    	final Context c=  this;
    	 Button loadbutt = (Button)this.findViewById(R.id.Button01);
         loadbutt.setOnClickListener(new OnClickListener() { 
             public void onClick(View v) { 
             	_("LOAD PRESSED");
             	//loadData();
             	
             	 setContentView(R.layout.loadscreen);	
             	 
             	 
             	
             	 
             	 state=LOAD_SCREEN;
             	 
             	 final Spinner sp =  (Spinner)findViewById(R.id.Spinner01);
             	 ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(c,
             		        android.R.layout.simple_spinner_dropdown_item,
             		        listFiles());
             		    sp.setAdapter(spinnerArrayAdapter);
             		    
             		    
             		    
             		   Button loadfile = (Button)findViewById(R.id.loadfilebutton);
             	         loadfile.setOnClickListener(new OnClickListener() { 
             	             public void onClick(View v) { 
             	            	 String file=sp.getSelectedItem().toString();
             	             _("LOAD THIS FILE:"+file);
             	            loadData(file,false);
             	             }
             	         });
             }
             });
         
         
         
        
    }
    
    private void goToWebsite()
    {
    	final Context c=  this;
      	 Button webb = (Button)this.findViewById(R.id.Button07);
      	webb.setOnClickListener(new OnClickListener() { 
               public void onClick(View v) { 
            	   _("VISIT URL PRESSED.");
            	   startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.alphasoftware.org/sonorox")));
               }
           });
    }
    
    public static boolean DEMOMODE;
    private void goToDemoScreen()
    {
    	final Context c=  this;
   	 Button loadbutt = (Button)this.findViewById(R.id.Button05);
        loadbutt.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) { 
        
            	
            	setContentView(R.layout.main);
            	
            	SonoroxActivity.lineCounter=2; // so intro is forced to end if there.
            	mGameThread.demoCounter=0;
            	clearMap();
            	finish(); 
	        	state=NORMAL_SCREEN;
	        	startActivity(getIntent()); //
	        	DEMOMODE=true;
            	_("DEMO PRESSED "+DEMOMODE);
            }

			
		  });
    }
    
    public void restartActivity()
    {
    	//finish(); 
    	state=NORMAL_SCREEN;
    	//startActivity(getIntent()); 
    }
    public static boolean CAN_WE_VOTE=false;
    private void goToDownloadScreen()
    {
    	
    	final Context c=  this;
    	 Button loadbutt = (Button)this.findViewById(R.id.Button03);
         loadbutt.setOnClickListener(new OnClickListener() { 
             public void onClick(View v) { 
             	_("DOWNLOAD PRESSED");
             	//loadData();
             	
             	 
             	 setContentView(R.layout.downloadscreen);	
             	 
             	 
             	 state=LOAD_SCREEN;
             	 
	             	 final Spinner sp =  (Spinner)findViewById(R.id.Spinner01);
	             	 ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(c,
	             		        android.R.layout.simple_spinner_dropdown_item,
	             		       networking.listOnlineFiles(null));
	             		    sp.setAdapter(spinnerArrayAdapter);
	             		    
	             		   Button downloadbutt = (Button)findViewById(R.id.downloadfilebutton);
	             		  downloadbutt.setOnClickListener(new OnClickListener() { 
		           	             public void onClick(View v) { 
		           	            	_("DOWNLOAD THIS SELECTED TUNE.. ");
		           	            	String selected = (String) sp.getSelectedItem();
		           	            	if (selected.indexOf("Empty, click button above to populate")==-1)
		           	            	{
		           	            		_("selected:"+selected);
			           	            	String tune=selected.substring(0,selected.indexOf(" by "));
			           	            	String artist=selected.substring(selected.indexOf(" by ")+4,selected.indexOf("rating:"));
			           	            	_("ARTIST:"+artist);
			           	            	_("TUNE:"+tune);
			           	            	clearMap();
			           	            	boolean success =true;//
			           	            	pleaseWait("Downloading "+tune);
											networking.execute("DOWNLOADTUNE",artist.trim(),tune.trim());
											_("threaded networking called.c");
											//afterDownloading(true,artist.trim(),tune.trim());
			           	            	//downloadThisTune(artist.trim(),tune.trim());
											CAN_WE_VOTE=true;
		           	            	}
		           	            	else
		           	            	{
		           	            		_("populate list first");
		           	            	}
		           	            	
		           	             }

								
		             		  });
	             		    
	             		   Button getlist = (Button)findViewById(R.id.Button01);
	             		  getlist.setOnClickListener(new OnClickListener() { 
	           	             public void onClick(View v) { 
	           	            	_("GET LIST OF TUNES FROM WEB!");
	           	            	pleaseWait("Downloading list");
	           	            	boolean success=true;
	           	            	/*boolean success =*/
									networking.execute("DOWNLOADLIST");
									
									/*while (pd.RUNNING)
									{
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									_("CALL AFTER DOWNLOADING LIST.");
									afterDownloadingList(true);*/
								//download();
	           	            	//stopWaiting();
	           	            	
									// now we call this from the networking class itself
								//	afterDownloadingList();
	           	            	_("threaded networking called.A");
	           	             
           	             }

							
           	         });
             		    
             		   /*Button loadfile = (Button)findViewById(R.id.loadfilebutton);
             	         loadfile.setOnClickListener(new OnClickListener() { 
             	             public void onClick(View v) { 
             	            	 String file=sp.getSelectedItem().toString();
             	             _("LOAD THIS FILE:"+file);
             	            loadData(file);
             	             }
             	         });*/
             }
             });
         
         
         
        
    }
    
    
    void afterDownloading(boolean success, String artist,String tune)
    {
    	if (success)
       	{
       	 TextView tv = (TextView)findViewById(R.id.TextView01);
       	 tv.setText("Successfully downloaded: "+tune+" by "+artist);
       	 
       	}
       	else
       	{
       		TextView tv = (TextView)findViewById(R.id.TextView01);
           	 tv.setText("There was a problem downloading the tune! "+LAST_REPONSE_FROM_SERVER);
           
       	}
    }
    void afterDownloadingList(boolean success) {
		// TODO Auto-generated method stub
    	final Context c=  this;
    	TextView tv = (TextView)findViewById(R.id.TextView01);
    	 final Spinner sp =  (Spinner)findViewById(R.id.Spinner01);
           if (success)
           {
        	   if (onlineFiles==null || onlineFiles.length==0)
        	   {
        		   onlineFiles = new String[1];
        		   onlineFiles[0]="No tunes yet! Make some!";
        	   }
          	 tv.setText("List downloaded successfully. Choose one and hit 'Download this tune'");
          	ArrayAdapter spinnerArrayAdapterB = new ArrayAdapter(c,
    		        android.R.layout.simple_spinner_dropdown_item,
    		       onlineFiles);
    		    sp.setAdapter(spinnerArrayAdapterB);
           }
           else
           {
          	 tv.setText("Error getting list from server. "+LAST_REPONSE_FROM_SERVER);
           }
	}
    
    private void gotoPleaseWaitScreen()
    {
    	//_("PLEASE WAIT.");
    	// setContentView(R.layout.pleasewait);	
    }
    
    private void gotoSaveScreen()
    {
    	 Button savebutt = (Button)this.findViewById(R.id.Button02);
         savebutt.setOnClickListener(new OnClickListener() { 
             public void onClick(View v) { 
             	_("SAVE PRESSED");
             	state=SAVE_SCREEN;
             	 setContentView(R.layout.savescreen);	
             	//saveData();
             	 
             	 
             	 ////
        ///////////
             	 Button ok = (Button)findViewById(R.id.okbutt);
                  ok.setOnClickListener(new OnClickListener() { 
                      public void onClick(View v) { 
                      	_("OK PRESSED");
                      	 //setContentView(R.layout.savescreen);
                      	EditText tunename = (EditText) findViewById(R.id.tunename);
                      	EditText songname = (EditText) findViewById(R.id.songname);
                      	boolean success = saveData(tunename.getText().toString(),songname.getText().toString());
                      	TextView tv = (TextView)findViewById(R.id.TextView02);
                      	if (success)
                      		tv.setText("Saved successfully in sd card/Sonorox/ "+tunename.getText().toString()+" by "+songname.getText().toString());
                      	else
                      		tv.setText("There was a problem saving the tune, the error was: "+FILE_ERROR);
                        
                      	if (DEMO)
                   	 {
                   		 tv.setText("Saved successfully. Sorry this is a demo version of Sonorox. In the demo you can only save one tune and it will be saved as DEMO_by_DEMO in Sonorox directory on SD Card.");
                   	 }
                      	 
                      } });
             	 ///////////
             	 ////
             	 
             	 
             	 
             	 
             }
             	 
             
             });
    }
    static boolean SOUNDSREADY;
    
    //////sound
    //SINE WAVE
    public static final int A1 = 9;
    public static final int A2 = 8;
    public static final int A3 = 7;
    public static final int A4 = 6;
    public static final int A5 = 5;
    public static final int A6 = 4;
    public static final int A7 = 3;
    public static final int A8 = 2;
    public static final int A9 = 1;
    public static final int A10 = 0;
    //GUITAR
    public static final int D1 = 10;
    public static final int D2 = 11;
    public static final int D3 = 12;
    public static final int D4 = 13;
    public static final int D5 = 14;
    public static final int D6 = 15;
    public static final int D7 = 16;
    public static final int D8 = 17;
    public static final int D9 = 18;
    public static final int D10 = 19;
    //DRUMS
    public static final int G1 = 20;
    public static final int G2 = 21;
    public static final int G3 = 22;
    public static final int G4 = 23;
    public static final int G5 = 24;
    public static final int G6 = 25;
    public static final int G7 = 26;
    public static final int G8 = 27;
    public static final int G9 = 28;
    public static final int G10 = 29;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;

    private void initSounds() {
    	_("LOAD SOUNDS");
    	if (soundPool!=null)
    	{
    		release();
    		mgr=null;
    	}
    //  if (mgr==null)
        {
         mgr = (AudioManager)getBaseContext().getSystemService(Context.AUDIO_SERVICE);
          streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
          streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
           volume = streamVolumeCurrent / streamVolumeMax;
        }
        
         soundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 0);
         soundPoolMap = new HashMap<Integer, Integer>();
         int soundsloaded=0;
         
         long now = System.currentTimeMillis();
         long after=System.currentTimeMillis();
         
         
         notRunning=true;//so a new thread will definitely be used for surface
         
         now = System.currentTimeMillis();soundPoolMap.put(A1, soundPool.load(getBaseContext(), R.raw.a1, 1));after=System.currentTimeMillis();_("("+(after-now)+")sLOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A2, soundPool.load(getBaseContext(), R.raw.a2, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A3, soundPool.load(getBaseContext(), R.raw.a3, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A4, soundPool.load(getBaseContext(), R.raw.a4, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A5, soundPool.load(getBaseContext(), R.raw.a5, 1));after=System.currentTimeMillis();_("("+(after-now)+")sLOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A6, soundPool.load(getBaseContext(), R.raw.a6, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A7, soundPool.load(getBaseContext(), R.raw.a7, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A8, soundPool.load(getBaseContext(), R.raw.a8, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A9, soundPool.load(getBaseContext(), R.raw.a9, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(A10, soundPool.load(getBaseContext(), R.raw.a10, 1));after=System.currentTimeMillis();_("("+(after-now)+")sLOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D1, soundPool.load(getBaseContext(), R.raw.d1, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D2, soundPool.load(getBaseContext(), R.raw.d2, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D3, soundPool.load(getBaseContext(), R.raw.d3, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D4, soundPool.load(getBaseContext(), R.raw.d4, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D5, soundPool.load(getBaseContext(), R.raw.d5, 1));after=System.currentTimeMillis();_("("+(after-now)+")sLOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D6, soundPool.load(getBaseContext(), R.raw.d6, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D7, soundPool.load(getBaseContext(), R.raw.d7, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D8, soundPool.load(getBaseContext(), R.raw.d8, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D9, soundPool.load(getBaseContext(), R.raw.d9, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(D10, soundPool.load(getBaseContext(), R.raw.d10, 1));after=System.currentTimeMillis();_("("+(after-now)+")sLOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G1, soundPool.load(getBaseContext(), R.raw.g1, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G2, soundPool.load(getBaseContext(), R.raw.g2, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G3, soundPool.load(getBaseContext(), R.raw.g3, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G4, soundPool.load(getBaseContext(), R.raw.g4, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G5, soundPool.load(getBaseContext(), R.raw.g5, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G6, soundPool.load(getBaseContext(), R.raw.g6, 1));after=System.currentTimeMillis();_("("+(after-now)+")sLOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G7, soundPool.load(getBaseContext(), R.raw.g7, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G8, soundPool.load(getBaseContext(), R.raw.g8, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G9, soundPool.load(getBaseContext(), R.raw.g9, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         now = System.currentTimeMillis();soundPoolMap.put(G10, soundPool.load(getBaseContext(), R.raw.g10, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);//SLEEP(50);
         
         //HACK ADD TEN MORE SO THE LAST ONES THAT FUCK UP ARENT NEEDED
        // now = System.currentTimeMillis();soundPoolMap.put(30, soundPool.load(getBaseContext(), R.raw.g1, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);
        // now = System.currentTimeMillis();soundPoolMap.put(31, soundPool.load(getBaseContext(), R.raw.g2, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);
        // now = System.currentTimeMillis();soundPoolMap.put(32, soundPool.load(getBaseContext(), R.raw.g3, 1));after=System.currentTimeMillis();_("("+(after-now)+")LOADING SOUND: "+soundsloaded++);
         /*soundPoolMap.put(33, soundPool.load(getBaseContext(), R.raw.g4, 1));
         soundPoolMap.put(34, soundPool.load(getBaseContext(), R.raw.g5, 1));
         soundPoolMap.put(35, soundPool.load(getBaseContext(), R.raw.g6, 1));
         soundPoolMap.put(36, soundPool.load(getBaseContext(), R.raw.g7, 1));
         soundPoolMap.put(37, soundPool.load(getBaseContext(), R.raw.g8, 1));
         soundPoolMap.put(38, soundPool.load(getBaseContext(), R.raw.g9, 1));
         soundPoolMap.put(39, soundPool.load(getBaseContext(), R.raw.g10, 1));*/
     	_("SOUNDS DONE.");
     	//for (int i=0;i<30;i++)
     	//	playSound(i);
    } 
    
    public void release() { 
    	
    	_("releasing pool");
		 soundPool.release(); 
		 soundPool=null;
		 soundPoolMap=null;
		 System.gc();
		 _("pool released");
} 
    public void SLEEP(long s)
    {
    	try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static boolean introscreen=true;//so we display our alpha softwar elogo first
	public static int splashCounter=0;
    public void finishUP()
    {
    	//RESET ALL VARS IN HERE FOR A FRESH RUN NEXT TIME
    	SOUNDSREADY=false;
    	_("Finishing up.");
    	 //mGameThread.setState(GameThread.STATE_KILL);
		 mGameThread.stopRunning();
		  SonoroxActivity.DEMOMODE =false;
		  splashCounter=0;
		  introscreen=true;
		  SonoroxActivity.lineCounter=0;
		  SonoroxActivity.doScroller=true;
		  scrollerCount=0;
		  SonoroxActivity.introscreen=true;
    	release();
    	
    	 // stop tracing
       // Debug.stopMethodTracing();
        
    	finish();
    }
    
    static AudioManager mgr;    
    static int streamVolumeCurrent;
    static int streamVolumeMax ; 
    static float volume;
	public static boolean notRunning=true;
	public static boolean doScroller;
	public static int scrollerCount;
    public static void playSound(int sound) {
        /* Updated: The next 4 lines calculate the current volume in a scale of 0.0 to 1.0 */
       if (!SOUNDSREADY)
       {
    	   _("SOUNDS STILL LOADING WILL NOT PLAY");
    	   return;
       }
        
        /* Play the sound with the correct volume */
       // soundPool.pl.play(soundPoolMap.get(sound), leftVolume, rightVolume, priority, loop, rate) //.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);     
       soundPool.play(soundPoolMap.get(sound), 1.0f, 1.0f, 1, 0, 1f);     
    }
    
    @Override
    public boolean onTrackballEvent(MotionEvent me) {
    	if (mGameView!=null && mGameView.getThread()!=null)
    		return mGameView.getThread().doTrackballEvent(me);
    	else
    		return false;
    }
    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        
       // if (!MENU_TEST)
        //	mGameView.getThread().pause(); // pause game when Activity pauses
        
        
    }
}
