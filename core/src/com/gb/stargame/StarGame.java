package com.gb.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StarGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Space space;

	@Override
	public void create () {
		batch = new SpriteBatch();
		space = new Space();
	}

	@Override
	public void render () {
//		Gdx.gl.glViewport(0, 0,450,800);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		space.render(batch);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
