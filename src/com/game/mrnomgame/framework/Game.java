package com.game.mrnomgame.framework;

public interface Game {
	public Input    getInput();
	public FileIO   getFileIO();
	public Graphics getGraphics();
	public Audio    getAudio();
	public Screen   getCurrentScreen();
	public Screen   getStartScreen();
	public void     setScreen(Screen screen);
}