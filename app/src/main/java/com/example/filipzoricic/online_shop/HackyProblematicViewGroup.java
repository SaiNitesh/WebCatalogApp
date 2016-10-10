package com.example.filipzoricic.online_shop;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by filipzoricic on 10/7/16.
 */
public class HackyProblematicViewGroup extends ViewPager {
    public HackyProblematicViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackyProblematicViewGroup(Context context) {
        super(context);
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
