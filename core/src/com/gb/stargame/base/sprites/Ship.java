package com.gb.stargame.base.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.math.ShipShape;
import com.gb.stargame.base.pools.BulletPool;
import com.gb.stargame.base.pools.ExplosionPool;

public class Ship extends Sprite {
    protected Vector2 speed = new Vector2(0, 0);
    protected int hp;
    public ShipShape hitBox;
    protected Sound shotSound;

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;
    private float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    protected static BulletPool bulletPool = BulletPool.getInstance();
    protected static ExplosionPool explosionPool = ExplosionPool.getInstance();
    protected TextureRegion bulletRegion;

    protected float bulletV;
    protected float bulletHeight;
    protected int bulletDamage;

    protected float reloadInterval;
    protected float reloadTimer;


    Ship() {
    }

    Ship(TextureRegion region, int rows, int cols, int frames, Rect worldBounds) {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    protected void shoot() {
        this.shotSound.play(0.09f);
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, this.speed.y + bulletV, bulletHeight, worldBounds, bulletDamage);
    }

    public void boom(Vector2 position) {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight() * 0.5f, position);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (!isOutside(worldBounds))
            boom(pos, 1.5f);
    }

    private void boom(Vector2 position, float scale) {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight() * scale, position);
    }

    public void damage(int damage) {
        frame = 1;
        damageAnimateTimer = 0f;
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }

    public int getHp() {
        return hp;
    }

    public void stop() {
        speed.set(0, 0);
    }
}
