package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by ZaiMon on 13/5/2559.
 */
public class Bomb extends GameObject {
    private Animation animation = new Animation();
    private int numFrames ;
    private long startTime ;

    public Bomb(Bitmap res , int x , int y , int w , int h, int numFrames){
        this.x = x;
        this.y = y;
        this.numFrames = numFrames;
        Bitmap[] image = new Bitmap[numFrames];

        height = res.getHeight() ;
        width = res.getWidth()/numFrames ;

        Matrix matrix = new Matrix();
        matrix.postScale(w*3/(width*1.f),h*3/(height*1.f));
        for (int i=0 ; i < image.length ; i++){
            image[i] = Bitmap.createBitmap(res , i*width , 0 , width , height,matrix,false);
        }
        width = image[0].getWidth();
        height = image[0].getHeight();

        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();

    }

    public void update(){
        animation.update();
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage() ,
                x-(animation.getImage().getWidth()/2.f)-GamePanel.focusX ,
                y-animation.getImage().getHeight()/2 , null
        );
    }

    public boolean lastFrame(){
        if (animation.getFrame()==numFrames-1){
            return true;
        }
        return false;
    }

}
