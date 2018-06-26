package com.gb.stargame.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Space {
    private Texture textureSpace;
    private Texture[] textureStar;
    private Star[] stars;
    private int x, y;

    public Space() {
        textureSpace = new Texture("space.jpg");
        textureStar = new Texture[]{new Texture("starBlue.png"), new Texture("starYellow.png")};
        x = Base2DScreen.x;
        y = Base2DScreen.y;
        stars = new Star[50];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    private class Star {
        private Texture texture;
        private Vector2 position;
        private float scale;
        private float speed;

        Star() {
            texture = textureStar[MathUtils.random(1)];
            position = new Vector2(MathUtils.random(0, x), MathUtils.random(0, y));
            scale = (x + y) / 1000 * MathUtils.random(0.3f, 1f);
            speed = 3f * scale - 1f;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureSpace, 0, 0, x, y);
        float scale;
        for (int i = 0; i < stars.length; i++) {
            if (MathUtils.random(0, 300) < 1) {
                scale = stars[i].scale * MathUtils.random(1.5f, 3f);
            } else scale = stars[i].scale;
            stars[i].position.y -= stars[i].speed;
            if (stars[i].position.y < -50) stars[i].position.y = y + 50;
            batch.draw(stars[i].texture, stars[i].position.x, stars[i].position.y, 12, 12, 32, 23, scale, scale, 0, 0, 0, 32, 23, false, false);
        }
    }
}
