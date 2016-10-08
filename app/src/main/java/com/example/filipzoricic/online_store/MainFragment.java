package com.example.filipzoricic.online_store;

import android.app.Fragment;
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


public class MainFragment extends Fragment implements Adapter.AdapterInterface, DataProvider.DataProviderInterface{
    JSONArray fragmentJsonArray;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    int selection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selection = bundle.getInt("selection");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        DataProvider dataProvider = new DataProvider(view.getContext(),this);
        dataProvider.getData(String.valueOf(selection),null);

        progressBar = (ProgressBar) view.findViewById(R.id.main_progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragent_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // 5 is the sum of items in one repeated section
                switch (position) {
                    // first two items span 3 columns each
                    case 0:
                        return 2;
                    // next 3 items span 2 columns each
                    default:
                        return 1;
                }

            }
        });
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }

    @Override
    public void changeLayout(int size) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), size);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onLoad(JSONArray jsonArray) {
        if(jsonArray!=null) {
            Log.i("eeee",String.valueOf(jsonArray.length()));
            Adapter adapter = new Adapter(jsonArray,this);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
