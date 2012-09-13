package sonoroxadc.garethmurfin.co.uk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
	
	SonoroxActivity sonorox;
	public DownloadFilesTask(SonoroxActivity s)
	{
		sonorox=s;
	}
	
    protected Long doInBackground(String... params) {
        //int count = urls.length;
        //long totalSize = 0;
        //for (int i = 0; i < count; i++) {
            //totalSize += Downloader.downloadFile(urls[i]);
            
        	//publishProgress((int) ((i / (float) count) * 100));
        //}
    	
    	if (params[0].equals("DOWNLOADLIST"))
    	{
    		_("NETWORKING WILL DOWNLOAD LIST");
    		boolean success = download();
    		//boolean success = downloadThisTune(params[1],params[2]);
    		_("SUCCESS IS "+success);
    		sonorox.stopWaiting();
    		sonorox.TASK="DOWNLOADLIST";
//sonorox.afterDownloadingList(success);
    		
    	} else
    	if (params[0].equals("UPLOAD"))
    	{
    		_("NETWORKING WILL UPLOAD TUNE");
    		boolean success = upload(params[1],params[2],params[3]);
    		_("SUCCESS IS "+success);
    		sonorox.stopWaiting();
    		sonorox.TASK="UPLOAD";
    	} else
    	if (params[0].equals("DOWNLOADTUNE"))
    	{
    		_("NETWORKING WILL DOWNLOAD TUNE");
    		//boolean success = download();
    		boolean success = downloadThisTune(params[1],params[2],false);
    		_("SUCCESS IS "+success);
    		sonorox.stopWaiting();
    		sonorox.TASK="DOWNLOAD";
    	} else
    		if (params[0].equals("VOTE"))
        	{
        		_("NETWORKING WILL VOTE FOR TUNE");
        		//boolean success = download();
        		boolean success = voteUpThisTune(params[1],params[2]);
        		_("SUCCESS IS "+success);
        		sonorox.stopWaiting();
        		sonorox.TASK="VOTE";
        	}else
        		if (params[0].equals("PARTY"))
            	{
            		_("NETWORKING WILL DOWNLOAD TOP TEN TUNES FOR PARTYING");
            		
            		partyTracks=new Vector();
            		partyTracksInfo = new Vector();
            		sonorox.partyModeIndex=0;
            		
            		//////////get list
            		_("GET LIST");
            		boolean success = download();//voteUpThisTune(params[1],params[2]);
            		_("SUCCESS IS "+success);
            		for (int i=0; i< SonoroxActivity.onlineFiles.length; i++)
            		{
            			_(i+". "+SonoroxActivity.onlineFiles[i]);
            			String tune=SonoroxActivity.onlineFiles[i].substring(0,SonoroxActivity.onlineFiles[i].indexOf(" by "));
       	            	String artist=SonoroxActivity.onlineFiles[i].substring(SonoroxActivity.onlineFiles[i].indexOf(" by ")+4,SonoroxActivity.onlineFiles[i].indexOf("rating:"));
       	            	_("get this tune");
       	            	_("ARTIST:"+artist);
       	            	_("TUNE:"+tune);
       	            	partyTracksInfo.add(SonoroxActivity.onlineFiles[i]);
       	            	downloadThisTune(artist,tune,true);
            		}
            		_("amount of tracks downloaded for party mode:"+partyTracks.size());
            		//// get each tune
            		//_("GET EACH TUNE");
            		//success = downloadThisTune(params[1],params[2],true);
            		_("SUCCESS IS "+success);
            		
            		sonorox.stopWaiting();
            		
            		
            		sonorox.TASK="PARTY";
            	}
    		else
    	{
    		_("ERROR NETWORKING RECEIVED ---> "+params[0]);
    	}
    	
        //download();
        return 1000L;//totalSize;
    }

   

	public static final void _(String s)
    {
    	//System.out.println(s);
    }
    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    	_("PROGRESS:"+progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    	_("COMPLETE.");
    }
    
    private boolean download()
    {
    	
    	    	boolean success=false;
    	    	//////////////
    	//////////////
    	    	HttpClient httpclient = new DefaultHttpClient();
    	    	HttpGet httpget = new HttpGet(URL+"viewtunes.php?password=formosa");
    	    	try {
    				HttpResponse response = httpclient.execute(httpget);
    						
    				String printme = getResponse(response.getEntity());
    				_("RESPONSE: "+printme);
    				SonoroxActivity.LAST_REPONSE_FROM_SERVER=printme;
    				if (printme.indexOf("Tune name")!=-1)
    		        {
    		        	_("DOWNLOADED LIST SUCCESSFULLY");
    		        	success=true;
    		        	SonoroxActivity.onlineFiles=listOnlineFiles(printme);
    		        }
    		        else
    		        {
    		        	_("PROBLEM IN DOWNLOADING LIST!");
    		        }
    			} catch (ClientProtocolException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				_("DOWNLOAD PROBLEM");
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				_("exception in download "+e.getMessage());
    			}
    	    	//////////////
    	    	//////////////
    			return success;
    	    	
    	    
		
    	
    }
    
    
    ///VOTE
    private boolean voteUpThisTune(String artist,String tune) {
    	
    	//lastArtist=artist;
    	//lastTune = tune;
    	boolean success=false;
		HttpClient httpclient = new DefaultHttpClient();
    	
    	String myurl=URL+"rate.php?artist="+tune+"&tunename="+artist;
		
		_("url is: "+myurl);
    	HttpGet httpget = new HttpGet(myurl);
    	String printme="";
    	try {
			HttpResponse response = httpclient.execute(httpget);
	
			
			 printme = getResponse(response.getEntity());
			_("RESPONSE: "+printme);
			
			
			SonoroxActivity.LAST_REPONSE_FROM_SERVER=printme;
    	}catch(Exception e)
    	{
    		_("ERROR getting tune ! "+e.getMessage());
    	}
    	
			success=true;
		
    	
    	return success;
		
	}
    
    
    public static String URL="http://www.alphasoftware.org/sonorox/";
    public static String lastArtist;
    public static String lastTune;
    private boolean downloadThisTune(String artist,String tune, boolean partyMode) {
    	artist=artist.trim();
    	tune=tune.trim();
    	
    	lastArtist=artist;
    	lastTune = tune;
    	boolean success=false;
    	HttpClient httpclient = new DefaultHttpClient();
    	String myurl=URL+"selecttune.php?artist="+artist+"&tunename="+tune;
    	myurl=myurl.trim();
    	
		_("url is: ["+myurl+"]");
    	HttpGet httpget = new HttpGet(myurl);
    	String printme="";
    	try {
			HttpResponse response = httpclient.execute(httpget);
	
			
			 printme = getResponse(response.getEntity());
			_("RESPONSE: "+printme);
			if (printme.contains("<BR>"))
			{
				printme=printme.substring(printme.indexOf("<BR>")+4, printme.length());
				_("ADJUSTED RESPONSE: "+printme);
			}
			if (printme.contains("<BR>"))
			{
				printme=printme.substring(0, printme.length()-4);
				printme.trim();
				_("ADJUSTED RESPONSE2: "+printme);
			}
			
			SonoroxActivity.LAST_REPONSE_FROM_SERVER=printme;
    	}catch(Exception e)
    	{
    		_("ERROR getting tune ! "+e.getMessage());
    	}
    	 int x=0;
		 int y=0;
		 String loaded="";
    	for (int i=0; i<160;i++)
		 {
			 String s = printme.substring(i, i+1);
			// System.out.print(""+s);
			 //_("["+x+"]");
			 if (s.equals("1"))
			 {
				 sonorox.putHere(x,y,1);
			 }
			 if (s.equals("2"))
			 {
				 sonorox.putHere(x,y,2);
			 }
			 if (s.equals("3"))
			 {
				 sonorox.putHere(x,y,3);
			 }
			if (x==15)
			 {
				//if (sonorox.debug)
				{
					//System.out.println("."+s);
				}
				x=-1;
				y++;
			 }
			 else
			 {
				 if (sonorox.debug)
				 {
					 //System.out.print(""+s);
				 }
			 }
			 x++;
			 
			loaded+=s;
			success=true;
		 }
    	_("loaded: "+loaded);
    	if (partyMode)
    	{
    		_("ADDED TUNE TO PARTY TRACKS.");
    		partyTracks.add(loaded);
    	}
    	return success;
		
	}
    
    public static Vector partyTracks = new Vector();
    public static Vector partyTracksInfo = new Vector();
    static String[] listOnlineFiles(String list) {
    	    	
    	if (list==null)
    	{
    		return new String[] {"Empty, click button above to populate"};
    	}
    	else
    	{
    		/*
    		 * sample:
    		    <BR>
				Tune
				name
				:
				TWATSONG
				by
				GAZ
				rating:
    		 * */
    		String row="";
    		StringTokenizer st = new StringTokenizer(list);
    		Vector rows = new Vector(1000);
    		 while (st.hasMoreTokens()) {
    			 
    		     String toke = st.nextToken();
    		     //_("toke: "+toke);
    		     if (toke.equals("<BR>"))
    		     {
    		    	 if (row.length()<3)
    		    	 {
    		    		 //forget it - its the first line which is blank
    		    	 }
    		    	 else
    		    	 {
	    		    	 row=row.substring(row.indexOf("Tune name : ")+12, row.length());
	    		    	 _("ROW:"+row);
	    		    	 rows.add(row);//add to our collection
	    		    	 row="";
    		    	 }
    		     }
    		     else
    		     {
    		    	 row+=" "+toke;
    		     }
    		     //
    		 }
    		
    		 String[] downloadedList = new String[rows.size()];
    		 for (int i=0; i<rows.size();i++)
    		 {
    			 downloadedList[i]=(String) rows.elementAt(i);
    		 }
    		return downloadedList;
    	}
    }
    private String getResponse( HttpEntity entity )
    {
      String response = "";
     
      try
      {
     
         response=convertStreamToString(entity.getContent());
        //_("response: "+responsestr);
      
        
        
      } catch ( IOException ioe ) {
        ioe.printStackTrace();
        _("IOE ERRRRROR");
      }

      return response;
    }
    
    private String convertStreamToString (InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8*1024);
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();

    }
    
    private boolean upload(String songname, String artist, String tune)
    {
    
    	boolean success=false;
    	//////////////
    	_("UPLOADING THIS TUNE:"+tune);
//////////////
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpGet httpget = new HttpGet(URL+"upload.php?artist="+artist+"&tunename="+songname+"&tune="+tune);
    	try {
			HttpResponse response = httpclient.execute(httpget);
	
			
			String printme = getResponse(response.getEntity());
			_("RESPONSE: "+printme);
			SonoroxActivity.LAST_REPONSE_FROM_SERVER=printme;
			if (printme.indexOf("tune uploaded!")!=-1)
	        {
	        	_("UPLOADED SUCCESSFULLY");
	        	success=true;
	        }
	        else
	        {
	        	_("PROBLEM IN UPLOADING!");
	        }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_("ERROR IN HERE.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_("exception in download "+e.getMessage());
		}
    	//////////////
    	//////////////
		return success;
    	
    }
    
    
}