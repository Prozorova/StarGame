package com.gb.stargame.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.gb.stargame.screen.*;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager {
    public enum ScreenType {MENU, GAME, OPTIONS, SCORE, GAMEOVER}

    private Game game;
    private List<Screen> screens = new ArrayList<>();

    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public void startGame(Game game) {
        this.game = game;
        screens.add(new MenuScreen());
        switchScreens(ScreenType.MENU);
    }

    private  <T extends Screen> T initNewScreen(Class<T> screenType) {
        for (Screen myScreen : screens) {
            if (myScreen.getClass() == screenType) return (T) myScreen;
        }
        try {
            T newScreen = screenType.newInstance();
            screens.add(newScreen);
            return newScreen;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void switchScreens(ScreenType screenType) {
        if (game.getScreen() != null) game.getScreen().dispose();
        switch (screenType) {
            case MENU:
                game.setScreen(initNewScreen(MenuScreen.class));
                break;
            case GAME:
                game.setScreen(initNewScreen(MainScreen.class));
                break;
            case OPTIONS:
//                game.setScreen(initNewScreen(MenuScreen.class));
                break;
            case SCORE:
//                game.setScreen(initNewScreen(MenuScreen.class));
                break;
            case GAMEOVER:
                game.setScreen(initNewScreen(GameOverScreen.class));
                break;
        }
    }
}
