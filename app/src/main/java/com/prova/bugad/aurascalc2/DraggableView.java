package com.prova.bugad.aurascalc2;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

class GestureTap extends GestureDetector.SimpleOnGestureListener {
    Context c;
    public GestureTap(Context c){
        this.c = c;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Toast.makeText(this.c,"Reset  size"+getBarSize(c),Toast.LENGTH_LONG).show();
        return true;
    }

    public float getBarSize(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
public class DraggableView extends AppCompatActivity implements View.OnTouchListener {
    float dX;
    float dY;
    int lastAction;
    int screenHight;
    int realHeight;
    int screenWidth;
    GestureDetector gestureDetector;
    Context c;

    public DraggableView (DisplayMetrics dm,DisplayMetrics rdm,Context c){
        screenHight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        realHeight = rdm.heightPixels;
        this.c = c;
        gestureDetector = new GestureDetector(this.c, new GestureTap(c));
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        float newX,newY =0;
        if (gestureDetector.onTouchEvent(event)){
            Toast.makeText(getBaseContext(), "Clicked!", Toast.LENGTH_LONG).show();
            return true;
        }else{
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    lastAction = MotionEvent.ACTION_DOWN;
                    break;

                case MotionEvent.ACTION_MOVE:
                    newX = event.getRawX() + dX;
                    newY = event.getRawY() + dY;



                    if ((newX <= 0 || newX >= screenWidth-view.getWidth()) || (newY <= 0 || newY >= screenHight-getBarSize(c)))
                    {
                        Toast.makeText(c,"NAO MOVEU "+String.valueOf(view.getY()) ,Toast.LENGTH_SHORT).show();
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;
                    }

                    view.setX(newX);
                    view.setY(newY);

                    lastAction = MotionEvent.ACTION_MOVE;
                    break;

                case MotionEvent.ACTION_UP:
                    //if (lastAction == MotionEvent.ACTION_DOWN)

                    break;

                default:
                    return false;
            }
            return true;
        }
    }

    public float getBarSize(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}