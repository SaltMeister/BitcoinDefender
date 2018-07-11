package com.missionbit.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BitcoinDefender extends ApplicationAdapter {
    // Variables
    private static final int INITIAL_ENEMY_SPAWN_RATE = 150;
    private static final int HEALTH_OF_WALL = 100;
    private OrthographicCamera camera;
    private Random randomSource;
    //private mainCharacter mainCharacter;
    private Sprite background;
    private Sprite wallHP;
    private SpriteBatch myBatch;
    private Vector2 gunPosition;
    private Vector2 shootClick;
    private ParticleEffect effect;
    //private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
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
    private bulletManager manager;

    @Override
    public void create() {
        // randomizer
        randomSource = new Random();

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

        enemies = new ArrayList<Enemy>();

        //bullets = new ArrayList<Bullet>();

        gunPosition = new Vector2();
        //gunPosition.x = mainCharacter.getX() + mainCharacter.getWidth();
        //gunPosition.y = mainCharacter.getY() + mainCharacter.getHeight();
        gunPosition.x = mainCharacter.getX() + mainCharacter.getWidth();
        gunPosition.y = mainCharacter.getY() + mainCharacter.getHeight();

        shootClick = new Vector2();

        effect = new ParticleEffect(); // particle effects for muzzle flash
        effect.load(Gdx.files.internal("particles/muzzleFlash.p"), Gdx.files.internal("images"));

        spawnRate = INITIAL_ENEMY_SPAWN_RATE; // sets the spawn rate to the default one
        healthOfWall = HEALTH_OF_WALL; // sets health of wall
        font = new BitmapFont();

        wallStart = new Vector2(186.99998f, 0.0f);
        wallEnd = new Vector2(320.0f, 258.0f);
        debugRenderer = new ShapeRenderer();
        manager = new bulletManager();
        //TODO: Load our image
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
       // manager.draw(camera);
        if (MathUtils.random(spawnRate) == 1)// randomizes spawn rate of the enemies
        {
            //spawns enemies
            enemies.add(new Enemy(mainCharacter.getX()));
        }
        if (healthOfWall > 0) // if health goes to 0 u cannot shoot anymore
        {
            if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
            {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

                camera.unproject(touchPos);
                System.out.println(touchPos.x + " " + touchPos.y); //todo remove this

                shootClick.x = touchPos.x;
                shootClick.y = touchPos.y;

                shootClick.sub(gunPosition);
                shootClick.nor();
                //for (int loop = 0; loop < 5; loop++) //loop for shotgun bullets
                //{                                                                                         //when adding animation fix this section to adapt to the larger texture size
                manager.spawnBullet(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65, shootClick.x, shootClick.y, false);
                //}

                // displays muzzle flash
                effect.setPosition(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65);
                effect.start();
            }
        }
        else
        {

        }
        // starts displaying the stuff
        myBatch.begin();
        background.draw(myBatch);
        wallHP.draw(myBatch);
        font.draw(myBatch, " " + healthOfWall, wallHP.getWidth() * 2, Gdx.graphics.getHeight() - wallHP.getHeight());
        //mainCharacter.update();
        mainCharacter.draw(myBatch);
        myBatch.draw(mainCharacter.getTexture(), mainCharacter.getX(), mainCharacter.getY());


        //enemy.draw(myBatch);
        //spawns multiple bullets
        collisionDetection(enemies, manager.getActiveBullets(), myBatch);
/*
        for (Bullet B : bullets)
        {
            // draws multiple bullets
            B.Draw(myBatch);
        }
*/

        // actually draws the particle effects
        effect.draw(myBatch, Gdx.graphics.getDeltaTime());
        myBatch.end();
        manager.draw(camera);
        //System.out.println(elapsedSeconds); //todo remove this
        if (elapsedTime >= 1000) // if seconds is larger than 1 vvvvv
            startTime = System.currentTimeMillis(); // resets the start time so then clock resets to 0
        //TODO: Draw our image!
        //enemies.removeIf(e->e.alive);
        if (showDebug)
        {
            debugRenderer.setProjectionMatrix(camera.combined);
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.setColor(0,0.5f,0.5f, 1);
            debugRenderer.line(wallStart, wallEnd);
            debugRenderer.end();
        }
    }

    private static boolean collisionDetection(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, SpriteBatch batch) {
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
                        if (elapsedTime >= 1000)
                            healthOfWall -= 2; // if the enemies touched the wall drop 2 hp every second
                    }
                }
                else
                {
                    healthOfWall = 0;
                    startTime = 0; // sets time to 0 so no more attacking from enemies
                    //System.out.println("Game over");
                }
                flag = true;
            }
            for (int j = bullets.size() - 1; j >= 0; j--)
            {
                if (enemies.get(loop).collideWithBullet(bullets.get(j)))
                {
                    //enemies.remove(loop);
                    //enemies.get(loop).health - 50;
                    //bullets.remove(j);

                    enemies.get(loop).dodamage(25);
                    bullets.get(j).alive = false;
                    flag = true;
                }
            }
            if (healthOfWall <= 0)
                enemies.get(loop).Draw(batch);
            else
            {
                //enemies.get(loop).walking(elapsedTime);
                enemies.get(loop).update();
                enemies.get(loop).Draw(batch);
            }

        }
        return flag;
    }

    @Override

    public void dispose() {
        myBatch.dispose();
    }

}