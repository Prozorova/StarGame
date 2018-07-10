package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;

public class MainMenu extends Sprite {
    private Logo logo;

    public MainMenu(TextureRegion region, TextureRegion tLogo) {
        super(region);
        logo = new Logo(tLogo);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
        logo.resize(worldBounds);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        logo.draw(batch);
    }

    private class Logo extends Sprite {
        Logo(TextureRegion region) {
            super(region);
        }

        @Override
        public void resize(Rect worldBounds) {
            setWidthProportion(worldBounds.getWidth() * 0.9f);
            pos.set(0f, worldBounds.getHeight() * 0.3f);
        }
    }
}