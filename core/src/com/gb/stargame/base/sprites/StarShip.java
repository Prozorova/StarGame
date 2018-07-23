package com.gb.stargame.base.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.math.ShipShape;

public class StarShip extends Ship {
    private Vector2 touchPosition;
    private Vector2 buff;
    private float maxSpeed;
    private float coef;
    private boolean movingX, movingY;
    private Vector2 delta;
    private Vector2 checkSpeed;
    private boolean braking;
    private boolean autoFire;
    private boolean fire;
    private int maxHP;

    public StarShip(TextureRegion tShip, TextureRegion tBullet, Sound shotSound, Rect worldBounds, boolean autoFire, int hp) {
        super(tShip, 1, 2, 2, worldBounds);
        this.bulletRegion = tBullet;
        this.bulletHeight = 0.01f;
        this.bulletV = 0.5f;
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;
        this.shotSound = shotSound;
        touchPosition = new Vector2(-1, -100);
        speed = new Vector2(0, 0);
        delta = new Vector2();
        movingX = movingY = false;
        buff = new Vector2();
        coef = 0.0003f;
        checkSpeed = new Vector2(100, 100);
        this.hp = this.maxHP = hp;
        this.autoFire = autoFire;
        fire = false;
        hitBox = new ShipShape(this.getWidth(), this.getHeight(), ShipShape.HitBoxTypes.PLAYER);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public void move(Vector2 touched) {
        if (!isMe(touched)) {
            touchPosition.set(touched);
            movingX = movingY = true;
            braking = false;
            updateDelta();
        }
    }

    public void move(Direction direction, boolean moving) {
        touchPosition.set(-1, -100);
        checkSpeed = new Vector2(100, 100);
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

    @Override
    public void update(float d) {
        super.update(d);
        if (autoFire || fire) {
            reloadTimer += d;
            if (reloadTimer >= reloadInterval) {
                reloadTimer = 0f;
                shoot();
            }
        }
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
        hitBox.update(this.pos);
        if (!touchPosition.epsilonEquals(-1, -100)) {
            boolean cond = speed.len() > maxSpeed / 3;
            if (braking && cond) {
                return;
            } else if (braking && !cond) {
                braking = false;
            }
            if (!braking && Math.abs(speed.angle(delta)) > 90 && cond) {
                delta.set(speed).scl(-0.3f);
                braking = true;
                return;
            }
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
            if (Math.abs(speed.x) > Math.abs(checkSpeed.x)) delta.x *= -1;
            if (Math.abs(speed.y) > Math.abs(checkSpeed.y)) delta.y *= -1;
        }
    }

    private void updateDelta() {
        buff.set(touchPosition);
        delta.set(buff.sub(pos).scl(coef * 15));
    }

    public void fire() {
        fire = true;
    }

    public void stopFire() {
        fire = false;
    }

    public void impact() {
        movingY = false;
        movingX = false;
        touchPosition.set(-1, -100);
        speed.x *= -1;
        delta.set(speed).scl(-0.03f);
        checkSpeed.set(speed);
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void addHP() {
        hp += getMaxHP() / 10;
        if (hp > getMaxHP()) hp = getMaxHP();
    }

    public enum Direction {LEFT, RIGHT, UP, DOWN}

    @Override
    public void destroy() {
        super.destroy();
        hp = 0;
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.x = pos.x * worldBounds.getWidth() / getChangeX();
        setHeightProportion(worldBounds.getHeight() * 0.1f);
        maxSpeed = 0.01f;
        hitBox = new ShipShape(this.getWidth(), this.getHeight(), ShipShape.HitBoxTypes.PLAYER);
    }

    @Override
    public void stop() {
        super.stop();
        movingY = false;
        movingX = false;
        touchPosition.set(-1, -100);
        delta.set(0, 0);
        this.reloadInterval =1000f;
    }
}
