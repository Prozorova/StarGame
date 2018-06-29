package com.gb.stargame.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class StarShip {
    private Texture texture;
    private Vector2 position;
    private float scale;
    private float x, y;
    private float speedX, speedY;
    private boolean movingX, movingY;
    private float deltaX, deltaY;
    private Vector2 touchPosition;
    private Vector2 buff;
    private float cons;
    private float maxSpeed;
    private float halfX, halfY;
    private float changeX;

    public StarShip(float x, float y, float scale) {
        changeX = -1f;
        setXY(x, y, x);
        this.scale = scale * 0.83f;
        texture = new Texture("ship.png");
        speedX = speedY = 0f;
        deltaX = deltaY = 0.34f * this.scale - 0.15f;
        touchPosition = new Vector2(-1, -1);
        movingX = movingY = false;
        maxSpeed = 7.5f * this.scale;
        position = new Vector2(0, 0);
        buff = new Vector2();
    }

    public void render(SpriteBatch batch) {
//        update();
        batch.draw(texture, position.x - halfX, position.y - halfY, halfX*2, halfY*2);
    }

    public void move(float x, float y) {
        touchPosition.set(x, y);
        movingX = movingY = true;
        updateDelta();
    }

    public void move(Direction direction, boolean moving) {
        deltaY = (deltaY < 0) ? -0.2f * scale : 0.2f * scale;
        deltaX = (deltaX < 0) ? -0.2f * scale : 0.2f * scale;
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

    private void update() {
        float modifX = 1f, modifY = 1f;
        if (deltaX - deltaY > 0.1f) modifY = Math.abs(deltaY / deltaX);
        else if (deltaY - deltaX > 0.1f) modifX = Math.abs(deltaX / deltaY);
        speedY = updateSpeed(speedY, movingY, deltaY, modifY);
        speedX = updateSpeed(speedX, movingX, deltaX, modifX);
        position.x = position.x + speedX;
        if (position.x > x + halfX) position.x = -halfX;
        if (position.x < -halfX) position.x = x + halfX;
        position.y = position.y + speedY;
        if (position.y >= y - halfY) position.y = y - halfY;
        if (position.y <= halfY) position.y = halfY;
        if (!touchPosition.epsilonEquals(-1, -1)) {
            updateDelta();
            buff.set(touchPosition);
            if (buff.sub(position).len() < 20f * scale || ((Math.abs(speedX) >= maxSpeed || Math.abs(speedY) >= maxSpeed) && (position.y == halfY || position.y == y - halfY))) {
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
            if (v >= 0.2f) delta = 0.2f * scale * modif;
            else if (v <= -0.2f) delta = -0.2f * scale * modif;
            else return 0.0f;
        }
        return (v - delta);
    }

    private void updateDelta() {
        buff.set(touchPosition);
        float tm = buff.sub(position).len() * cons;
        buff.set(touchPosition);
        deltaX = 2 * (buff.sub(position).x - speedX * tm) / (float) Math.pow(tm, 2);
        buff.set(touchPosition);
        deltaY = 2 * (buff.sub(position).y - speedY * tm) / (float) Math.pow(tm, 2);
    }

    public enum Direction {LEFT, RIGHT, UP, DOWN}

    public void dispose() {
        texture.dispose();
    }

    public void setXY(float x, float y, float changeX) {
        this.x = x;
        this.y = y;
        if (this.changeX != -1f)
            position.x = position.x * changeX / this.changeX;
        this.changeX = changeX;
        halfX = x * 0.052f;
        halfY = y * 0.05655f;
        cons = 100f / (new Vector2(x, y).len());
    }
}
