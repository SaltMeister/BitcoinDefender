package com.missionbit.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class TutorialState extends State
{
    private Texture tutorial;
    private Sprite exitTutorial;

    public TutorialState(GameStateManager gsm)
    {
        super(gsm);
        tutorial = new Texture(Gdx.files.internal("images/tutorial.png"));

        exitTutorial = new Sprite(new Texture(Gdx.files.internal("images/leaveTutorialButton.png")));
        exitTutorial.setX(25);
        exitTutorial.setY(400);

        cam.setToOrtho(false, 800, 480);
    }

    protected void handleInput()
    {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

        cam.unproject(touchPos);

        if (Gdx.input.justTouched())
        {
            if (exitTutorial.getBoundingRectangle().contains(touchPos.x, touchPos.y))
                gsm.pop();
        }
    }
    public void update(float dt)
    {
        handleInput();
    }

    public void render(SpriteBatch sb)
    {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(tutorial, 0, 0);
        exitTutorial.draw(sb);
        sb.end();
    }

    public void dispose()
    {
        tutorial.dispose();
    }
}
