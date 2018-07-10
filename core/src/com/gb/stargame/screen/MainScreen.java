package com.gb.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.*;
import com.gb.stargame.base.sprites.*;

public class MainScreen extends Base2DScreen {
    private Space space;
    private StarShip ship;
    private Enemies enemies;
    private Texture[] textureStar;
    private TextureRegion[] enemyTexture;
    private TextureAtlas atlas;
    private boolean pressedLeft;
    private boolean pressedRight;
    private boolean pressedUp;
    private boolean pressedDown;

    public MainScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        textureStar = new Texture[]{new Texture("starBlue.png"), new Texture("starYellow.png")};
        space = new Space(new TextureRegion(textureSpace), textureStar, worldBounds);
        atlas = new TextureAtlas("mainAtlas.tpack");
        ship = new StarShip(atlas.findRegion("main_ship"), worldBounds);
        enemyTexture = new TextureRegion[]{atlas.findRegion("enemy0"),     // маленький корабль
                atlas.findRegion("enemy1"),                                // средний
                atlas.findRegion("enemy2")};                               // большой
        enemies = new Enemies(enemyTexture, worldBounds);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        space.resize(worldBounds);
        ship.resize(worldBounds);
        enemies.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        space.draw(batch);
        ship.draw(batch);
        enemies.update(delta);
        enemies.draw(batch);
        checkCollisions();
        deleteAllDestroyed();
        batch.end();
    }

    private void checkCollisions() {
        if (enemies.checkCollisions(ship))  game.setScreen(new GameOverScreen(game));
    }

    private void deleteAllDestroyed() {
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case 51:
            case 19:
                pressedUp = true;
                ship.move(StarShip.Direction.UP, true);
                break;
            case 47:
            case 20:
                pressedDown = true;
                ship.move(StarShip.Direction.DOWN, true);
                break;
            case 29:
            case 21:
                pressedLeft = true;
                ship.move(StarShip.Direction.LEFT, true);
                break;
            case 32:
            case 22:
                pressedRight = true;
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
                pressedUp = false;
                if (pressedDown) {
                    ship.move(StarShip.Direction.DOWN, true);
                } else {
                    ship.move(StarShip.Direction.UP, false);
                }
                break;
            case 47:
            case 20:
                pressedDown = false;
                if (pressedUp) {
                    ship.move(StarShip.Direction.UP, true);
                } else {
                    ship.move(StarShip.Direction.DOWN, false);
                }
                break;
            case 29:
            case 21:
                pressedLeft = false;
                if (pressedRight) {
                    ship.move(StarShip.Direction.RIGHT, true);
                } else {
                    ship.move(StarShip.Direction.LEFT, false);
                }
                break;
            case 32:
            case 22:
                pressedRight = false;
                if (pressedLeft) {
                    ship.move(StarShip.Direction.LEFT, true);
                } else {
                    ship.move(StarShip.Direction.RIGHT, false);
                }
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
        atlas.dispose();
        for (Texture txt : textureStar) txt.dispose();
        super.dispose();
    }
}
