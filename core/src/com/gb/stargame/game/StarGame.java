package com.gb.stargame.game;

import com.badlogic.gdx.Game;
import com.gb.stargame.screen.*;

public class StarGame extends Game {
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}

