package com.missionbit.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameWinState extends State
{

    private Texture gameWin;
    private Sprite retry;

    public GameWinState(GameStateManager gsm)
    {
        super(gsm);

        gameWin = new Texture(Gdx.files.internal("images/gameWin.png"));
        cam.setToOrtho(false, 800, 480);

        retry = new Sprite(new Texture(Gdx.files.internal("images/retryButton.png")));
        retry.setX(350);
        retry.setY(100);
    }

    protected void handleInput()
    {
        if (Gdx.input.isTouched())
            gsm.set(new MenuState(gsm));
    }

    public void update(float dt)
    {
        handleInput();
    }

    public void render(SpriteBatch batch)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set up our camera
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.draw(gameWin, 0, 0);
        retry.draw(batch);
        batch.end();
    }

    public void dispose()
    {
        gameWin.dispose();
    }
}
