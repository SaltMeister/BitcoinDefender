package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet
{
    public Sprite bullet;
    public Vector2 direction;

    public Bullet(float startX, float startY, float directionX, float directionY)
    {
        direction = new Vector2();
        direction.x = directionX;
        direction.y = directionY;

        bullet = new Sprite( new Texture(Gdx.files.internal("images/bullet.png")));// add a image for the background
        bullet.setX(startX);
        bullet.setY(startY);
    }

    public void Draw(SpriteBatch sprite)
    {
        bullet.setX(bullet.getX() + direction.x * 10);
        bullet.setY(bullet.getY() + direction.y * 10);

        bullet.draw(sprite);
    }
}