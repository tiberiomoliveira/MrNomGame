package com.game.mrnomgame;

import java.util.Random;

public class World {
	static final int   WORLD_WIDTH     = 10;
	static final int   WORLD_HEIGHT    = 13;
	static final int   SCORE_INCREMENT = 10;
	static final float TICK_INITIAL    = 0.5f;
	static final float TICK_DECREMENT  = 0.05f;
	
	public Snake   snake;
	public Stain   stain;
	public boolean game_over = false;
	public int     score = 0;
	
	boolean      fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
	Random       random     = new Random();
	float        tick_time  = 0;
	static float tick       = TICK_INITIAL;
	
	public World() {
		snake = new Snake();
		placeStain();
	}
	
	private void placeStain() {
		for (int x = 0; x < WORLD_WIDTH; ++x) {
			for (int y = 0; y < WORLD_HEIGHT; ++y) {
				fields[x][y] = false;
			}
		}
		
		for (int i = 0; i < snake.parts.size(); ++i) {
			SnakePart part = snake.parts.get(i);
			fields[part.x][part.y] = true;
		}
		
		int stain_x = random.nextInt(WORLD_WIDTH);
		int stain_y = random.nextInt(WORLD_HEIGHT);
		while (true) {
			if (fields[stain_x][stain_y] == false)
				break;
			++stain_x;
			if (stain_x >= WORLD_WIDTH) {
				stain_x =  0;
				++stain_y;
				if (stain_y >= WORLD_HEIGHT)
					stain_y = 0;
			}
		}
		stain = new Stain(stain_x, stain_y, random.nextInt(3));
	}
	
	public void update(float delta_time) {
		if (game_over)
			return;
		
		tick_time += delta_time;
		
		while(tick_time > tick) {
			tick_time -= tick;
			snake.advance();
			if (snake.checkBitten()) {
				game_over = true;
				return;
			}
			
			SnakePart head = snake.parts.get(0);
			if (head.x == stain.x && head.y == stain.y) {
				score += SCORE_INCREMENT;
				snake.eat();
				
				if (snake.parts.size() == WORLD_WIDTH * WORLD_HEIGHT) {
					game_over = true;
					return;
				}
				else {
					placeStain();
				}
				
				if (score % 100 == 0 && tick - TICK_DECREMENT > 0)
					tick -= TICK_DECREMENT;
			}
		}
	}
}