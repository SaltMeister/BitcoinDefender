package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Enemy
{
    public final int ENEMY_HP = 100;
    public Sprite enemy;
    public Vector2 direction;
    private float lastDistance;
    public boolean alive;
    public int health;

    public Enemy(float directionX)
    {
        direction = new Vector2();
        direction.x = directionX;

        float randomSpawn = MathUtils.random() * 200;
        health = ENEMY_HP;
        lastDistance = 5000;

        enemy = new Sprite(new Texture(Gdx.files.internal("images/enemyDefault.png")));// add a image for the background
        enemy.setX(Gdx.graphics.getWidth());// enemies spawn on the outside of the right side
        enemy.setY(randomSpawn); // randomizes the spawn of the enemy
    }


    public float getX()
    {
        return enemy.getX(); // gets the enemies x value
    }

    public float getY()
    {
        return enemy.getY(); // gets the enemies y value
    }

    public void update()
    {
        enemy.setX(enemy.getX() + direction.x * -0.009f); // moved the enemy in a set speed
    }

    public boolean collideWithFence(Vector2 fenceStart, Vector2 fenceEnd)
    {
        float distance = Intersector.distanceLinePoint(fenceStart.x, fenceStart.y, fenceEnd.x, fenceEnd.y, enemy.getX(), enemy.getY());
        lastDistance = distance;
        return distance < 10 || lastDistance < distance;

    }

    public boolean collideWithBullet(Bullet b) // checks if enemy has touch the bullet
    {
        return  b.getX() > getX() && //returns true or false if bullet hit the enemy
                b.getX() < getX() + enemy.getWidth() &&
                b.getY() > getY() &&
                b.getY() < getY() + enemy.getHeight();
    }

    public void stopEnemy()
    {
        //enemy.setX(enemy.getX() + direction.x * 0.009f); // moved the enemy in a set speed
        direction.setZero();
    }

    public void Draw(SpriteBatch sprite)
    {
        enemy.draw(sprite); // draws the enemy
    }
}
