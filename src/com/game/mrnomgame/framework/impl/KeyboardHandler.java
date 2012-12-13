package com.game.mrnomgame.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

import com.game.mrnomgame.framework.Input.KeyEvent;
import com.game.mrnomgame.framework.Pool;
import com.game.mrnomgame.framework.Pool.PoolObjectFactory;

public class KeyboardHandler implements OnKeyListener {
	boolean[]      pressed_keys = new boolean[128];
	Pool<KeyEvent> key_event_pool;
	List<KeyEvent> key_events_buffer = new ArrayList<KeyEvent>();
	List<KeyEvent> key_events        = new ArrayList<KeyEvent>();
	
	public KeyboardHandler(View view) {
		PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
			@Override
			public KeyEvent createObject() {
				return new KeyEvent();
			}
		};
		
		key_event_pool = new Pool<KeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}
	
	@Override
	public boolean onKey(View view, int key_code, android.view.KeyEvent event) {
		if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
			return false;
		
		synchronized (this) {
			KeyEvent key_event = key_event_pool.newObject();
			key_event.keyCode = key_code;
			key_event.keyChar = (char) event.getUnicodeChar();
			
			if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				key_event.type = KeyEvent.KEY_DOWN;
				if (key_code > 0 && key_code < 127)
					pressed_keys[key_code] = true;
			}
			
			if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
				key_event.type = KeyEvent.KEY_UP;
				if (key_code > 0 && key_code < 127)
					pressed_keys[key_code] = false;
			}
			key_events_buffer.add(key_event);
		}
		
		return false;
	}
	
	public boolean isKeyPressed(int key_code) {
		if (key_code < 0 || key_code > 127)
			return false;
		return pressed_keys[key_code];
	}
	
	public List<KeyEvent> getKeyEvents() {
		synchronized (this) {
			int len = key_events.size();
			
			for (int i = 0; i < len; ++i)
				key_event_pool.free(key_events.get(i));
			
			key_events.clear();
			key_events.addAll(key_events_buffer);
			key_events_buffer.clear();
			
			return key_events;
		}
	}
}