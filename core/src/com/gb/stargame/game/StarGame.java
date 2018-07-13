package com.gb.stargame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.gb.stargame.base.ScreenManager;
import com.gb.stargame.screen.*;

public class StarGame extends Game {
	@Override
	public void create() {
		ScreenManager.getInstance().startGame(this);
	}
}

