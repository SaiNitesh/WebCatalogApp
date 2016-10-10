package com.example.filipzoricic.online_shop;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by filipzoricic on 10/2/16.
 */
public class SimpleRamCache {
    LruCache<String, Bitmap> lruCache;

    static SimpleRamCache open(int memory){

        SimpleRamCache simpleRamCache = new SimpleRamCache();
        simpleRamCache.lruCache = new LruCache<String, Bitmap>(memory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        return simpleRamCache;
    }

    public Bitmap getBitmap(String key){
        return lruCache.get(key);
    }

    public void putBitmap(Bitmap bitmap, String key){
        if(getBitmap(key)==null){
            lruCache.put(key, bitmap);
        }

    }



}
