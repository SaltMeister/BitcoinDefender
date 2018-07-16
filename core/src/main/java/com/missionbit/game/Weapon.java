package com.missionbit.game;

public class Weapon {
    protected int damage;
    protected int size;
    protected int fireRate;
    public Weapon(int startdamage,int startsize,int startfireRate){

        damage = startdamage;
        size = startsize;
        fireRate = startfireRate;
    }
    public void fire(float startx,float starty,float directionx,float directiony,bulletManager manager){
       // manager.spawnBullet(mainCharacter.startx() + mainCharacter.getWidth(), mainCharacter.getY() + 65, shootClick.x, shootClick.y, true);
    manager.spawnBullet(startx,starty,directionx,directiony,false);

    }
}
