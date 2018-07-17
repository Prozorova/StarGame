package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;

public class RepairTool extends Sprite {
    private Vector2 speed;

    public RepairTool(TextureRegion texture) {
        super(texture);
    }

    public void set() {
        pos.set(MathUtils.random(worldBounds.getLeft() + getHalfWidth(), worldBounds.getRight() - getHalfWidth()), worldBounds.getTop() + getHalfHeight());
        speed = new Vector2(0, -MathUtils.random(0.1f, 0.3f));
        setHeightProportion(worldBounds.getHeight() * 0.05f);
    }

    public void stop() {
        speed.set(0, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.x = pos.x * worldBounds.getWidth() / getChangeX();
        setHeightProportion(worldBounds.getHeight() * 0.05f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(speed, delta);
        if (getTop() < worldBounds.getBottom()) destroy();
    }
}
