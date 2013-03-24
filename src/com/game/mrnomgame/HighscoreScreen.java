package com.game.mrnomgame;

import java.util.List;

import com.game.mrnomgame.framework.Game;
import com.game.mrnomgame.framework.Graphics;
import com.game.mrnomgame.framework.Input.TouchEvent;
import com.game.mrnomgame.framework.Screen;

public class HighscoreScreen extends Screen {
	String lines[] = new String[5];
	
	public HighscoreScreen(Game game) {
		super(game);
		
		for (int i = 0; i < 5; ++i)
			lines[i] = "\t" + (i + 1) + ".\t" + Settings.highscores[i];
	}
	
	@Override
	public void update(float delta_time) {
		List<TouchEvent> touch_events = game.getInput().getTouchEvents();
		int              len          = touch_events.size();
		
		for (int i = 0; i < len; ++i) {
			TouchEvent event = touch_events.get(i);
			
			if (event.type == TouchEvent.TOUCH_UP &&
				event.x < 64 && event.y > 416) {
				if (Settings.sound_enabled)
					Assets.click.play(1);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}
	
	public void present(float delta_time) {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.main_menu, 64, 20, 0, 42, 196, 42);
		
		int y = 100;
		for (int i = 0; i < 5; ++i) {
			drawText(g, lines[i], 20, y);
			y += 50;
		}
		
		g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
	}
	
	public void drawText(Graphics g, String line, int x, int y) {
		int len = line.length();
		
		for (int i = 0; i < len; ++i) {
			char c = line.charAt(i);
			
			if (c == ' ') {
				x += 20;
				continue;
			}
			
			int src_x     = 0;
			int src_width = 0;
			if (c == '.') {
				src_x     = 200;
				src_width = 10;
			}
			else {
				src_x     = (c - '0') * 20;
				src_width = 20;
			}
			
			g.drawPixmap(Assets.numbers, x, y, src_x, 0, src_width, 32);
			x += src_width;
		}
	}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
}