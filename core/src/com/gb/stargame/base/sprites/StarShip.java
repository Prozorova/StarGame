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
    private float speedX, speedY;
    private boolean movingX, movingY;
    private float deltaX, deltaY;
    private float cons;


    public StarShip(TextureRegion tShip, Rect worldBounds) {
        super(tShip);
        this.worldBounds = worldBounds;
        touchPosition = new Vector2(-1, -1);
        changeX = 1f;
        speedX = speedY = 0f;
        movingX = movingY = false;
        buff = new Vector2();
        coef = 0.0003f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
    }

    public void move(Vector2 touched) {
        touchPosition.set(touched);
        movingX = movingY = true;
        updateDelta();
    }

    public void move(Direction direction, boolean moving) {
        deltaY = (deltaY < 0) ? -coef : coef;
        deltaX = (deltaX < 0) ? -coef : coef;
        touchPosition.set(-1, -1);
        switch (direction) {
            case DOWN:
                deltaY = -Math.abs(deltaY);
                movingY = moving;
                break;
            case UP:
                deltaY = Math.abs(deltaY);
                movingY = moving;
                break;
            case LEFT:
                deltaX = -Math.abs(deltaX);
                movingX = moving;
                break;
            case RIGHT:
                deltaX = Math.abs(deltaX);
                movingX = moving;
                break;
        }
    }

    public void update() {
        float modifX = 1f, modifY = 1f;
        if (deltaX - deltaY > coef / 2) modifY = Math.abs(deltaY / deltaX);
        else if (deltaY - deltaX > coef / 2) modifX = Math.abs(deltaX / deltaY);
        speedY = updateSpeed(speedY, movingY, deltaY, modifY);
        speedX = updateSpeed(speedX, movingX, deltaX, modifX);
        pos.x = pos.x + speedX;
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        pos.y = pos.y + speedY;
        if (getTop() >= worldBounds.getTop()) setTop(worldBounds.getTop());
        if (getBottom() <= worldBounds.getBottom()) setBottom((worldBounds.getBottom()));
        if (!touchPosition.epsilonEquals(-1, -1)) {
            updateDelta();
            buff.set(touchPosition);
            if (buff.sub(pos).len() < getHeight()) { // || ((Math.abs(speedX) >= maxSpeed || Math.abs(speedY) >= maxSpeed) && (position.y == halfY || position.y == y - halfY)))
                movingY = false;
                movingX = false;
                touchPosition.set(-1, -1);
            }
        }
    }

    private float updateSpeed(float v, boolean flag, float delta, float modif) {
        if (flag) {
            v = v + delta;
            if (Math.abs(v) > maxSpeed) v = (v > 0) ? maxSpeed : -maxSpeed;
            return v;
        } else {
            if (v >= coef) delta = coef * modif;
            else if (v <= -coef) delta = -coef * modif;
            else return 0.0f;
        }
        return (v - delta);
    }

    private void updateDelta() {
        buff.set(touchPosition);
        float tm = buff.sub(pos).len() * cons;
        buff.set(touchPosition);
        deltaX = 2 * (buff.sub(pos).x - speedX * tm) / (float) Math.pow(tm, 2);
        buff.set(touchPosition);
        deltaY = 2 * (buff.sub(pos).y - speedY * tm) / (float) Math.pow(tm, 2);
        float ratio;
        if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > coef * 2) {
            ratio = deltaX / deltaY;
            deltaX = (deltaX < 0) ? -coef * 2 : coef * 2;
            deltaY = deltaX / ratio;
        } else if (Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > coef * 2) {
            ratio = deltaY / deltaX;
            deltaY = (deltaY < 0) ? -coef * 2 : coef * 2;
            deltaX = deltaY / ratio;
        }
        System.out.println(deltaX + " " + deltaY);
    }

    public enum Direction {LEFT, RIGHT, UP, DOWN}

    @Override
    public void resize(Rect worldBounds) {
        pos.x = pos.x * worldBounds.getWidth() / changeX;
        setHeightProportion(worldBounds.getHeight() * 0.09f);
        changeX = worldBounds.getWidth();
        cons = coef / (new Vector2(worldBounds.getWidth(), worldBounds.getHeight()).len());
        maxSpeed = 0.01f;
    }
}
