package com.gb.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Base2DScreen;
import com.gb.stargame.base.ScreenManager;
import com.gb.stargame.base.sprites.MainMenu;
import com.gb.stargame.base.sprites.ScaledTouchUpButton;
import com.gb.stargame.base.utils.ActionListener;

public class MenuScreen extends Base2DScreen implements ActionListener {
    private MainMenu mainMenu;
    private TextureAtlas atlas;

    private static final int BUTTON_AMOUNT = 4;

    private ScaledTouchUpButton[] buttons = new ScaledTouchUpButton[BUTTON_AMOUNT];

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas("menuAtlas.tpack");
        TextureRegion textureLogo = atlas.findRegion("stargame_logo");
        TextureRegion[] exitRegion = {atlas.findRegion("EXIT1"), atlas.findRegion("EXIT2")};
        TextureRegion[] playRegion = {atlas.findRegion("PLAY1"), atlas.findRegion("PLAY2")};
        TextureRegion[] optionsRegion = {atlas.findRegion("OPTIONS1"), atlas.findRegion("OPTIONS2")};
        TextureRegion[] scoreRegion = {atlas.findRegion("SCORE1"), atlas.findRegion("SCORE2")};
        mainMenu = new MainMenu(new TextureRegion(textureSpace), textureLogo);
        buttons[0] = new ScaledTouchUpButton(playRegion, this, PRESS_SCALE);
        buttons[1] = new ScaledTouchUpButton(scoreRegion, this, PRESS_SCALE);
        buttons[2] = new ScaledTouchUpButton(optionsRegion, this, PRESS_SCALE);
        buttons[3] = new ScaledTouchUpButton(exitRegion, this, PRESS_SCALE);
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
        for (ScaledTouchUpButton button : buttons)
            button.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        mainMenu.resize(worldBounds);
        for (int i = 0; i < BUTTON_AMOUNT; i++)
            buttons[i].resize(worldBounds, i);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        super.touchDown(touch, pointer);
        for (ScaledTouchUpButton button : buttons)
            button.touchDown(touch, pointer);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        super.touchUp(touch, pointer);
        for (ScaledTouchUpButton button : buttons)
            button.touchUp(touch, pointer);
    }

    @Override
    public void mouseMoved(Vector2 v) {
        for (ScaledTouchUpButton button : buttons)
            button.mouseMoved(v);
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttons[3]) {
            Gdx.app.exit();
        } else if (src == buttons[0]) {
            screenManager.switchScreens(ScreenManager.ScreenType.GAME);
        } else {
            System.out.println("Пока нет реализации");
//            throw new RuntimeException("Unknown src");
        }
    }
}
