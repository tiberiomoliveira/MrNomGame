package com.game.mrnomgame;

import com.game.mrnomgame.framework.Game;
import com.game.mrnomgame.framework.Graphics;
import com.game.mrnomgame.framework.Graphics.PixmapFormat;
import com.game.mrnomgame.framework.Screen;

public class LoadingScreen extends Screen {
	public LoadingScreen(Game game) {
		super(game);
	}
	
	@Override
	public void update(float delta_time) {
		Graphics g = game.getGraphics();
		
		Assets.background = g.newPixmap("textures/background.png", PixmapFormat.RGB565);
		Assets.logo       = g.newPixmap("textures/logo.png", PixmapFormat.ARGB4444);
		Assets.main_menu  = g.newPixmap("textures/mainmenu.png", PixmapFormat.ARGB4444);
		Assets.buttons    = g.newPixmap("textures/buttons.png", PixmapFormat.ARGB4444);
		Assets.help1      = g.newPixmap("textures/help1.png", PixmapFormat.ARGB4444);
		Assets.help2      = g.newPixmap("textures/help2.png", PixmapFormat.ARGB4444);
		Assets.help3      = g.newPixmap("textures/help3.png", PixmapFormat.ARGB4444);
		Assets.numbers    = g.newPixmap("textures/numbers.png", PixmapFormat.ARGB4444);
		Assets.ready      = g.newPixmap("textures/ready.png", PixmapFormat.ARGB4444);
		Assets.pause      = g.newPixmap("textures/pause.png", PixmapFormat.ARGB4444);
		Assets.game_over  = g.newPixmap("textures/gameover.png", PixmapFormat.ARGB4444);
		Assets.head_up    = g.newPixmap("textures/headup.png", PixmapFormat.ARGB4444);
		Assets.head_left  = g.newPixmap("textures/headleft.png", PixmapFormat.ARGB4444);
		Assets.head_down  = g.newPixmap("textures/headdown.png", PixmapFormat.ARGB4444);
		Assets.head_right = g.newPixmap("textures/headright.png", PixmapFormat.ARGB4444);
		Assets.tail       = g.newPixmap("textures/tail.png", PixmapFormat.ARGB4444);
		Assets.stain1     = g.newPixmap("textures/stain1.png", PixmapFormat.ARGB4444);
		Assets.stain2     = g.newPixmap("textures/stain2.png", PixmapFormat.ARGB4444);
		Assets.stain3     = g.newPixmap("textures/stain3.png", PixmapFormat.ARGB4444);
		
		Assets.click  = game.getAudio().newSound("sounds/click.ogg");
		Assets.eat    = game.getAudio().newSound("sounds/eat.ogg");
		Assets.bitten = game.getAudio().newSound("sounds/bitten.ogg");
		
		Settings.load(game.getFileIO());
		game.setScreen(new MainMenuScreen(game));
	}
	
	@Override
	public void present(float delta_time) {}
	
	@Override
	public void pause() {}
	
	@Override
	public void resume() {}
	
	@Override
	public void dispose() {}
}