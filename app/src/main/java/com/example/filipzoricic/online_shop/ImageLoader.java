package com.example.filipzoricic.online_shop;

/**
 * Created by filipzoricic on 9/30/16.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by filipzoricic on 9/26/16.
 */
public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

    public static interface ImageLoaderInterface{
        public void onLoadImage(Bitmap image);
    }

    ImageLoaderInterface imageLoaderInterface;
    String url;
    String id;
    SimpleDiskCache simpleDiskCache;
    SimpleRamCache simpleRamCache;

    public ImageLoader(ImageLoaderInterface imageLoaderInterface, String url, String id, SimpleDiskCache simpleDiskCache, SimpleRamCache simpleRamCache) throws IOException {
        this.imageLoaderInterface = imageLoaderInterface;
        this.simpleDiskCache=simpleDiskCache;
        this.simpleRamCache=simpleRamCache;
        this.url=url;
        this.id=id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        try {
            if (simpleRamCache.getBitmap(id) != null) {
                Log.i("aaa17", "pronasao sliku na RAM");
                return simpleRamCache.getBitmap(id);
            } else if (simpleDiskCache.contains(id)) {
                SimpleDiskCache.BitmapEntry bitmapEntry = simpleDiskCache.getBitmap(id);
                Bitmap image = bitmapEntry.getBitmap();
                Log.i("aaa17", "pronasao sliku na HDD");
                simpleRamCache.putBitmap(image, id);
                return image;

            } else {
                Log.i("aaa17", "Preuzeo sliku sa Weba");
                URL newurl = new URL(url);
                bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                InputStream is = new ByteArrayInputStream(stream.toByteArray());
                Log.i("aaa17", "Spremio sliku u Ram");
                simpleDiskCache.put(id, is);
                simpleRamCache.putBitmap(bitmap, id);
                Log.i("aaa17", "Spremio sliku u HDD");
                return bitmap;

            }
        }catch (Exception e){Log.e("eee",e.toString());}


        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!=null){
            imageLoaderInterface.onLoadImage(bitmap);
        }
    }



}
