package com.game.mrnomgame.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.game.mrnomgame.framework.Input.TouchEvent;
import com.game.mrnomgame.framework.Pool;
import com.game.mrnomgame.framework.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler {
	private static final int MAX_TOUCHPOINTS = 10;
	
	boolean[]        is_touched = new boolean[MAX_TOUCHPOINTS];
	int[]            touch_x    = new int[MAX_TOUCHPOINTS];
	int[]            touch_y    = new int[MAX_TOUCHPOINTS];
	int[]            id         = new int[MAX_TOUCHPOINTS];
	Pool<TouchEvent> touch_event_pool;
	List<TouchEvent> touch_events        = new ArrayList<TouchEvent>();
	List<TouchEvent> touch_events_buffer = new ArrayList<TouchEvent>();
	float scale_x;
	float scale_y;
	
	public MultiTouchHandler(View view, float scale_x, float scale_y) {
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
			int action        = event.getAction() & MotionEvent.ACTION_MASK;
			int pointer_index = (event.getAction() &
								 MotionEvent.ACTION_POINTER_INDEX_MASK) >>
								MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			int pointer_count = event.getPointerCount();
			TouchEvent touch_event;
			
			for (int i = 0; i < MAX_TOUCHPOINTS; ++i) {
				if (i >= pointer_count) {
					is_touched[i] = false;
					id[i] = -1;
					continue;
				}
				
				int pointer_id = event.getPointerId(i);
				if (event.getAction() != MotionEvent.ACTION_MOVE &&
					i != pointer_index) {
					// If it's an up/down/cancel/out event, mask the id to see
					// if we should process it for this touch point
					continue;
				}
				
				switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					touch_event = touch_event_pool.newObject();
					touch_event.type = TouchEvent.TOUCH_DOWN;
					touch_event.pointer = pointer_id;
					touch_event.x = touch_x[i] = (int)(event.getX(i) * scale_x);
					touch_event.y = touch_y[i] = (int)(event.getY(i) * scale_y);
					is_touched[i] = true;
					id[i] = pointer_id;
					touch_events_buffer.add(touch_event);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
					touch_event = touch_event_pool.newObject();
					touch_event.type = TouchEvent.TOUCH_UP;
					touch_event.pointer = pointer_id;
					touch_event.x = touch_x[i] = (int)(event.getX(i) * scale_x);
					touch_event.y = touch_y[i] = (int)(event.getY(i) * scale_y);
					is_touched[i] = false;
					id[i] = -1;
					touch_events_buffer.add(touch_event);
					break;
				case MotionEvent.ACTION_MOVE:
					touch_event = touch_event_pool.newObject();
					touch_event.type = TouchEvent.TOUCH_DRAGGED;
					touch_event.pointer = pointer_id;
					touch_event.x = touch_x[i] = (int)(event.getX(i) * scale_x);
					touch_event.y = touch_y[i] = (int)(event.getY(i) * scale_y);
					is_touched[i] = true;
					id[i] = pointer_id;
					touch_events_buffer.add(touch_event);
					break;
				}
			}
			
			return true;
		}
	}
	
	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			int index = getIndex(pointer);
			return (index < 0 || index >= MAX_TOUCHPOINTS) ?
					false :
					is_touched[pointer];
		}
	}
	
	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			int index = getIndex(pointer);
			return (index < 0 || index >= MAX_TOUCHPOINTS) ?
					0 :
					touch_x[index];
		}
	}
	
	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			int index = getIndex(pointer);
			return (index < 0 || index >= MAX_TOUCHPOINTS) ?
					0 :
					touch_y[index];
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
	
	// Returns the index for a given pointer_id or -1 if no index.
	private int getIndex(int pointer_id) {
		for (int i = 0; i < MAX_TOUCHPOINTS; ++i)
			if (id[i] == pointer_id)
				return i;
		
		return -1;
	}
}