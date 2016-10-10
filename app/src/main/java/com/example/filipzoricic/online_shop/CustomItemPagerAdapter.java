package com.example.filipzoricic.online_shop;

/**
 * Created by filipzoricic on 10/4/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;

import uk.co.senab.photoview.PhotoViewAttacher;


public class CustomItemPagerAdapter extends PagerAdapter{



    public static interface PagerAdapterInterface{
        public void onPagerAdapterClick(int positionY);
    }

    boolean on_off_switch;
    JSONArray images;
    PagerAdapterInterface pagerAdapterInterface;
    SimpleDiskCache simpleDiskCache;
    SimpleRamCache simpleRamCache;


    public CustomItemPagerAdapter(JSONArray images, SimpleDiskCache simpleDiskCache, SimpleRamCache simpleRamCache, PagerAdapterInterface pagerAdapterInterface) {
        this.pagerAdapterInterface = pagerAdapterInterface;
        this.simpleDiskCache = simpleDiskCache;
        this.simpleRamCache = simpleRamCache;
        this.images = images;
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

        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.pager_item_progressBar);
        try {
            String url = images.getJSONObject(position).getString("image_url");
            String id = images.getJSONObject(position).getString("id");
            Log.i("eeerrr",url);
            ImageLoader imageLoader = new ImageLoader(new ImageLoader.ImageLoaderInterface() {
                @Override
                public void onLoadImage(Bitmap image) {
                    progressBar.setVisibility(View.GONE);
                    imageView.setImageBitmap(image);
                    PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
                    attacher.update();
                    attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                        @Override
                        public void onViewTap(View view, float x, float y) {
                            on_off_switch=!on_off_switch;
                            pagerAdapterInterface.onPagerAdapterClick((on_off_switch) ? 1 : 0);
                        }
                    });
                }
            }, url, id, simpleDiskCache, simpleRamCache);
            imageLoader.execute();

        }catch (Exception e){ Log.i("eeerrr", e.toString());}
        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageBitmap(null);
        container.removeView(view);
    }


}

