package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    public Sprite enemy;
    public Vector2 direction;

    public Enemy(float startX, float startY, float directionX, float directionY)
    {
        direction = new Vector2();
        direction.x = directionX;
        direction.y = directionY; //0.1f; //todo hack
        System.out.println(directionY);
        float randomSpawn = MathUtils.random() * 200;

        enemy = new Sprite( new Texture(Gdx.files.internal("images/enemyDefault.png")));// add a image for the background
        enemy.setX(Gdx.graphics.getWidth());// enemies spawn on the outside of the right side
        enemy.setY(randomSpawn);
    }

    public void Draw(SpriteBatch sprite)
    {
        enemy.setX(enemy.getX() + direction.x * -0.003f);

        enemy.draw(sprite);
    }
}
