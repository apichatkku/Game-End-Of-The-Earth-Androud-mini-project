package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by ZaiMon on 13/5/2559.
 */
public class Pumice extends GameObject {
    private int speed = 40 , movex , movey;
    private Bitmap imagePumice ;
    private float angle;
    private int damage = 50;
    private Animation animation = new Animation();

    public Pumice(Bitmap res1 , int x , int y , int tox , int toy){
        this.x = x;
        this.y = y;
        int dx = tox-x;
        int dy = toy-y;
        int c = (int)Math.sqrt(dx*dx+dy*dy);
        movex = (int)(dx*speed/(c*1.f));
        movey = (int)(dy*speed/(c*1.f));
        width = res1.getWidth();
        height = res1.getHeight();
        imagePumice = Bitmap.createBitmap(res1,0,0,width,height);
    }

    public void update(){
        x+=movex;
        y+=movey;
        angle = (angle+10>360)?0:angle+10;
    }

    public void draw(Canvas canvas){
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(angle,imagePumice.getWidth(),imagePumice.getHeight());
        Bitmap pumiceRt =Bitmap.createBitmap(imagePumice,0,0,imagePumice.getWidth(),imagePumice.getHeight() , rotateMatrix , true);
        canvas.drawBitmap(pumiceRt,x-pumiceRt.getWidth()/2.f-GamePanel.focusX,y-pumiceRt.getHeight()/2.f,null);
    }

    public int getDamage(){
        return damage;
    }
}
