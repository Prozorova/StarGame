package com.gb.stargame.base.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Sprite;
import com.gb.stargame.base.math.Rect;
import com.gb.stargame.screen.MenuScreen;

public class MainMenu extends Sprite {
    private Logo logo;
    private ExitButton exitButton;
    private PlayButton playButton;
    private MenuScreen menuScreen;
    private float scale;

    public MainMenu(TextureRegion region, TextureRegion tLogo, TextureRegion tExit, TextureRegion tPlay, MenuScreen menuScreen) {
        super(region);
        this.menuScreen = menuScreen;
        logo = new Logo(tLogo);
        exitButton = new ExitButton(tExit);
        playButton = new PlayButton(tPlay);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
        logo.resize(worldBounds);
        exitButton.resize(worldBounds);
        playButton.resize(worldBounds);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        logo.draw(batch);
        exitButton.draw(batch);
        playButton.draw(batch);
    }

    public void touch(Vector2 touched) {
        if (exitButton.isMe(touched)) {
            scale = exitButton.getScale();
            exitButton.setScale(scale * 0.9f);
        }
        if (playButton.isMe(touched)) {
            scale = playButton.getScale();
            playButton.setScale(scale * 0.9f);
        }
    }

    public void release(Vector2 touched) {
        if (scale != 0f) {
            exitButton.setScale(scale);
            playButton.setScale(scale);
            scale = 0f;
        }
        if (exitButton.isMe(touched)) {
            menuScreen.dispose();
            System.exit(0);
        }
        if (playButton.isMe(touched)) {
            menuScreen.setScreen();
        }
    }

    private class Logo extends Sprite {

        Logo(TextureRegion region) {
            super(region);
        }

        @Override
        public void resize(Rect worldBounds) {
            setHeightProportion(worldBounds.getHeight() * 0.2f);
            pos.set(0f, worldBounds.getHeight() * 0.3f);
        }

        @Override
        public void draw(SpriteBatch batch) {
            super.draw(batch);
        }
    }

    private class ExitButton extends Sprite {

        ExitButton(TextureRegion region) {
            super(region);
        }

        @Override
        public void resize(Rect worldBounds) {
            setHeightProportion(worldBounds.getHeight() * 0.2f);
            pos.set(worldBounds.getLeft() + halfWidth, worldBounds.getBottom() + halfHeight);
        }

        @Override
        public void draw(SpriteBatch batch) {
            super.draw(batch);
        }
    }

    private class PlayButton extends Sprite {

        PlayButton(TextureRegion region) {
            super(region);
        }

        @Override
        public void resize(Rect worldBounds) {
            setHeightProportion(worldBounds.getHeight() * 0.2f);
            pos.set(worldBounds.getRight() - halfWidth, worldBounds.getBottom() + halfHeight);
        }

        @Override
        public void draw(SpriteBatch batch) {
            super.draw(batch);
        }
    }
}