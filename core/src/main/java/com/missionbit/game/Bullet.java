package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bullet
{
    //variables
    public Sprite bullet;
    public Vector2 direction;
    public boolean alive;
    public float x;
    public float y;


    public Bullet(float startX, float startY, float directionX, float directionY, boolean isRandom) {
        direction = new Vector2();
        direction.x = directionX;
        direction.y = directionY; //0.1f; //todo hack
        alive = true;
        x = startX;
        y = startY;

        bullet = new Sprite(new Texture(Gdx.files.internal("images/bullet.png")));// loads the bullet image
        bullet.setX(x);
        bullet.setY(y);

        if(isRandom == true)
        {
            // code for a random spread in bullets, SHOGUNS
            float randomDirection = MathUtils.random() * 10 - 5;
            direction.rotate(randomDirection);
        }
    }

    public float getX()
    {
        return bullet.getX(); // gets the x value of the bullet
    }

    public float getY()
    {
        return bullet.getY(); // gets the y value of the bullet
    }

    public void reset(){
        alive = true;
        direction = new Vector2(MathUtils.random() * 300, MathUtils.random() * 300);
        bullet.setX(0);
        bullet.setY(0);
    }

    public void update()
    {
        bullet.setX(x);
        bullet.setY(y);

        bullet.setX(bullet.getX() + direction.x * 10); // sets the velocity of the bullet in set direction
        bullet.setY(bullet.getY() + direction.y * 10);
    }

    public void handleClick()
    {

    }

    public boolean isActive()
    {
        return alive;
    }

    public void Draw(SpriteBatch sprite)
    {
        bullet.setX(bullet.getX() + direction.x * 10); // sets the velocity of the bullet in set direction
        bullet.setY(bullet.getY() + direction.y * 10); // ^^^

        bullet.draw(sprite); // draws bullet
    }
}

