package com.missionbit.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private static final int INITIAL_ENEMY_SPAWN_RATE = 50;
    private static final int HEALTH_OF_WALL = 100;
    private OrthographicCamera camera;
    private Random randomSource;
    private mainCharacter mainCharacter;
    private Sprite background;
    private SpriteBatch myBatch;
    private Vector2 gunPosition;
    private Vector2 shootClick;
    private ParticleEffect effect;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private static int spawnRate;
    private int healthOfWall; //the amount of lives the wall has
    private static Vector2 wallStart;
    private static Vector2 wallEnd;
    private boolean showDebug = true;
    private ShapeRenderer debugRenderer;

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

        mainCharacter = new mainCharacter(120, 150); // creates the main character

        enemies = new ArrayList<Enemy>();

        bullets = new ArrayList<Bullet>();

        gunPosition = new Vector2();
        gunPosition.x = mainCharacter.getX() + mainCharacter.getWidth();
        gunPosition.y = mainCharacter.getY() + mainCharacter.getHeight();

        shootClick = new Vector2();

        effect = new ParticleEffect(); // particle effects for muzzle flash
        effect.load(Gdx.files.internal("particles/muzzleFlash.p"), Gdx.files.internal("images"));

        spawnRate = INITIAL_ENEMY_SPAWN_RATE; // sets the spawn rate to the default one
        healthOfWall = HEALTH_OF_WALL; // sets health of wall
        wallStart = new Vector2(186.99998f, 0.0f);
        wallEnd = new Vector2(320.0f, 258.0f);
        debugRenderer = new ShapeRenderer();
        //TODO: Load our image
    }

    @Override
    public void render()
    {

        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Set up our camera
        camera.update();
        myBatch.setProjectionMatrix(camera.combined);


        if (MathUtils.random(spawnRate) == 1)// randomizes spawn rate of the enemies
        {
            //spawns enemies
            enemies.add(new Enemy(mainCharacter.getX()));
        }

        if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            camera.unproject(touchPos);
            System.out.println(touchPos.x + " " + touchPos.y);

            shootClick.x = touchPos.x;
            shootClick.y = touchPos.y;

            shootClick.sub(gunPosition);
            shootClick.nor();
            //for (int loop = 0; loop < 5; loop++) //loop for shotgun bullets
            //{
                Bullet B = new Bullet(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 60, shootClick.x, shootClick.y, false);
                bullets.add(B);
            //}

            // displays muzzle flash
            effect.setPosition(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 60);
            effect.start();
        }

        // starts displaying the stuff
        myBatch.begin();
        background.draw(myBatch);
        mainCharacter.update();
        myBatch.draw(mainCharacter.getTexture(), mainCharacter.getX(), mainCharacter.getY());

        //enemy.draw(myBatch);
        //spawns multiple bullets
        collisionDetection(enemies, bullets, myBatch);

        for (Bullet B : bullets)
        {
            // draws multiple bullets
            B.Draw(myBatch);
        }




        // actually draws the particle effects
        effect.draw(myBatch, Gdx.graphics.getDeltaTime());
        myBatch.end();

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
                flag = true;
            }
            for (int j = bullets.size() - 1; j >= 0; j--)
            {
                if (enemies.get(loop).collideWithBullet(bullets.get(j)))
                {
                    //enemies.remove(loop);
                    //bullets.remove(j);
                    enemies.get(loop).alive = false;
                    bullets.get(j).alive = false;
                    flag = true;
                }
            }
            enemies.get(loop).update();
            enemies.get(loop).Draw(batch);

        }
        return flag;
    }

    @Override

    public void dispose() {
        myBatch.dispose();
    }

}