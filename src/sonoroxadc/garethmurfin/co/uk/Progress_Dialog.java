package sonoroxadc.garethmurfin.co.uk;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;



	public class Progress_Dialog extends Activity implements Runnable {
		 
	    private ProgressDialog pd;
	    SonoroxActivity sonorox;
	    public Progress_Dialog(SonoroxActivity p,String message)
	    {
	    	pd = ProgressDialog.show(p, "Please wait...", message, true, false);
	    	   RUNNING=true;
	    	   sonorox=p;
		       
	    }
	    public void stopMe()
	    {
	    	RUNNING=false;
	    }
	    public boolean RUNNING=false;
	    public void run() {
	        //pi_string = Pi.computePi(800).toString();
	    	
	    	while (RUNNING)
	    	{
	    		try {
					Thread.sleep(30);
					//_("sleep");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	        handler.sendEmptyMessage(0);
	    }
	    public static final void _(String s)
	    {
	    	System.out.println(s);
	    }
	    private Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	            pd.dismiss();
	            //tv.setText(pi_string);
	            if (SonoroxActivity.TASK.equals("DOWNLOADLIST"))
	            	sonorox.afterDownloadingList(true);
	            if (SonoroxActivity.TASK.equals("DOWNLOAD"))
	            	sonorox.afterDownloading(true, DownloadFilesTask.lastArtist, DownloadFilesTask.lastTune);
	            if (SonoroxActivity.TASK.equals("UPLOAD"))
	            	sonorox.afterUploading(true);
	            if (SonoroxActivity.TASK.equals("VOTE"))
	            	sonorox.afterVoting(true);
	            if (SonoroxActivity.TASK.equals("PARTY"))
	            	sonorox.afterParty(true);
	        }
	    };
	 
	}


