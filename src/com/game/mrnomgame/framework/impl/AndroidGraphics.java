package com.game.mrnomgame.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.game.mrnomgame.framework.Graphics;
import com.game.mrnomgame.framework.Pixmap;

public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap       frame_buffer;
	Canvas       canvas;
	Paint        paint;
	Rect         src_rect = new Rect();
	Rect         dst_rect = new Rect();
	
	public AndroidGraphics (AssetManager assets, Bitmap frame_buffer) {
		this.assets       = assets;
		this.frame_buffer = frame_buffer;
		this.canvas       = new Canvas(frame_buffer);
		this.paint        = new Paint();
	}
	
	@Override
	public Pixmap newPixmap(String filename, PixmapFormat format) {
		Config      config  = null;
		Options     options = new Options();
		InputStream is      = null;
		Bitmap      bitmap  = null;
		
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;
		
		options.inPreferredConfig = config;
		
		try {
			is     = assets.open(filename);
			bitmap = BitmapFactory.decodeStream(is);
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '" +
										   filename + "'");
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '" +
									   filename + "'");
		}
		finally {
			if (is != null) {
				try { is.close(); }
				catch (IOException e) {}
			}
		}
		
		if (bitmap.getConfig() == Config.RGB_565) 
			format = PixmapFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else
			format = PixmapFormat.ARGB8888;
		
		return new AndroidPixmap(bitmap, format);
	}
	
	@Override
	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16,
					   (color & 0x00ff00) >> 8, 
					   (color & 0x0000ff));
	}
	
	@Override
	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}
	
	@Override
	public void drawLine(int start_x, int start_y,
						 int stop_x, int stop_y,
						 int color) {
		paint.setColor(color);
		canvas.drawLine(start_x, start_y, stop_x, stop_y, paint);
	}
	
	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y,
						x + width - 1, y + height - 1,
						paint);
	}
	
	@Override
	public void drawPixmap(Pixmap pixmap,
						   int dst_x, int dst_y,
						   int src_x, int src_y,
						   int width, int height) {
		src_rect.left   = src_x;
		src_rect.top    = src_y;
		src_rect.right  = src_x + width - 1;
		src_rect.bottom = src_y + height - 1;
		
		dst_rect.left   = dst_x;
		dst_rect.top    = dst_y;
		dst_rect.right  = dst_x + width - 1;
		dst_rect.bottom = dst_y + height - 1;
		
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap,
						  src_rect, dst_rect, null);
	}
	
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y) {
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
	}
	
	@Override
	public int getWidth() {
		return frame_buffer.getWidth();
	}
	
	@Override
	public int getHeight() {
		return frame_buffer.getHeight();
	}
}