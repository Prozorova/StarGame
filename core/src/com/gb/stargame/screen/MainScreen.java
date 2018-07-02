package com.gb.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.*;
import com.gb.stargame.base.sprites.Space;
import com.gb.stargame.base.sprites.StarShip;

public class MainScreen extends Base2DScreen {
    private Space space;
    private StarShip ship;
    private Texture textureShip;
    private Texture[] textureStar;

    public MainScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        textureStar = new Texture[]{new Texture("starBlue.png"), new Texture("starYellow.png")};
        space = new Space(new TextureRegion(textureSpace), textureStar, worldBounds);
        textureShip = new Texture("ship.png");
        ship = new StarShip(new TextureRegion(textureShip), worldBounds);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        space.resize(worldBounds);
        ship.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        space.draw(batch);
        ship.draw(batch);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case 51:
            case 19:
                ship.move(com.gb.stargame.base.sprites.StarShip.Direction.UP, true);
                break;
            case 47:
            case 20:
                ship.move(com.gb.stargame.base.sprites.StarShip.Direction.DOWN, true);
                break;
            case 29:
            case 21:
                ship.move(com.gb.stargame.base.sprites.StarShip.Direction.LEFT, true);
                break;
            case 32:
            case 22:
                ship.move(com.gb.stargame.base.sprites.StarShip.Direction.RIGHT, true);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case 51:
            case 19:
                ship.move(com.gb.stargame.base.sprites.StarShip.Direction.UP, false);
                break;
            case 47:
            case 20:
                ship.move(com.gb.stargame.base.sprites.StarShip.Direction.DOWN, false);
                break;
            case 29:
            case 21:
                ship.move(com.gb.stargame.base.sprites.StarShip.Direction.LEFT, false);
                break;
            case 32:
            case 22:
                ship.move(StarShip.Direction.RIGHT, false);
                break;
        }
        return false;
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        ship.move(touch);
    }

    @Override
    public void dispose() {
        for (Texture txt : textureStar) txt.dispose();
        super.dispose();
    }
}
