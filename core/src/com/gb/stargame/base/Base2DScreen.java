package com.gb.stargame.base;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.gb.stargame.base.math.*;
import com.gb.stargame.base.utils.Font;

public abstract class Base2DScreen implements Screen, InputProcessor {
    private final float coordY = 1f;
    private Rect screenBounds; // границы экрана в пикселях
    protected Rect worldBounds; // границы проэкции мировых координат
    private Rect glBounds; // gl-левские координаты

    private Matrix4 worldToGl;
    private Matrix3 screenToWorld;

    private Vector2 touch = new Vector2();

    protected static final float PRESS_SCALE = 0.9f;
    protected static ScreenManager screenManager = ScreenManager.getInstance();
    protected static SpriteBatch batch = new SpriteBatch();
    protected static Music music = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
    protected static Texture textureSpace = new Texture("galaxy-st.jpg");

    protected static final float FONT_SIZE = 0.02f;
    protected Font font;

    // ПАРАМЕТРЫ ДЛЯ МЕНЮ OPTIONS
    protected static boolean autoFire = false;                          // автоогонь - будет включаться в настройках
    protected static boolean fireButtonPlace = false;                   // справа - false, слева - true
    protected static int maxEnemyHP = 50;                               // один из параметров сложности: от него считаем все хп
    public static boolean shipReturn = false;                            // один из параметров сложности: если враг не уничтожен - он возвращается в игру
    protected static boolean musicON = true;
    protected static boolean soundON = true;


    public Base2DScreen() {
        if (musicON) {
            music.setVolume(0.3f);
            music.play();
            music.setLooping(true);
        }
        screenBounds = new Rect();
        worldBounds = new Rect();
        glBounds = new Rect(0, 0, 1f, 1f);
        worldToGl = new Matrix4();
        screenToWorld = new Matrix3();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        font = new Font("font/font.fnt", "font/font.png");
        font.setWorldSize(FONT_SIZE);
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
        music.dispose();
        font.dispose();
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
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer);
        return false;
    }

    public void touchUp(Vector2 touch, int pointer) {
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    private void touchDragged(Vector2 touch, int pointer) {
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        mouseMoved(touch);
        return false;
    }

    public void mouseMoved(Vector2 v) {
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
