package com.example.zaimon.gametb01;

import android.graphics.Rect;

/**
 * Created by ZaiMon on 12/5/2559.
 */
public abstract class GameObject {
    protected int x ;
    protected int y ;
    protected int width ;
    protected int height ;

    public void setX(int x){
        this.x = x ;
    }

    public void setY(int y){
        this.y = y ;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Rect getRectangle(){
        return new Rect(x-(int)(width/2.f) , y-height , x+(int)(width/2.f) , y);
    }
}
