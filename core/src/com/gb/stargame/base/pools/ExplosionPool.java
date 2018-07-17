package com.gb.stargame.base.pools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gb.stargame.base.SpritesPool;
import com.gb.stargame.base.sprites.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {

    private Sound sound;
    private TextureRegion textureRegion;

    private static final ExplosionPool ourInstance = new ExplosionPool();

    public static ExplosionPool getInstance() {
        return ourInstance;
    }

    public void set(Sound sound, TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        this.sound = sound;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(textureRegion, 9, 9, 74, sound);
    }
}
