package com.gb.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Base2DScreen;
import com.gb.stargame.base.ScreenManager;
import com.gb.stargame.base.sprites.*;
import com.gb.stargame.base.utils.ActionListener;

public class GameOverScreen extends Base2DScreen implements ActionListener {
    private MainMenu mainMenu;
    private TextureAtlas atlas;
    private ScaledTouchUpButton toMainMenuButton;
    private ScaledTouchUpButton newGameButton;

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas("gameOverAtlas.tpack");
        TextureRegion textureLabel = atlas.findRegion("GameOver");
        TextureRegion[] toMainMenu = {atlas.findRegion("MainMenuButton1"), atlas.findRegion("MainMenuButton2")};
        TextureRegion[] toNewGame = {atlas.findRegion("NewGame1"), atlas.findRegion("NewGame2")};
        mainMenu = new MainMenu(new TextureRegion(textureSpace), textureLabel);
        toMainMenuButton = new ScaledTouchUpButton(toMainMenu, this, PRESS_SCALE);
        newGameButton = new ScaledTouchUpButton(toNewGame, this, PRESS_SCALE);
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
        newGameButton.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        mainMenu.resize(worldBounds);
        toMainMenuButton.resize(worldBounds, 3);
        newGameButton.resize(worldBounds, 2);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        super.touchDown(touch, pointer);
        toMainMenuButton.touchDown(touch, pointer);
        newGameButton.touchDown(touch, pointer);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        super.touchUp(touch, pointer);
        toMainMenuButton.touchUp(touch, pointer);
        newGameButton.touchUp(touch, pointer);
    }

    @Override
    public void mouseMoved(Vector2 v) {
        toMainMenuButton.mouseMoved(v);
        newGameButton.mouseMoved(v);
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == toMainMenuButton)
            screenManager.switchScreens(ScreenManager.ScreenType.MENU);
        else if (src == newGameButton){
            screenManager.switchScreens(ScreenManager.ScreenType.GAME);}
    }
}
