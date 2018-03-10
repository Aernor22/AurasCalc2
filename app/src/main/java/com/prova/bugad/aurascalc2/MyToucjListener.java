package com.prova.bugad.aurascalc2;

import android.content.ClipData;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by bugad on 6/11/2017.
 */

final class MyTouchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            case MotionEvent.ACTION_UP:
                TheHandler th = new TheHandler();
                th.getHandler().sendEmptyMessage(0);
                view.setVisibility(View.VISIBLE);
            case MotionEvent.ACTION_MOVE:
                return true;
            default:
                break;
        }
        return true;
    }
}

class MyDragListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        View view = (View) event.getLocalState();
        Button drag = (Button) view;
        TheHandler th = new TheHandler();
        int action = event.getAction();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                Button target = (Button) v;
                //do stuff
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                th.getHandler().sendEmptyMessage(0);
                drag.setVisibility(View.VISIBLE);
                return true;
            default:
                break;
        }
        return true;
    }
}

class TheHandler{
    private Handler drophandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        }
    };

    public void TheHandler(){}

    public Handler getHandler(){
        return this.drophandler;
    }
}
