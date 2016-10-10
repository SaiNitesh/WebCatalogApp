package com.example.filipzoricic.online_shop;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by filipzoricic on 10/7/16.
 */
public class HackyProblematicDrawerLayout extends android.support.v4.widget.DrawerLayout {


    public HackyProblematicDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HackyProblematicDrawerLayout(Context context) {
        super(context);
    }

    public HackyProblematicDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            Log.i("eeerrr",e.toString());
            return false;
        } catch (ArrayIndexOutOfBoundsException e)
        {
            Log.i("eeerrr",e.toString());
            return false;
        }
    }
}
