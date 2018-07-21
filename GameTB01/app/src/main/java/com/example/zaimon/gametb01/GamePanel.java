package com.example.zaimon.gametb01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by ZaiMon on 12/5/2559.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 960 ;
    public static final int HEIGHT = 540 ;
    public static float focusX = 0;
    public static float ratioW;
    public static float ratioH;
    public static int selectedSkill = 0;

    public int testx=0 , testy=0;

    private MainThread thread ;

    //object in game
    private ArrayList<Background> backgrounds = new ArrayList<Background>() ;
    private Player player ;
    private ArrayList<Building> building = new ArrayList<Building>();
    private ArrayList<Skill> skills;
    public ArrayList<RockThrow> rockThrows = new ArrayList<RockThrow>();
    private ArrayList<Volcano> volcanos = new ArrayList<Volcano>();
    private ArrayList<Pumice> pumices = new ArrayList<Pumice>();

    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    private Context context ;

    public GamePanel(Context context){
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        thread = new MainThread(getHolder() , this) ;

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true ;
        while (retry){
            try {
                thread.setRunning(false);
                thread.join();

            }catch (InterruptedException e){
                e.printStackTrace();
            }
            retry  = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ratioW = getWidth()/(WIDTH*1.f);
        ratioH = getHeight()/(HEIGHT*1.f);

        createGameObject();


        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (!player.getPlaying()){
                if (player.getHp()>0){
                    player.setPlaying(true);
                }else{
                    context.startActivity(new Intent(context.getApplicationContext(),StartActivity.class));
                    ((Activity) context).finish();
                }
            }
            else {
                float pageX = event.getX()/ratioW;
                float pageY = event.getY()/ratioH;
                int curserX = (int)pageX+(int)focusX , curserY = (int)pageY;
                testx = curserX ; testy = curserY;

                //touch menu game
                //control move
                if (Rect.intersects(new Rect((int)pageX-1,curserY-1,(int)pageX+1,curserY+1),
                        new Rect(60,400,60+100,400+100))) {
                    player.setMove(3);
                    return true;
                }else if (Rect.intersects(new Rect((int)pageX-1,curserY-1,(int)pageX+1,curserY+1),
                        new Rect(160+50,400,160+100+100,400+100))) {
                    player.setMove(4);
                    return true;
                }
                //selected skill
                for (int i = skills.size()-1; i >= 0 ; i-- ){
                    if (Rect.intersects(new Rect((int)pageX-1,curserY-1,(int)pageX+1,curserY+1),skills.get(i).getRectangle())){
                        if (selectedSkill==i+1){
                            selectedSkill = 0;
                        }else{
                            if (skills.get(i).getType()==1){
                                selectedSkill = i+1;
                            }else{
                                useSkill(i,curserX,curserY);
                            }
                        }
                        return true;
                    }
                }

                //selected location use skill
                if (selectedSkill>0){
                    useSkill(selectedSkill-1,curserX,curserY);
                    return true;
                }
            }
            return true;
        }
        if (event.getAction()==MotionEvent.ACTION_UP){
            player.clearMove();
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update(){
        if (player.getPlaying()) {

            player.update();
            //set focus on player
            focusX = player.x-(int)(WIDTH/2.f);
            if (focusX<WIDTH*(-3)){
                focusX=WIDTH*(-3);
            }else if (focusX > WIDTH*2){
                focusX=WIDTH*2;
            }

            for (int i = skills.size()-1 ; i >= 0 ; i--){
                skills.get(i).update();
            }


            for (int i = building.size()-1 ; i >= 0 ; i--){
                building.get(i).update();
                if (building.get(i).getHp()<=0){
                    player.setHp(onHeal((int)(building.get(i).getMaxHp()*0.1f),player.getHp(),player.getMaxHp()));
                    player.addScore(1);
                    building.remove(i);
                    continue;
                }
            }

            for (int i = rockThrows.size()-1 ; i >= 0 ; i--){
                rockThrows.get(i).update();
                if (Math.abs(rockThrows.get(i).getX()-player.getX())>1000||Math.abs(rockThrows.get(i).getY()-player.getY())>1000){
                    rockThrows.remove(i);
                    continue;
                }
                for (int j = building.size()-1 ; j >= 0 ; j--){
                    if(collision(building.get(j),rockThrows.get(i)) || rockThrows.get(i).getY()>=370)
                    {
                        building.get(j).setHp(onDamage(rockThrows.get(i).getDamage(),building.get(j).getHp()));
                            bombs.add(new Bomb(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.bomb01),
                                    rockThrows.get(i).getX(),
                                    rockThrows.get(i).getY(),
                                    rockThrows.get(i).getWidth(),
                                    rockThrows.get(i).getHeight(),
                                    5
                            )
                        );
                        rockThrows.remove(i);
                        break;
                    }
                }
            }

            for (int i=volcanos.size()-1 ; i>=0 ; i--){
                //Log.d("Volcano", "update: ");
                if (volcanos.get(i).getY()>540+volcanos.get(i).getHeight()){
                    volcanos.remove(i);
                    continue;
                }else if (volcanos.get(i).getY()==390){
                    if (volcanos.get(i).spurt==false){
                        volcanos.get(i).spurt=true;
                        for(int j=0;j<10;j++){
                            int tmpX = volcanos.get(i).getX()-WIDTH/2;
                            pumices.add(
                                    new Pumice(BitmapFactory.decodeResource(getResources(), R.drawable.pumice),
                                            volcanos.get(i).getX(),
                                            volcanos.get(i).getY()-volcanos.get(i).getHeight()+50,
                                            WIDTH/10*j+tmpX,
                                            0)
                            );
                        }
                        for(int j=0;j<10;j++){
                            int tmpX = (int)(Math.random()*(WIDTH))+(volcanos.get(i).getX()-WIDTH/2);
                            pumices.add(
                                    new Pumice(BitmapFactory.decodeResource(getResources(), R.drawable.pumice),
                                            tmpX,
                                            -500,
                                            tmpX,
                                            540)
                            );
                        }
                    }
                }
                volcanos.get(i).update();
            }

            for (int i=pumices.size()-1 ; i>=0 ; i--){
                pumices.get(i).update();
                if (Math.abs(pumices.get(i).getX()-player.getX())>1000||Math.abs(pumices.get(i).getY()-player.getY())>1000||
                        pumices.get(i).getY()>=375){
                        bombs.add(new Bomb(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.bomb01),
                                    pumices.get(i).getX(),
                                    pumices.get(i).getY(),
                                    pumices.get(i).getWidth(),
                                    pumices.get(i).getHeight(),
                                    5
                        )
                    );
                    pumices.remove(i);
                    continue;
                }
                for (int j = building.size()-1 ; j >= 0 ; j--){
                    if(collision(building.get(j),pumices.get(i)))
                    {
                        building.get(j).setHp(onDamage(pumices.get(i).getDamage(),building.get(j).getHp()));
                        bombs.add(new Bomb(
                                        BitmapFactory.decodeResource(getResources(), R.drawable.bomb01),
                                        pumices.get(i).getX(),
                                        pumices.get(i).getY(),
                                        pumices.get(i).getWidth(),
                                        pumices.get(i).getHeight(),
                                        5
                                )
                        );
                        pumices.remove(i);
                        break;
                    }
                }
            }

            for (int i=bombs.size()-1 ; i>=0 ; i--){
                if (bombs.get(i).lastFrame()){
                    bombs.remove(i);
                    continue;
                }
                bombs.get(i).update();
            }

            if (building.size()<=35){
                addBuilding();
            }

            /*Log.d("Object", "building: "+building.size()+" , Volcano: "+volcanos.size());
            Log.d("Object", "Throw: "+rockThrows.size()+" , Pumice: "+pumices.size()+" , Bomb : "+bombs.size());*/

        }
    }


    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(),b.getRectangle()))
        {
            return true;
        }
        return false;
    }

    public int onDamage(int damage,int hp){
        int bonusdamage = 1;
        if (volcanos.size()>0){
            bonusdamage = 2;
        }
        hp = (hp-damage <0 )? 0 : hp-damage*bonusdamage;
        return hp;
    }
    public int onHeal(int heal , int hp , int maxhp){
        hp = (hp+heal > maxhp)? maxhp : hp+heal;
        return hp;
    }

    public boolean flashControl = true;
    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = ratioW;
        final float scaleFactorY = ratioH;
        int sheck = 0;
        if (canvas != null) {
            final int saveState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            Paint paint = new Paint();
            if (volcanos.size()>0){
                if (flashControl){
                    paint.setColor(Color.RED);
                    flashControl = false;
                    sheck = 10;
                }else{
                    paint.setColor(Color.BLACK);
                    flashControl = true;
                    sheck = -10;
                }
            }else{
                paint.setColor(Color.parseColor("#C4FFF5"));
            }
            focusX += sheck;

            canvas.drawRect(0,0,WIDTH,HEIGHT, paint);

            for (int i=0 ; i<volcanos.size() ; i++){
                volcanos.get(i).draw(canvas);
            }

            for (int i=0 ; i<building.size();i++){
                building.get(i).draw(canvas);
            }

            for (int i=0 ; i<backgrounds.size() ; i++){
                backgrounds.get(i).draw(canvas);
            }

            player.draw(canvas);

            for (int i=0 ; i<rockThrows.size();i++){
                rockThrows.get(i).draw(canvas);
            }

            for (int i=0 ; i<pumices.size();i++){
                pumices.get(i).draw(canvas);
            }

            for (int i=0 ; i<bombs.size();i++){
                bombs.get(i).draw(canvas);
            }
            drawEtc(canvas);

            canvas.restoreToCount(saveState);
            focusX-=sheck;
        }
    }

    public void drawEtc(Canvas canvas){

        Bitmap bmMaxHp = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.maxhp));
        canvas.drawBitmap(bmMaxHp , 20 , 20 , null);
        if (player.getHp()>0) {
            Bitmap bmHp = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.hp),
                    0, 0, (int) (player.getHp() * bmMaxHp.getWidth() / player.getMaxHp() * 1.f), bmMaxHp.getHeight());
            canvas.drawBitmap(bmHp, 20, 20, null);
        }

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        String showscore = "SCORE : "+player.getScore();
        paint.setColor(Color.BLACK);
        canvas.drawRect(740, 20, 740+200 , 20+50 , paint);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(30);
        canvas.drawText(showscore, 720 + 200 - paint.measureText(showscore), 20+35, paint);


        for (int i=0;i<skills.size();i++){
            skills.get(i).draw(canvas);
        }

        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.btnml) , 60 , 400 , null);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.btnmr) , 60+100+50 , 400 , null);

        if (player.getPlaying()==false&&player.getHp()<=0){
            Bitmap alert = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.alert01));
            canvas.drawBitmap(alert , WIDTH/2.f-alert.getWidth()/2.f , HEIGHT/2.f-alert.getHeight()/2.f , null);
        }
    }

    void useSkill(int numSkill,int curserX , int curserY){
        if (skills.get(numSkill).getCooldown()>0){
            if (skills.get(numSkill).getType()==0){
                selectedSkill = 0;
            }
            return;
        }
        switch (skills.get(numSkill).getName()){
            case "Rock Throw":
                rockThrows.add(
                        new RockThrow(BitmapFactory.decodeResource(getResources(), R.drawable.rockthrow),
                                player.getX(), player.getY() - (int) (player.getHeight() / 2.f), curserX, curserY)
                );
                if (curserX<player.getX()){
                    player.face="l";
                }else{
                    player.face="r";
                }
                break;
            case "Magma Dance":
                volcanos.add(
                        new Volcano(BitmapFactory.decodeResource(getResources(), R.drawable.volcano),
                        player.getX())
                );
                break;
            case "Rock Fall":
                for (int i = 0 ;i<5 ; i++){
                    int tmpX;
                    int tmpTx;
                    if (player.face.equals("r")){
                        tmpX=player.getX();
                        tmpTx = -50;
                    }else{
                        tmpX=player.getX()-WIDTH/2;
                        tmpTx = 50;
                    }
                    rockThrows.add(
                            new RockThrow(BitmapFactory.decodeResource(getResources(), R.drawable.rockthrow),
                                    (WIDTH/2)/5*i+tmpX+tmpTx*2, -100, (WIDTH/2)/5*i+tmpX-tmpTx, 540)
                    );
                }
        }
        if (skills.get(numSkill).getType()==0){
            selectedSkill = 0;
        }
        skills.get(numSkill).useSkill();
    }

    private void createGameObject(){
        for (int i=-3;i<3;i++){
            backgrounds.add(new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg001),960*i));
        }
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.fire_sprite), "rock" , 100 , 100 , 4);

        setSkill(player.getCharacter());

        for (int i=0;i<40;i++){
            int randBuild = (int)(Math.random()*20)+1;
            int resID = getResources().getIdentifier("building"+randBuild , "drawable", getContext().getPackageName());
            int randX = (int)(Math.random()*40)+1;
            int tmpHp = 100;
            if (randBuild >= 19){
                tmpHp = 200;
            }
            building.add(new Building( BitmapFactory.decodeResource(getResources(), resID) , randX*150-3840+randBuild*20 , tmpHp , 5));
        }
    }

    public void setSkill(String namePlay){
        skills = new ArrayList<Skill>();
        switch (namePlay){
            case "rock":
                skills.add(new Skill(BitmapFactory.decodeResource(getResources(), R.drawable.skillrockthrow),"Rock Throw",0.3f,1,1));
                skills.add(new Skill(BitmapFactory.decodeResource(getResources(), R.drawable.skillrockfall),"Rock Fall",2,0,2));
                skills.add(new Skill(BitmapFactory.decodeResource(getResources(), R.drawable.skillvolcano),"Magma Dance",15,0,3));
                break;
        }
    }

    public void addBuilding(){
        for (int i=0;i<5;i++){
            int randBuild = (int)(Math.random()*20)+1;
            int resID = getResources().getIdentifier("building"+randBuild , "drawable", getContext().getPackageName());
            int randX = (int)(Math.random()*40)+1;
            int tmpHp = 100;
            if (randBuild >= 19){
                tmpHp = 800;
            }else if(randBuild >= 18){
                tmpHp = 600;
            }
            else if(randBuild >= 14){
                tmpHp = 400;
            }
            building.add(new Building( BitmapFactory.decodeResource(getResources(), resID) , randX*200-3840+randBuild*20 , tmpHp , 5));
        }
    }

}
