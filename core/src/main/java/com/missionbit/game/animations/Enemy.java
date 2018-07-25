package com.missionbit.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.missionbit.game.Bullet;

public class Enemy
{
    private final float INITIAL_DAMAGE_REDUCTION = 1.0f;
    public final int ENEMY_HP = 100;
    public static float damageReduction;
    public Vector2 direction;
    public  Vector2 position;
    private float lastDistance;
    public boolean alive;
    public int health;
    public Sprite healthbar;
    private Sound enemyDeath;
    private Animation<Texture> walkinganimation;
    private Array<Texture> walkingframes = new Array<Texture>();
    private Animation<Texture> attackAnimation;
    private Array<Texture> attackFrames = new Array<Texture>();
    private Animation<Texture> deathAnimation;
    private Array<Texture> deathFrames = new Array<Texture>();
    private float walkinganimationTime;
    private float attackAnimationTime;
    private float deathAnimationTime;
    public int enemyWidthDefault, enemyHeightDefault;
    public int enemyAttackWidth, enemyAttackHeight;;
    public float randomSpawn;
    public boolean isAttack = false;
    public ParticleEffect blood;
    public ParticleEffect wallEffect;

    public Enemy(float directionX)
    {
        damageReduction = INITIAL_DAMAGE_REDUCTION;

        position = new Vector2();
        direction = new Vector2();
        direction.x = directionX;

        randomSpawn = MathUtils.random() * 200;
        health = ENEMY_HP;
        lastDistance = 5000;

        position.x = Gdx.graphics.getWidth();// enemies spawn on the outside of the right side
        position.y = randomSpawn; // randomizes the spawn of the enemy
        alive = true;
        healthbar = new Sprite(new Texture(Gdx.files.internal("images/Healthbar.png")));
        healthbar.setX(position.x);
        healthbar.setY(position.y);

        walkingframes.add(new Texture(Gdx.files.internal("images/enemyDefault.png")));
        walkingframes.add(new Texture(Gdx.files.internal("images/enemyDefaultWalking2.png")));
        walkingframes.add(new Texture(Gdx.files.internal("images/enemyDefault.png")));
        walkingframes.add(new Texture(Gdx.files.internal("images/enemyDefaultWalking1.png")));
        walkinganimation = new Animation<Texture>(0.25f, walkingframes);
        walkinganimationTime = 0;

        enemyWidthDefault = walkingframes.get(0).getWidth();
        enemyHeightDefault = walkingframes.get(0).getHeight();

        attackFrames.add(new Texture(Gdx.files.internal("images/enemyDefaultSwing1.png")));
        attackFrames.add(new Texture(Gdx.files.internal("images/enemyDefaultSwing2.png")));
        attackFrames.add(new Texture(Gdx.files.internal("images/enemyDefaultSwing3.png")));
        attackFrames.add(new Texture(Gdx.files.internal("images/enemyDefaultSwing4.png")));
        attackAnimation = new Animation<Texture>(0.25f, attackFrames);
        attackAnimationTime = 0;

        enemyAttackWidth = attackFrames.get(0).getWidth();
        enemyAttackHeight = attackFrames.get(0).getHeight();

        deathFrames.add(new Texture(Gdx.files.internal("images/enemyDeath1.png")));
        deathFrames.add(new Texture(Gdx.files.internal("images/enemyDeath2.png")));
        deathFrames.add(new Texture(Gdx.files.internal("images/enemyDeath3.png")));
        deathFrames.add(new Texture(Gdx.files.internal("images/enemyDeath4.png")));
        deathFrames.add(new Texture(Gdx.files.internal("images/enemyDeath5.png")));
        deathFrames.add(new Texture(Gdx.files.internal("images/enemyDeath6.png")));
        deathFrames.add(new Texture(Gdx.files.internal("images/enemyDeath7.png")));
        deathAnimation = new Animation<Texture>(0.25f, deathFrames);
        deathAnimationTime = 0;

        blood = new ParticleEffect();
        blood.load(Gdx.files.internal("particles/splatter.p"), Gdx.files.internal("images"));

        wallEffect = new ParticleEffect();
        wallEffect.load(Gdx.files.internal("particles/brokenWall.p"), Gdx.files.internal("images"));

        enemyDeath = Gdx.audio.newSound(Gdx.files.internal("music/enemyDeathNoise.mp3"));
        enemyDeath.setLooping(1,false);
        enemyDeath.setVolume(1, 1f);
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
        position.x = position.x + direction.x * -0.005f; // moved the enemy in a set speed
        healthbar.setX(position.x);
        healthbar.setY(position.y);

        if (isAttack)
            attackAnimationTime += Gdx.graphics.getDeltaTime();
        else
            walkinganimationTime += Gdx.graphics.getDeltaTime();
    }

    public int damageDealt()
    {
        if (attackAnimation.getKeyFrameIndex(attackAnimationTime) == 3)
        {
            wallEffect.setPosition(position.x, position.y + 50);
            wallEffect.start();
            attackAnimationTime = 0;
            return 1;
        }
        else
            return 0;
    }

    public boolean collideWithFence(Vector2 fenceStart, Vector2 fenceEnd)
    {

        float distance = Intersector.distanceLinePoint(fenceStart.x, fenceStart.y, fenceEnd.x, fenceEnd.y, position.x, position.y);
        lastDistance = distance;
        if (distance < 10 || lastDistance < distance)
            isAttack = true;

        return isAttack;
    }

    public boolean collideWithBullet(Bullet b) // checks if enemy has touch the bullet
    {
        if (!alive)
            return false;

        return  b.getX() > getX() && //returns true or false if bullet hit the enemy
                b.getX() < getX() + enemyWidthDefault &&
                b.getY() > getY() &&
                b.getY() < getY() + enemyHeightDefault;
    }

    public void stopEnemy()
    {
        direction.setZero();
    }

    public void Draw(SpriteBatch sprite)
    {
        Texture draw;
        if(alive)
        {
            float healthpercent = health / (float)ENEMY_HP;
            //Texture draw;

            if (isAttack)
            {
                draw = attackAnimation.getKeyFrame(attackAnimationTime, true);
                sprite.draw(draw, position.x, position.y, enemyAttackWidth, enemyAttackHeight);
            }
            else
            {
               draw = walkinganimation.getKeyFrame(walkinganimationTime, true);
               sprite.draw(draw, position.x, position.y, enemyWidthDefault, enemyHeightDefault);
            }

            sprite.draw(healthbar, position.x,position.y - 5,enemyWidthDefault * healthpercent,5);
        }
        else // draw death animation and other stuff
        {
            stopEnemy();
            draw = deathAnimation.getKeyFrame(deathAnimationTime, true);
            sprite.draw(draw, position.x, position.y);
            deathAnimationTime += Gdx.graphics.getDeltaTime();
        }

        blood.draw(sprite, Gdx.graphics.getDeltaTime());
        wallEffect.draw(sprite, Gdx.graphics.getDeltaTime());
    }
    public void dodamage(int damage)
    {
        health -= damage * damageReduction;

        if(health <= 0)
        {
            deathAnimationTime = 0;
            enemyDeath.play();
            blood.start();
            alive = false;
        }

        if(alive)
            blood.start();
    }

    public void reset(float directionX)
    {
        direction.x = directionX;
        isAttack = false;
        alive = true;
        position.x = Gdx.graphics.getWidth();// enemies spawn on the outside of the right side
        position.y = randomSpawn; // randomizes the spawn of the enemy
        position.x = position.x + direction.x * -0.005f; // moved the enemy in a set speed

        health = 100;
        healthbar.setX(position.x);
        healthbar.setY(position.y);
        walkinganimationTime += Gdx.graphics.getDeltaTime();

    }

    public boolean isActive()
    {
        return alive || !blood.isComplete() || !deathAnimation.isAnimationFinished(deathAnimationTime);
    }

}
