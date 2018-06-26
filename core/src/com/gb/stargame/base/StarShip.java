package com.gb.stargame.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class StarShip {
    private Texture texture;
    private Vector2 position;
    private float scale;
    private int x, y;
    private float speedX, speedY;
    private boolean movingX, movingY;
    private float deltaX, deltaY;
    private Vector2 touchPosition;
    private final float cons;
    private final float maxSpeed;
    private final int centX, centY;

    public StarShip() {
        x = Base2DScreen.x;
        y = Base2DScreen.y;
        texture = new Texture("ship.png");
        scale = (float) (x + y) / 1200f;
        speedX = speedY = 0f;
        deltaX = deltaY = 0.34f * scale - 0.15f;
        touchPosition = new Vector2(-1, -1);
        cons = 100f / (new Vector2(x, y).len());
        movingX = movingY = false;
        maxSpeed = 7.5f * scale;
        centX = (int) (43 * scale);
        centY = (int) (45 * scale);
        position = new Vector2(x / 2, centY);
    }

    public void render(SpriteBatch batch) {
        update();
        batch.draw(texture, position.x - centX, position.y - centY, 0, 0, 80, 87, scale, scale, 0, 0, 0, 80, 87, false, false);
    }

    public void move(int x, int y) {
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
        speedY = updateSpeed(speedY, movingY, deltaY);
        speedX = updateSpeed(speedX, movingX, deltaX);
        position.x = position.x + speedX;
        if (position.x > x + centX) position.x = -centX;
        if (position.x < -centX) position.x = x + centX;
        position.y = position.y + speedY;
        if (position.y >= y - centY) position.y = y - centY;
        if (position.y <= centY) position.y = centY;
        if (!touchPosition.epsilonEquals(-1, -1)) {
            updateDelta();
            if (touchPosition.cpy().sub(position).len() < 50f * scale || ((Math.abs(speedX) >= maxSpeed || Math.abs(speedY) >= maxSpeed) && (position.y == centY || position.y == y - centY))) {
                movingY = false;
                movingX = false;
                touchPosition.set(-1, -1);
            }
        }
    }

    private float updateSpeed(float v, boolean flag, float delta) {
        if (flag) {
            v = v + delta;
            if (Math.abs(v) > maxSpeed) v = (v > 0) ? maxSpeed : -maxSpeed;
            return v;
        } else {
            if (v >= 0.2f) delta = Math.abs(0.2f * scale);
            else if (v <= -0.2f) delta = -Math.abs(0.2f * scale);
            else return 0.0f;
        }
        return (v - delta);
    }

    private void updateDelta() {
        float tm = touchPosition.cpy().sub(position).len() * cons;
        deltaX = 2 * (touchPosition.cpy().sub(position).x - speedX * tm) / (float) Math.pow(tm, 2);
        deltaY = 2 * (touchPosition.cpy().sub(position).y - speedY * tm) / (float) Math.pow(tm, 2);
    }

    public enum Direction {LEFT, RIGHT, UP, DOWN}
}
