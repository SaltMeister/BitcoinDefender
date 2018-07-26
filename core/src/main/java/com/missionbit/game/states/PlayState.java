package com.missionbit.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    private Music music;
    private Sound shotgunShot;
    private Sound reload;
    private Sound autoRifleReload;
    private ParticleEffect muzzleFlash;
    private Sprite wallHP;
    private Sprite graphicCard;
    private Vector2 gunPosition;
    private Vector2 shootClick;
    private int spawnRate;
    private int healthOfWall; //the amount of lives the wall has
    private BitmapFont font;
    private BitmapFont ammo;
    private BitmapFont timer;
    private Vector2 wallStart;
    private Vector2 wallEnd;
    private long startTimeEnemies = System.currentTimeMillis(); // sets the time for enemy spawn
    private long elapsedTime;
    private long reloadTimer = System.currentTimeMillis();
    private Sprite mainCharacter1;
    private enemyManager enemyManager;
    private bulletManager manager;
    private Weapon weapon;
    private Sprite pauseButton;
    private Sprite reloadButton;
    private boolean playMode = true;
    private int weaponChoice;

    public PlayState(GameStateManager gsm, int choice)
    {
        super(gsm);

        weaponChoice = choice;

        background = new Texture(Gdx.files.internal("images/background.png"));// add a image for the background

        music = Gdx.audio.newMusic(Gdx.files.internal("music/CitySoundEffects.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        graphicCard = new Sprite( new Texture( Gdx.files.internal("images/graphicCard.png")));
        graphicCard.setX(100);
        graphicCard.setY(150);

        shotgunShot = Gdx.audio.newSound(Gdx.files.internal("sounds/ShotgunShotSoundEffect.mp3"));
        shotgunShot.setLooping(1, false);
        shotgunShot.setVolume(1, 0.25f);

        reload = Gdx.audio.newSound(Gdx.files.internal("sounds/ShotgunReloadSoundEffect.mp3"));
        reload.setLooping(1, false);
        reload.setVolume(1, 0.5f);

        autoRifleReload = Gdx.audio.newSound(Gdx.files.internal("sounds/autoRifleReload.mp3"));
        autoRifleReload.setLooping(1, false);
        autoRifleReload.setVolume(1, 0.5f);

        wallHP = new Sprite( new Texture(Gdx.files.internal("images/Heart.png")));
        wallHP.setX(wallHP.getWidth());
        wallHP.setY(background.getHeight() - wallHP.getHeight() * 1.75f);

        if (choice == 1)
        {
            mainCharacter1 = new Sprite(new Texture(Gdx.files.internal("images/autoRifle.png")));

            weapon = new Weapon(25,30,70);
        }
        else if (choice == 2)
        {
            mainCharacter1 = new Sprite(new Texture(Gdx.files.internal("images/doubleBarrelShotgun.png")));

            weapon = new Weapon(30,2,1);
        }

        mainCharacter1.setX(140);
        mainCharacter1.setY(150);

        reloadButton = new Sprite(new Texture(Gdx.files.internal("images/reloadButton.png")));
        reloadButton.setX(25);
        reloadButton.setY(25);

        pauseButton = new Sprite(new Texture(Gdx.files.internal("images/pauseButton.png")));
        pauseButton.setX(background.getWidth() - pauseButton.getWidth());
        pauseButton.setY(background.getHeight() - pauseButton.getHeight());

        gunPosition = new Vector2();
        gunPosition.x = mainCharacter1.getX() + mainCharacter1.getWidth();
        gunPosition.y = mainCharacter1.getY() + mainCharacter1.getHeight();

        muzzleFlash = new ParticleEffect(); // particle effects for muzzle flash
        muzzleFlash.load(Gdx.files.internal("particles/muzzleFlash.p"), Gdx.files.internal("images"));

        shootClick = new Vector2();

        spawnRate = INITIAL_ENEMY_SPAWN_RATE; // sets the spawn rate to the default one
        healthOfWall = HEALTH_OF_WALL; // sets health of wall
        font = new BitmapFont();
        ammo = new BitmapFont();

        wallStart = new Vector2(186.99998f, 0.0f);
        wallEnd = new Vector2(320.0f, 258.0f);
        enemyManager = new enemyManager();
        character = new mainCharacter(weaponChoice);
        manager = new bulletManager();

        cam.setToOrtho(false, 800, 480);
    }

    @Override
    protected void handleInput()
    {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

        cam.unproject(touchPos);
        //TODO add it to click pause button to stop the render function and set the boolean to false
        if (healthOfWall > 0) // if health goes to 0 u cannot shoot anymore
        {
            if (Gdx.input.justTouched())
            {
                if (pauseButton.getBoundingRectangle().contains(touchPos.x, touchPos.y))
                {
                    playMode = !playMode;
                    if (playMode)
                        music.play();
                    else
                        music.pause();
                    return;
                }
            }
            if (weaponChoice == 1)
            {
                if (Gdx.input.isTouched())
                {
                    shootClick.x = touchPos.x;
                    shootClick.y = touchPos.y;

                    shootClick.sub(gunPosition);
                    shootClick.nor();

                    if (playMode)
                    {
                        if(reloadButton.getBoundingRectangle().contains(touchPos.x, touchPos.y) && weapon.bullets < weapon.size)
                        {
                            character.isReloading = true;
                            if (System.currentTimeMillis() - reloadTimer >= 1000)
                            {
                                autoRifleReload.play();
                                reloadTimer = System.currentTimeMillis();
                            }

                        }
                        else if(weapon.fire(mainCharacter1.getX() + mainCharacter1.getWidth() - 10,
                                mainCharacter1.getY() + 50, shootClick.x, shootClick.y, manager, weaponChoice))
                        {
                            weapon.setParticlePositionAutoRifle(mainCharacter1.getX() + mainCharacter1.getWidth(), mainCharacter1.getY() + 55);
                        }
                    }
                }
            }
            else if (weaponChoice == 2)
            {
                if (Gdx.input.justTouched()) // if screen is touched once, shoot bullet, at set direction and load muzzle flash
                {
                    shootClick.x = touchPos.x;
                    shootClick.y = touchPos.y;

                    shootClick.sub(gunPosition);
                    shootClick.nor();

                    if (playMode)
                    {
                        if((reloadButton.getBoundingRectangle().contains(touchPos.x, touchPos.y) && weapon.bullets < weapon.size))
                        {
                            System.out.println("clicked");
                            character.isReloading = true;
                            reload.play(); // plays the reload sound
                        }
                        else if(weapon.fire(mainCharacter1.getX() + mainCharacter1.getWidth(),
                                mainCharacter1.getY() + 60, shootClick.x, shootClick.y, manager, weaponChoice))
                        {
                            shotgunShot.play(); // plays the shotgun shot
                            muzzleFlash.setPosition(mainCharacter1.getX() + mainCharacter1.getWidth(), mainCharacter1.getY() + 60);
                            muzzleFlash.start();
                        }
                    }

                }
            }
        }
    }

    @Override
    public void update(float dt)
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
            elapsedTime = System.currentTimeMillis() - startTimeEnemies; // sets the time that has past in milliseconds


            manager.update();
            enemyManager.update();

            if (MathUtils.random(spawnRate) == 1)// randomizes spawn rate of the enemies
                enemyManager.spawnEnemy(mainCharacter1.getX());  //spawns enemies
        }

        // starts displaying the stuff
        myBatch.begin();
        myBatch.draw(background, 0, 0);
        wallHP.draw(myBatch);
        font.draw(myBatch, " " + healthOfWall, wallHP.getWidth() * 2, background.getHeight() - wallHP.getHeight());
        ammo.draw(myBatch, "Ammo: " + weapon.bullets + "/" + weapon.showMaxBullets(), wallHP.getWidth() * 6, background.getHeight() - wallHP.getHeight());

        pauseButton.draw(myBatch);

        reloadButton.draw(myBatch);

        graphicCard.draw(myBatch);

        //spawns multiple bullets
        collisionDetection(enemyManager.getActiveEnemies(), bulletManager.getActiveBullets(), myBatch);

        // actually draws the particle effects
        muzzleFlash.draw(myBatch, Gdx.graphics.getDeltaTime());
        weapon.draw(myBatch, Gdx.graphics.getDeltaTime());
        enemyManager.draw(cam);
        manager.draw(cam);
        myBatch.end();

        //TODO: Draw our image

        myBatch.begin();
        pauseButton.draw(myBatch);
        character.draw(myBatch, weapon);
        myBatch.end();

        if (elapsedTime >= 5000)
        {
            if (Enemy.damageReduction > 0.05)
                Enemy.damageReduction *= 0.95;

            startTimeEnemies = System.currentTimeMillis();
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
                        healthOfWall -= enemies.get(loop).damageDealt(); // if the enemies touched the wall drop 1 hp every attack
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
                gsm.set(new GameOverState(gsm));
        }

        return flag;
    }

    @Override
    public void dispose()
    {
        background.dispose();
        music.dispose();
        shotgunShot.dispose();
        autoRifleReload.dispose();
        reload.dispose();
    }
}
