package com.gb.stargame.base.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Base2DScreen;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.base.math.ShipShape;

public class EnemyShip extends Ship {
    private enum State {DESCENT, FIGHT, STOP, GOTOPOSITION}

    private State state;
    private Vector2 descentV = new Vector2(0, -0.15f);

    private static boolean shipReturn = Base2DScreen.shipReturn;
    private float goToPositionInterval = 4f;
    private float goToPositionTimer;

    public EnemyShip() {
        super();
        this.state = State.DESCENT;
    }

    @Override
    public void resize(Rect worldBounds) {
        pos.x = pos.x * worldBounds.getWidth() / getChangeX();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state) {
            case DESCENT:
                pos.mulAdd(descentV, delta);
                if (getTop() <= worldBounds.getTop()) {
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0f;
                    shoot();
                }
                pos.mulAdd(speed, delta);
                hitBox.update(this.pos);
                if (isOutside(worldBounds)) {
                    if (shipReturn) {
                        state = State.GOTOPOSITION;
                    } else destroy();
                }
                break;
            case GOTOPOSITION:
                goToPositionTimer += delta;
                if (goToPositionTimer >= goToPositionInterval) {
                    goToPositionTimer = 0f;
                    state = State.DESCENT;
                    pos.set(pos.x, worldBounds.getTop() + getHalfHeight());
                }
                break;
        }
    }

    public void set(
            TextureRegion[] regions,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval,
            float height,
            int hp,
            Sound sound,
            ShipShape.HitBoxTypes type
    ) {
        this.regions = regions;
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV = bulletVY;
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
        reloadTimer = reloadInterval;
        this.hp = hp;
        this.shotSound = sound;
        this.hitBox = new ShipShape(this.getWidth(), this.getHeight(), type);
    }

    public void changeState() {
        state = State.STOP;
    }
}
