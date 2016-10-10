package com.example.filipzoricic.online_shop;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import org.json.JSONArray;

import java.io.File;
import java.util.Hashtable;

public class MainMenuActivity extends AppCompatActivity implements Adapter.AdapterInterface, MainFragment.MainFragmentInterface{
        RecyclerView drawerList;
        DrawerLayout drawerLayout;
        ActionBarDrawerToggle drawerToggle;
        JSONArray jsonArray;
        int layoutRow;
        SimpleRamCache simpleRamCache;
        SimpleDiskCache simpleDiskCache;
        static int top;
        static FrameLayout frameLayout;
        static Toolbar toolbar;





        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.black));
            }
            return super.onCreateView(parent, name, context, attrs);
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            //outState.putInt("selection",selection);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_menu);
            layoutRow = 2;
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("ONLINE SHOP");
            setSupportActionBar(toolbar);

            try {
                long size=50000000;
                File external = Environment.getExternalStorageDirectory();

                if(external.getFreeSpace()<size)
                    size=external.getFreeSpace();
                Log.i("aaa17", "HDD memory free: "+(String.valueOf(external.getFreeSpace()/ 1024)));
                simpleDiskCache = SimpleDiskCache.open(this.getExternalCacheDir(),1,size);
                int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
                Log.i("aaa17","RAM memory free: "+String.valueOf(maxMemory));
                simpleRamCache = SimpleRamCache.open(maxMemory / 4);

            }catch (Exception e){
                Log.i("aaa17","nije inicijalizirao "+e.toString());
            }


            if(savedInstanceState!=null){
                //selection = savedInstanceState.getInt("selection");
                //changeData();
            }


            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                String js =  extras.getString("Data");
                try{
                    jsonArray = new JSONArray(js);
                }catch (Exception e){}
            }
            toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

            top = (int) (57 * getResources().getDisplayMetrics().density + 0.5f);
            frameLayout = (FrameLayout) findViewById(R.id.activity_main_FragmentLayout);
            drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
            drawerList = (RecyclerView) this.findViewById(R.id.main_menu_side_RecyclerView);
            drawerList.setLayoutManager(new LinearLayoutManager(this));
            Log.i("aaa",jsonArray.toString());
            drawerList.setAdapter(new Adapter(jsonArray,this,simpleDiskCache,simpleRamCache));

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    System.gc();
                    Log.i("eee","brisem");
                }
            };

            drawerLayout.setDrawerListener(drawerToggle);
            //recyclerView = (RecyclerView) findViewById(R.id.main_menu_RecyclerView);
            changeData(null,"");


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
        public void changeFragment(Hashtable<String, Integer> params, String title) {
            changeData(params,title);
        }


        public void changeData(Hashtable<String, Integer> params, String title){
            if(drawerLayout.isDrawerOpen(drawerList))
                drawerLayout.closeDrawer(Gravity.LEFT);
            Bundle bundle = new Bundle();

            bundle.putSerializable("params", new Container(params));
            bundle.putString("title", title);

            MainFragment fragment = new MainFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.activity_main_FragmentLayout, fragment);
            ft.addToBackStack("fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        }



        @Override
        public void changeLayout(int size) {
            layoutRow=size;
        }

        @Override
        public void requestNewData(Hashtable<String, Integer> params, String title) {
            changeData(params, title);
        }

        @Override
        protected void onStop() {
            super.onStop();
            Log.i("aaa17","unistavam");
            try{simpleDiskCache.clear(); Log.i("aaa17","pobrisao");}catch (Exception e){Log.i("aaa17",e.toString());}

        }


        @Override
        public void onBackPressed() {
            // TODO Auto-generated method stub
            if(drawerLayout.isDrawerOpen(drawerList))
                drawerLayout.closeDrawer(Gravity.LEFT);
            else if(getFragmentManager().getBackStackEntryCount()!=0&&getFragmentManager().getBackStackEntryCount()>1)
                getFragmentManager().popBackStack();
            else{
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }


        }

}
