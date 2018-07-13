package com.gb.stargame.base.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.math.ShipShape;
import com.gb.stargame.base.pools.BulletPool;

public class Ship extends Sprite {
    protected Vector2 speed = new Vector2(0, 0);
    protected Rect worldBounds;
    protected float changeX;
    protected int health;
    public ShipShape hitBox;
    protected Sound shotSound;


    protected static BulletPool bulletPool = BulletPool.getInstance();
    protected TextureRegion bulletRegion;

    protected float bulletV;
    protected float bulletHeight;
    protected int bulletDamage;

    protected float reloadInterval;
    protected float reloadTimer;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames, Rect worldBounds) {
        super(region, rows, cols, frames);
        this.worldBounds = worldBounds;
        changeX = 1f;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        changeX = worldBounds.getWidth();
    }

    protected void shoot() {
        this.shotSound.play(0.09f);
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, this.speed.y + bulletV, bulletHeight, worldBounds, bulletDamage);
    }
}
