package com.missionbit.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState extends State
{

    private Texture GameOverScreen;
    private Music gameOverMusic;

    public GameOverState(GameStateManager gsm)
    {
        super(gsm);

        GameOverScreen = new Texture(Gdx.files.internal("images/gameOverScreen.png"));
        cam.setToOrtho(false, 800, 480);

        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("music/gameOverMusic.mp3"));
        gameOverMusic.setLooping(true);
        gameOverMusic.setVolume(0.5f);
        gameOverMusic.play();
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
        batch.draw(GameOverScreen, 0, 0);
        batch.end();
    }

    public void dispose()
    {
        GameOverScreen.dispose();
    }

}
