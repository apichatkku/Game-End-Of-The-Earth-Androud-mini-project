package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.util.Objects;

/**
 * Created by ZaiMon on 12/5/2559.
 */
public class Building extends GameObject {
    private int hp;
    private int maxHp;
    private Bitmap[] image;
    private int iImage;
    private int numFrames ;
    private int buildY ;

    public Building(Bitmap res , int x ,int hp ,int numFrames ){
        this.x = x ;
        y = 540-150;
        maxHp = hp;
        this.hp = hp;
        iImage = 0;
        this.numFrames = numFrames;
        image = new Bitmap[numFrames];
        height = res.getHeight() ;
        buildY = height/2;
        width = res.getWidth()/numFrames ;

        for (int i=0 ; i < image.length ; i++){
            Bitmap tmpBm = Bitmap.createBitmap(res , i*width , 0 , width , height);
            Matrix matrix = new Matrix();
            matrix.postScale(0.5f,0.5f);
            image[i] = Bitmap.createBitmap(tmpBm,0,0,width,height,matrix,false);

        }
        width = image[iImage].getWidth();
        height = image[iImage].getHeight();

    }

    public void update(){
        if (buildY>0){
            buildY-=10;
            if (buildY<0){
                buildY=0;
            }
        }
        if (hp!=0){
            iImage = (int)(Math.ceil((maxHp-hp)*numFrames/maxHp*1.f));
        }
    }

    public void draw(Canvas canvas){
        if (hp==0) {
            return;
        }

        //flip image
        /*Matrix flipHorizontalMatrix = new Matrix();
        flipHorizontalMatrix.setScale(-1,1);
        flipHorizontalMatrix.postTranslate(image[iImage].getWidth(),0);
        Bitmap testBm = Bitmap.createBitmap(image[iImage],0,0,width,height,flipHorizontalMatrix,false);*/

        //set scale image
        /*Matrix matrix = new Matrix();
        matrix.postScale(1,1);
        Bitmap testBm = Bitmap.createBitmap(image[iImage],0,0,width,height,matrix,false);
        canvas.drawBitmap(testBm , x-(testBm.getWidth()/2.f) , y-testBm.getHeight() , null);*/

        //rotate
        /*Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(-90,image[iImage].getWidth(),image[iImage].getHeight());
        Bitmap testRt = Bitmap.createBitmap(image[iImage],0,0,image[iImage].getWidth(),image[iImage].getHeight() , rotateMatrix , true);
        canvas.drawBitmap(testRt,x-(image[iImage].getWidth()/2.f) - GamePanel.focusX , y-image[iImage].getHeight() , null);*/

        canvas.drawBitmap(image[iImage],x-(image[iImage].getWidth()/2.f) - GamePanel.focusX , y-image[iImage].getHeight()+buildY , null);
    }

    public int getMaxHp(){
        return maxHp;
    }
    public int getHp(){
        return hp;
    }
    public void setHp(int hp){
        this.hp = hp;
    }
}
