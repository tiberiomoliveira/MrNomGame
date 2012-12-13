package com.game.mrnomgame;

import java.util.List;

import com.game.mrnomgame.framework.Game;
import com.game.mrnomgame.framework.Graphics;
import com.game.mrnomgame.framework.Input.TouchEvent;
import com.game.mrnomgame.framework.Screen;

public class HelpScreen3 extends Screen {
	public HelpScreen3(Game game) {
		super(game);
	}
	
	@Override
	public void update(float delta_time) {
		List<TouchEvent> touch_events = game.getInput().getTouchEvents();
		int              len          = touch_events.size();
		
		for (int i = 0; i < len; ++i) {
			TouchEvent event = touch_events.get(i);
			
			if (event.type == TouchEvent.TOUCH_UP &&
				event.x > 256 && event.y > 416) {
				game.setScreen(new MainMenuScreen(game));
				if (Settings.sound_enabled)
					Assets.click.play(1);
				return;
			}
		}
	}
	
	@Override
	public void present(float delta_time) {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.help3, 64, 100);
		g.drawPixmap(Assets.buttons, 256, 416, 0, 128, 64, 64);
	}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
}