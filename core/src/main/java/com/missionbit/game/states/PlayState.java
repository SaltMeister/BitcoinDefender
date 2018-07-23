package com.missionbit.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.missionbit.game.Bullet;
import com.missionbit.game.animations.Enemy;
import com.missionbit.game.Weapon;
import com.missionbit.game.manager.bulletManager;
import com.missionbit.game.manager.enemyManager;
import com.missionbit.game.animations.mainCharacter;

import java.util.ArrayList;

public class PlayState extends State
{
    /** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
    // Variables
    private static final int INITIAL_ENEMY_SPAWN_RATE = 150;
    private static final int HEALTH_OF_WALL = 100;
    private mainCharacter character;
    private Texture background;
    private Sprite wallHP;
    private Vector2 gunPosition;
    private Vector2 shootClick;
    private ParticleEffect muzzleFlash;
    private int spawnRate;
    private int healthOfWall; //the amount of lives the wall has
    private BitmapFont font;
    private BitmapFont ammo;
    private Vector2 wallStart;
    private Vector2 wallEnd;
    private long startTime = System.currentTimeMillis(); // sets the time
    private long elapsedTime;
    private Sprite mainCharacter1;
    private enemyManager enemyManger;
    private bulletManager manager;
    private Weapon weapon;
    private Sprite pauseButton;
    private Sprite reloadbutton;
    private boolean playMode = true;

    public PlayState(GameStateManager gsm)
    {
        super(gsm);

        background = new Texture(Gdx.files.internal("images/background.png"));// add a image for the background

        wallHP = new Sprite( new Texture(Gdx.files.internal("images/Heart.png")));
        wallHP.setX(wallHP.getWidth());
        wallHP.setY(background.getHeight() - wallHP.getHeight() * 1.75f);

        mainCharacter1 = new Sprite(new Texture(Gdx.files.internal("images/doubleBarrelShotgun.png")));
        mainCharacter1.setX(140);
        mainCharacter1.setY(150);

        reloadbutton = new Sprite(new Texture(Gdx.files.internal("images/reloadButton.png")));
        reloadbutton.setX(25);
        reloadbutton.setY(25);

        pauseButton = new Sprite(new Texture(Gdx.files.internal("images/pauseButton.png")));
        pauseButton.setX(Gdx.graphics.getWidth() - pauseButton.getWidth());
        pauseButton.setY(Gdx.graphics.getHeight() - pauseButton.getHeight());

        gunPosition = new Vector2();
        gunPosition.x = mainCharacter1.getX() + mainCharacter1.getWidth();
        gunPosition.y = mainCharacter1.getY() + mainCharacter1.getHeight();

        shootClick = new Vector2();

        muzzleFlash = new ParticleEffect(); // particle effects for muzzle flash
        muzzleFlash.load(Gdx.files.internal("particles/muzzleFlash.p"), Gdx.files.internal("images"));

        spawnRate = INITIAL_ENEMY_SPAWN_RATE; // sets the spawn rate to the default one
        healthOfWall = HEALTH_OF_WALL; // sets health of wall
        font = new BitmapFont();
        ammo = new BitmapFont();

        wallStart = new Vector2(186.99998f, 0.0f);
        wallEnd = new Vector2(320.0f, 258.0f);
        enemyManger = new enemyManager();
        character = new mainCharacter();
        manager = new bulletManager();

        weapon = new Weapon(20,2,1);

        cam.setToOrtho(false, 800, 480);
    }

    @Override
    protected  void handleInput()
    {
        //TODO add it to click pause button to stop the render function and set the boolean to false
        if (healthOfWall > 0) // if health goes to 0 u cannot shoot anymore
        {
            if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
            {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                cam.unproject(touchPos);

                shootClick.x = touchPos.x;
                shootClick.y = touchPos.y;

                shootClick.sub(gunPosition);
                shootClick.nor();

                if (pauseButton.getBoundingRectangle().contains(touchPos.x, touchPos.y) && playMode)
                    playMode = false;
                else
                    playMode = true;

                if (playMode)
                {
                    if(reloadbutton.getBoundingRectangle().contains(touchPos.x, touchPos.y) && weapon.bullets < weapon.size)
                    {
                        System.out.println("clicked");
                        character.isReloading = true;
                    }
                    else if(weapon.fire(mainCharacter1.getX() + mainCharacter1.getWidth(),
                            mainCharacter1.getY() + 60, shootClick.x, shootClick.y, manager))
                    {
                        muzzleFlash.setPosition(mainCharacter1.getX() + mainCharacter1.getWidth(), mainCharacter1.getY() + 60);
                        muzzleFlash.start();
                    }
                }
            }
        }
    }

    @Override
    public  void update(float dt)
    {
        //todo move all update code into this from the render function
        handleInput();
    }

    @Override
    public void render(SpriteBatch myBatch)
    {
        //Set up our camera
        myBatch.setProjectionMatrix(cam.combined);

        if (playMode)
        {
            elapsedTime = System.currentTimeMillis() - startTime; // sets the time that has past in milliseconds

            manager.update();
            enemyManger.update();

            if (MathUtils.random(spawnRate) == 1)// randomizes spawn rate of the enemies
                enemyManger.spawnEnemy(mainCharacter1.getX());  //spawns enemies
        }

        // starts displaying the stuff
        myBatch.begin();
        myBatch.draw(background, 0, 0);
        wallHP.draw(myBatch);
        font.draw(myBatch, " " + healthOfWall, wallHP.getWidth() * 2, background.getHeight() - wallHP.getHeight());
        ammo.draw(myBatch, "Ammo: " + weapon.bullets + "/" + weapon.showMaxBullets(), wallHP.getWidth() * 6, background.getHeight() - wallHP.getHeight());


            pauseButton.draw(myBatch);

            //spawns multiple bullets
            collisionDetection(enemyManager.getActiveEnemies(), bulletManager.getActiveBullets(), myBatch);

        reloadbutton.draw(myBatch);

        //spawns multiple bullets
        collisionDetection(enemyManager.getActiveEnemies(), bulletManager.getActiveBullets(), myBatch);

        // actually draws the particle effects
        muzzleFlash.draw(myBatch, Gdx.graphics.getDeltaTime());

        enemyManger.draw(cam);
        manager.draw(cam);
        myBatch.end();

        //TODO: Draw our image!

        if (weapon.bullets == 0)
            character.isReloading = true;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.R) && weapon.bullets < weapon.size)
            character.isReloading = true;


        myBatch.begin();
        pauseButton.draw(myBatch);
        character.draw(myBatch, weapon);
        myBatch.end();

        if (elapsedTime >= 30000)
        {
            if (Enemy.damageReduction > 0.05)
                Enemy.damageReduction *= 0.95;

            startTime = System.currentTimeMillis();
        }
    }

    private boolean collisionDetection(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, SpriteBatch batch)
    {
        boolean flag = false;

        for (int loop = enemies.size() - 1; loop >= 0; loop--)
        {
            if (enemies.get(loop).collideWithFence(wallStart, wallEnd))
            {
                enemies.get(loop).stopEnemy();
                if (healthOfWall > 0)
                {
                    if (enemies.get(loop).alive)
                        healthOfWall -= enemies.get(loop).damageDealt(); // if the enemies touched the wall drop 2 hp every second
                }
                else
                {
                    healthOfWall = 0;
                    startTime = 0; // sets time to 0 so no more attacking from enemies
                }

                flag = true;
            }

            for (int j = bullets.size() - 1; j >= 0; j--)
            {
                if (enemies.get(loop).collideWithBullet(bullets.get(j)))
                {
                    enemies.get(loop).dodamage(weapon.damage);
                    bullets.get(j).alive = false;
                    flag = true;
                    enemies.get(loop).blood.setPosition(enemies.get(loop).position.x, enemies.get(loop).position.y + 50);
                }
            }

            if (healthOfWall <= 0)
                enemies.get(loop).Draw(batch);
        }

        return flag;
    }

    @Override
    public void dispose()
    {
        background.dispose();
        //TODO dispose all assets
    }

}
