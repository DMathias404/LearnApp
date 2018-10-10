package com.dragon.mathias.learnapp;

import android.graphics.Canvas;

//unused class
public class GameLoopThread extends Thread {
    static final long FPS = 20;
    private GameView theView;
    private boolean isRunning = false;

    public GameLoopThread(GameView theView) {
        this.theView = theView;
    }

    public void setRunning(boolean run) {
        isRunning = run;
    }

    // @SuppressLint("WrongCall")  hinzugefügt am 20.02.14
    @Override
    public void run() {
        long TPS = 1000 / FPS;
        long startTime, sleepTime;
        while (isRunning) {
            Canvas theCanvas = null;
            try {
                theCanvas = theView.getHolder().lockCanvas();
                synchronized (theView.getHolder()) {
                    if (theCanvas != null) {
                        theView.onDraw(theCanvas);
                    }
                }
            } finally {
                if (theCanvas != null) {
                    theView.getHolder().unlockCanvasAndPost(theCanvas);
                }
            }
        }
    }
}
