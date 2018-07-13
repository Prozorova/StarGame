package com.gb.stargame.base.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.math.ShipShape;

public class EnemyShip extends Ship {

    @Override
    public void resize(Rect worldBounds) {
        pos.x = pos.x * worldBounds.getWidth() / changeX;
        setHeightProportion(worldBounds.getWidth() * getHeight() / changeX);
    }

    @Override
    public void update(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
        pos.mulAdd(speed, delta);
        hitBox.update(this.pos);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval,
            float height,
            int health,
            Sound sound,
            ShipShape.HitBoxTypes type
    ) {
        this.regions = regions;
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV = bulletVY;
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
        reloadTimer = reloadInterval;
        this.health = health;
        this.shotSound = sound;
        this.hitBox = new ShipShape(this.pos, this.getWidth(), this.getHeight(), type);
    }
}
