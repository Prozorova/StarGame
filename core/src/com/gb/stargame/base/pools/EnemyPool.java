package com.gb.stargame.base.pools;

import com.gb.stargame.base.SpritesPool;
import com.gb.stargame.base.sprites.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {
    private static final EnemyPool ourInstance = new EnemyPool();

    public static EnemyPool getInstance() {
        return ourInstance;
    }

    @Override
    protected EnemyShip newObject() {
        return new EnemyShip();
    }
}
