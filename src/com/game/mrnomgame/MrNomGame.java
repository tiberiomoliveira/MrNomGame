package com.game.mrnomgame;

import com.game.mrnomgame.framework.Screen;
import com.game.mrnomgame.framework.impl.AndroidGame;

public class MrNomGame extends AndroidGame {
	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
}