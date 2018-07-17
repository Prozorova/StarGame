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

    public EnemiesEmitter(TextureRegion[] enemyTexture, TextureRegion bulletEnemy, int maxHP, Sound[] sounds, Rect worldBounds) {
        this.worldBounds = worldBounds;
        enemyPool = EnemyPool.getInstance();
        bulletPool = BulletPool.getInstance();
        SMALL_ENEMY_SHIP = new EnemyShip();
        SMALL_ENEMY_SHIP.set(Regions.split(enemyTexture[0], 1, 2, 2), bulletEnemy, 0.01f, -0.3f, 1,
                0.5f, 0.1f, maxHP / 5, sounds[1], ShipShape.HitBoxTypes.SMALL_ENEMY);
        MEDIUM_ENEMY_SHIP = new EnemyShip();
        MEDIUM_ENEMY_SHIP.set(Regions.split(enemyTexture[1], 1, 2, 2), bulletEnemy, 0.015f, -0.2f, 3,
                1f, 0.13f, maxHP / 2, sounds[2], ShipShape.HitBoxTypes.MEDIUM_ENEMY);
        LARGE_ENEMY_SHIP = new EnemyShip();
        LARGE_ENEMY_SHIP.set(Regions.split(enemyTexture[2], 1, 2, 2), bulletEnemy, 0.02f, -0.1f, 10,
                2f, 0.18f, maxHP, sounds[3], ShipShape.HitBoxTypes.LARGE_ENEMY);
    }

    public void generateEnemies(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            EnemyShip enemy = enemyPool.obtain();
            int i = MathUtils.random(100);
            if (i < 60) cloneShip(enemy, Type.SMALL_ENEMY);
            else if (i < 90) cloneShip(enemy, Type.MEDIUM_ENEMY);
            else cloneShip(enemy, Type.LARGE_ENEMY);
            enemy.pos.set(MathUtils.random(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth()), worldBounds.getTop() + enemy.getHalfHeight());
        }
    }

    private EnemyShip cloneShip(EnemyShip ship, Type type) {
        Ship enemy;
        switch (type) {
            case SMALL_ENEMY:
                enemy = SMALL_ENEMY_SHIP;
                ship.speed = new Vector2(0, -MathUtils.random(0.1f, 0.2f));
                break;
            case MEDIUM_ENEMY:
                enemy = MEDIUM_ENEMY_SHIP;
                ship.speed = new Vector2(0, -MathUtils.random(0.08f, 0.12f));
                break;
            default:
                enemy = LARGE_ENEMY_SHIP;
                ship.speed = new Vector2(0, -MathUtils.random(0.05f, 0.09f));
                break;
        }
        ship.set(enemy.regions, enemy.bulletRegion, enemy.bulletHeight, enemy.bulletV, enemy.bulletDamage, enemy.reloadInterval,
                enemy.getHeight(), enemy.hp, enemy.shotSound, enemy.hitBox.getType());
        return ship;
    }

    public boolean checkCollisions(StarShip ship) {
        boolean result = false;
        if (!enemyPool.getActiveObjects().isEmpty())
            for (EnemyShip enemyShip : enemyPool.getActiveObjects()) {
                bulletPool.checkCollisions(enemyShip);
                if (!ship.isOutside2(enemyShip)) {
                    result = true;
                    enemyShip.destroy();
                    ship.destroy();
                } else if (!enemyShip.hitBox.isOutside(ship.hitBox.getPart1())) {
                    switch (enemyShip.hitBox.getType()) {
                        case MEDIUM_ENEMY:
                            ship.damage((int) (ship.getMaxHP() * 0.35));
                        case SMALL_ENEMY:
                            ship.damage((int) (ship.getMaxHP() * 0.35));
                            enemyShip.destroy();
                            ship.impact();
                            break;
                        case LARGE_ENEMY:
                            ship.damage((int) (ship.getMaxHP() * 0.9));
                            enemyShip.damage((150 - ship.getMaxHP()) / 2);
                            ship.impact();
                            break;
                    }
                }
            }
        return result;
    }

    public void stopGenerating() {
        generateInterval = 1000f;
    }
}
