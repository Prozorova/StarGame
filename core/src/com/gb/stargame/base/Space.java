package com.gb.stargame.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Space {
    private Texture textureSpace;
    private Texture[] textureStar;
    private Star[] stars;
    private float x, y;
    private float scale;
    private float changeX;

    public Space(float x, float y, float scale) {
        changeX = -1f;
        setXY(x, y, x);
        this.scale = scale;
        textureSpace = new Texture("space.jpg");
        textureStar = new Texture[]{new Texture("starBlue.png"), new Texture("starYellow.png")};
        stars = new Star[70];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    private class Star {
        private Texture texture;
        private Vector2 position;
        private float speed;
        private float starScale;

        Star() {
            texture = textureStar[MathUtils.random(1)];
            starScale = scale * MathUtils.random(0.02f, 0.05f);
            position = new Vector2(MathUtils.random(-x / 2, (x - starScale) / 2), MathUtils.random(-y / 2, y / 2));
            speed = 0.07f * starScale;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureSpace, -x / 2, -y / 2, x, y);
        float scale;
        for (Star star : stars) {
            if (MathUtils.random(0, 300) < 0) {
                scale = star.starScale * MathUtils.random(1.5f, 3f);
            } else scale = star.starScale;
            star.position.y -= star.speed;
            if (star.position.y < -y / 2 - 0.1f) star.position.y = y / 2 + 0.1f;
            batch.draw(star.texture, star.position.x, star.position.y, 0, 0, 1, 1, scale, scale, 0, 0, 0, 32, 23, false, false);
        }
    }

    public void dispose() {
        textureSpace.dispose();
        for (Texture txt : textureStar)
            txt.dispose();
    }

    public void setXY(float x, float y, float changeX) {
        this.x = x;
        this.y = y;
        if (this.changeX != -1f)
            for (Star star : stars) {
                star.position.x = star.position.x * changeX / this.changeX;
            }
        this.changeX = changeX;

    }
}
