package sonoroxadc.garethmurfin.co.uk;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

public class Graphics {
	// these need not to clash pal!
	public static final int UP=25;
	public static final int DOWN=26;
	public static final int LEFT=27;
	public static final int RIGHT=28;
	public static final int TOP=29;
	public static final int BOTTOM=30;
	public static final int HCENTER=77;
	public static final int VCENTER=78;
	
	public static final int KEY_NUM0=0;
	public static final int KEY_NUM1=1;
	public static final int KEY_NUM2=2;
	public static final int KEY_NUM3=3;
	public static final int KEY_NUM4=4;
	public static final int KEY_NUM5=5;
	public static final int KEY_NUM6=6;
	public static final int KEY_NUM7=7;
	public static final int KEY_NUM8=8;
	public static final int KEY_NUM9=9;
	public static final int FIRE=10;
	public static final int KEY_POUND=11;
	public static final int KEY_STAR=12;
	public Canvas canvas;
	public Paint paint ;
	Color color;
	int icolour;
	public Graphics(Canvas c)
	{
		//System.out.println("Creating Graphics()...");
		if (c==null)
		{
			System.out.println("FATAL ERROR IN CREATION OF GRAPHICS canvas is null");
		}
		else
		{
			//System.out.println("Graphics() created successfully.");
		}
		paint = new Paint();
		color = new Color();
		
		paint.setColor(0xFFFFFFFF);
		canvas=c;
		paint.setTextSize(21);
	}
	
	public void fillRoundRect(int a, int b, int c, int d, int e, int f)
	{
		System.out.println("fillRoundRect()");
	}
	public void drawRoundRect(int a, int b, int c, int d, int e, int f)
	{
		System.out.println("drawRoundRect()");
	}
	
	public void setColor(int r , int g, int b)
	{
		//System.out.println("setColor()");
		//int col = color.rgb(r, g, b);
		//paint.setColor(col);
	}
	public void setColor(int RGB) {
		//paint.setColor(0xff000000 | RGB);
		// TODO AndroidFont.paint cannot be static 
		//paint.setTextSize(8);
		//paint.setColor(0xff000000 | RGB);
		//paint.setColor(0xffff0000 | RGB);
	}
	public void drawString(String s, int x, int y, int position)
	{
		//paint.setColor(0xff000000 | 255);
		canvas.drawText(s, x,y, paint);
	}
	public void setClip(int x, int y, int w, int h)
	{
		//System.out.println("setClip()");
		canvas.clipRect(x,y,x+w,y+h,Region.Op.REPLACE);
	}
	public void fillRect(int x, int y, int w, int h)
	{
		//paint.setStyle(Paint.Style.FILL);//HACKDD
		//canvas.drawRect(x, y, x + w, y + h, paint);
	}
	public void drawRect(int x, int y, int w, int h)
	{
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(x, y, x + w, y + h, paint);
	}
	
	public int getColor()
	{
		return icolour;
	}
	
	Rect rect;
	public int getClipWidth()
	{
		rect = canvas.getClipBounds();
		return rect.width();
	}
	public int getClipHeight()
	{
		rect = canvas.getClipBounds();
		return rect.height();
	}
	public int getClipX()
	{
		rect = canvas.getClipBounds();
		return rect.left;
	}
	public int getClipY()
	{
		rect = canvas.getClipBounds();
		return rect.right;
	}
	public void drawRegion(Bitmap src, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor) {
        // may throw NullPointerException, this is ok
        if (x_src + width > src.getWidth() || y_src + height > src.getHeight() || width < 0 || height < 0 || x_src < 0
                || y_src < 0)
            throw new IllegalArgumentException("Area out of Image");

        // this cannot be done on the same image we are drawing
        // check this if the implementation of getGraphics change so
        // as to return different Graphic Objects on each call to
        // getGraphics
        ////if (src.isMutable() && src.getGraphics() == this)
         ////   throw new IllegalArgumentException("Image is source and target");

        Bitmap img=src;

       /* if (src.isMutable()) {
            img = ((AndroidMutableImage) src).getBitmap();
        } else {
            img = ((AndroidImmutableImage) src).getBitmap();
        }*/            

// TODO  
        if (anchor != 0) {
//        	Logger.debug("(todo) drawRegion: " + anchor);
        }
/*        java.awt.geom.AffineTransform t = new java.awt.geom.AffineTransform();

        int dW = width, dH = height;
        switch (transform) {
        case Sprite.TRANS_NONE: {
            break;
        }
        case Sprite.TRANS_ROT90: {
            t.translate((double) height, 0);
            t.rotate(Math.PI / 2);
            dW = height;
            dH = width;
            break;
        }
        case Sprite.TRANS_ROT180: {
            t.translate(width, height);
            t.rotate(Math.PI);
            break;
        }
        case Sprite.TRANS_ROT270: {
            t.translate(0, width);
            t.rotate(Math.PI * 3 / 2);
            dW = height;
            dH = width;
            break;
        }
        case Sprite.TRANS_MIRROR: {
            t.translate(width, 0);
            t.scale(-1, 1);
            break;
        }
        case Sprite.TRANS_MIRROR_ROT90: {
            t.translate((double) height, 0);
            t.rotate(Math.PI / 2);
            t.translate((double) width, 0);
            t.scale(-1, 1);
            dW = height;
            dH = width;
            break;
        }
        case Sprite.TRANS_MIRROR_ROT180: {
            t.translate(width, 0);
            t.scale(-1, 1);
            t.translate(width, height);
            t.rotate(Math.PI);
            break;
        }
        case Sprite.TRANS_MIRROR_ROT270: {
            t.rotate(Math.PI * 3 / 2);
            t.scale(-1, 1);
            dW = height;
            dH = width;
            break;
        }
        default:
            throw new IllegalArgumentException("Bad transform");
        }

        // process anchor and correct x and y _dest
        // vertical
        boolean badAnchor = false;

        if (anchor == 0) {
            anchor = TOP | LEFT;
        }

        if ((anchor & 0x7f) != anchor || (anchor & BASELINE) != 0)
            badAnchor = true;

        if ((anchor & TOP) != 0) {
            if ((anchor & (VCENTER | BOTTOM)) != 0)
                badAnchor = true;
        } else if ((anchor & BOTTOM) != 0) {
            if ((anchor & VCENTER) != 0)
                badAnchor = true;
            else {
                y_dst -= dH - 1;
            }
        } else if ((anchor & VCENTER) != 0) {
            y_dst -= (dH - 1) >>> 1;
        } else {
            // no vertical anchor
            badAnchor = true;
        }

        // horizontal
        if ((anchor & LEFT) != 0) {
            if ((anchor & (HCENTER | RIGHT)) != 0)
                badAnchor = true;
        } else if ((anchor & RIGHT) != 0) {
            if ((anchor & HCENTER) != 0)
                badAnchor = true;
            else {
                x_dst -= dW - 1;
            }
        } else if ((anchor & HCENTER) != 0) {
            x_dst -= (dW - 1) >>> 1;
        } else {
            // no horizontal anchor
            badAnchor = true;
        }

        if (badAnchor)
            throw new IllegalArgumentException("Bad Anchor");

        java.awt.geom.AffineTransform savedT = g.getTransform();

        g.translate(x_dst, y_dst);
        g.transform(t);*/

        Rect srcRect = new Rect(x_src, y_src, x_src + width, y_src + height);
        Rect dstRect = new Rect(x_dst, y_dst, x_dst + width, y_dst + height);
        canvas.drawBitmap(img, srcRect, dstRect, paint);

        // return to saved
// TODO        
//        g.setTransform(savedT);
	}

	public void clipRect(int x, int y, int w, int h)
	{
		System.out.println("clipRect!!()");
	}
	public void drawLine(int x, int y, int w, int h)
	{
		/*System.out.println("drawRect()");
		paint.setColor(icolour);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(x, y, w, h, paint);*/
		canvas.drawLine(x, y, w, h, paint);
	}
	public void drawImage(Bitmap b,int posx, int posy, int position)
	{
		//System.out.println("drawImage()");
		//if (canvas==null)
		//	System.out.println("canvas is null");
		//if (b==null)
		//	System.out.println("bitmap is null");
		
		if (b==null)
		{
			System.out.println("BITMAP WONT DRAW IT IS NULL YOU SILLY BAST.");
		}
		canvas.drawBitmap(b, posx, posy, paint);
	}
	public int getTranslateX()
	{
		System.out.println("getTranslateX()");
		return 0;
		
	}
	public int getTranslateY()
	{
		System.out.println("getTranslateY()");
		return 0;
		
	}
	public void translate(int x, int y)
	{
		System.out.println("translate()");
	}
	
	public void drawRGB(int[] rgbData, int offset, int scanlength, int x,
			int y, int width, int height, boolean processAlpha) {
        // this is less than ideal in terms of memory
        // but it's the easiest way

        if (rgbData == null)
            throw new NullPointerException();

        if (width == 0 || height == 0) {
            return;
        }

        int l = rgbData.length;

        if (width < 0 || height < 0 || offset < 0 || offset >= l || (scanlength < 0 && scanlength * (height - 1) < 0)
                || (scanlength >= 0 && scanlength * (height - 1) + width - 1 >= l))
            throw new ArrayIndexOutOfBoundsException();

        int[] rgb = new int[width * height];
        // this way we dont create yet another array in createImage
        int transparencyMask = processAlpha ? 0 : 0xff000000;
        for (int row = 0; row < height; row++) {
            for (int px = 0; px < width; px++)
                rgb[row * width + px] = rgbData[offset + px] | transparencyMask;
            offset += scanlength;
        }

        // help gc
        rgbData = null;
        Bitmap img = /*DeviceFactory.getDevice().getDeviceDisplay().*/createRGBImage(rgb, width, height, true);
        drawImage(img, x, y, TOP | LEFT);
	}

	public Bitmap createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
		if (rgb == null)
			throw new NullPointerException();
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException();
		
		// TODO processAlpha is not handled natively, check whether we need to create copy of rgb
		int[] newrgb = rgb;
		if (!processAlpha) {
			newrgb = new int[rgb.length];
			for (int i = 0; i < rgb.length; i++) {
				newrgb[i] = (0x00ffffff & rgb[i]) | 0xff000000;
			}
		}
		return Bitmap.createBitmap(newrgb, width, height, Bitmap.Config.ARGB_8888);
		//return new AndroidImmutableImage(Bitmap.createBitmap(newrgb, width, height, Bitmap.Config.ARGB_8888));
	}
	
	public void drawArc(int a, int b, int c, int d, int i, int j)
	{
		//T._("DRAWARC");
	}

	public void fillArc(int i, int j, int k, int l, int m, int n) {
		// TODO Auto-generated method stub
		
	}

	

	
}
