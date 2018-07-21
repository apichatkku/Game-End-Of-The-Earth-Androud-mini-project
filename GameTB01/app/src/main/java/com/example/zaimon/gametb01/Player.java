package com.example.zaimon.gametb01;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Created by ZaiMon on 12/5/2559.
 */
public class Player extends GameObject {
    private Bitmap spritesheet ;
    private int score ;
    private double dya ;
    private int maxHp;
    private int hp ;
    private int speed = 50;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    private boolean playing ;
    private Animation animation = new Animation();
    private long startTime ;
    private String character ;

    public String face = "r";

    public Player(Bitmap res , String character , int w , int h , int numFrames){
        x = GamePanel.WIDTH/2 ;
        y = 540-150+20;
        score = 0 ;
        maxHp = 100;
        hp = 100;
        height = h ;
        width = w ;
        this.character = character;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res ;

        for (int i=0 ; i < image.length ; i++){
            image[i] = Bitmap.createBitmap(spritesheet , i*width , 0 , width , height);
        }
        height *=2;
        width *=2;

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

    }

    public void update(){
        long elapsed = (System.nanoTime()-startTime) / 1000000;
        if (elapsed>500){
            hp-=2 ;
            startTime = System.nanoTime();
            if (hp<=0){
                playing = false;
            }
        }

        animation.update();

        if (moveUp){
            y -= speed;
        }
        else if (moveDown){
            y += speed;
        }

        if (moveLeft){
            face = "l";
            x -= speed;
            if (x<GamePanel.WIDTH*(-3)){
                x=GamePanel.WIDTH*(-3);
            }
        }else if(moveRight){
            face = "r";
            x += speed;
            if (x>GamePanel.WIDTH*3){
                x=GamePanel.WIDTH*3;
            }
        }

    }

    public void setMove(int num){
        if (num == 1){
            moveUp = true;
        }else if (num == 2){
            moveDown = true;
        }
        if (num == 3){
            moveLeft = true;
        }else if (num == 4){
            moveRight = true;
        }
    }
    public void clearMove(){
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
    }

    public void draw(Canvas canvas){
        Matrix matrix = new Matrix();
        matrix.postScale(width/animation.getImage().getWidth(),height/animation.getImage().getHeight());
        Bitmap testBm = Bitmap.createBitmap(
                animation.getImage(),0,0,animation.getImage().getWidth(),animation.getImage().getHeight(),matrix,false
        );
        if (face.equals("r")){
            canvas.drawBitmap(testBm , x-(testBm.getWidth()/2.f)-GamePanel.focusX , y-testBm.getHeight() , null);
        }else{
            Matrix flipHorizontalMatrix = new Matrix();
            flipHorizontalMatrix.setScale(-1,1);
            flipHorizontalMatrix.postTranslate(testBm.getWidth(),0);
            Bitmap flipBm = Bitmap.createBitmap(testBm,0,0,width,height,flipHorizontalMatrix,false);
            canvas.drawBitmap(flipBm , x-(flipBm.getWidth()/2.f)-GamePanel.focusX , y-flipBm.getHeight() , null);
        }
        //canvas.drawBitmap(animation.getImage() , x-(animation.getImage().getWidth()/2.f)-GamePanel.focusX , y-animation.getImage().getHeight() , null);
    }

    public int getScore(){return score;}
    public void addScore(int add){
        score+=add;
    }


    public boolean getPlaying(){return playing;}

    public void setPlaying(boolean b){playing = b;}

    public void resetDYA(){dya = 0;}
    public void resetScore(){score = 0;}
    public int getMaxHp(){return maxHp;}
    public int getHp(){return hp;}
    public void setHp(int hp){
        this.hp = hp;
    }
    public String getCharacter(){
        return character;
    }

}
