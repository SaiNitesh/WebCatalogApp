package com.example.filipzoricic.online_store;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by filipzoricic on 9/26/16.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements View.OnClickListener
{
    public static gridSize=2
    public static interface AdapterInterface{
        public void changeLayout(int size);
    }

    JSONArray jsonArray;
    AdapterInterface adapterInterface;
    @Override
    public void onClick(View v) {
        adapterInterface.changeLayout(1);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public MyViewHolder(CardView cardView) {
            super(cardView);
            this.cardView=cardView;
        }


    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public Adapter(JSONArray jsonArray, AdapterInterface adapterInterface){
        this.jsonArray=jsonArray;
        this.adapterInterface=adapterInterface;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cardView;

        switch (viewType){
            case 0:
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_filter,parent, false);
                break;
            default:
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent, false);
                break;
        }


        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        switch (position){
            case 0:
                CardView cardView = holder.cardView;
                Button button = (Button) cardView.findViewById(R.id.button);
                button.setOnClickListener(this);
                break;
            default:
                setCardView(holder,position);

                break;
        }

    }

    public void setCardView(MyViewHolder holder, int position){
        CardView cardView = holder.cardView;
        String name = "";
        String url="";
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            name= jsonObject.getString("name");
            url=jsonObject.getString("image_url");
        }catch (Exception e){

        }
        ImageView imageView = (ImageView)cardView.findViewById(R.id.card_view_image);
        //Drawable drawable = cardView.getResources().getDrawable(imageIds[position]);
        //imageView.setImageDrawable(drawable); imageView.setContentDescription(captions[position]);
        ImageLoader imageLoader = new ImageLoader(imageView,url);
        imageLoader.execute();

        //profile_photo.setImageBitmap(mIcon_val);
        TextView textView = (TextView)cardView.findViewById(R.id.info_text);
        textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

}
