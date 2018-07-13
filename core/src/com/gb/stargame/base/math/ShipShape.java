package com.gb.stargame.base.math;

import com.badlogic.gdx.math.Vector2;

public class ShipShape extends Rect {
    private Rect part1, part2, part3;
    private HitBoxTypes type;

    public enum HitBoxTypes {PLAYER, SMALL_ENEMY, MEDIUM_ENEMY, LARGE_ENEMY}

    public ShipShape(Vector2 pos, float width, float height, HitBoxTypes type) {
        super();
        switch (type) {
            case PLAYER:
                part1 = new Rect(pos.x, pos.y - height / 4, width / 2, height / 4);
                part2 = new Rect(pos.x, pos.y + height / 4, width / 8, height / 4);
                break;
            case SMALL_ENEMY:
                part1 = new Rect(pos.x, pos.y + height / 4, width / 2, height / 4);
                part2 = new Rect(pos.x, pos.y - height / 4, width / 4, height / 4);
                break;
            case MEDIUM_ENEMY:
                part1 = new Rect(pos.x, pos.y + height / 4, width / 2, height / 4);
                part2 = new Rect(pos.x, pos.y - height / 4, width / 8, height / 4);
                break;
            case LARGE_ENEMY:
                part1 = new Rect(pos.x, pos.y + height / 8, width / 2, height * 0.375f);
                part2 = new Rect(pos.x - width * 0.375f, pos.y - height * 0.375f, width / 8, height / 8);
                part3 = new Rect(pos.x + width * 0.375f, pos.y - height * 0.375f, width / 8, height / 8);
                break;
        }
        this.type = type;
    }

    public void update(Vector2 shipPosition) {
        switch (type) {
            case PLAYER:
                part1.pos.set(shipPosition.x, shipPosition.y - part1.getHalfHeight());
                part2.pos.set(shipPosition.x, shipPosition.y + part1.getHalfHeight());
                break;
            case SMALL_ENEMY:
            case MEDIUM_ENEMY:
                part1.pos.set(shipPosition.x, shipPosition.y + part1.getHalfHeight());
                part2.pos.set(shipPosition.x, shipPosition.y - part1.getHalfHeight());
                break;
            case LARGE_ENEMY:
                part1.pos.set(shipPosition.x, shipPosition.y + part1.getHalfHeight() / 3);
                part2.pos.set(shipPosition.x - 3 * part2.getHalfWidth(), shipPosition.y - 3 * part2.getHalfHeight());
                part3.pos.set(shipPosition.x + 3 * part2.getHalfWidth(), shipPosition.y - 3 * part2.getHalfHeight());
                break;
        }
    }

    @Override
    public boolean isOutside(Rect other) {
        boolean isPart3 = true;
        if (part3 != null) isPart3 = part3.isOutside(other);
        return part1.isOutside(other) && part2.isOutside(other) && isPart3;
    }
}
