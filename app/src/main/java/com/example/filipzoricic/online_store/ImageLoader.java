package com.example.filipzoricic.online_store;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

/**
 * Created by filipzoricic on 9/26/16.
 */
public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
    ImageView imageView;
    String url;
    public ImageLoader(ImageView imageView, String url){
        this.imageView=imageView;
        this.url=url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        try{
            URL newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
        }catch (Exception e){

        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
    }
}
