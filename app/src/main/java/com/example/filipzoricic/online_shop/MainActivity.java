package com.example.filipzoricic.online_shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements DataProvider.DataProviderInterface{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataProvider dataProvider = new DataProvider(this,this);

        Hashtable<String, Integer> params = new Hashtable<String, Integer>();
        params.put("category",0);
        dataProvider.getData(params);

    }


    @Override
    public void onLoad(JSONArray jsonArray) {
        if(jsonArray!=null){
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("Data",jsonArray.toString());
            startActivity(intent);
        }

    }
}
