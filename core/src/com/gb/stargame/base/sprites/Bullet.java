package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;

public class Bullet extends Sprite {
    private Rect worldBounds;
    private Vector2 speed;
    private int damage;
    private Ship owner;

    public Bullet() {
        regions = new TextureRegion[1];
        speed = new Vector2();
    }

    public void set(Ship ship, TextureRegion bulletRegion, Vector2 pos, float bulletV, float bulletHeight, Rect worldBounds, int bulletDamage) {
        this.owner = ship;
        this.regions[0] = bulletRegion;
        this.pos.set(pos);
        this.speed.set(0f, bulletV);
        setHeightProportion(bulletHeight);
        this.worldBounds = worldBounds;
        this.damage = bulletDamage;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(speed, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public void stop() {
        speed.set(0, 0);
    }

    public int getDamage() {
        return damage;
    }

    public Object getOwner() {
        return owner;
    }
}
