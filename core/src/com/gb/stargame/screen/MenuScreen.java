package com.gb.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.gb.stargame.base.Base2DScreen;
import com.gb.stargame.base.sprites.MainMenu;

public class MenuScreen extends Base2DScreen {
    private Texture textureLogo;
    private MainMenu mainMenu;
    private TextureAtlas atlas;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        textureLogo = new Texture("stargame_logo.jpg");
        atlas = new TextureAtlas("menuAtlas.tpack");
        TextureRegion exitRegion = atlas.findRegion("btExit");
        TextureRegion playRegion = atlas.findRegion("btPlay");
        mainMenu = new MainMenu(new TextureRegion(textureSpace), new TextureRegion(textureLogo), exitRegion, playRegion, this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        mainMenu.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        mainMenu.resize(worldBounds);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        mainMenu.touch(touch);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        mainMenu.release(touch);
    }

    @Override
    public void dispose() {
        textureLogo.dispose();
        atlas.dispose();
        super.dispose();
    }
}
