package com.gb.stargame.base.pools;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gb.stargame.base.SpritesPool;
import com.gb.stargame.base.sprites.RepairTool;
import com.gb.stargame.base.sprites.StarShip;

public class RepairToolPool extends SpritesPool<RepairTool> {

    private TextureRegion textureRegion;
    private float generateInterval = 10f;
    private float generateTimer;

    private static final RepairToolPool ourInstance = new RepairToolPool();

    public static RepairToolPool getInstance() {
        return ourInstance;
    }

    public void generateRepTools(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            this.obtain().set();
        }
    }

    public boolean checkCollisions(StarShip ship) {
        for (RepairTool repTool : getActiveObjects())
            if (!ship.hitBox.isOutside(repTool)) {
                repTool.destroy();
                return true;
            }
        return false;
    }

    public void stopGenerating(){
        generateInterval = 1000f;
    }

    public void setTexture(TextureRegion textureRegion) {
        generateInterval = 10f;
        generateTimer = 0f;
        this.textureRegion = textureRegion;
    }

    @Override
    protected RepairTool newObject() {
        return new RepairTool(textureRegion);
    }

}
