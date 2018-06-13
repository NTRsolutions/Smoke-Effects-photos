package com.dvinfosys.smokeeffectphotomaker.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.dvinfosys.smokeeffectphotomaker.Adapter.VerticalMoreAppsRecyclerViewAdapter;
import com.dvinfosys.smokeeffectphotomaker.Adapter.TopAppsRecyclerAdapter;
import com.dvinfosys.smokeeffectphotomaker.R;
import com.dvinfosys.smokeeffectphotomaker.Response.TopAppListResponse;
import com.dvinfosys.smokeeffectphotomaker.Response.VerticalMoreAppsResponse;
import com.dvinfosys.smokeeffectphotomaker.Utils.ApiInterface;
import com.dvinfosys.smokeeffectphotomaker.Utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppHub extends AppCompatActivity {

    TopAppsRecyclerAdapter topAppsRecyclerAdapter;
    VerticalMoreAppsRecyclerViewAdapter verticalMoreAppsRecyclerViewAdapter;
    private ApiInterface apiInterface;
    private RetrofitClient apiServices;
    Call<TopAppListResponse> topAppListResponseCall;
    Call<VerticalMoreAppsResponse> verticalMoreAppsResponseCall;

    private AdView adView;
    private InterstitialAd interstitialAd;
    LinearLayout pixelAppLayout, bffAppLayout, tattooAppLayout, appslayout;
    private RecyclerView topAppsRecyclerview, verticalAppsRecyclerview;
    RelativeLayout appHubRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_hub);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.app_hub_actionbar);

        topAppsRecyclerview = (RecyclerView) findViewById(R.id.top_apps_recyclerview);
        verticalAppsRecyclerview = (RecyclerView) findViewById(R.id.apps_recyclerview);

        apiInterface = apiServices.getRetrofit().create(ApiInterface.class);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(AppHub.this, LinearLayoutManager.HORIZONTAL, false);
        topAppsRecyclerview.setLayoutManager(horizontalLayoutManagaer);
        topAppsRecyclerview.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(AppHub.this, LinearLayoutManager.VERTICAL, false);
        verticalAppsRecyclerview.setLayoutManager(verticalLayoutManagaer);
        verticalAppsRecyclerview.setItemAnimator(new DefaultItemAnimator());

        interstitialAd = new InterstitialAd(AppHub.this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest1 = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest1);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });


        // Load ads into Interstitial Ads
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        topApps();
        verticalMoreApps();
    }
    /*
    for full screeen google add
    */

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    /*
    * method for check internet connection
    * */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void topApps() {

        if (isNetworkAvailable()) {
            topAppListResponseCall = apiInterface.TOP_APP_LIST_RESPONSE_CALL();
            topAppListResponseCall.enqueue(new Callback<TopAppListResponse>() {

                @Override
                public void onResponse(Call<TopAppListResponse> call, Response<TopAppListResponse> response) {

                    if (!response.body().isError()) {

                        List<TopAppListResponse.CategoryEntity> data = response.body().getRow();
                        topAppsRecyclerAdapter = new TopAppsRecyclerAdapter(AppHub.this, data);
                        topAppsRecyclerview.setAdapter(topAppsRecyclerAdapter);
                    } else {

                        Toast.makeText(AppHub.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TopAppListResponse> call, Throwable t) {

                    Toast.makeText(AppHub.this, "Failure Connection..!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            snackBar();
        }
    }

    public void verticalMoreApps() {
        if (isNetworkAvailable()) {
            verticalMoreAppsResponseCall = apiInterface.VERTICAL_MORE_APPS_RESPONSE_CALL();
            verticalMoreAppsResponseCall.enqueue(new Callback<VerticalMoreAppsResponse>() {
                @Override
                public void onResponse(Call<VerticalMoreAppsResponse> call, Response<VerticalMoreAppsResponse> response) {
                    if (!response.body().isError()) {

                        List<VerticalMoreAppsResponse.CategoryEntity> data = response.body().getRow();
                        verticalMoreAppsRecyclerViewAdapter = new VerticalMoreAppsRecyclerViewAdapter(AppHub.this, data);
                        verticalAppsRecyclerview.setAdapter(verticalMoreAppsRecyclerViewAdapter);
                    } else {

                        Toast.makeText(AppHub.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<VerticalMoreAppsResponse> call, Throwable t) {

                }
            });
        } else {
            snackBar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_hub_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(AppHub.this, MainActivity.class);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void snackBar() {
        Snackbar snackbar = Snackbar
                .make(appHubRelativeLayout, "Please Turn Your Internet Connection On..!", 15000)
                .setDuration(15000)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
        Toast.makeText(this, "Please Turn Your Internet Connection On..!", Toast.LENGTH_SHORT).show();
    }


}
