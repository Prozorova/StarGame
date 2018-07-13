package com.gb.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.*;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.pools.*;
import com.gb.stargame.base.sprites.*;

public class MainScreen extends Base2DScreen {
    private Space space;
    private StarShip ship;
    private Texture[] textureStar;
    private Texture textureFire;
    private FireButton fireButton;
    private TextureRegion[] enemyTexture;
    private TextureRegion bulletTexture;
    private TextureAtlas atlas;
    private boolean pressedLeft;
    private boolean pressedRight;
    private boolean pressedUp;
    private boolean pressedDown;
    private Sound[] sounds;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemiesEmitter enemies;

    @Override
    public void show() {
        super.show();
        textureFire = new Texture("fireButton.png");
        fireButton = new FireButton(new TextureRegion(textureFire));
        textureStar = new Texture[]{new Texture("starBlue.png"), new Texture("starYellow.png")};
        space = new Space(new TextureRegion(textureSpace), textureStar, worldBounds);
        atlas = new TextureAtlas("mainAtlas.tpack");
        sounds = new Sound[]{Gdx.audio.newSound(Gdx.files.internal("Sounds/playerShip.wav")), Gdx.audio.newSound(Gdx.files.internal("Sounds/smallEnemy.wav")),
                Gdx.audio.newSound(Gdx.files.internal("Sounds/mediumEnemy.wav")), Gdx.audio.newSound(Gdx.files.internal("Sounds/largeEnemy.wav"))};
        ship = new StarShip(atlas.findRegion("main_ship"), atlas.findRegion("bulletMainShip"), sounds[0], worldBounds, autoFire);
        enemyTexture = new TextureRegion[]{atlas.findRegion("enemy0"),     // маленький корабль
                atlas.findRegion("enemy1"),                                // средний
                atlas.findRegion("enemy2")};                               // большой
        bulletTexture = new TextureRegion(atlas.findRegion("bulletEnemy"));
        enemies = new EnemiesEmitter(enemyTexture, bulletTexture, sounds, worldBounds);
        bulletPool = BulletPool.getInstance();
        enemyPool = EnemyPool.getInstance();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        space.resize(worldBounds);
        ship.resize(worldBounds);
        fireButton.resize(worldBounds);
//        enemies.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        space.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        ship.draw(batch);
        if (!autoFire) fireButton.draw(batch);
        batch.end();
    }

    private void update(float delta) {
        ship.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemies.generateEnemies(delta);
    }

    private void checkCollisions() {
        if (enemies.checkCollisions(ship))
            screenManager.switchScreens(ScreenManager.ScreenType.GAMEOVER);
        bulletPool.checkCollisions(ship);
    }

    private void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
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
            case 62:
                ship.fire();
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
            case 62:
                ship.stopFire();
                break;
        }
        return false;
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        if (autoFire || !fireButton.touchDown(touch, pointer))
            ship.move(touch);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        super.touchUp(touch, pointer);
        fireButton.touchUp(touch, pointer);
    }

    @Override
    public void dispose() {
        textureFire.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        for (Sound s : sounds)
            s.dispose();
        atlas.dispose();
        for (Texture txt : textureStar) txt.dispose();
    }

    private class FireButton extends Sprite {
        private boolean pressed;
        private int pointer;
        private float pressScale;

        FireButton(TextureRegion textureRegion) {
            super(textureRegion);
            pressed = false;
            pressScale = 0.9f;
        }

        @Override
        public boolean touchDown(Vector2 touch, int pointer) {
            if (!isMe(touch)) return false;
            if (pressed) {
                return true;
            }
            this.pressed = true;
            this.pointer = pointer;
            this.scale = pressScale;
            ship.fire();
            return true;
        }

        @Override
        public void touchUp(Vector2 touch, int pointer) {
            if (this.pointer != pointer || !pressed) return;
            ship.stopFire();
            this.pressed = false;
            this.scale = 1f;
        }

        @Override
        public void resize(Rect worldBounds) {
            setHeightProportion(worldBounds.getHeight() * 0.12f);
            this.setBottom(worldBounds.getBottom());
            if (fireButtonPlace) this.setLeft(worldBounds.getLeft());
            else this.setRight(worldBounds.getRight());
        }
    }
}
