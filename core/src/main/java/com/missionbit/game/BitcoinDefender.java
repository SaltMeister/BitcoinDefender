package com.missionbit.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BitcoinDefender extends ApplicationAdapter {
    // Variables
    private static final int INITIAL_ENEMY_SPAWN_RATE = 150;
    private static final int HEALTH_OF_WALL = 100;
    private OrthographicCamera camera;
    //private mainCharacter mainCharacter;
    private Sprite background;
    private Sprite wallHP;
    private SpriteBatch myBatch;
    private Vector2 gunPosition;
    private Vector2 shootClick;
    private ParticleEffect muzzleFlash;
    private static ParticleEffect blood;
    private static int spawnRate;
    private static int healthOfWall; //the amount of lives the wall has
    private BitmapFont font;
    private static Vector2 wallStart;
    private static Vector2 wallEnd;
    private boolean showDebug = true;
    private ShapeRenderer debugRenderer;
    private static long startTime = System.currentTimeMillis(); // sets the time
    private static long elapsedTime;
    private Sprite mainCharacter;
    private enemyManager enemyManger;
    private bulletManager manager;
    private Weapon weapon;

    @Override
    public void create() {
        // Set up camera for 2d view of 800x480 pixels
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Create a sprite batch for rendering our image
        myBatch = new SpriteBatch();

        background = new Sprite( new Texture(Gdx.files.internal("images/background.png")));// add a image for the background
        background.setX(0);
        background.setY(0);

        wallHP = new Sprite( new Texture(Gdx.files.internal("images/wallHP.png")));
        wallHP.setX(wallHP.getWidth());
        wallHP.setY(Gdx.graphics.getHeight() - wallHP.getHeight() * 2);

        mainCharacter = new Sprite(new Texture(Gdx.files.internal("images/doubleBarrelShotgun.png")));
        mainCharacter.setX(150);
        mainCharacter.setY(150);

        //mainCharacter = new mainCharacter(120, 150); // creates the main character

        gunPosition = new Vector2();
        gunPosition.x = mainCharacter.getX() + mainCharacter.getWidth();
        gunPosition.y = mainCharacter.getY() + mainCharacter.getHeight();

        shootClick = new Vector2();

        muzzleFlash = new ParticleEffect(); // particle effects for muzzle flash
        muzzleFlash.load(Gdx.files.internal("particles/muzzleFlash.p"), Gdx.files.internal("images"));
        blood = new ParticleEffect();
        blood.load(Gdx.files.internal("particles/bloodSplatter.p"), Gdx.files.internal("images"));

        spawnRate = INITIAL_ENEMY_SPAWN_RATE; // sets the spawn rate to the default one
        healthOfWall = HEALTH_OF_WALL; // sets health of wall
        font = new BitmapFont();

        wallStart = new Vector2(186.99998f, 0.0f);
        wallEnd = new Vector2(320.0f, 258.0f);
        debugRenderer = new ShapeRenderer();
        enemyManger = new enemyManager();

        manager = new bulletManager();
        weapon = new Weapon(25,5,1);
    }

    @Override
    public void render()
    {
       elapsedTime = System.currentTimeMillis() - startTime; // sets the time that has past in milliseconds
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set up our camera
        camera.update();
        myBatch.setProjectionMatrix(camera.combined);

        manager.update();
        enemyManger.update();

       // manager.draw(camera);
        if (MathUtils.random(spawnRate) == 1)// randomizes spawn rate of the enemies
            enemyManger.spawnEnemy(mainCharacter.getX());  //spawns enemies

        if (healthOfWall > 0) // if health goes to 0 u cannot shoot anymore
        {
            if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
            {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                camera.unproject(touchPos);

                shootClick.x = touchPos.x;
                shootClick.y = touchPos.y;

                shootClick.sub(gunPosition);
                shootClick.nor();

                weapon.fire(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65, shootClick.x, shootClick.y,manager);
                //for (int loop = 0; loop < 5; loop++) //loop for shotgun bullets
              //  {                                                                                         //when adding animation fix this section to adapt to the larger texture size
                //manager.spawnBullet(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65, shootClick.x, shootClick.y, true);
               // }
                for (int loop = 0; loop < 5; loop++) //loop for shotgun bullets //when adding animation fix this section to adapt to the larger texture size
                    manager.spawnBullet(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65, shootClick.x, shootClick.y, true);

                // displays muzzle flash
                muzzleFlash.setPosition(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65);
                muzzleFlash.start();
            }
        }

        // starts displaying the stuff
        myBatch.begin();
        background.draw(myBatch);
        wallHP.draw(myBatch);
        font.draw(myBatch, " " + healthOfWall, wallHP.getWidth() * 2, Gdx.graphics.getHeight() - wallHP.getHeight());
        //mainCharacter.update();
        mainCharacter.draw(myBatch);
        myBatch.draw(mainCharacter.getTexture(), mainCharacter.getX(), mainCharacter.getY());
        //spawns multiple bullets
        collisionDetection(enemyManager.getActiveEnemies(), bulletManager.getActiveBullets(), myBatch);
        // actually draws the particle effects
        muzzleFlash.draw(myBatch, Gdx.graphics.getDeltaTime());
        blood.draw(myBatch, Gdx.graphics.getDeltaTime());

        enemyManger.draw(camera);
        manager.draw(camera);

        myBatch.end();

        if (elapsedTime >= 1000) // if seconds is larger than 1 vvvvv
            startTime = System.currentTimeMillis(); // resets the start time so then clock resets to 0
        //TODO: Draw our image!
    }

    private static boolean collisionDetection(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, SpriteBatch batch)
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
                    {
                        //Draw attack animation
                        if (elapsedTime >= 1000)
                            healthOfWall -= 2; // if the enemies touched the wall drop 2 hp every second
                    }
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
                    //todo blood.setPosition(enemy.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65);
                    //blood.start();
                    enemies.get(loop).dodamage(25);
                    bullets.get(j).alive = false;
                    flag = true;
                }
            }
            if (healthOfWall <= 0)
                enemies.get(loop).Draw(batch);
            else
            {
                enemies.get(loop).update();
                enemies.get(loop).Draw(batch);
            }
        }
        return flag;
    }

    @Override
    public void dispose()
    {
        myBatch.dispose();
    }
}