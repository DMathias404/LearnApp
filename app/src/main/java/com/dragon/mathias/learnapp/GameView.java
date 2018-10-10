package com.dragon.mathias.learnapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dragon.mathias.learnapp.classes.Lecture;
import com.dragon.mathias.learnapp.classes.Lesson;
import com.dragon.mathias.learnapp.classes.Sprite;

import java.util.ArrayList;

//unused gameview
public class GameView extends SurfaceView {

    private SurfaceHolder surfaceHolder;
    private GameLoopThread theGameLoopThread;
    private int y = 0;
    private int ySpeed;
    private ArrayList<Sprite> spriteList = new ArrayList<Sprite>();
    private ArrayList<Integer> spriteListNum = new ArrayList<Integer>();
    private boolean createSprites = true;
    private long lastClick;
    private int counter = 0;
    private SideScrollActivity gameActivity;

    private Lesson currentLesson;
    private ArrayList<Lecture> lectures;
    private ArrayList<Lecture> questtionPool;


    public GameView(Context context) {
        super(context);
        theGameLoopThread = new GameLoopThread(this);
        gameActivity = (SideScrollActivity) context;

        currentLesson = gameActivity.getLesson();
        lectures =  currentLesson.getLectureList();
        questtionPool = new ArrayList<Lecture>();

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                theGameLoopThread.setRunning(false);
                while(retry){
                    try {
                        theGameLoopThread.join();
                        retry=false;
                    }
                    catch(InterruptedException e) {
                    }
                }
            }

            public void surfaceCreated(SurfaceHolder holder) {
                theGameLoopThread.setRunning(true);
                theGameLoopThread.start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
            }
        });
    }

    public void increasePool(){
        if(!questtionPool.contains(lectures.get(questtionPool.size())) && questtionPool.size() < lectures.size()) {
            questtionPool.add(lectures.get(questtionPool.size()));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.DKGRAY);

        initialSprites();

        if(spriteList.size() <= 3){
            for (int j = 0; j < 3; j++)
                createSprite(j);
        }

        for (Sprite sprite : spriteList) {
            sprite.onDraw(canvas);
        }
    }

    private void initialSprites() {
       if(createSprites) {
           for (int i = 0; i < 4; i++) {
                   createSprite(i);
           }
       }
        createSprites=false;
    }

    private void createSprite(int index) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.test);

        Sprite sprite = new Sprite(this, bmp);
        spriteList.add(sprite);
        spriteListNum.add(index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                for (int i = spriteList.size() - 1; i >= 0; i--) {
                    Sprite sprite = spriteList.get(i);
                    if (sprite.isTouched(event.getX(), event.getY())) {
                        removeSprite(i);
                        counter++;
                        break;
                    }
                }
            }
        }

        if(counter >= 15){
            gameActivity.onGameOver();
        }

        return true;
    }

    private void removeSprite(int index) {
        spriteList.remove(index);
        spriteListNum.remove(index);
    }

}
