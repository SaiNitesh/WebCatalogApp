package com.example.filipzoricic.online_shop;

/**
 * Created by filipzoricic on 9/30/16.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;


/**
 * Created by filipzoricic on 9/25/16.
 */
public class DataProvider implements Response.Listener<JSONArray>, Response.ErrorListener {

    public static interface DataProviderInterface{
        public void onLoad(JSONArray jsonArray);
    };

    String url="http://192.168.5.13/webShop/main.php";
    Context context;
    DataProviderInterface dataProviderInterface;

    public DataProvider(Context context, DataProviderInterface dataProviderInterface ){
        this.context = context;
        this.dataProviderInterface = dataProviderInterface;
    }



    public void getData(Hashtable<String, Integer> params){
        RequestQueue queue = Volley.newRequestQueue(context);

        if(params!=null){
            if(params.get("item_id")!=null){
                url+="?item_id="+params.get("item_id");
            }
            else{
                url+="?category="+params.get("category")+"&item="+params.get("item")+"&item_category="+params.get("item_category");
            }
        }

        Log.i("eeetttt", url);
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
