package com.example.filipzoricic.online_store;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by filipzoricic on 9/26/16.
 */
public class ListViewAdapter extends BaseAdapter {

    public static interface ListViewAdapterInterface{
        public void onItemClick(int position);
    }

    public static interface ListViewAdapterSwiperInterface{
        public void onSwiperClick(int positionX, int positionY);
    }

    JSONArray jsonArray;
    ListViewAdapterInterface listViewAdapterInterface;
    ListViewAdapterSwiperInterface listViewAdapterSwiperInterface;
    private static LayoutInflater inflater=null;
    Context context;
    float scale;
    boolean animation=true;
    CustumPagerAdapter[] custumPagerAdapterlist;
    int customcount;


    public ListViewAdapter(Context context, JSONArray jsonArray, ListViewAdapterInterface listViewAdapterInterface) {
        scale = context.getResources().getDisplayMetrics().density;
        this.jsonArray=jsonArray;
        this.listViewAdapterInterface = listViewAdapterInterface;
        this.context=context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public ListViewAdapter(Context context, JSONArray jsonArray, ListViewAdapterInterface listViewAdapterInterface, ListViewAdapterSwiperInterface listViewAdapterSwiperInterface) {
        scale = context.getResources().getDisplayMetrics().density;
        this.jsonArray=jsonArray;
        this.listViewAdapterInterface = listViewAdapterInterface;
        this.context=context;
        this.listViewAdapterSwiperInterface=listViewAdapterSwiperInterface;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        custumPagerAdapterlist = new CustumPagerAdapter[3];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String type="";
        View rowView=null;
        try{
            final JSONObject jsonObject = jsonArray.getJSONObject(position);
            type = jsonObject.getString("type");
            Holder holder=new Holder();
            switch (type){
                case "poster":
                    String url = jsonObject.getString("image_url");
                    rowView = inflater.inflate(R.layout.menu_bar_poster, null);
                    ImageView imageView = (ImageView) rowView.findViewById(R.id.poster_imageView);
                    ImageLoader imageLoader = new ImageLoader(imageView,url);
                    imageLoader.execute();
                    break;

                case "swiper":
                    rowView = inflater.inflate(R.layout.swiper, null);
                    final ViewPager viewPager = (ViewPager) rowView.findViewById(R.id.pager);

                    try{
                        JSONObject swiperData = jsonArray.getJSONObject(position);
                        JSONArray images = swiperData.getJSONArray("images");
                        final int height = swiperData.getInt("height");
                        ImagesLoader imagesLoader = new ImagesLoader(images, new ImagesLoader.ImagesLoaderInterface() {
                            @Override
                            public void onDownload(Bitmap[] bitmaps) {
                                CustumPagerAdapter customPagerAdapter = new CustumPagerAdapter(context, bitmaps, new CustumPagerAdapter.PagerAdapterInterface() {
                                    @Override
                                    public void onPagerAdapterClick(int positionY) {
                                        if(listViewAdapterInterface!=null)
                                            listViewAdapterSwiperInterface.onSwiperClick(position, positionY);

                                    }
                                });

                                viewPager.setAdapter(customPagerAdapter);
                                viewPager.getLayoutParams().height=(int) (height * scale + 0.5f);
                                Handler handler = new Handler();
                                SwiperAnimate swiperAnimate = new SwiperAnimate(viewPager,handler);
                                //handler.postDelayed(swiperAnimate,3000);
                                custumPagerAdapterlist[customcount]=customPagerAdapter;
                                customcount+=1;

                            }
                        });
                        imagesLoader.execute();
                    }catch (Exception e){}

                    break;
                case "title":
                    Log.i("eee","stvoren");
                    String name = jsonObject.getString("name");
                    rowView = inflater.inflate(R.layout.side_menu_bar, null);
                    holder.tv=(TextView) rowView.findViewById(R.id.side_menu_bar_text_view);
                    holder.tv.setText(name);
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listViewAdapterInterface.onItemClick(position);
                        }
                    });
                    break;

            }
        }catch (Exception e){}



        return rowView;
    }


    public class SwiperAnimate implements Runnable {
        ViewPager viewPager;
        Handler handler;
        boolean direction;
        int size;
        int position;
        public SwiperAnimate(ViewPager viewPager, Handler handler){
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
            if(animation)
                handler.postDelayed(this,3000);
        }

    }


    public void killAnimations(){
        animation=false;
        for(int i=0; i<customcount; i++){
            for(int j=0; j<custumPagerAdapterlist[i].bitmaps.length; j++){

                custumPagerAdapterlist[j].bitmaps[j].recycle();
                custumPagerAdapterlist[j].bitmaps[j]=null;

            }

        }
    }


}