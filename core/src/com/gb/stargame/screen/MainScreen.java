package com.gb.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
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
    private Texture textureRepTool;
    private FireButton fireButton;
    private TextureRegion[] enemyTexture;
    private TextureRegion bulletTexture;
    private TextureAtlas atlas;
    private StarShipHP starShipHP;
    private TextureAtlas atlasHP;
    private boolean pressedLeft;
    private boolean pressedRight;
    private boolean pressedUp;
    private boolean pressedDown;
    private Sound[] sounds;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private RepairToolPool repairToolPool;
    private EnemiesEmitter enemies;

    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbStage = new StringBuilder();

    private float freezeInterval = 4f;
    private float freezeTimer;
    private boolean isFinished;

    @Override
    public void show() {
        super.show();
        textureFire = new Texture("fireButton.png");
        fireButton = new FireButton(new TextureRegion(textureFire));
        textureStar = new Texture[]{new Texture("starBlue.png"), new Texture("starYellow.png")};
        space = new Space(new TextureRegion(textureSpace), textureStar);
        atlas = new TextureAtlas("mainAtlas.tpack");
        if (soundON) {
            sounds = new Sound[]{Gdx.audio.newSound(Gdx.files.internal("Sounds/playerShip.wav")), Gdx.audio.newSound(Gdx.files.internal("Sounds/smallEnemy.wav")),
                    Gdx.audio.newSound(Gdx.files.internal("Sounds/mediumEnemy.wav")), Gdx.audio.newSound(Gdx.files.internal("Sounds/largeEnemy.wav")),
                    Gdx.audio.newSound(Gdx.files.internal("Sounds/explosion.wav"))};
        } else {
            sounds = new Sound[5];
            for (int i = 0; i < 5; i++) {
                sounds[i] = Gdx.audio.newSound(Gdx.files.internal("Sounds/Void.wav"));
            }
        }
            ship = new StarShip(atlas.findRegion("main_ship"), atlas.findRegion("bulletMainShip"), sounds[0], worldBounds, autoFire, 150 - maxEnemyHP);
        enemyTexture = new TextureRegion[]{atlas.findRegion("enemy0"),     // маленький корабль
                atlas.findRegion("enemy1"),                                // средний
                atlas.findRegion("enemy2")};                               // большой
        bulletTexture = new TextureRegion(atlas.findRegion("bulletEnemy"));
        enemies = new EnemiesEmitter(enemyTexture, bulletTexture, maxEnemyHP, sounds, worldBounds);
        bulletPool = BulletPool.getInstance();
        enemyPool = EnemyPool.getInstance();
        explosionPool = ExplosionPool.getInstance();
        explosionPool.set(sounds[4], atlas.findRegion("explosion"));
        repairToolPool = RepairToolPool.getInstance();
        textureRepTool = new Texture("repTool.png");
        repairToolPool.setTexture(new TextureRegion(textureRepTool));
        atlasHP = new TextureAtlas("atlasHP.tpack");
        starShipHP = new StarShipHP(new TextureRegion(atlasHP.findRegion("hp100")));
        isFinished = false;
        freezeTimer = 0f;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        space.resize(worldBounds);
        ship.resize(worldBounds);
        fireButton.resize(worldBounds);
        starShipHP.resize(worldBounds);
        for (EnemyShip enemy : enemyPool.getActiveObjects()) {
            enemy.resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (isFinished) {
            freezeTimer += delta;
            if (freezeTimer >= freezeInterval) {
                freezeTimer = 0f;
                screenManager.switchScreens(ScreenManager.ScreenType.GAMEOVER);
            }
        }
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
        if (!ship.isDestroyed()) ship.draw(batch);
        repairToolPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        if (!autoFire) fireButton.draw(batch);
        starShipHP.draw(batch);
        printInfo();
        batch.end();
    }

    private void update(float delta) {
        ship.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        explosionPool.updateActiveSprites(delta);
        repairToolPool.updateActiveSprites(delta);
        enemies.generateEnemies(delta);
        repairToolPool.generateRepTools(delta);
    }

    private void checkCollisions() {
        if (!isFinished) {
            bulletPool.checkCollisions(ship);
            if (enemies.checkCollisions(ship) || ship.isDestroyed()) {
                ship.stop();
                for (EnemyShip enemy : enemyPool.getActiveObjects()) enemy.stop();
                for (Bullet bullet : bulletPool.getActiveObjects()) bullet.stop();
                for (RepairTool repairTool : repairToolPool.getActiveObjects()) repairTool.stop();
                enemies.stopGenerating();
                enemyPool.stopShooting();
                repairToolPool.stopGenerating();
                isFinished = true;
            }
            if (repairToolPool.checkCollisions(ship)) ship.addHP();
        }
        if (ship.getHp() > 0)
            starShipHP.set(ship.getHp() * 100 / ship.getMaxHP());
        else starShipHP.set(0);
    }

    private void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        repairToolPool.freeAllDestroyedActiveSprites();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbStage.setLength(0);
        font.draw(batch, sbFrags.append("Score: ").append(enemies.getScore()), worldBounds.getLeft(), worldBounds.getTop() - 0.01f);
        font.draw(batch, sbStage.append("Stage: ").append(enemies.getStage()), worldBounds.pos.x, worldBounds.getTop() - 0.01f, Align.center);
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
        textureRepTool.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        repairToolPool.dispose();
        for (Sound s : sounds)
            s.dispose();
        atlas.dispose();
        atlasHP.dispose();
        font.dispose();
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

    private class StarShipHP extends Sprite {

        StarShipHP(TextureRegion tReg) {
            super(tReg);
        }

        public void set(int rate) {
            regions[0] = atlasHP.findRegion("hp" + rate);
        }

        @Override
        public void resize(Rect worldBounds) {
            setHeightProportion(worldBounds.getHeight() * 0.1f);
            this.setTop(worldBounds.getTop());
            this.setRight(worldBounds.getRight());
        }
    }
}
