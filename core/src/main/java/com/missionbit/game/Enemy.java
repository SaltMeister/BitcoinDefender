package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Enemy
{
    public Sprite enemy;
    public Vector2 direction;

    public Enemy(float directionX)
    {
        direction = new Vector2();
        direction.x = directionX;

        float randomSpawn = MathUtils.random() * 200;

        enemy = new Sprite(new Texture(Gdx.files.internal("images/enemyDefault.png")));// add a image for the background
        enemy.setX(Gdx.graphics.getWidth());// enemies spawn on the outside of the right side
        enemy.setY(randomSpawn);
        System.out.println(enemy.getY() * 0.55 + 190);
    }


    public float getX()
    {
        return enemy.getX();
    }

    public float getY()
    {
        return enemy.getY();
    }
    public void update()
    {
        enemy.setX(enemy.getX() + direction.x * -0.009f);
    }

    public boolean collideWithFence()
    {
        return enemy.getY() * 0.55 + 190 >= getX();
    }

    public boolean collideWithBullet(Bullet b)
    {
        return  b.getX() > getX() &&
                b.getX() < getX() + enemy.getWidth() &&
                b.getY() > getY() &&
                b.getY() < getY() + enemy.getHeight();
    }

    public void Draw(SpriteBatch sprite)
    {
        enemy.draw(sprite);
    }
}
