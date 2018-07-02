package com.gb.stargame.base;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.gb.stargame.base.math.*;
import com.gb.stargame.screen.MainScreen;

public class Base2DScreen implements Screen, InputProcessor {
    protected Texture textureSpace;
    protected final float coordY = 1f;
    protected SpriteBatch batch;

    private Rect screenBounds; // границы экрана в пикселях
    protected Rect worldBounds; // границы проэкции мировых координат
    private Rect glBounds; // gl-левские координаты

    private Matrix4 worldToGl;
    private Matrix3 screenToWorld;

    private Vector2 touch = new Vector2();

    private Game game;
    private float scale;

    public Base2DScreen(Game game) {
        this.game = game;
        screenBounds = new Rect();
        worldBounds = new Rect();
        glBounds = new Rect(0, 0, 1f, 1f);
        worldToGl = new Matrix4();
        screenToWorld = new Matrix3();
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
        scale = ((Gdx.graphics.getWidth() + Gdx.graphics.getHeight()) / 3000f + 0.75f) * coordY;
    }

    protected float getScale() {
        return scale;
    }

    @Override
    public void show() {
        textureSpace = new Texture("galaxy-st.jpg");
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);
        float aspect = width / (float) height;
        worldBounds.setHeight(coordY);
        worldBounds.setWidth(coordY * aspect);
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds);
        resize(worldBounds);
        scale = ((width + height) / 3000f + 0.75f) * coordY;
    }

    private void resize(Rect worldBounds) {
        System.out.println("resize w=" + worldBounds.getWidth() + " h=" + worldBounds.getHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        textureSpace.dispose();
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch, pointer);
        return false;
    }

    public void touchDown(Vector2 touch, int pointer) {
        System.out.println("touchDown X=" + touch.x + " Y=" + touch.y);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer);
        return false;
    }

    public void touchUp(Vector2 touch, int pointer) {
        System.out.println("touchUp X=" + touch.x + " Y=" + touch.y);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    private void touchDragged(Vector2 touch, int pointer) {
        System.out.println("touchDragged X=" + touch.x + " Y=" + touch.y);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void setScreen(){
        game.setScreen(new MainScreen(game));
    }
}
