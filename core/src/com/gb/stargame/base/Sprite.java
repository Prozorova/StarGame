package com.gb.stargame.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.utils.Regions;

public abstract class Sprite extends Rect {
    private float angle = 0f;
    protected float scale = 1f;
    public TextureRegion[] regions;
    protected int frame;
    private boolean isDestroyed;

    protected Sprite() {
    }

    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new NullPointerException();
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public Sprite(TextureRegion[] region) {
        if (region == null) {
            throw new NullPointerException();
        }
        regions = region;
    }

    public Sprite(TextureRegion region, float scale, float angle) {
        if (region == null) {
            throw new NullPointerException();
        }
        regions = new TextureRegion[1];
        regions[0] = region;
        setScale(scale);
        setAngle(angle);
    }

    public Sprite(TextureRegion region, int rows, int cols, int frames) {
        if (region == null) {
            throw new NullPointerException();
        }
        regions = Regions.split(region, rows, cols, frames);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame], // текущий регион
                getLeft(), getBottom(), // точка отрисовки
                halfWidth, halfHeight, // точка вращения
                getWidth(), getHeight(), // ширина и высота
                scale, scale, // масштаб по оси x и y
                angle // угол вращения
        );
    }

    public void draw(SpriteBatch batch, float scaleTemp) {
        batch.draw(
                regions[frame], // текущий регион
                getLeft(), getBottom(), // точка отрисовки
                halfWidth, halfHeight, // точка вращения
                getWidth(), getHeight(), // ширина и высота
                scaleTemp, scaleTemp, // масштаб по оси x и y
                angle // угол вращения
        );
    }

    protected void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    protected void setWidthProportion(float width) {
        setWidth(width);
        float aspect = regions[frame].getRegionHeight() / (float) regions[frame].getRegionWidth();
        setHeight(width * aspect);
    }

    public void resize(Rect worldBounds) {
    }

    public void resize(Rect worldBounds, int k) {
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        return false;
    }

    public void touchUp(Vector2 touch, int pointer) {
    }

    public void update(float delta) {
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void destroy() {
        this.isDestroyed = true;
    }

    public void flushDestroy() {
        this.isDestroyed = false;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
