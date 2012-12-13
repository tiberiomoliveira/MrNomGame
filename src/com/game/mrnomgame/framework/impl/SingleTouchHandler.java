package com.game.mrnomgame.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.game.mrnomgame.framework.Input.TouchEvent;
import com.game.mrnomgame.framework.Pool;
import com.game.mrnomgame.framework.Pool.PoolObjectFactory;

public class SingleTouchHandler implements TouchHandler {
	boolean          is_touched;
	int              touch_x;
	int              touch_y;
	Pool<TouchEvent> touch_event_pool;
	List<TouchEvent> touch_events        = new ArrayList<TouchEvent>();
	List<TouchEvent> touch_events_buffer = new ArrayList<TouchEvent>();
	float            scale_x;
	float            scale_y;
	
	public SingleTouchHandler(View view, float scale_x, float scale_y) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		
		touch_event_pool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		
		this.scale_x = scale_x;
		this.scale_y = scale_y;
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		synchronized (this) {
			TouchEvent touch_event = touch_event_pool.newObject();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_event.type = TouchEvent.TOUCH_DOWN;
				is_touched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touch_event.type = TouchEvent.TOUCH_DRAGGED;
				is_touched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touch_event.type = TouchEvent.TOUCH_UP;
				is_touched = false;
				break;
			}
			
			touch_event.x = touch_x = (int)(event.getX() * scale_x);
			touch_event.y = touch_y = (int)(event.getY() * scale_y);
			touch_events_buffer.add(touch_event);
			
			return true;
		}
	}
	
	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			return (pointer == 0) ? is_touched : false;
		}
	}
		
	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			return touch_x;
		}
	}
	
	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			return touch_y;
		}
	}
	
	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = touch_events.size();
			
			for (int i = 0; i < len; ++i)
				touch_event_pool.free(touch_events.get(i));
			
			touch_events.clear();
			touch_events.addAll(touch_events_buffer);
			touch_events_buffer.clear();
			
			return touch_events;
		}
	}
}