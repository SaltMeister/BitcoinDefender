package com.missionbit.game;

public class Weapon {
    protected int damage;
    protected int size;
    protected int bullets;
    protected int fireRate;
    public Weapon(int startdamage,int startsize,int startfireRate){

        damage = startdamage;
        size = startsize;
        bullets = startsize;
        fireRate = startfireRate;
    }
    public void reload(){
        bullets = size;


    }

    public boolean fire(float startx,float starty,float directionx,float directiony,bulletManager manager){
       if(bullets > 0)
       {
           manager.spawnBullet(startx,starty,directionx,directiony,false);
           bullets = bullets - 1;
           return true;




       }
       return false;

    }
}
