package com.dvinfosys.smokeeffectphotomaker.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.InterstitialAd;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.dvinfosys.smokeeffectphotomaker.Adapter.ExitRecyclerViewAdapter;
import com.dvinfosys.smokeeffectphotomaker.R;
import com.dvinfosys.smokeeffectphotomaker.Response.CreateDialogApiResponse;
import com.dvinfosys.smokeeffectphotomaker.Response.ExitDialogOnlineResponse;
import com.dvinfosys.smokeeffectphotomaker.Utils.ApiInterface;
import com.dvinfosys.smokeeffectphotomaker.Utils.RetrofitClient;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Uri picUri;
    private Button startBtn;
    private AdView mAdView;
    InterstitialAd interstitialAd;

    RetrofitClient retrofitClient;
    Call<CreateDialogApiResponse> apiResponseCall;
    Call<ExitDialogOnlineResponse> exitDialogOnlineResponseCall;
    ApiInterface apiInterface;
    private ImageView appIconImg;
    private Button okBtn, cancelBtn;
    private TextView appnameTv, appDescriptionTv;
    private ProgressDialog pDialog;
    public String appLink;
    private Button exitBtn, cancle;
    private RecyclerView exitOnlineRecyclerView;
    private LinearLayout linerLayout1, linerLayout2, appLayout1, appLayout2,
            appLayout3, appLayout4, appLayout5, appLayout6;
    ExitRecyclerViewAdapter exitRecyclerViewAdapter;
    GifImageView appHubGifImg;
    RelativeLayout activityMainLayout;
    private static boolean mHasItRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialization of Retrofit Client
        apiInterface = retrofitClient.getRetrofit().create(ApiInterface.class);
        initialize();
    }

    /*
   Initialization of all view element from xml file.
    - start_btn
    */
    public void initialize() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        startBtn = (Button) findViewById(R.id.start_btn);

        appHubGifImg = (GifImageView) findViewById(R.id.gifimage);
        activityMainLayout = (RelativeLayout) findViewById(R.id.activity_main_layout);

        interstitialAd = new InterstitialAd(MainActivity.this);
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

        appHubGifImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    startActivity(new Intent(MainActivity.this, AppHub.class));
                } else {
                    snackBar();
                }
            }
        });

        if (!mHasItRun) {
            showDialog();
            mHasItRun = true;
        }
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    /**
     * button click for Start pick image activity with chooser.
     */
    public void onSelectImageClick(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    /*
    * method for choose image in gallery and set in next activity
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                Uri imageUri = result.getUri();

                Intent intent = new Intent(MainActivity.this, EditImage.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
//                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    * method for Snack Bar
    * */
    public void snackBar() {
        Snackbar snackbar = Snackbar
                .make(activityMainLayout, "Please Turn Your Internet Connection On..!", 15000)
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

    /*
    for full screeen google add
    */
    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.rateus) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/r6A2FD"));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/r6A2FD"));
                startActivity(intent);
            }
            return true;
        }

        if (id == R.id.privacypolicy) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.privay_dialog);

            ImageView imageView = (ImageView) dialog.findViewById(R.id.img_close);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }

        if (id == R.id.moreapps) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=dreamyinfotech&hl=en"));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=dreamyinfotech&hl=en"));
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    for display app dialog
    * */
    public void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.start_screen_dialog_layout);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        appIconImg = (ImageView) dialog.findViewById(R.id.app_icon_img);
        appnameTv = (TextView) dialog.findViewById(R.id.app_name_tv);
        appDescriptionTv = (TextView) dialog.findViewById(R.id.description_tv);
        cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
        okBtn = (Button) dialog.findViewById(R.id.ok_btn);

        /*pDialog = pDialog.show(MainActivity.this, null, "Please Wait", false, false);*/

        if (isNetworkAvailable()) {
            apiResponseCall = apiInterface.API_RESPONSE_CALL();
            apiResponseCall.enqueue(new Callback<CreateDialogApiResponse>() {

                @Override
                public void onResponse(Call<CreateDialogApiResponse> call, Response<CreateDialogApiResponse> response) {
                    Log.d("response code", String.valueOf(response.code()));
                    if (!response.body().isError()) {
                        Glide.with(MainActivity.this).load(response.body().getImage()).into(appIconImg);
                        appnameTv.setText(response.body().getName());
                        appDescriptionTv.setText(response.body().getDescription());
                        appLink = response.body().getLink();
                    } else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CreateDialogApiResponse> call, Throwable t) {
                     Toast.makeText(MainActivity.this, "Failure Connection...!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dialog.cancel();
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
                startActivity(intent);
            }
        });
    }

    public void backDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.back_btn_exit_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        exitBtn = (Button) dialog.findViewById(R.id.exit_button6);
        cancle = (Button) dialog.findViewById(R.id.cancle_button7);
        exitOnlineRecyclerView = (RecyclerView) dialog.findViewById(R.id.online_recycler1);
        linerLayout1 = (LinearLayout) dialog.findViewById(R.id.linear1);
        linerLayout2 = (LinearLayout) dialog.findViewById(R.id.linear2);
        appLayout1 = (LinearLayout) dialog.findViewById(R.id.app1);
        appLayout2 = (LinearLayout) dialog.findViewById(R.id.app2);
        appLayout3 = (LinearLayout) dialog.findViewById(R.id.app3);
        appLayout4 = (LinearLayout) dialog.findViewById(R.id.app4);
        appLayout5 = (LinearLayout) dialog.findViewById(R.id.app5);
        appLayout6 = (LinearLayout) dialog.findViewById(R.id.app6);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, GridLayoutManager.VERTICAL);
        int numberOfColumns = 3;
        exitOnlineRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        // recyclerView.setLayoutManager(horizontalLayoutManagaer);
        exitOnlineRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isNetworkAvailable()) {

            exitDialogOnlineResponseCall = apiInterface.EXIT_DIALOG_ONLINE_RESPONSE_CALL();
            exitDialogOnlineResponseCall.enqueue(new Callback<ExitDialogOnlineResponse>() {
                @Override
                public void onResponse(Call<ExitDialogOnlineResponse> call, Response<ExitDialogOnlineResponse> response) {

                    if (!response.body().isError()) {

                        List<ExitDialogOnlineResponse.CategoryEntity> data = response.body().getRow();
                        exitRecyclerViewAdapter = new ExitRecyclerViewAdapter(MainActivity.this, data);
                        exitOnlineRecyclerView.setAdapter(exitRecyclerViewAdapter);
                    } else {

                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ExitDialogOnlineResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failure Connection..!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
            animation.setDuration(500);
            appLayout1.startAnimation(animation);
            appLayout2.startAnimation(animation);
            appLayout3.startAnimation(animation);
            appLayout4.startAnimation(animation);
            appLayout5.startAnimation(animation);
            appLayout6.startAnimation(animation);

            linerLayout1.setVisibility(View.VISIBLE);
            linerLayout2.setVisibility(View.VISIBLE);
            exitOnlineRecyclerView.setVisibility(View.INVISIBLE);
            appLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.bfffriendshiptestapp&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            appLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.blendmephotoeditor&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            appLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.application.vakratunda.fingerbloodpressurecheck&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            appLayout4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.vastushastratpisforhome&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            appLayout5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.cookingtipssweetandspicy&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            appLayout6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.cookingtipssweetandspicy&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {}
                }
            });

        }
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                AppHub topAppsActivity = new AppHub();
                topAppsActivity.finish();
                System.exit(0);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    /*
    * method for open dialog for exitBtn application
    * */
    @Override
    public void onBackPressed() {
        backDialog();
    }
}
