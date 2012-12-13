package com.game.mrnomgame.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.game.mrnomgame.framework.Audio;
import com.game.mrnomgame.framework.FileIO;
import com.game.mrnomgame.framework.Game;
import com.game.mrnomgame.framework.Graphics;
import com.game.mrnomgame.framework.Input;
import com.game.mrnomgame.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView render_view;
	Graphics              graphics;
	Audio                 audio;
	Input                 input;
	FileIO                file_io;
	Screen                screen;
	WakeLock              wake_lock;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		boolean is_landscape = getResources().getConfiguration().orientation ==
							   Configuration.ORIENTATION_LANDSCAPE;
		int     frame_buffer_width  = is_landscape ? 480 : 320;
		int     frame_buffer_height = is_landscape ? 320 : 420;
		Bitmap  frame_buffer = Bitmap.createBitmap(frame_buffer_width,
												   frame_buffer_height,
												   Config.RGB_565);
		float   scale_x = (float)frame_buffer_width /
						  getWindowManager().getDefaultDisplay().getWidth();
		float   scale_y = (float)frame_buffer_height /
						  getWindowManager().getDefaultDisplay().getHeight();
		
		render_view = new AndroidFastRenderView(this, frame_buffer);
		graphics    = new AndroidGraphics(getAssets(), frame_buffer);
		audio       = new AndroidAudio(this);
		input       = new AndroidInput(this, render_view, scale_x, scale_y);
		file_io     = new AndroidFileIO(this);
		screen      = getStartScreen();
		
		setContentView(render_view);
		
		wake_lock = ((PowerManager)getSystemService(Context.POWER_SERVICE)).
					newWakeLock(PowerManager.FULL_WAKE_LOCK, "GlGame");
	}
	
	@Override
	public void onResume() {
		super.onRestart();
		
		wake_lock.acquire();
		screen.resume();
		render_view.resume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		wake_lock.release();
		render_view.pause();
		screen.pause();
		
		if (isFinishing())
			screen.dispose();
	}
	
	@Override
	public Graphics getGraphics() {
		return graphics;
	}
	
	@Override
	public Audio getAudio() {
		return audio;
	}
	
	@Override
	public Input getInput() {
		return input;
	}
	
	@Override
	public FileIO getFileIO() {
		return file_io;
	}
	
	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null.");
		
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
	
	@Override
	public Screen getCurrentScreen() {
		return screen;
	}
}