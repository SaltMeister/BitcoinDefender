package com.missionbit.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionbit.game.states.GameStateManager;
import com.missionbit.game.states.MenuState;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BitcoinDefender extends ApplicationAdapter
{
    private SpriteBatch myBatch;
    private GameStateManager gsm;

    @Override
    public void create()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        myBatch = new SpriteBatch();
        gsm = new GameStateManager();
        gsm.push(new MenuState(gsm));
    }

    @Override
    public void render()
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(myBatch);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        myBatch.dispose();
    }
}