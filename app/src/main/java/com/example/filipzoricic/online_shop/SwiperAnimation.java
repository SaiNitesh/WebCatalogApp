package com.example.filipzoricic.online_shop;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by filipzoricic on 10/1/16.
 */
public class SwiperAnimation implements Runnable {
    ViewPager viewPager;
    Handler handler;
    boolean direction;
    int size;
    int position;
    public SwiperAnimation(ViewPager viewPager, Handler handler){
        this.viewPager=viewPager;
        this.handler=handler;
        size=viewPager.getAdapter().getCount()-1;
        direction=true;
    }
    @Override
    public void run() {
        Log.i("eee4", String.valueOf(direction));
        if(direction) {
            if (position < size) {
                position += 1;
                viewPager.setCurrentItem(position,true);
            } else {
                direction = false;
            }
        }
        else if(!direction) {
            if (position > 0) {
                position -= 1;
                viewPager.setCurrentItem(position, true);
            } else {
                direction = true;
            }
            Log.i("eee4", String.valueOf(position));
        }
        handler.postDelayed(this,3000);
    }

}

