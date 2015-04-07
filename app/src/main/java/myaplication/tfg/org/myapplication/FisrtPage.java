package myaplication.tfg.org.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FisrtPage extends ActionBarActivity {
    private String[] drawerListViewItems;
    private ListView listView;
    private ArrayList<HashMap<String,String>> list;
    private SimpleAdapter listAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView navigationList;
    private Intent intent;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisrt_page);
        initListView();
        initCusmizedActionBar();
        initNavigationList();

    }




    private void initNavigationList() {
        drawerListViewItems = getResources().getStringArray(R.array.items);
        navigationList = (ListView)findViewById(R.id.left_drawer);
        navigationList.setAdapter(new ArrayAdapter<String>(this,R.layout.navigation_list_items,drawerListViewItems));
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

    }

    private void initCusmizedActionBar() {
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.customactionbar, null);
        imageButton = (ImageButton)findViewById(R.id.shopCartButton);



        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

    }

    public void Cart(View view){
        Intent intent = new Intent(this,shopCart.class);
        startActivity(intent);
    }
    private void initListView() {   listView = (ListView)findViewById(R.id.list1);
        list =new ArrayList<HashMap<String, String>>();
        HashMap<String,String> map1=new HashMap<String,String>();
        HashMap<String,String> map2=new HashMap<String,String>();
        HashMap<String,String> map3=new HashMap<String,String>();
        map1.put("nom","Oferta >>");
        map1.put("images",Integer.toString(R.drawable.oferta));
        map2.put("nom","Novedades >>");
        map2.put("images",Integer.toString(R.drawable.novedades));
        map3.put("nom","Más Vendidos >>");
        map3.put("images",Integer.toString(R.drawable.mas_vendidos));
        list.add(map1);
        list.add(map2);
        list.add(map3);
        listAdapter = new SimpleAdapter(this, list,R.layout.list_item, new String[]{"nom","images"},new int[]{R.id.rowTextView,R.id.images});
        listView.setAdapter(listAdapter);
    }


    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_fisrt_page, menu);
    
    
            return true;
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            if(  mDrawerLayout.isDrawerOpen(GravityCompat.START)
                    ){
                mDrawerLayout.closeDrawers();
            }else{
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
