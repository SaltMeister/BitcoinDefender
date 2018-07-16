package com.missionbit.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class enemyManager
{
    //Creatures we're actively drawing
    private static ArrayList<Enemy> activeEnemies = new ArrayList<Enemy>();

    //Expired creatures we'll re-use
    private ArrayList<Enemy> pool = new ArrayList<Enemy>(); // reuse them

    //Temporary list for accounting
    private ArrayList<Enemy> removed = new ArrayList<Enemy>(); // remove all of the bullets

    private SpriteBatch batch;

    public enemyManager()
    {
        batch = new SpriteBatch();
    }

    public Enemy spawnEnemy(float directionX)
    {
        Enemy f;

        if(pool.isEmpty())
        {
            f = new Enemy(directionX);
            activeEnemies.add(f);
            f.update();
        }
        else
        {
            f = pool.remove(0);
            f.reset();
            activeEnemies.add(f);
        }

        return f;
    }

    public void update()
    {
        for(Enemy c : activeEnemies)
        {
            c.update();

            if(!c.isActive())
                removed.add(c);
        }

        activeEnemies.removeAll(removed);
        pool.addAll(removed);
        removed.clear();
    }

    public static ArrayList<Enemy> getActiveEnemies()
    {
        return activeEnemies;
    }

    public void draw(Camera camera)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for(Enemy b : activeEnemies)
        {
            b.Draw(batch);
        }

        batch.end();
    }
}
