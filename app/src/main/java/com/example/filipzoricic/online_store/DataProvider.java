package com.example.filipzoricic.online_store;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by filipzoricic on 9/25/16.
 */
public class DataProvider implements Response.Listener<JSONArray>, Response.ErrorListener {

    public static interface DataProviderInterface{
        public void onLoad(JSONArray jsonArray);
    };

    String url="http://192.168.5.10/webShop/main.php";
    JSONArray response;
    Context context;
    DataProviderInterface dataProviderInterface;

    public DataProvider(Context context, DataProviderInterface dataProviderInterface ){
        this.context = context;
        this.dataProviderInterface = dataProviderInterface;
    }



   public void getData(String selection_1, String selection_2){
       RequestQueue queue = Volley.newRequestQueue(context);
       if(selection_1!=null){
           url+="?selection_1="+selection_1;
       }
       if(selection_1!=null && selection_2 != null){
           url+="&selection_2="+selection_2;
       }
       response=null;
       Log.i("eee", url);
       JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
       queue.add(jsObjRequest);
   };

    @Override
    public void onResponse(JSONArray response) {
        dataProviderInterface.onLoad(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("eee", error.toString());
        dataProviderInterface.onLoad(null);
    }
}
