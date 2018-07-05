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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BitcoinDefender extends ApplicationAdapter {
    private static final int INITIAL_ENEMY_SPAWN_RATE = 100;
    private static final int HEALTH_OF_WALL = 100;
    private OrthographicCamera camera;
    private Random randomSource;
    private Sprite bullet;
    private Sprite mainCharacter, enemy, background;
    private Animation regularEnemyAnimation;
    private SpriteBatch myBatch;
    private Vector2 velocity;
    private Vector2 gunPosition;
    private Vector2 shootClick;
    private Vector2 bulletVelocity;
    private ParticleEffect effect;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;
    private static int spawnRate;
    private int healthOfWall; //the amount of lives the wall has

    @Override
    public void create() {
        randomSource = new Random();

        // Set up camera for 2d view of 800x480 pixels
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Create a sprite batch for rendering our image
        myBatch = new SpriteBatch();

        background = new Sprite( new Texture(Gdx.files.internal("images/background.png")));// add a image for the background
        background.setX(0);
        background.setY(0);

        mainCharacter = new Sprite(new Texture(Gdx.files.internal("images/doubleBarrelShotgun.png"))); // creates the main character
        //regularEnemyAnimation = new Animation(new TextureRegion(regularEnemy), 2, 1);
        mainCharacter.setX(150);
        mainCharacter.setY(150);

        //enemy = new Sprite(new Texture(Gdx.files.internal("images/enemyDefault.png")));
        //enemy.setX(150);/*Gdx.graphics.getWidth()*/
        //enemy.setY(200);
        enemies = new ArrayList<Enemy>();

        bullets = new ArrayList<Bullet>();

        gunPosition = new Vector2();
        gunPosition.x = mainCharacter.getX() + mainCharacter.getWidth();
        gunPosition.y = mainCharacter.getY() + mainCharacter.getHeight();

        shootClick = new Vector2();

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/muzzleFlash.p"), Gdx.files.internal("images"));

        spawnRate = INITIAL_ENEMY_SPAWN_RATE; // sets the spawn rate to the default one
        healthOfWall = HEALTH_OF_WALL; // sets health of wall

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

        if (Gdx.input.justTouched())
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            camera.unproject(touchPos);
            System.out.println(touchPos.x + " " + touchPos.y);

            shootClick.x = touchPos.x;
            shootClick.y = touchPos.y;

            shootClick.sub(gunPosition);
            shootClick.nor();
            //for (int loop = 0; loop < 5; loop++) loop for shotgun bullets
            //{
                Bullet B = new Bullet(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 60, shootClick.x, shootClick.y, false);
                bullets.add(B);
            //}
            effect.setPosition(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 65);
            effect.start();
        }

        myBatch.begin();
        background.draw(myBatch);
        mainCharacter.draw(myBatch);
        //enemy.draw(myBatch);
        //spawns multiple bullets
        collisionDetection(enemies, bullets, myBatch);

        for (Bullet B : bullets)
        {
            B.Draw(myBatch);
        }

        effect.draw(myBatch, Gdx.graphics.getDeltaTime());
        myBatch.end();
        //TODO: Draw our image!
    }

    private static boolean collisionDetection(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, SpriteBatch batch) {
        for (int loop = enemies.size() - 1; loop >= 0; loop--)
        {
            for (int j = bullets.size() - 1; j >= 0; j--)
            {
                if (enemies.get(loop).collideWithBullet(bullets.get(j)))
                {
                    enemies.remove(loop);
                    bullets.remove(j);
                    return true;
                }
            }

            if (enemies.get(loop).collideWithFence())
            {
                enemies.remove(loop);
                return true;
            }

            enemies.get(loop).update(); // TODO remove outside of collision detection function
            enemies.get(loop).Draw(batch); ////// ^^^
        }

        return false;
    }

    @Override

    public void dispose() {
        myBatch.dispose();
    }

}