package com.gb.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Base2DScreen;
import com.gb.stargame.base.ScreenManager;
import com.gb.stargame.base.sprites.MainMenu;
import com.gb.stargame.base.sprites.ScaledTouchUpButton;
import com.gb.stargame.base.utils.ActionListener;

public class OptionsScreen extends Base2DScreen implements ActionListener {
    private MainMenu mainMenu;
    private TextureAtlas atlas;
    private TextureAtlas atlas2;
    private ScaledTouchUpButton toMainMenuButton;

    private ScaledTouchUpButton[] sliders = new MySliders[4];
    private ScaledTouchUpButton sliderDiff;

    private String[] labels = new String[5];

    @Override
    public void show() {
        super.show();
        font.setColor(Color.YELLOW);
        atlas = new TextureAtlas("optionsAtlas.tpack");
        mainMenu = new MainMenu(new TextureRegion(textureSpace), atlas.findRegion("options"));
        TextureRegion[] slider = new TextureRegion[]{atlas.findRegion("switchOFF"), atlas.findRegion("switchON")};
        TextureRegion[] difficulty = new TextureRegion[]{atlas.findRegion("level1"), atlas.findRegion("level2"),
                atlas.findRegion("level3"), atlas.findRegion("level4"), atlas.findRegion("level5")};
        sliderDiff = new ScaledTouchUpButton(difficulty, this);
        for (int i = 0; i < 4; i++)
            sliders[i] = new MySliders(slider, this);
        labels[0] = new String("Difficulty");
        labels[1] = new String("Autofire");
        labels[2] = new String("Firebutton place left");
        labels[3] = new String("Music");
        labels[4] = new String("Sounds");
        sliderDiff.setColor(2);
        sliders[2].setColor(1);
        sliders[3].setColor(1);
        atlas2 = new TextureAtlas("gameOverAtlas.tpack");
        TextureRegion[] toMainMenu = {atlas2.findRegion("MainMenuButton1"), atlas2.findRegion("MainMenuButton2")};
        toMainMenuButton = new ScaledTouchUpButton(toMainMenu, this, PRESS_SCALE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        mainMenu.draw(batch);
        draw();
        batch.end();
    }

    private void draw() {
        toMainMenuButton.draw(batch);
        sliderDiff.draw(batch);
        for (ScaledTouchUpButton slider : sliders)
            slider.draw(batch);
        for (int i = 0; i < 5; i++) {
            font.draw(batch, labels[i], worldBounds.getLeft() + 0.1f, 0.1f * (1 - i));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        mainMenu.resize(worldBounds);
        sliderDiff.resize(worldBounds, -1);
        sliderDiff.setRight(worldBounds.getRight() - 0.05f);
        for (int i = 0; i < 4; i++) {
            sliders[i].resize(worldBounds, i);
            sliders[i].setRight(worldBounds.getRight() - 0.05f);
        }
        toMainMenuButton.resize(worldBounds, 4);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        super.touchDown(touch, pointer);
        toMainMenuButton.touchDown(touch, pointer);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        super.touchUp(touch, pointer);
        toMainMenuButton.touchUp(touch, pointer);
        int k = sliderDiff.touchUp(touch);
        if (k != 0) {
            sliderDiff.setColor(k - 1);
            actionPerformed(sliderDiff);
        }
        for (ScaledTouchUpButton slider : sliders)
            slider.touchUp(touch, pointer);
    }

    @Override
    public void mouseMoved(Vector2 v) {
        toMainMenuButton.mouseMoved(v);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        atlas2.dispose();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == sliderDiff) {
            switch (sliderDiff.getColor()) {
                case 0:
                    maxEnemyHP = 25;
                    shipReturn = false;
                    break;
                case 1:
                    maxEnemyHP = 40;
                    shipReturn = false;
                    break;
                case 2:
                    maxEnemyHP = 50;
                    shipReturn = false;
                    break;
                case 3:
                    maxEnemyHP = 75;
                    shipReturn = true;
                    break;
                case 4:
                    maxEnemyHP = 100;
                    shipReturn = true;
                    break;
            }
        } else if (src == sliders[0]) {
            autoFire = !autoFire;
            sliders[0].setColor(autoFire ? 1 : 0);
        } else if (src == sliders[1]) {
            fireButtonPlace = !fireButtonPlace;
            sliders[1].setColor(fireButtonPlace ? 1 : 0);
        } else if (src == sliders[2]) {
            musicON = !musicON;
            if (musicON) {
                music.setVolume(0.3f);
                music.play();
                music.setLooping(true);
            } else
                music.stop();
            sliders[2].setColor(musicON ? 1 : 0);
        } else if (src == sliders[3]) {
            soundON = !soundON;
            sliders[3].setColor(soundON ? 1 : 0);
        } else if (src == toMainMenuButton)
            screenManager.switchScreens(ScreenManager.ScreenType.MENU);
    }

    private class MySliders extends ScaledTouchUpButton {
        public MySliders(TextureRegion[] region, ActionListener actionListener) {
            super(region, actionListener);
        }

        @Override
        public void touchUp(Vector2 touch, int pointer) {
            if (isMe(touch)) {
                actionPerformed(this);
            }
        }
    }
}
