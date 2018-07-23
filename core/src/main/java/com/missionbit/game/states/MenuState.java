package com.missionbit.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MenuState extends State
{
    private Texture homeScreen;
    private Sprite playButton;
    private Sprite helpButton;
    private Sprite exitTutorial;
    private Sprite tutorial;
    protected boolean showHelp;

    public MenuState(GameStateManager gsm)
    {
        super(gsm);
        showHelp = false;

        homeScreen = new Texture(Gdx.files.internal("images/menu.png"));

        playButton = new Sprite(new Texture(Gdx.files.internal("images/playButton.png")));
        playButton.setX(250);
        playButton.setY(100);

        helpButton = new Sprite(new Texture(Gdx.files.internal("images/helpButton.png")));
        helpButton.setX(450);
        helpButton.setY(100);

        tutorial =new Sprite(new Texture(Gdx.files.internal("images/tutorial.png")));
        exitTutorial = new Sprite(new Texture(Gdx.files.internal("images/leaveTutorialButton.png")));
        exitTutorial.setX(25);
        exitTutorial.setY(400);

        cam.setToOrtho(false, 800, 480);
    }

    @Override
    public void handleInput()
    {
        if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            cam.unproject(touchPos);

            if (playButton.getBoundingRectangle().contains(touchPos.x, touchPos.y))
                gsm.set(new PlayState(gsm));

            if (helpButton.getBoundingRectangle().contains(touchPos.x, touchPos.y))
                gsm.push(new TutorialState(gsm));
        }
    }

    @Override
    public void update(float dt)
    {
        handleInput();
    }

    @Override
    public void render(SpriteBatch batch)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set up our camera
        batch.setProjectionMatrix(cam.combined);

        batch.begin();

        if (showHelp == false)
        {
            batch.draw(homeScreen, 0, -95);
            playButton.draw(batch);
            helpButton.draw(batch);
        }
        else
        {
            tutorial.draw(batch);
            exitTutorial.draw(batch);
        }

        batch.end();
    }

    @Override
    public void dispose()
    {
        homeScreen.dispose();
        System.out.println("Disposing of Menu State");
    }
}

