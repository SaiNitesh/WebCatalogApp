package com.example.filipzoricic.online_shop;

/**
 * Created by filipzoricic on 9/30/16.
 */

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;

/**
 * Created by filipzoricic on 9/28/16.
 */
public class CustumPagerAdapter extends PagerAdapter {

    public static interface PagerAdapterInterface{
        public void onPagerAdapterClick(int positionY);
        public void onPagerAdapterTouch();
    }

    JSONArray images;
    PagerAdapterInterface pagerAdapterInterface;
    SimpleDiskCache simpleDiskCache;
    SimpleRamCache simpleRamCache;


    public CustumPagerAdapter(JSONArray images, SimpleDiskCache simpleDiskCache, SimpleRamCache simpleRamCache, PagerAdapterInterface pagerAdapterInterface) {
        this.images = images;
        this.pagerAdapterInterface = pagerAdapterInterface;
        this.simpleDiskCache=simpleDiskCache;
        this.simpleRamCache=simpleRamCache;
    }

    @Override
    public int getCount() {
        return images.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.i("aaa11", "postavio "+position);
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_poster, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.pager_poster_progressBar);

        try{
            String url =images.getJSONObject(position).getString("image_url");
            String id = images.getJSONObject(position).getString("id");


            ImageLoader imageLoader = new ImageLoader(new ImageLoader.ImageLoaderInterface() {
                @Override
                public void onLoadImage(Bitmap image) {
                    progressBar.setVisibility(View.GONE);
                    imageView.setImageBitmap(image);
                }
            },url,id,simpleDiskCache,simpleRamCache);
            imageLoader.execute();
        }catch (Exception e){}


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {pagerAdapterInterface.onPagerAdapterClick(position);}
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pagerAdapterInterface.onPagerAdapterTouch();
                return true;
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View itemView = (View) object;
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(null);
        container.removeView(itemView);
    }



}

