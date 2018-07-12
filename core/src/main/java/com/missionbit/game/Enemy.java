package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy
{
    public final int ENEMY_HP = 100;
    //public Sprite enemy;
    public Vector2 direction;
    public Vector2 position;
    private float lastDistance;
    public boolean alive;
    public int health;
    public Sprite healthbar;
    private Animation<Texture> animation;
    private Array<Texture> frames = new Array<Texture>();
    private float animationTime;
    public int enemyWidth;
    public int enemyHeight;

    public Enemy(float directionX)
    {
        position = new Vector2();
        direction = new Vector2();
        direction.x = directionX;

        float randomSpawn = MathUtils.random() * 200;
        health = ENEMY_HP;
        lastDistance = 5000;

        //enemy = new Sprite(new Texture(Gdx.files.internal("images/enemyDefault.png")));
        position.x = Gdx.graphics.getWidth();// enemies spawn on the outside of the right side
        position.y = randomSpawn; // randomizes the spawn of the enemy
        alive = true;
        healthbar = new Sprite(new Texture(Gdx.files.internal("images/Healthbar.png")));
        healthbar.setX(position.x);
        healthbar.setY(position.y);

        frames.add(new Texture(Gdx.files.internal("images/enemyDefault.png")));
        frames.add(new Texture(Gdx.files.internal("images/enemyDefaultWalking2.png")));
        frames.add(new Texture(Gdx.files.internal("images/enemyDefault.png")));
        frames.add(new Texture(Gdx.files.internal("images/enemyDefaultWalking1.png")));
        animation = new Animation<Texture>(0.25f, frames);
        animationTime = 0;

        enemyWidth = frames.get(0).getWidth();
        enemyHeight = frames.get(0).getHeight();

    }


    public float getX()
    {
        return position.x; // gets the enemies x value
    }

    public float getY()
    {
        return position.y; // gets the enemies y value
    }

    public void update()
    {
        position.x = position.x + direction.x * -0.009f; // moved the enemy in a set speed
        healthbar.setX(position.x);
        healthbar.setY(position.y);
        animationTime += Gdx.graphics.getDeltaTime();
        System.out.println(Gdx.graphics.getDeltaTime());

    }

    public boolean collideWithFence(Vector2 fenceStart, Vector2 fenceEnd)
    {
        float distance = Intersector.distanceLinePoint(fenceStart.x, fenceStart.y, fenceEnd.x, fenceEnd.y, position.x, position.y);
        lastDistance = distance;

        return distance < 10 || lastDistance < distance;
    }

    public boolean collideWithBullet(Bullet b) // checks if enemy has touch the bullet
    {
        if (!alive)
            return false;
        return  b.getX() > getX() && //returns true or false if bullet hit the enemy
                b.getX() < getX() + enemyWidth &&
                b.getY() > getY() &&
                b.getY() < getY() + enemyHeight;
    }

    public void stopEnemy()
    {
        //enemy.setX(enemy.getX() + direction.x * 0.009f); // moved the enemy in a set speed
        direction.setZero();
    }

    public void Draw(SpriteBatch sprite)
    {
        if(alive)
        {
            float healthpercent = health / (float)ENEMY_HP;
            Texture draw = animation.getKeyFrame(animationTime, true);
            sprite.draw(draw, position.x, position.y, enemyWidth, enemyHeight);
            sprite.draw(healthbar, position.x,position.y - 5,enemyWidth * healthpercent,5);
        }
        else // draw death animation and other stuff
        {

        }
    }
    public void dodamage(int damage)
    {
        health=health-damage;

        if(health <= 0){
            alive = false;
        }
    }
}
