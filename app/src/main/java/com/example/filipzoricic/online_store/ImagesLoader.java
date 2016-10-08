package com.example.filipzoricic.online_store;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.net.URL;

/**
 * Created by filipzoricic on 9/29/16.
 */
public class ImagesLoader extends AsyncTask<Void, Void, Bitmap[]> {

    public static interface ImagesLoaderInterface{
        public void onDownload(Bitmap[] bitmaps);
    }

    JSONArray jsonArray;
    ImagesLoaderInterface imagesLoaderInterface;

    public ImagesLoader(JSONArray jsonArray, ImagesLoaderInterface imagesLoaderInterface){
        this.jsonArray=jsonArray;
        this.imagesLoaderInterface = imagesLoaderInterface;
    }

    @Override
    protected Bitmap[] doInBackground(Void... params) {
        Bitmap[] bitmaps = new Bitmap[jsonArray.length()];
        for(int i=0; i<jsonArray.length(); i++){
            try{
                URL newurl = new URL(jsonArray.getJSONObject(i).getString("image_url"));
                Log.i("eee","filip" +jsonArray.getJSONObject(i).getString("image_url"));

                bitmaps[i] = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
            }catch (Exception e){ Log.i("eee",e.toString());}
        }
        return  bitmaps;
    }

    @Override
    protected void onPostExecute(Bitmap[] bitmaps) {
        super.onPostExecute(bitmaps);
        imagesLoaderInterface.onDownload(bitmaps);

    }
}
