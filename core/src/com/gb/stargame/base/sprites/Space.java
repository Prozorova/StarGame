package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;

public class Space extends Sprite {
    private TextureRegion[] regionsStar;
    private Star[] stars;
    private Rect worldBounds;
    private float changeX;

    public Space(TextureRegion textureSpace, Texture[] textureStar, Rect worldBounds) {
        super(textureSpace);
        this.worldBounds = worldBounds;
        regionsStar = new TextureRegion[textureStar.length];
        for (int i = 0; i < textureStar.length; i++)
            regionsStar[i] = new TextureRegion(textureStar[i]);
    }

    private class Star extends Sprite {
        private float speed;

        Star(TextureRegion texture, float starScale) {
            super(new TextureRegion(texture), starScale, MathUtils.random(0, 180));
            setHeightProportion(scale);
            pos.set(MathUtils.random(worldBounds.getLeft(), worldBounds.getRight()), MathUtils.random(worldBounds.getBottom(), worldBounds.getHeight()));
            speed = 0.02f * starScale;
        }

        @Override
        public void resize(Rect worldBounds) {
            pos.x = pos.x * worldBounds.getWidth() / changeX;
            setHeightProportion(worldBounds.getWidth() * getHeight() / changeX);
        }

        @Override
        public void update(float delta) {
            setBottom(getBottom() - speed);
            if (getTop() < worldBounds.getBottom()) {
                pos.set(MathUtils.random(worldBounds.getLeft(), worldBounds.getRight()), worldBounds.getTop() + getHeight());
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        float randScale;
        for (Star star : stars) {
            if (MathUtils.random(0, 300) < 1) {
                randScale = star.getScale() * MathUtils.random(1.5f, 3f);
            } else randScale = star.getScale();
            star.update(1f);
            star.draw(batch, randScale);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
        if (stars == null) {
            stars = new Star[70];
            for (int i = 0; i < stars.length; i++)
                stars[i] = new Star(regionsStar[MathUtils.random(1)], MathUtils.random(0.09f, 0.165f));
        } else
            for (Star star : stars) {
                star.resize(worldBounds);
            }
        changeX = worldBounds.getWidth();
    }
}