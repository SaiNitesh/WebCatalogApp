package com.example.filipzoricic.online_store;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainMenuActivity extends AppCompatActivity implements ListViewAdapter.ListViewAdapterInterface {
    ListView drawerList;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    JSONArray jsonArray;
    int selection;
    Toolbar toolbar;

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selection",selection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        if(savedInstanceState!=null){
            selection = savedInstanceState.getInt("selection");
            createFragment();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String js =  extras.getString("Data");
            try{
                jsonArray = new JSONArray(js);
            }catch (Exception e){}
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);


        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        drawerList = (ListView) this.findViewById(R.id.main_list_view);
        drawerList.setAdapter(new ListViewAdapter(this,jsonArray,this));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);


    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        try{
            this.selection= jsonArray.getJSONObject(position).getInt("id");
            toolbar.setTitle(jsonArray.getJSONObject(position).getString("name"));
        }catch (Exception e){}
        createFragment();
        drawerLayout.closeDrawer(drawerList);

    }

    public void createFragment(){

        Bundle bundle = new Bundle();
        bundle.putInt("selection", selection);
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame_layout, menuFragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        Log.i("eeeee", "UNISTIO SAM TE");
        super.onDestroy();
    }
}
