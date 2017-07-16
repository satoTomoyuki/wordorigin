package com.wordOrigin.common;

import android.view.MotionEvent;
import android.view.View;

public abstract class BaseFlickTouch implements View.OnTouchListener {

    private float lastTouchX;
    private float currentX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                currentX = event.getX();
                if (lastTouchX < currentX) {
                    //前に戻る動作
                    flickLeft();
                }
                if (lastTouchX > currentX) {
                    //次に移動する動作
                    flickRight();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                currentX = event.getX();
                if (lastTouchX < currentX) {
                    //前に戻る動作
                    flickLeft();
                }
                if (lastTouchX > currentX) {
                    //次に移動する動作
                    flickRight();
                }
                break;
        }
        return false;
    }

    //左フリック
    public abstract void flickLeft();

    //右フリック
    public abstract void flickRight();


}