package com.example.filipzoricic.online_shop;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by filipzoricic on 10/5/16.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    public interface CustomExpandableListAdapterInterface{
        public void onChildClick(int item, int item_caterory, String title);
    }

    CustomExpandableListAdapterInterface customExpandableListAdapterInterface;
    Context context;
    JSONArray jsonArray;

    public CustomExpandableListAdapter(Context context,JSONArray jsonArray, CustomExpandableListAdapterInterface customExpandableListAdapterInterface){
        this.jsonArray = jsonArray;
        this.context = context;
        this.customExpandableListAdapterInterface = customExpandableListAdapterInterface;
    }

    @Override
    public int getGroupCount() {
        return jsonArray.length();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size=0;
        try{size = jsonArray.getJSONObject(groupPosition).getJSONArray("child_list").length();}catch (Exception e){}
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        JSONArray jsonArray = null;
        try{jsonArray = jsonArray.getJSONArray(groupPosition);}catch (Exception e){}
        return jsonArray;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        JSONObject jsonObject=null;
        try{jsonObject = jsonArray.getJSONObject(groupPosition).getJSONArray("child_list").getJSONObject(childPosition);}catch (Exception e){}
        return jsonObject;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label, parent, false);
        String value = "";
        try {value = jsonArray.getJSONObject(groupPosition).getString("value");} catch (Exception e){}
        TextView textView = (TextView) view.findViewById(R.id.label_text_view);
        textView.setText(value);
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_child, parent, false);
        try{
            final int item_category = jsonArray.getJSONObject(groupPosition).getJSONArray("child_list").getJSONObject(childPosition).getInt("id");
            final int item = jsonArray.getJSONObject(groupPosition).getInt("id");
            final String title = jsonArray.getJSONObject(groupPosition).getJSONArray("child_list").getJSONObject(childPosition).getString("value");


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customExpandableListAdapterInterface.onChildClick(item, item_category, title);
                }
            });
        }catch (Exception e){Log.i("aaarrr",e.toString());}

        String value = "";
        try {value = jsonArray.getJSONObject(groupPosition).getJSONArray("child_list").getJSONObject(childPosition).getString("value");} catch (Exception e){}
        TextView textView = (TextView) view.findViewById(R.id.label_text_view);
        textView.setText(value);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
