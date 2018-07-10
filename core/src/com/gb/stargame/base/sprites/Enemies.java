package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.utils.Regions;

import java.util.ArrayList;
import java.util.List;

public class Enemies {
    private Rect worldBounds;
    private float changeX;

    private static List<EnemyShip> enemiesActive;
    private static List<EnemyShip> enemiesInactive;
    private TextureRegion[] enemyTexture;
    private float generateInterval = 4f;
    private float generateTimer;

    public Enemies(TextureRegion[] enemyTexture, Rect worldBounds) {
        this.worldBounds = worldBounds;
        this.enemyTexture = enemyTexture;
        enemiesActive = new ArrayList<>();
        enemiesInactive = new ArrayList<>();
    }

    public void resize(Rect worldBounds) {
        if (enemiesActive.size() != 0)
            for (EnemyShip ship : enemiesActive)
                ship.resize(worldBounds);
        changeX = worldBounds.getWidth();
    }

    public void draw(SpriteBatch batch) {
        if (enemiesActive.size() != 0)
            for (EnemyShip ship : enemiesActive) ship.draw(batch);
    }

    public void update(float delta) {
        generateTimer += delta;
        if (generateInterval <= generateTimer) {
            generateTimer = 0f;
            if (enemiesInactive.size() == 0) {
                enemiesActive.add(new EnemyShip(chooseEnemyShip()));
            } else {
                enemiesInactive.get(0).activate(chooseEnemyShip());
                updateList(enemiesInactive.get(0), enemiesInactive, enemiesActive);
            }
        }
        for (int i = 0; i < enemiesActive.size(); i++)
            enemiesActive.get(i).update();
    }

    private void updateList(EnemyShip ship, List<EnemyShip> list1, List<EnemyShip> list2) {
        list1.remove(ship);
        list2.add(ship);
    }

    private TextureRegion chooseEnemyShip() {
        int i = MathUtils.random(100);
        if (i < 60) return enemyTexture[0];
        if (i < 90) return enemyTexture[1];
        return enemyTexture[2];
    }

    public boolean checkCollisions(StarShip ship) {
        if (enemiesActive.size() != 0)
            for (EnemyShip enemyShip : enemiesActive) {
                if (!ship.isOutside(enemyShip)) return true;
            }
        return false;
    }

    private class EnemyShip extends Sprite {
        private float speed;

        EnemyShip(TextureRegion tShip) {
            super(tShip);
            activate(tShip);
        }

        void activate(TextureRegion tShip) {
            regions = Regions.split(tShip, 1, 2, 2);
            setHeightProportion(worldBounds.getHeight() * 0.1f);
            pos.set(MathUtils.random(worldBounds.getLeft() + getHalfWidth(), worldBounds.getRight() - getHalfWidth()), worldBounds.getTop() + getHeight());
            speed = MathUtils.random(0.001f, 0.005f);
        }

        @Override
        public void resize(Rect worldBounds) {
            pos.x = pos.x * worldBounds.getWidth() / changeX;
            setHeightProportion(worldBounds.getWidth() * getHeight() / changeX);
        }

        @Override
        public void update() {
            setBottom(getBottom() - speed);
            if (getTop() < worldBounds.getBottom())
                updateList(this, enemiesActive, enemiesInactive);
        }
    }
}
