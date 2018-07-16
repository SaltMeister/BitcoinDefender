package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class menuScreen
{
private Sprite homescreen;
private Sprite playbutton;
private SpriteBatch batch;
protected boolean gamestarted=false;

public menuScreen (){
    homescreen=new Sprite(new Texture(Gdx.files.internal("images/game home.png")));
    homescreen.setX(0);
    homescreen.setY(-95);
    batch=new SpriteBatch();
    playbutton=new Sprite(new Texture(Gdx.files.internal("images/game home play button.png")));
    playbutton.setX(100);
    playbutton.setY(100);

}
public void draw(Camera c){
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //Set up our camera
    c.update();
    batch.setProjectionMatrix(c.combined);
    batch.begin();
    homescreen.draw(batch);
    playbutton.draw(batch);
    batch.end();
    if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
    {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

        c.unproject(touchPos);
        if (playbutton.getBoundingRectangle().contains(touchPos.x,touchPos.y)){
            System.out.println("clicked");
            gamestarted = true;
        }
    }

}
}
