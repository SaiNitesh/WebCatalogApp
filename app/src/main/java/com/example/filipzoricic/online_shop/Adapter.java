package com.example.filipzoricic.online_shop;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Hashtable;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements View.OnClickListener
{

    public static interface AdapterInterface{
        public void changeLayout(int size);
        public void requestNewData(Hashtable<String, Integer> params, String title);
    }

    JSONArray jsonArray;
    AdapterInterface adapterInterface;
    SimpleDiskCache simpleDiskCache;
    SimpleRamCache simpleRamCache;


    @Override
    public void onClick(View v) {
        adapterInterface.changeLayout(1);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        Handler handler;
        SwiperAnimation swiperAnimation;

        public MyViewHolder(CardView cardView) {
            super(cardView);
            this.cardView=cardView;
        }

        public void setHandler(Handler handler){
            this.handler=handler;
        }

        public void setSwiperAnimation(SwiperAnimation swiperAnimation){
            this.swiperAnimation=swiperAnimation;
        }

        public void killTask(){
            if(handler!=null){
                handler.removeCallbacks(swiperAnimation);
                Log.i("aaaeeee","task killed");
            }

        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public Adapter(JSONArray jsonArray, AdapterInterface adapterInterface, SimpleDiskCache simpleDiskCache, SimpleRamCache simpleRamCache){
        this.jsonArray=jsonArray;
        this.adapterInterface=adapterInterface;
        this.simpleDiskCache=simpleDiskCache;
        this.simpleRamCache=simpleRamCache;
        MainMenuActivity.frameLayout.setPadding(0,MainMenuActivity.top,0,0);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        CardView cardView = holder.cardView;
        switch ((String)cardView.getTag()){
            case "swiper":
                holder.killTask();
                Log.i("eee11","ubio");
                break;
            case "item_details":
                MainMenuActivity.frameLayout.setPadding(0,MainMenuActivity.top,0,0);
                break;
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        String type="";
        try{type=jsonArray.getJSONObject(viewType).getString("type");}catch (Exception e){}
        CardView cardView= null;
        Log.i("aaa",type);
        switch (type) {
            case "filter":
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_filter, parent, false);
                cardView.setTag("filter");
                break;
            case "item":
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
                cardView.setTag("item");
                break;
            case "item_details":
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_details_swiper, parent, false);
                cardView.setTag("item_details");
                break;
            case "label":
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_label, parent, false);
                cardView.setTag("label");
                break;
            case "swiper":
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_swiper, parent, false);
                cardView.setTag("swiper");
                break;
            case "poster":
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_poster, parent, false);
                cardView.setTag("poster");
                break;
            case "expandable_list":
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_expandable_list, parent, false);
                cardView.setTag("expandable_list");
        }


        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String type="";
        try{type=jsonArray.getJSONObject(position).getString("type"); Log.i("eee4",jsonArray.toString());}catch (Exception e){}

        Log.i("eeettt",String.valueOf(position));
        switch (type){
            case "filter":
                CardView cardView = holder.cardView;
                Button button = (Button) cardView.findViewById(R.id.button);
                button.setOnClickListener(this);
                break;
            case "item":
                setCardView(holder,position);
                break;
            case "item_details":
                setItemdetails(holder, position);
                break;
            case "label":
                setLabelView(holder, position);
                break;
            case "swiper":
                setSwiperView(holder, position);
                break;
            case "poster":
                setPosterView(holder, position);
                break;
            case "expandable_list":
                setExpandibleView(holder, position);
                break;
        }

    }

    public void setExpandibleView(MyViewHolder holder, int position){
        Log.i("aaaa","expandable dolazi");
        CardView cardView = holder.cardView;
        final Hashtable<String, Integer> params = new  Hashtable<String, Integer>();
        ExpandableListView expandableListView = (ExpandableListView) cardView.findViewById(R.id.expandableListView);
        JSONArray jsonArraydata=null;
        try {
            jsonArraydata=jsonArray.getJSONObject(position).getJSONArray("list");
            final int category = jsonArray.getJSONObject(position).getInt("id");
            expandableListView.setAdapter(new CustomExpandableListAdapter(expandableListView.getContext(), jsonArraydata, new CustomExpandableListAdapter.CustomExpandableListAdapterInterface() {
                @Override
                public void onChildClick(int item, int item_caterory, String title) {
                    params.put("category",category);
                    params.put("item", item);
                    params.put("item_category", item_caterory);
                    Log.i("aaarrr",params.toString());
                    adapterInterface.requestNewData(params, title);
                }
            }));
        }catch (Exception e){}
    }

    public void setPosterView(MyViewHolder holder, int position){
        Log.i("aaaa","poster dolazi");
        CardView cardView = holder.cardView;
        try {
            int height = jsonArray.getJSONObject(position).getInt("height");
            String image_url=jsonArray.getJSONObject(position).getString("image_url");
            String id=jsonArray.getJSONObject(position).getString("id");
            final ProgressBar progressBar = (ProgressBar) cardView.findViewById(R.id.card_view_poster_progressBar);
            final ImageView imageView = (ImageView) cardView.findViewById(R.id.card_view_poster_imageView);
            cardView.getLayoutParams().height=(int) (height * cardView.getContext().getResources().getDisplayMetrics().density + 0.5f);;

            ImageLoader imageLoader = new ImageLoader(new ImageLoader.ImageLoaderInterface() {
               @Override
               public void onLoadImage(Bitmap image) {
                   imageView.setImageBitmap(image);
                   progressBar.setVisibility(View.GONE);
               }
            },image_url,id, simpleDiskCache,simpleRamCache);
            imageLoader.execute();
        }catch (Exception e){}
    }

    public void setSwiperView(final MyViewHolder holder, int position){
        final CardView cardView = holder.cardView;
        try {
            final ViewPager viewPager = (ViewPager) cardView.findViewById(R.id.pager);
            JSONObject swiperData = jsonArray.getJSONObject(position);
            JSONArray images = swiperData.getJSONArray("images");
            final int height = swiperData.getInt("height");
            viewPager.getLayoutParams().height=(int) (height * viewPager.getContext().getResources().getDisplayMetrics().density + 0.5f);
                    CustumPagerAdapter customPagerAdapter = new CustumPagerAdapter(images, simpleDiskCache, simpleRamCache, new CustumPagerAdapter.PagerAdapterInterface() {
                        @Override
                        public void onPagerAdapterClick(int positionY) {

                        }

                        @Override
                        public void onPagerAdapterTouch() {
                            if(holder!=null)
                                holder.killTask();
                        }
                    });
                    viewPager.setAdapter(customPagerAdapter);
                    Handler handler = new Handler();
                    SwiperAnimation swiperAnimate = new SwiperAnimation(viewPager,handler);
                    holder.setHandler(handler);
                    holder.setSwiperAnimation(swiperAnimate);
                    handler.postDelayed(swiperAnimate,1000);


        }catch (Exception e){Log.i("eeegreskaa",e.toString());}




    }
    public void setCardView(MyViewHolder holder, final int position){
        CardView cardView = holder.cardView;


        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            final String name= jsonObject.getString("name");
            String url=jsonObject.getString("image_url");
            final int id =jsonObject.getInt("id");
            final Hashtable<String, Integer> params = new Hashtable<String, Integer>();

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    params.put("item_id", id);
                    adapterInterface.requestNewData(params, name);
                }
            });

            final ImageView imageView = (ImageView)cardView.findViewById(R.id.card_view_image);
            final ProgressBar progressBar = (ProgressBar) cardView.findViewById(R.id.card_view_progressBar);
            TextView textView = (TextView)cardView.findViewById(R.id.info_text);
            textView.setText(name);


            ImageLoader imageLoader = new ImageLoader(new ImageLoader.ImageLoaderInterface() {
                @Override
                public void onLoadImage(Bitmap image) {
                    progressBar.setVisibility(View.GONE);
                    imageView.setImageBitmap(image);
                }
            },url,String.valueOf(id),simpleDiskCache,simpleRamCache);
            imageLoader.execute();


        }catch (Exception e){}
    }

    public void setLabelView(MyViewHolder holder, int position){
        try{
            final String title=jsonArray.getJSONObject(position).getString("value");
            final int category_id=jsonArray.getJSONObject(position).getInt("id");
            final Hashtable<String, Integer> params = new Hashtable<String, Integer>();
            CardView cardView = holder.cardView;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    params.put("category", category_id);
                    adapterInterface.requestNewData(params, title);

                }
            });
            TextView textView = (TextView) cardView.findViewById(R.id.card_view_label_textView);
            textView.setText(title);

        }catch (Exception e){Log.i("greska","nije postavio");}
    }

    public void setItemdetails(MyViewHolder holder, final int position){
        final CardView cardView = holder.cardView;
        MainMenuActivity.frameLayout.setPadding(0,0,0,0);
        try {

            Log.i("eeetttt", jsonArray.getJSONObject(position).toString());
            final HackyProblematicViewGroup viewPager = (HackyProblematicViewGroup) cardView.findViewById(R.id.pager);
            final LinearLayout linearLayout = (LinearLayout) cardView.findViewById(R.id.card_view_item_detail_swiper_LinearLayout);
            JSONObject swiperData = jsonArray.getJSONObject(position);
            JSONArray images = swiperData.getJSONArray("images");
            String title = swiperData.getString("name");
            String description = swiperData.getString("description");

            TextView textViewtitle= (TextView) cardView.findViewById(R.id.card_view_item_detail_swiper_title);
            TextView textViewDescripction= (TextView) cardView.findViewById(R.id.card_view_item_detail_swiper_description);
            textViewtitle.setText(title);
            textViewDescripction.setText(description);
            CustomItemPagerAdapter customItemPagerAdapter = new CustomItemPagerAdapter(images, simpleDiskCache, simpleRamCache, new CustomItemPagerAdapter.PagerAdapterInterface() {
                @Override
                public void onPagerAdapterClick(int positionY) {
                    if(positionY==1){
                        MainMenuActivity.toolbar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                    }else{
                        linearLayout.setVisibility(View.VISIBLE);
                        MainMenuActivity.toolbar.setVisibility(View.VISIBLE);
                    }
                }

            });
            viewPager.setAdapter(customItemPagerAdapter);
        }catch (Exception e){Log.i("eeegreskaa",e.toString());}

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}

