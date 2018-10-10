package com.dragon.mathias.learnapp.classes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.dragon.mathias.learnapp.GameView;

import java.util.Random;

public class Sprite {
    private int x = 0;
    private int y = 0;
    private int movementSpeed;

    private GameView theGameView;
    private Paint paintBg;
    private Paint paintText;

    float textWidth;
    float textHeight;

    String s = "test-string";

    public Sprite(GameView theGameView, Bitmap bmp) {
        this.theGameView = theGameView;

        Random rnd = new Random();
        movementSpeed = 3 + rnd.nextInt(3);

        textWidth = paintText.measureText(s);
        textHeight = paintText.getTextSize();

        x = theGameView.getWidth()+ (int) textWidth;
        y = rnd.nextInt(theGameView.getHeight() - (int) textWidth);

        paintBg= new Paint();
        paintBg.setColor(Color.MAGENTA);
        paintBg.setStyle(Paint.Style.FILL);

        paintText= new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(50);
        paintText.setTextAlign(Paint.Align.CENTER);
    }

    private void moving() {
        if (x + textWidth < 0) {
            Random rnd = new Random();
            x = theGameView.getWidth()+ (int) textWidth;
            y = rnd.nextInt(theGameView.getHeight() - (int) textHeight);
            movementSpeed = 3 + rnd.nextInt(3);
        }
        x -= movementSpeed;
    }

    public void onDraw(Canvas canvas) {
        moving();

        canvas.drawRect(x - (1.1f * textWidth)/2, y - textHeight, x + (1.1f * textWidth)/2, y + textHeight/2, paintBg);
        canvas.drawText(s, x, y, paintText);
    }

    public boolean isTouched(float x2, float y2) {
        return x2 > x - (1.1f * textWidth)/2 && x2 < x + (1.1f * textWidth)/2 && y2 > y - textHeight && y2 < y + textHeight/2;
    }
}
