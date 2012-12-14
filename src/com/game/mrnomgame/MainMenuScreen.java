package com.game.mrnomgame;

import java.util.List;

import com.game.mrnomgame.framework.Game;
import com.game.mrnomgame.framework.Graphics;
import com.game.mrnomgame.framework.Input.TouchEvent;
import com.game.mrnomgame.framework.Screen;

public class MainMenuScreen extends Screen {
	public MainMenuScreen(Game game) {
		super(game);
	}
	
	@Override
	public void update(float delta_time) {
		Graphics         g            = game.getGraphics();
		List<TouchEvent> touch_events = game.getInput().getTouchEvents();
		int              len          = touch_events.size();
		
		for (int i = 0; i < len; ++i) {
			TouchEvent event = touch_events.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
					Settings.sound_enabled = !Settings.sound_enabled;
					if (Settings.sound_enabled)
						Assets.click.play(1);
				}
				if (inBounds(event, 64, 220, 192, 42)) {
					game.setScreen(new GameScreen(game));
					if (Settings.sound_enabled)
						Assets.click.play(1);
				}
				if (inBounds(event, 64, 220 + 42, 192, 42)) {
					game.setScreen(new HighscoreScreen(game));
					if (Settings.sound_enabled)
						Assets.click.play(1);
				}
				if (inBounds(event, 64, 220 + 84, 192, 42)) {
					game.setScreen(new HelpScreen1(game));
					if (Settings.sound_enabled)
						Assets.click.play(1);
					return;
				}
			}
		}
	}
	
	private boolean inBounds(TouchEvent event,
							 int x, int y,
							 int width, int height) {
		return (event.x > x && event.x < x + width - 1 &&
				event.y > y && event.y < y + height - 1) ? true : false;
	}
	
	@Override
	public void present(float delta_time) {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.logo, 32, 20);
		g.drawPixmap(Assets.main_menu, 64, 220);
		g.drawPixmap(Assets.buttons, 0, 416,
					 (Settings.sound_enabled) ? 0 : 64, 0, 64, 64);
	}
	
	@Override
	public void pause() {
		Settings.save(game.getFileIO());
	}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
}