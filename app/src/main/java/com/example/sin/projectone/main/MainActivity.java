package com.example.sin.projectone.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sin.projectone.Constant;
import com.example.sin.projectone.HttpUtilsAsync;
import com.example.sin.projectone.ProductDBHelper;
import com.example.sin.projectone.R;
import com.example.sin.projectone.WebService;
import com.example.sin.projectone.item.MainItem;
import com.example.sin.projectone.payment.MainPayment;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainItem.OnFragmentInteractionListener{
    private boolean doubleBackToExitPressedOnce = false;
    private FragmentManager fragmentManager;
    private String userName= "";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.deleteDatabase(ProductDBHelper.DATABASE_NAME); // debug
        int a = Constant.SHOP_ID;
        loadProducts();
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        //setContentView(R.layout.content_main);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView emailNavText = (TextView) headerView.findViewById(R.id.textView);
        emailNavText.setText(userName);
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
            MainPayment firstFragment = new MainPayment();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_main, firstFragment).commit();
            toolbar.setTitle("Payment");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment containerFragment = fragmentManager.findFragmentByTag(Constant.TAG_FRAGMENT_CONTAINER);
        int childFragmentStack = 0;
        int fragmentStack = getFragmentManager().getBackStackEntryCount();
        if(containerFragment!=null){
            childFragmentStack = containerFragment.getChildFragmentManager().getBackStackEntryCount();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(childFragmentStack>0){
            containerFragment.getChildFragmentManager().popBackStack();
        } else if(fragmentStack>0){
            getFragmentManager().popBackStack();
        }
        else if(doubleBackToExitPressedOnce){
            super.onBackPressed();
        }else{
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
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
            toolbar.setTitle("Payment");
            newFragment = new MainPayment();
        } else if (id == R.id.nav_product) {
            //newFragment = new com.example.sin.projectone.item.Container();
            newFragment = new MainItem();
            toolbar.setTitle("Product");
        } else if (id == R.id.nav_report) {
            newFragment = new com.example.sin.projectone.report.Container();
            toolbar.setTitle("Report");
        } else if (id == R.id.nav_receipt) {
            newFragment = new com.example.sin.projectone.receipt.Container();
        } else if (id == R.id.nav_contact) {
            newFragment = new com.example.sin.projectone.help.Main();
            toolbar.setTitle("Contact");
        }
        if(newFragment!=null){
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            String tag = Constant.TAG_FRAGMENT_CONTAINER;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_main, newFragment ,tag);

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


    @Override
    public void onRepleceFragment(Fragment newFragment, Bundle bundle) {
        if(newFragment!=null){
            String tag = Constant.TAG_FRAGMENT_CONTAINER;
            newFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_main, newFragment ,tag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void clearBackStackFragment(){

    }
}
