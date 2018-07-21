package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.util.Log;

/**
 * Created by ZaiMon on 13/5/2559.
 */
public class RockThrow extends GameObject {
    private int speed = 50 , movex , movey;
    private Bitmap image ;
    private float angle;
    private int damage = 20;

    public RockThrow(Bitmap res , int x , int y , int tox , int toy){
        this.x = x;
        this.y = y;
        int dx = tox-x;
        int dy = toy-y;
        int c = (int)Math.sqrt(dx*dx+dy*dy);
        movex = (int)(dx*speed/(c*1.f));
        movey = (int)(dy*speed/(c*1.f));
        Bitmap tmpImage = Bitmap.createBitmap(res,0,0,50,50);
        width = tmpImage.getWidth();
        height = tmpImage.getHeight();


        angle = (float) Math.toDegrees(Math.atan2(dy, dx));

        if(angle < 0){
            angle += 360;
        }

        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(angle+45,tmpImage.getWidth(),tmpImage.getHeight());
        image = Bitmap.createBitmap(tmpImage,0,0,tmpImage.getWidth(),tmpImage.getHeight() , rotateMatrix , true);

    }

    public void update(){
        x+=movex;
        y+=movey;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image,x-image.getWidth()/2.f-GamePanel.focusX,y-image.getHeight()/2.f,null);
    }

    public int getDamage(){
        return damage;
    }
}
