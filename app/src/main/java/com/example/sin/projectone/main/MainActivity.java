package com.example.sin.projectone.main;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.HttpUtilsAsync;
import com.example.sin.projectone.MainNav;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;
import com.example.sin.projectone.WebService;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deleteDatabase(ProductDBHelper.DATABASE_NAME); // debug
        int a = Constant.SHOP_ID;
        loadProducts();
        //setContentView(R.layout.content_main);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container_main) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            WelcomeFragment firstFragment = new WelcomeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_main, firstFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment newFragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_payment) {
             //newFragment = new com.example.sin.projectone.payment.Container();
            newFragment = new com.example.sin.projectone.payment.Main2();
        } else if (id == R.id.nav_product) {
            newFragment = new com.example.sin.projectone.item.Container();
        } else if (id == R.id.nav_report) {
            newFragment = new com.example.sin.projectone.report.Container();
        } else if (id == R.id.nav_receipt) {
            newFragment = new com.example.sin.projectone.receipt.Container();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        if(newFragment!=null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_main, newFragment);

            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean loadProducts(){
        WebService.getAllProduct(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.length()>0){
                        ProductDBHelper.getInstance(MainActivity.this.getApplicationContext()).LoadProduct(response.getJSONArray("Products"));
                    }
                    else if(response.length()==0){
                        System.out.println("Empty");
                    }
                    loadTransaction();
                    System.out.println("finish");
                    //ProductDBHelper.getInstance(getApplicationContext()).ShowListProduct();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    private boolean loadTransaction(){
        // debug
        HttpUtilsAsync.get(Constant.URL_SEND_TRANSACTION+Constant.SHOP_ID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.length()>0){
                        ProductDBHelper.getInstance(MainActivity.this.getApplicationContext()).loadTransaction(response.getJSONArray("transaction"));
                        for(int i=0;i<response.getJSONArray("transaction").length();i++){
                            System.out.println(response.getJSONArray("transaction").length());
                            System.out.println(response.getJSONArray("transaction").getJSONObject(i));
                            JSONObject jsonObj = response.getJSONArray("transaction").getJSONObject(i);
                            String createDate = jsonObj.getString("createAt");
                            createDate = createDate.replace(' ','T');
                            System.out.println(jsonObj.getString("transactionID"));
                            System.out.println(jsonObj.getString("createAt"));
                            HttpUtilsAsync.get(Constant.URL_GET_TRANSACTION_DETAIL+jsonObj.getString("transactionID")+"/"+createDate, null, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    System.out.println(response);
                                    try {
                                        ProductDBHelper.getInstance(MainActivity.this.getApplicationContext()).loadTransactionDetail(response.getJSONArray("transactionDetail"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    else if(response.length()==0){
                        System.out.println("Empty");
                    }
                    System.out.println("finish");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }


}
