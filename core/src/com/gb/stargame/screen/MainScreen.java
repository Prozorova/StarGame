package com.gb.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gb.stargame.base.*;

public class MainScreen extends Base2DScreen {
    private SpriteBatch batch;

    public MainScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        space = new Space();
        ship = new StarShip();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        space.render(batch);
        ship.render(batch);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case 51:
            case 19:
                ship.move(StarShip.Direction.UP, true);
                break;
            case 47:
            case 20:
                ship.move(StarShip.Direction.DOWN, true);
                break;
            case 29:
            case 21:
                ship.move(StarShip.Direction.LEFT, true);
                break;
            case 32:
            case 22:
                ship.move(StarShip.Direction.RIGHT, true);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case 51:
            case 19:
                ship.move(StarShip.Direction.UP, false);
                break;
            case 47:
            case 20:
                ship.move(StarShip.Direction.DOWN, false);
                break;
            case 29:
            case 21:
                ship.move(StarShip.Direction.LEFT, false);
                break;
            case 32:
            case 22:
                ship.move(StarShip.Direction.RIGHT, false);
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        ship.move(screenX, y - screenY);
        return false;
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
