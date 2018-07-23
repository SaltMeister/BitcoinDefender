package com.missionbit.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.missionbit.game.Weapon;

public class mainCharacter
{
    private Animation<Texture> reloadAnimation;
    private Array<Texture> reloadFrames = new Array<Texture>();
    private float reloadAnimationTime;
    public static boolean isReloading = false;

    public mainCharacter()
    {
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload1.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload2.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload3.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload4.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload5.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload6.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload4.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload5.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload6.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload7.png")));
        reloadFrames.add(new Texture(Gdx.files.internal("images/shotgunReload8.png")));
        reloadAnimation = new Animation<Texture>(0.25f, reloadFrames);
        reloadAnimationTime = 0;
    }

    public void draw(SpriteBatch sprite, Weapon weapon)
    {
        Texture draw;
        if (isReloading && weapon.bullets != weapon.size)
        {
            draw = reloadAnimation.getKeyFrame(reloadAnimationTime, false);
            reloadAnimationTime += Gdx.graphics.getDeltaTime();
            if (reloadAnimation.isAnimationFinished(reloadAnimationTime))
            {
                isReloading = false;
                reloadAnimationTime = 0;
                weapon.reload();
            }
        }
        else
            draw = reloadFrames.get(0);
        sprite.draw(draw, 140, 150, draw.getWidth(), draw.getHeight());
    }
}
