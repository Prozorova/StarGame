package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.utils.ActionListener;

public class ScaledTouchUpButton extends Sprite {
    private int pointer;
    private boolean pressed;
    private float pressScale;
    private ActionListener actionListener;

    public ScaledTouchUpButton(TextureRegion[] region, ActionListener actionListener) {
        super(region);
        pressScale = 1;
        this.actionListener = actionListener;
        this.pressed = false;
    }

    public ScaledTouchUpButton(TextureRegion[] region, ActionListener actionListener, float pressScale) {
        super(region);
        this.pressScale = pressScale;
        this.actionListener = actionListener;
        this.pressed = false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (pressed || !isMe(touch)) {
            return false;
        }
        setColor(1);
        this.pointer = pointer;
        this.scale = pressScale;
        this.pressed = true;
        return false;
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        if (this.pointer != pointer || !pressed) {
            return;
        }
        if (isMe(touch))
            actionListener.actionPerformed(this);
        setColor(0);
        this.pressed = false;
        this.scale = 1f;
    }

    public int touchUp(Vector2 touch) {
        if (isMe(touch)) {
            float k = getWidth() / 4;
            if (touch.x <= getLeft() + k / 2) return 1;
            if (touch.x <= getLeft() + k * 1.5f) return 2;
            if (touch.x <= getLeft() + k * 2.5f) return 3;
            if (touch.x <= getLeft() + k * 3.5f) return 4;
            return 5;
        }
        return 0;
    }

    public void mouseMoved(Vector2 v) {
        if (isMe(v)) setColor(1);
        else setColor(0);
    }

    @Override
    public void resize(Rect worldBounds, int k) {
        setHeightProportion(worldBounds.getHeight() * 0.1f);
        pos.set(0f, -k * 0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight() * 0.1f);
        pos.set(0f, -0.3f);
    }

    public void setColor(int i) {
        frame = i;
    }

    public int getColor(){
        return frame;
    }
}
