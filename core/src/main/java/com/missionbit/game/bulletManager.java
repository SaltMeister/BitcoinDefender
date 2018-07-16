package com.missionbit.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class bulletManager
{
    //Creatures we're actively drawing
    private static ArrayList<Bullet> activeBullets = new ArrayList<Bullet>();

    //Expired creatures we'll re-use
    private ArrayList<Bullet> pool = new ArrayList<Bullet>(); // reuse them

    //Temporary list for accounting
    private ArrayList<Bullet> removed = new ArrayList<Bullet>(); // remove all of the bullets

    private SpriteBatch batch;

    public bulletManager()
    {
        batch = new SpriteBatch();
    }

    public Bullet spawnBullet(float startX, float startY, float directionX, float directionY, boolean isRandom)
    {
        Bullet f;
        if(pool.isEmpty())
        {
            f = new Bullet(startX, startY, directionX, directionY, isRandom);
            activeBullets.add(f);
            //f.update();
            //System.out.println("New");
        }
        else
        {
            f = pool.remove(0);
            f.reset(startX, startY, directionX, directionY, isRandom);
            //f.update();
            activeBullets.add(f);
            //System.out.println("From pool");
        }

        return f;
    }

    public void update()
    {
        for(Bullet c : activeBullets)
        {
            if(!c.isActive())
                removed.add(c);
        }

        activeBullets.removeAll(removed);
        pool.addAll(removed);
        removed.clear();
    }

    public static ArrayList<Bullet> getActiveBullets()
    {
        return activeBullets;
    }

    public void draw(Camera camera)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for(Bullet b : activeBullets)
            b.Draw(batch);
        batch.end();
    }
}

