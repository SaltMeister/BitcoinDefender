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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BitcoinDefender extends ApplicationAdapter {
    private OrthographicCamera camera;
    private Random randomSource;
    private Sprite bullet;
    private Sprite background;
    private Sprite mainCharacter;
    private Animation regularEnemyAnimation;
    private SpriteBatch myBatch;
    private Vector2 velocity;
    private Vector2 gunPosition;
    private Vector2 shootClick;
    private Vector2 bulletVelocity;
    private ParticleEffect effect;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

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

        mainCharacter = new Sprite(new Texture(Gdx.files.internal("images/mainCharacterWithGun.png"))); // creates the main character
        //regularEnemyAnimation = new Animation(new TextureRegion(regularEnemy), 2, 1);
        mainCharacter.setX(150);
        mainCharacter.setY(150);




        gunPosition = new Vector2();
        gunPosition.x = mainCharacter.getX() + mainCharacter.getWidth();
        gunPosition.y = mainCharacter.getY() + mainCharacter.getHeight();

        shootClick = new Vector2();

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

        myBatch.begin();
        background.draw(myBatch);
        mainCharacter.draw(myBatch);
        for (Bullet B : bullets)
            B.Draw(myBatch);
        myBatch.end();

        if(Gdx.input.justTouched())
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

            camera.unproject(touchPos);
            System.out.println(touchPos.x + " " + touchPos.y);

            shootClick.x = touchPos.x;
            shootClick.y = touchPos.y;

            shootClick.sub(gunPosition);
            shootClick.nor();

            Bullet B = new Bullet(mainCharacter.getX() + mainCharacter.getWidth(), mainCharacter.getY() + 60, shootClick.x, shootClick.y);
            bullets.add(B);
        }

        //TODO: Draw our image!
    }

    @Override

    public void dispose() {
        myBatch.dispose();
    }

}