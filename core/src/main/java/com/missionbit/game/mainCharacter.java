package com.missionbit.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class mainCharacter
{
    public Animation mainCharacterAnimation;
    public Texture mainCharacter;
    public Vector2 mainCharacterPositon;
    public Rectangle bounds;

    public mainCharacter(int x, int y)
    {
        mainCharacterPositon = new Vector2(x, y);
        mainCharacter = new Texture("images/doubleBarrelShotgun.png");
        bounds = new Rectangle(x, y, mainCharacter.getWidth() / 3, mainCharacter.getHeight() / 3);

        mainCharacterAnimation = new Animation(new TextureRegion(mainCharacter), 2, 0.5f);
    }

    public void update()
    {
        bounds.setPosition(mainCharacterPositon.x, mainCharacterPositon.y);
    }
    public float getX()
    {
        return mainCharacterPositon.x;
    }
    public float getY()
    {
        return mainCharacterPositon.y;
    }

    public float getWidth()
    {
            return mainCharacter.getWidth();
    }

    public float getHeight()
    {
        return mainCharacter.getHeight();
    }
    public Vector2 getMainCharacterPositon()
    {
        return mainCharacterPositon;
    }

    public TextureRegion getTexture()
    {
        return mainCharacterAnimation.getFrame();
    }

    public void dispose()
    {
        mainCharacter.dispose();
    }

}
