package com.missionbit.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ChooseState extends State
{
    private Texture chooseScreen;
    private Sprite shotgunButton, autoRifleButton;
    public int buttonChoice;

    public ChooseState(GameStateManager gsm)
    {
        super(gsm);

        chooseScreen = new Texture(Gdx.files.internal("images/weaponChoiceScreen.png"));

        shotgunButton = new Sprite(new Texture(Gdx.files.internal("images/shotgunChoice.png")));
        shotgunButton.setX(100);
        shotgunButton.setY(175);

        autoRifleButton = new Sprite(new Texture(Gdx.files.internal("images/autoRifleChoice.png")));
        autoRifleButton.setX(550);
        autoRifleButton.setY(175);

        cam.setToOrtho(false, 800, 480);
    }

    protected void handleInput()
    {
        if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            cam.unproject(touchPos);

            if (shotgunButton.getBoundingRectangle().contains(touchPos.x, touchPos.y))
                gsm.set(new PlayState(gsm, 2));

            if (autoRifleButton.getBoundingRectangle().contains(touchPos.x, touchPos.y))
                gsm.set(new PlayState(gsm, 1));

        }
    }

    public void update(float dt)
    {
        handleInput();
    }

    public void render(SpriteBatch sb)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set up our camera
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(chooseScreen, 0, 0);
        shotgunButton.draw(sb);
        autoRifleButton.draw(sb);

        sb.end();
    }

    public void dispose()
    {
        chooseScreen.dispose();
    }
}
