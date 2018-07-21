package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by ZaiMon on 12/5/2559.
 */
public class Skill {
    private float cooldown = 0;
    private float timecooldown;
    private String name;
    private Bitmap image;
    private int numSkill;
    private int x;
    private int y;
    private long startTime;
    private int type ;


    public Skill(Bitmap res,String name,float timecooldown,int type,int numSkill){
        image = Bitmap.createBitmap(res,0,0,100,100);
        this.name = name;
        this.timecooldown = timecooldown;
        this.type = type;
        this.numSkill = numSkill;
        x = 510+(100+50)*(numSkill-1);
        y = 430;
        startTime = System.nanoTime();
    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime) / 1000000;
        if (cooldown>0&&elapsed>100){
            cooldown-=0.1f ;
            startTime = System.nanoTime();
            if (cooldown<0){
                cooldown=0;
            }
        }
    }

    public void draw(Canvas canvas){
        if (cooldown>0){
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawRect(x,y,x+100,y+100, paint);
            Paint alphaPaint = new Paint();
            alphaPaint.setAlpha(80);
            canvas.drawBitmap(image, x, y, alphaPaint);
        }else {
            canvas.drawBitmap(image, x, y, null);
        }
        if (GamePanel.selectedSkill==numSkill){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setColor(Color.parseColor("#FFD100"));
            canvas.drawRect(x,y,x+95,y+95, paint);
        }
        if (cooldown>0){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            String showCoolcown = ""+(int)cooldown;
            paint.setColor(Color.YELLOW);
            paint.setTextSize(30);
            canvas.drawText(showCoolcown,x+100-paint.measureText(showCoolcown),y+90, paint);
        }
    }

    public void useSkill(){
        cooldown=timecooldown;
    }

    public String getName(){
        return name;
    }
    public int getType(){
        return type;
    }

    public Rect getRectangle(){
        return new Rect(x,y,x+100,y+100);
    }
    public float getCooldown(){
        return cooldown;
    }

}
