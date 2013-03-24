package com.game.mrnomgame;

import java.util.List;

import android.graphics.Color;

import com.game.mrnomgame.framework.Game;
import com.game.mrnomgame.framework.Graphics;
import com.game.mrnomgame.framework.Input.TouchEvent;
import com.game.mrnomgame.framework.Pixmap;
import com.game.mrnomgame.framework.Screen;

public class GameScreen extends Screen {
	enum GameState {
		Ready,
		Running,
		Paused,
		GameOver
	}
	
	GameState state     = GameState.Ready;
	World     world;
	int       old_score = 0;
	String    score     = "0";
	
	public GameScreen(Game game) {
		super(game);
		world = new World();
	}
	
	@Override
	public void update(float delta_time) {
		List<TouchEvent> touch_events = game.getInput().getTouchEvents();
		
		if (state == GameState.Ready)
			updateReady(touch_events);
		if (state == GameState.Running)
			updateRunning(touch_events, delta_time);
		if (state == GameState.Paused)
			updatePaused(touch_events);
		if (state == GameState.GameOver)
			updateGameOver(touch_events);
	}
	
	private void updateReady(List<TouchEvent> touch_events) {
		if (touch_events.size() > 0)
			state = GameState.Running;
	}
	
	private void updateRunning(List<TouchEvent> touch_events, float delta_time) {
		int len = touch_events.size();
		
		for (int i = 0; i < len; ++i) {
			TouchEvent event = touch_events.get(i);
			
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x < 64 && event.y < 64) {
					if (Settings.sound_enabled)
						Assets.click.play(1);
					state = GameState.Paused;
					return;
				}
			}
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (event.x < 64 && event.y > 416)
					world.snake.turnLeft();
				if (event.x > 256 && event.y > 416)
					world.snake.turnRight();
			}
		}
		
		world.update(delta_time);
		if (world.game_over) {
			if (Settings.sound_enabled)
				Assets.bitten.play(1);
			state = GameState.GameOver;
		}
		if (old_score != world.score) {
			old_score = world.score;
			score = "" + old_score;
			if (Settings.sound_enabled)
				Assets.eat.play(1);
		}
	}
	
	private void updatePaused(List<TouchEvent> touch_events) {
		int len = touch_events.size();
		
		for (int i = 0; i < len; ++i) {
			TouchEvent event = touch_events.get(i);
			
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x > 80 && event.x <= 240) {
					if (event.y > 100 && event.y <= 148) {
						if (Settings.sound_enabled)
							Assets.click.play(1);
						state = GameState.Running;
						return;
					}
					if (event.y > 148 && event.y <= 196) {
						if (Settings.sound_enabled)
							Assets.click.play(1);
						game.setScreen(new MainMenuScreen(game));
						return;
					}
				}
			}
		}
	}
	
	private void updateGameOver(List<TouchEvent> touch_events) {
		int len = touch_events.size();
		
		for (int i = 0; i < len; ++i) {
			TouchEvent event = touch_events.get(i);
			
			if (event.type == TouchEvent.TOUCH_UP) {
				if (event.x >= 128 && event.x <= 192 &&
					event.y >= 200 && event.y <= 264) {
					if (Settings.sound_enabled)
						Assets.click.play(1);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}
	
	@Override
	public void present(float delta_time) {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.background, 0, 0);
		drawWorld(world);
		
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();
		
		drawText(g, score, g.getWidth() / 2 - score.length() * 20 / 2,
				 g.getHeight() - 42);
	}
	
	private void drawWorld(World world) {
		Graphics  g            = game.getGraphics();
		Snake     snake        = world.snake;
		SnakePart head         = snake.parts.get(0);
		Stain     stain        = world.stain;
		Pixmap    stain_pixmap = null;
		
		if (stain.type == Stain.TYPE_1)
			stain_pixmap = Assets.stain1;
		if (stain.type == Stain.TYPE_2)
			stain_pixmap = Assets.stain2;
		if (stain.type == Stain.TYPE_3)
			stain_pixmap = Assets.stain3;
		
		g.drawPixmap(stain_pixmap, stain.x * 32, stain.y * 32);
		
		int len = snake.parts.size();
		for (int i = 1; i < len; ++i) {
			SnakePart part = snake.parts.get(i);
			g.drawPixmap(Assets.tail, part.x * 32, part.y * 32);
		}
		
		Pixmap head_pixmap = null;
		if (snake.direction == Snake.UP)
			head_pixmap = Assets.head_up;
		if (snake.direction == Snake.LEFT)
			head_pixmap = Assets.head_left;
		if (snake.direction == Snake.DOWN)
			head_pixmap = Assets.head_down;
		if (snake.direction == Snake.RIGHT)
			head_pixmap = Assets.head_right;
		
		g.drawPixmap(head_pixmap,
					 (head.x * 32 + 16) - head_pixmap.getWidth() / 2,
					 (head.y * 32 + 16) - head_pixmap.getHeight() / 2);
	}
	
	private void drawReadyUI() {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.ready, 47, 100);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
	}
	
	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
		g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
		g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
	}
	
	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.pause, 80, 100);
		g.drawLine(0, 416, 480, 416, Color.BLACK);	
	}
	
	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		
		g.drawPixmap(Assets.game_over, 62, 100);
		g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
		g.drawLine(0, 416, 480, 416, Color.BLACK);
	}
	
	public void drawText(Graphics g, String line, int x, int y) {
		int len = line.length();
		
		for (int i = 0; i < len; ++i) {
			char c = line.charAt(i);
			
			if (c == ' ') {
				x += 20;
				continue;
			}
			
			int src_x = 0;
			int src_width = 0;
			if (c == '.') {
				src_x = 200;
				src_width = 10;
			}
			else {
				src_x = (c - '0') * 20;
				src_width = 20;
			}
			
			g.drawPixmap(Assets.numbers, x, y, src_x, 0, src_width, 32);
			x += src_width;
		}
	}
	
	@Override
	public void pause() {
		if (state == GameState.Running)
			state = GameState.Paused;
		
		if (world.game_over) {
			Settings.addScore(world.score);
			Settings.save(game.getFileIO());
		}
	}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
}