package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class menuScreen
{
private Sprite homescreen;
private SpriteBatch batch;
public menuScreen (){
    homescreen=new Sprite(new Texture(Gdx.files.internal("images/game home.png")));
    homescreen.setX(0);
    homescreen.setY(-25);
    batch=new SpriteBatch();
}
public void draw(Camera c){
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //Set up our camera
    c.update();
    batch.setProjectionMatrix(c.combined);
    batch.begin();
    homescreen.draw(batch);
    batch.end();


}
}
