package com.example.filipzoricic.online_store;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by filipzoricic on 9/28/16.
 */
public class CustumPagerAdapter extends PagerAdapter {

    public static interface PagerAdapterInterface{
        public void onPagerAdapterClick(int positionY);
    }

    Context mContext;
    Bitmap[] bitmaps;
    LayoutInflater mLayoutInflater;
    PagerAdapterInterface pagerAdapterInterface;

    public CustumPagerAdapter(Context context, Bitmap[] bitmaps, PagerAdapterInterface pagerAdapterInterface) {
        this.bitmaps = bitmaps;
        this.pagerAdapterInterface = pagerAdapterInterface;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return bitmaps.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmaps[position]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {pagerAdapterInterface.onPagerAdapterClick(position);}
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}

