package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by ZaiMon on 13/5/2559.
 */
public class Volcano extends GameObject {
    private Bitmap image ;
    private int damage = 20;
    private int dx = 10;
    private long startTime ;

    public boolean spurt=false;

    public Volcano(Bitmap res , int x ){
        this.x = x;
        y = 390+res.getHeight();
        width = res.getWidth();
        height = res.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(2,2);
        Bitmap tmpImage = Bitmap.createBitmap(res,0,0,width,height,matrix,false);

        width *=2;
        height *=2;
        image = Bitmap.createBitmap(tmpImage,0,0,tmpImage.getWidth(),tmpImage.getHeight());

        startTime = System.nanoTime();

    }

    public void update(){
        if (spurt){
            long elapsed = (System.nanoTime()-startTime) / 1000000;
            if (elapsed<3000) {
                return;
            }
            y+=10;
            if (dx==-10){
                dx=10;
            }else{
                dx=-10;
            }
            return;
        }
        if (y>390){
            y-=10;
            if (dx==-10){
                dx=10;
            }else{
                dx=-10;
            }
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image,x-image.getWidth()/2.f-GamePanel.focusX+dx,y-image.getHeight(),null);
    }

    public int getDamage(){
        return damage;
    }
}
