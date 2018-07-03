package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;

public class StarShip extends Sprite {
    private Rect worldBounds;
    private float changeX;
    private Vector2 touchPosition;
    private Vector2 buff;
    private float maxSpeed;
    private float coef;
    private Vector2 speed;
    private boolean movingX, movingY;
    private Vector2 delta;
    private Vector2 checkSpeed;

    public StarShip(TextureRegion tShip, Rect worldBounds) {
        super(tShip);
        this.worldBounds = worldBounds;
        touchPosition = new Vector2(-1, -100);
        changeX = 1f;
        speed = new Vector2(0, 0);
        delta = new Vector2();
        movingX = movingY = false;
        buff = new Vector2();
        coef = 0.0003f;
        checkSpeed = new Vector2(100,100);
    }

    @Override
    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
    }

    public void move(Vector2 touched) {
        if (!isMe(touched)) {
            touchPosition.set(touched);
            movingX = movingY = true;
            updateDelta();
        }
    }

    public void move(Direction direction, boolean moving) {
        touchPosition.set(-1, -100);
        checkSpeed = new Vector2(100,100);
        int a = (moving) ? 1 : -1;
        switch (direction) {
            case DOWN:
                delta.y = -coef * a;
                movingY = moving;
                break;
            case UP:
                delta.y = coef * a;
                movingY = moving;
                break;
            case LEFT:
                delta.x = -coef * a;
                movingX = moving;
                break;
            case RIGHT:
                delta.x = coef * a;
                movingX = moving;
                break;
        }
    }

    public void update() {
        updateSpeed();
        pos.add(speed);
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getTop() >= worldBounds.getTop()) {
            setTop(worldBounds.getTop());
            speed.y = 0f;
            delta.y = 0f;
        }
        if (getBottom() <= worldBounds.getBottom()) {
            setBottom((worldBounds.getBottom()));
            speed.y = 0f;
            delta.y = 0f;
        }
        if (!touchPosition.epsilonEquals(-1, -100)) {
            buff.set(touchPosition);
            if (buff.sub(pos).len() < getHalfHeight()) {
                movingY = false;
                movingX = false;
                touchPosition.set(-1, -100);
                delta.scl(-3);
                checkSpeed.set(speed);
                return;
            }
            updateDelta();
        }
    }

    private void updateSpeed() {
        if (!movingX && Math.abs(speed.x) <= coef * 2) {
            speed.x = 0f;
            delta.x = 0f;
            checkSpeed.x = 100f;
        }
        if (!movingY && Math.abs(speed.y) <= coef * 2) {
            speed.y = 0f;
            delta.y = 0f;
            checkSpeed.y = 100f;
        }
        speed.add(delta);
        if (movingX || movingY) {
            if (Math.abs(speed.len()) > maxSpeed) {
                float ratio = Math.abs(speed.x / speed.y);
                int a = (speed.x >= 0) ? 1 : -1;
                int b = (speed.y >= 0) ? 1 : -1;
                if (Math.abs(speed.x) > Math.abs(speed.y))
                    speed.set(a * maxSpeed, b * maxSpeed / ratio);
                else speed.set(a * maxSpeed * ratio, b * maxSpeed);
            }
        } else {
            if (Math.abs(speed.x) > Math.abs(checkSpeed.x)) delta.x *=-1;
            if (Math.abs(speed.y) > Math.abs(checkSpeed.y)) delta.y *=-1;
        }
    }

    private void updateDelta() {
        buff.set(touchPosition);
        delta.set(buff.sub(pos).scl(coef * 15));
    }

    public enum Direction {LEFT, RIGHT, UP, DOWN}

    @Override
    public void resize(Rect worldBounds) {
        pos.x = pos.x * worldBounds.getWidth() / changeX;
        setHeightProportion(worldBounds.getHeight() * 0.09f);
        changeX = worldBounds.getWidth();
        maxSpeed = 0.009f;
    }
}
