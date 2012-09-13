package sonoroxadc.garethmurfin.co.uk;

import android.graphics.Bitmap;

public class Image {

	public static Bitmap createImage(String p)
	{
		//T._("CREATE IMAGE NO IMPLEMENTATION");
		return null;
	}
	
	
	public static Bitmap createImage(int w, int h)
	{
		//T._("CREATE IMAGE "+w+","+h);
		Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		return b;
	}
	public static Graphics getGraphics()
	{
		//T._("getGraphics NO IMPLEMENTATION");
		return null;
	}
}
