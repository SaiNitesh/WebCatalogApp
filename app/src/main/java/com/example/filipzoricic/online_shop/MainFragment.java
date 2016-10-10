package com.example.filipzoricic.online_shop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;


public class MainFragment extends android.app.Fragment implements DataProvider.DataProviderInterface, Adapter.AdapterInterface{


    public static interface MainFragmentInterface{
        public void changeFragment(Hashtable<String, Integer> params, String title);
    }

    MainFragmentInterface mainFragmentInterface;
    Hashtable<String, Integer> params;
    String title;
    SimpleRamCache simpleRamCache;
    SimpleDiskCache simpleDiskCache;
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Container container = (Container) bundle.getSerializable("params");
            params = container.getList();
            title = bundle.getString("title");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(title.length()>1)
            MainMenuActivity.toolbar.setTitle(title);
        else {
            MainMenuActivity.toolbar.setTitle("Web Catalog");
            params = new Hashtable<String, Integer>();
            params.put("empty",0);
        }
        try {
            if (simpleDiskCache.contains(params.toString())) {
                SimpleDiskCache.StringEntry stringEntry = simpleDiskCache.getString(params.toString());
                String k=stringEntry.getString();
                Log.i("aaa12",k);
                onLoad(new JSONArray(k));
            } else {
                DataProvider dataProvider = new DataProvider(getActivity(),this);
                dataProvider.getData(params);
            }
        } catch (Exception e){Log.i("aaa12", e.toString());}

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("eee","postavio");
        MainMenuActivity mainMenuActivity = (MainMenuActivity) context;
        mainFragmentInterface=mainMenuActivity;
        simpleDiskCache =mainMenuActivity.simpleDiskCache;
        simpleRamCache = mainMenuActivity.simpleRamCache;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        //TextView textView = (TextView) view.findViewById(R.id.textView);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragent_recycler_view);
        //textView.setText(params);
        Log.i("aaaeee","create view");

        return view;
    }

    @Override
    public void onLoad(JSONArray jsonArray) {
        if(jsonArray!=null){
            Log.i("aaa12",jsonArray.toString());
            try{simpleDiskCache.put(params.toString(),jsonArray.toString()); }catch (Exception e){}
            Adapter adapter = new Adapter(jsonArray,this,simpleDiskCache,simpleRamCache);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE );


            String type="";
            try{type= jsonArray.getJSONObject(0).getString("type");}catch (Exception e){}
            if(!type.equals("filter")){
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(linearLayoutManager);
            }else{
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch (position) {
                            case 0:
                                return 2;
                            default:
                                return 1;
                        }

                    }
                });
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        }
    }

    @Override
    public void changeLayout(int size) {

    }

    @Override
    public void requestNewData(Hashtable<String, Integer> params, String title) {
        mainFragmentInterface.changeFragment(params, title);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(recyclerView!=null){
            recyclerView.setAdapter(null);
        }


    }
}
