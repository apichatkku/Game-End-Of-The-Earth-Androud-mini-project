package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by ZaiMon on 12/5/2559.
 */
public class Background {
    private Bitmap image ;
    private int x , y , dx ;
    private boolean r,l;

    public Background(Bitmap res , int x){
        this.x = x;
        image = res ;
    }

    public void update(){
        if (l){
            x += 5 ;
        }else if (r){
            x -= 5 ;
        }
        if (x< -GamePanel.WIDTH){
            x = 0 ;
        }else if(x> GamePanel.WIDTH){
            x = 0 ;
        }
    }

    public void setVector(int vector){
        if (vector == 1){
            l=true;
        }else if (vector == 2){
            r=true;
        }
    }
    public void clearVector(){
        r = false;
        l = false;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image , x-GamePanel.focusX , 540-150 , null);
    }
}
