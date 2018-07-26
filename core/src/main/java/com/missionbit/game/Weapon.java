package com.missionbit.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.missionbit.game.animations.mainCharacter;
import com.missionbit.game.manager.bulletManager;

public class Weapon
{
    private ParticleEffect autoRifleFlash;
    private long startTimeBullet = System.currentTimeMillis();
    private long bulletElapsedTime;
    private float autoRiflePositionX, autoRiflePositionY;
    public int damage;
    public int size;
    public int bullets;
    public int fireRate;
    private Sound autoRifle;

    public Weapon(int startdamage, int startsize, int startfireRate)
    {
        damage = startdamage;
        size = startsize;
        bullets = startsize;
        fireRate = startfireRate;

        autoRifleFlash = new ParticleEffect();
        autoRifleFlash.load(Gdx.files.internal("particles/muzzleFlashAuto.p"), Gdx.files.internal("images"));

        autoRifle = Gdx.audio.newSound(Gdx.files.internal("sounds/autoRifleShot.mp3"));
        autoRifle.setLooping(1, false);
        autoRifle.setVolume(1, 0.25f); // auto rifle noise
    }

    public void reload()
    {
        bullets = size;
    }

    public boolean fire(float startx, float starty, float directionx, float directiony, bulletManager manager, int weapon)
    {
        bulletElapsedTime = System.currentTimeMillis() - startTimeBullet;

        if(bullets > 0 && !mainCharacter.isReloading)
        {
            if (weapon == 1)
            {
                if (bulletElapsedTime >= fireRate)
                {
                    manager.spawnBullet(startx, starty, directionx, directiony, false);

                    autoRifle.play();
                    autoRifleFlash.setPosition(autoRiflePositionX, autoRiflePositionY);

                    if (autoRifleFlash.isComplete())
                        autoRifleFlash.start();

                    bullets = bullets - 1;
                    startTimeBullet = System.currentTimeMillis();
                }
            }

            else if (weapon == 2)
            {
                for (int loop = 0; loop <= 5; loop++)
                    manager.spawnBullet(startx, starty, directionx, directiony, true);
                bullets = bullets - 1;
            }

            return true;
        }

        return false;
    }

    public int showMaxBullets()
    {
        return size;
    }

    public void setParticlePositionAutoRifle(float x, float y)
    {
        autoRiflePositionX = x;
        autoRiflePositionY = y;
    }

    public void draw(SpriteBatch batch, float dt)
    {
        autoRifleFlash.draw(batch, dt);
    }
}