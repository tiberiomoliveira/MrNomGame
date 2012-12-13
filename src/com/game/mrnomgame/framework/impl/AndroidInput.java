package com.game.mrnomgame.framework.impl;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.game.mrnomgame.framework.Input;

public class AndroidInput implements Input {
	AccelerometerHandler accel_handler;
	KeyboardHandler      key_handler;
	TouchHandler         touch_handler;
	
	@SuppressWarnings("deprecation")
	public AndroidInput(Context context, View view, float scale_x, float scale_y) {
		accel_handler = new AccelerometerHandler(context);
		key_handler   = new KeyboardHandler(view);
		touch_handler = (Integer.parseInt(VERSION.SDK) < 5) ?
						new SingleTouchHandler(view, scale_x, scale_y) :
						new MultiTouchHandler(view, scale_x, scale_y);
	}
	
	@Override
	public boolean isKeyPressed(int key_code) {
		return key_handler.isKeyPressed(key_code);
	}
	
	@Override
	public boolean isTouchDown(int pointer) {
		return touch_handler.isTouchDown(pointer);
	}	
	
	@Override
	public int getTouchX(int pointer) {
		return touch_handler.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		return touch_handler.getTouchY(pointer);
	}
	
	@Override
	public float getAccelX() {
		return accel_handler.getAccelX();
	}
	
	@Override
	public float getAccelY() {
		return accel_handler.getAccelY();
	}
	
	@Override
	public float getAccelZ() {
		return accel_handler.getAccelZ();
	}
	
	@Override
	public List<TouchEvent> getTouchEvents() {
		return touch_handler.getTouchEvents();
	}
	
	@Override
	public List<KeyEvent> getKeyEvents() {
		return key_handler.getKeyEvents();
	}
}