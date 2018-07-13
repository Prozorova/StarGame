package com.gb.stargame.base.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.math.ShipShape;
import com.gb.stargame.base.pools.BulletPool;
import com.gb.stargame.base.pools.EnemyPool;
import com.gb.stargame.base.utils.Regions;

public class EnemiesEmitter {
    public enum Type {SMALL_ENEMY, MEDIUM_ENEMY, LARGE_ENEMY}

    private static EnemyShip SMALL_ENEMY_SHIP;
    private static EnemyShip MEDIUM_ENEMY_SHIP;
    private static EnemyShip LARGE_ENEMY_SHIP;

    private Rect worldBounds;
    private float generateInterval = 4f;
    private float generateTimer;
    private EnemyPool enemyPool;
    private BulletPool bulletPool;
    private Sound[] sounds;

    public EnemiesEmitter(TextureRegion[] enemyTexture, TextureRegion bulletEnemy, Sound[] sounds, Rect worldBounds) {
        this.worldBounds = worldBounds;
        enemyPool = EnemyPool.getInstance();
        bulletPool = BulletPool.getInstance();
        this.sounds = sounds;
        SMALL_ENEMY_SHIP = new EnemyShip();
        SMALL_ENEMY_SHIP.set(Regions.split(enemyTexture[0], 1, 2, 2), bulletEnemy, 0.01f, -0.3f, 1,
                0.5f, 0.1f, 20, sounds[1], ShipShape.HitBoxTypes.SMALL_ENEMY);
        MEDIUM_ENEMY_SHIP = new EnemyShip();
        MEDIUM_ENEMY_SHIP.set(Regions.split(enemyTexture[1], 1, 2, 2), bulletEnemy, 0.015f, -0.2f, 3,
                1f, 0.13f, 70, sounds[2], ShipShape.HitBoxTypes.MEDIUM_ENEMY);
        LARGE_ENEMY_SHIP = new EnemyShip();
        LARGE_ENEMY_SHIP.set(Regions.split(enemyTexture[2], 1, 2, 2), bulletEnemy, 0.02f, -0.1f, 10,
                2f, 0.18f, 150, sounds[3], ShipShape.HitBoxTypes.LARGE_ENEMY);
    }

    public void generateEnemies(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            EnemyShip enemy = enemyPool.obtain();
            enemy.worldBounds = worldBounds;
            int i = MathUtils.random(100);
            if (i < 60) cloneShip(enemy, Type.SMALL_ENEMY);
            else if (i < 90) cloneShip(enemy, Type.MEDIUM_ENEMY);
            else cloneShip(enemy, Type.LARGE_ENEMY);
            enemy.pos.set(MathUtils.random(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth()), worldBounds.getTop() + enemy.getHalfHeight());
        }
    }

    private EnemyShip cloneShip(EnemyShip ship, Type type) {
        switch (type) {
            case SMALL_ENEMY:
                ship.set(SMALL_ENEMY_SHIP.regions, SMALL_ENEMY_SHIP.bulletRegion, SMALL_ENEMY_SHIP.bulletHeight, SMALL_ENEMY_SHIP.bulletV,
                        SMALL_ENEMY_SHIP.bulletDamage, SMALL_ENEMY_SHIP.reloadInterval, SMALL_ENEMY_SHIP.getHeight(), SMALL_ENEMY_SHIP.health,
                        SMALL_ENEMY_SHIP.shotSound, ShipShape.HitBoxTypes.SMALL_ENEMY);
                ship.speed = new Vector2(0, -MathUtils.random(0.1f, 0.2f));
                break;
            case MEDIUM_ENEMY:
                ship.set(MEDIUM_ENEMY_SHIP.regions, MEDIUM_ENEMY_SHIP.bulletRegion, MEDIUM_ENEMY_SHIP.bulletHeight, MEDIUM_ENEMY_SHIP.bulletV,
                        MEDIUM_ENEMY_SHIP.bulletDamage, MEDIUM_ENEMY_SHIP.reloadInterval, MEDIUM_ENEMY_SHIP.getHeight(), MEDIUM_ENEMY_SHIP.health,
                        MEDIUM_ENEMY_SHIP.shotSound, ShipShape.HitBoxTypes.MEDIUM_ENEMY);
                ship.speed = new Vector2(0, -MathUtils.random(0.08f, 0.12f));
                break;
            case LARGE_ENEMY:
                ship.set(LARGE_ENEMY_SHIP.regions, LARGE_ENEMY_SHIP.bulletRegion, LARGE_ENEMY_SHIP.bulletHeight, LARGE_ENEMY_SHIP.bulletV,
                        LARGE_ENEMY_SHIP.bulletDamage, LARGE_ENEMY_SHIP.reloadInterval, LARGE_ENEMY_SHIP.getHeight(), LARGE_ENEMY_SHIP.health,
                        LARGE_ENEMY_SHIP.shotSound,ShipShape.HitBoxTypes.LARGE_ENEMY);
                ship.speed = new Vector2(0, -MathUtils.random(0.05f, 0.09f));
                break;
        }
        return ship;
    }

    public boolean checkCollisions(StarShip ship) {
        boolean result = false;
        if (!enemyPool.getActiveObjects().isEmpty())
            for (EnemyShip enemyShip : enemyPool.getActiveObjects()) {
                bulletPool.checkCollisions(enemyShip);
                if (!ship.isOutside2(enemyShip)) result = true;
            }
        return result;
    }
}
