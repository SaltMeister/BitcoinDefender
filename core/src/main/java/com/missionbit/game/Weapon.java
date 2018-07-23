package com.missionbit.game;

import com.missionbit.game.animations.mainCharacter;
import com.missionbit.game.manager.bulletManager;

public class Weapon
{
    public int damage;
    public int size;
    public int bullets;
    public int fireRate;

    public Weapon(int startdamage, int startsize, int startfireRate)
    {
        damage = startdamage;
        size = startsize;
        bullets = startsize;
        fireRate = startfireRate;
    }
    public void reload()
    {
        bullets = size;
    }

    public boolean fire(float startx, float starty, float directionx, float directiony, bulletManager manager)
    {
       if(bullets > 0 && !mainCharacter.isReloading)
       {
           for (int loop = 0; loop <= 5; loop++)
               manager.spawnBullet(startx, starty, directionx, directiony, true);
           bullets = bullets - 1;
           return true;
       }

       return false;
    }

    public int showMaxBullets()
    {
        return size;
    }
}
