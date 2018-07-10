package com.gb.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Base2DScreen;
import com.gb.stargame.base.sprites.*;
import com.gb.stargame.base.utils.ActionListener;

public class GameOverScreen extends Base2DScreen implements ActionListener {
    private MainMenu mainMenu;
    private TextureAtlas atlas;
    private ScaledTouchUpButton toMainMenuButton;

    public GameOverScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas("gameOverAtlas.tpack");
        TextureRegion textureLabel = atlas.findRegion("GameOver");
        TextureRegion[] toMainMenu = {atlas.findRegion("MainMenuButton1"), atlas.findRegion("MainMenuButton2")};
        mainMenu = new MainMenu(new TextureRegion(textureSpace), textureLabel);
        toMainMenuButton = new ScaledTouchUpButton(toMainMenu, this, MenuScreen.PRESS_SCALE);
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

    public void draw() {
        toMainMenuButton.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        mainMenu.resize(worldBounds);
        toMainMenuButton.resize(worldBounds);
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
    }

    @Override
    public void mouseMoved(Vector2 v) {
        toMainMenuButton.mouseMoved(v);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        super.dispose();
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == toMainMenuButton)
            game.setScreen(new MenuScreen(game));     //не работает на десктопе:
//                                                      A fatal error has been detected by the Java Runtime Environment:
//                                                      EXCEPTION_ACCESS_VIOLATION (0xc0000005) at pc=0x00007ffa4e9455d7, pid=11668, tid=0x0000000000000ee0
//            Gdx.app.exit();
    }
}
