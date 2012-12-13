package com.game.mrnomgame.framework;

public interface Graphics {
	public static enum PixmapFormat {
		ARGB8888,
		ARGB4444,
		RGB565
	}
	
	public Pixmap newPixmap (String filename, PixmapFormat format);
	
	public void clear(int color);
	public void drawPixel(int x, int y, int color);
	public void drawLine(int x_start, int y_start,
						 int x_end, int y_end,
						 int color);
	public void drawRect(int x, int y,
						 int width, int height,
						 int color);
	public void drawPixmap(Pixmap pixmap,
						   int x, int y,
						   int src_x, int src_y,
						   int src_width, int src_height);
	public void drawPixmap(Pixmap pixmap, int x, int y);
	
	public int getWidth();
	public int getHeight();
}