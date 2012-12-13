package com.game.mrnomgame.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame      game;
	Bitmap           frame_buffer;
	Thread           render_thread = null;
	SurfaceHolder    holder;
	volatile boolean running = false;
	
	public AndroidFastRenderView(AndroidGame game, Bitmap frame_buffer) {
		super(game);
		
		this.game         = game;
		this.frame_buffer = frame_buffer;
		this.holder       = getHolder();
	}
	
	public void resume() {
		running       = true;
		render_thread = new Thread();
		render_thread.start();
	}
	
	public void run() {
		Rect dst_rect   = new Rect();
		long start_time = System.nanoTime();
		
		while (running) {
			if (!holder.getSurface().isValid())
				continue;
			
			float delta_time = (System.nanoTime() - start_time) / 1000000000.0f;
			start_time = System.nanoTime();
			
			game.getCurrentScreen().update(delta_time);
			game.getCurrentScreen().present(delta_time);
			
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dst_rect);
			canvas.drawBitmap(frame_buffer, null, dst_rect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void pause() {
		running = false;
		while (true) {
			try {
				render_thread.join();
				break;
			}
			catch (InterruptedException e) {
				// Retry
			}
		}
	}
}