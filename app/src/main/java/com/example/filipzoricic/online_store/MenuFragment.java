package com.example.filipzoricic.online_store;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;


public class MenuFragment extends Fragment implements ListViewAdapter.ListViewAdapterInterface, ListViewAdapter.ListViewAdapterSwiperInterface, DataProvider.DataProviderInterface
{
    ListView listView;
    View view;
    int selection;
    JSONArray jsonArray;
    ListViewAdapter listViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selection = bundle.getInt("selection");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        listView = (ListView) view.findViewById(R.id.fragment_menu_list);
        DataProvider dataProvider = new DataProvider(view.getContext(),this);
        dataProvider.getData(String.valueOf(selection),null);
        return view;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLoad(JSONArray jsonArray) {
        Log.i("eee",jsonArray.toString());
        this.jsonArray =  jsonArray;
        listViewAdapter = new ListViewAdapter(view.getContext(),jsonArray,this,this);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    public void onSwiperClick(int positionX, int positionY) {
        try{
            JSONObject jsonObject = jsonArray.getJSONObject(positionX).getJSONArray("images").getJSONObject(positionY);
            Log.i("eee", jsonObject.getString("id"));
        }catch (Exception e){
            Log.i("eee", e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListViewAdapter listViewAdapter= (ListViewAdapter)listView.getAdapter();
        listViewAdapter.killAnimations();
    }


}
