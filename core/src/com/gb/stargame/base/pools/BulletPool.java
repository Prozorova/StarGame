package com.gb.stargame.base.pools;

import com.gb.stargame.base.SpritesPool;
import com.gb.stargame.base.sprites.Bullet;
import com.gb.stargame.base.sprites.Ship;

public class BulletPool extends SpritesPool<Bullet> {
    private static final BulletPool ourInstance = new BulletPool();

    public static BulletPool getInstance() {
        return ourInstance;
    }

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public void checkCollisions(Ship ship) {
        for (int i = 0; i < activeObjects.size(); i++) {
            Bullet bullet = activeObjects.get(i);
            if (!ship.hitBox.isOutside(bullet) && bullet.getOwner() != ship) {
                ship.damage(bullet.getDamage());
                ship.boom(bullet.pos);
                bullet.destroy();
            }
        }
    }
}
