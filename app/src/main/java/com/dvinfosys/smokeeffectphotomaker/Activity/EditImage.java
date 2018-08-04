package com.dvinfosys.smokeeffectphotomaker.Activity;

     /*
        Person : DV Bhuva
        Email : dvinfosys0@gmail.com
        File Name : EditImage.java
        Description :
          - initialize()  : Initialization of all elements in this screen
          - motionViewCallback class : class for motionViewCallback for text editing visibility
          - startTextEntityEditing() : method for display select font type dialog
          - hideControls() : method for hide controls of sticker
          - changeTextEntityColor() : method for change text color
          - createEffectList() : add sticker in arraylist
          - bottomTab() : method for three bottom tab
          - checkPermission() : check permission of version status
          - shareFile() : method for save file to gallery
          - RecyclerItemClickListner class : class for sticker item  click listener
          - addTextSticker() : method for add sticker in  motionview
          - changeTextEntityFont() : class for change font
        */

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.dvinfosys.smokeeffectphotomaker.Adapter.StickerRecyclerViewAdapter;
import com.dvinfosys.smokeeffectphotomaker.BuildConfig;
import com.dvinfosys.smokeeffectphotomaker.Extras.widget.entity.MotionEntity;
import com.dvinfosys.smokeeffectphotomaker.Extras.widget.entity.TextEntity;
import com.dvinfosys.smokeeffectphotomaker.Fragment.TextEditorDialogFragment;
import com.dvinfosys.smokeeffectphotomaker.Adapter.FontsAdapter;

import com.dvinfosys.smokeeffectphotomaker.Extras.utils.FontProvider;
import com.dvinfosys.smokeeffectphotomaker.Extras.viewmodel.Font;
import com.dvinfosys.smokeeffectphotomaker.Extras.viewmodel.TextLayer;
import com.dvinfosys.smokeeffectphotomaker.Extras.widget.entity.MotionView;

import com.dvinfosys.smokeeffectphotomaker.R;
import com.dvinfosys.smokeeffectphotomaker.View.StickerImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditImage extends AppCompatActivity implements TextEditorDialogFragment.OnTextLayerCallback {

    FrameLayout stickerFrameLayout;
    ImageView picEditImageView;
    TextView smokeTv, textTv, shareTv;
    RecyclerView stickerRecyclerView;
    Bitmap bitMapImg;
    ArrayList<View> viewArrayList;
    ArrayList<Integer> effectImageList;
    GestureDetector gestureDetector;
    RecyclerItemClickListener.OnItemClickListener onItemClickListener;
    StickerRecyclerViewAdapter stickerRecyclerViewAdapter;
    AdView adView;
    StickerImageView ivSticker;
    ArrayList<StickerImageView> ivStickerArraylist;
    private Bitmap mBitmap, bitmap1, bitmap2;
    private static MotionView motionView;
    private FontProvider fontProvider;
    int flag = 0, flag1 = 0, flag2 = 0;
    RelativeLayout relativeLayout;
    private ImageButton font, color;
    private LinearLayout mainMotionTextEntityEditPanel;
    protected View textEntityEditPanel;
    private int Request = 99;

    /*
    * class for motionViewCallback for text editing visibility
    * */
    private final MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
            if (entity instanceof TextEntity) {
                textEntityEditPanel.setVisibility(View.VISIBLE);
            } else {
                textEntityEditPanel.setVisibility(View.GONE);
            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            startTextEntityEditing();
        }
    };

    /*
    * method for display select font type dialog
    * */
    private void startTextEntityEditing() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
        }
    }

    @Nullable
    private TextEntity currentTextEntity() {
        if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) motionView.getSelectedEntity());
        } else {
            return null;
        }
    }

    /*
    * method for hide controls of sticker
    * */

    public void hideControls() {
        int iv_sicker_arraylist_size = ivStickerArraylist.size();
        if (!ivStickerArraylist.isEmpty()) {
            for (int i = 0; i <= iv_sicker_arraylist_size - 1; i++) {
                ivSticker = ivStickerArraylist.get(i);
                ivSticker.setControlItemsHidden(true);
                ivSticker.setControlsVisibility(false);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        initialize();
    }

    /*
    Initialization of all view element from xml file.
     - sticker_recycler_view, pic_edit_imageview, main_motion_text_entity_edit_panel, smoke_tv,
       text_tv, share_tv,rl_content_root, sticker_framelayout, main_motion_view
     - initialize of StickerRecyclerViewAdapter, mainMotionTextEntityEditPanel, viewArrayList, effectImageList, ivStickerArraylist
     - addOnItemTouchListener : item click listner of recyclerview for add sticker on that position
    */
    public void initialize() {
        stickerRecyclerView = (RecyclerView) findViewById(R.id.sticker_recycler_view);
        picEditImageView = (ImageView) findViewById(R.id.pic_edit_imageview);
        textEntityEditPanel = findViewById(R.id.main_motion_text_entity_edit_panel);
        smokeTv = (TextView) findViewById(R.id.smoke_tv);
        textTv = (TextView) findViewById(R.id.text_tv);
        shareTv = (TextView) findViewById(R.id.share_tv);
        bottomTab();
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_content_root);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Smoke Effect Photo Maker</small>"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        stickerFrameLayout = (FrameLayout) findViewById(R.id.sticker_framelayout);
        mainMotionTextEntityEditPanel = (LinearLayout) findViewById(R.id.main_motion_text_entity_edit_panel);
        adView = (AdView) findViewById(R.id.ad_view_editimg);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        motionView = (MotionView) findViewById(R.id.main_motion_view);
        motionView.setMotionViewCallback(motionViewCallback);
        Bundle extras = getIntent().getExtras();
        Uri myUri = Uri.parse(extras.getString("imageUri"));
        picEditImageView.setImageURI(myUri);
        color = (ImageButton) findViewById(R.id.text_entity_color_change);
        font = (ImageButton) findViewById(R.id.text_entity_font_change);
        this.fontProvider = new FontProvider(getResources());
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextEntityColor();
            }
        });
        font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextEntityFont();
            }
        });

        viewArrayList = new ArrayList<>();
        effectImageList = new ArrayList<>();
        ivStickerArraylist = new ArrayList<>();

        createEffectList();
        stickerRecyclerViewAdapter = new StickerRecyclerViewAdapter(EditImage.this, effectImageList);
        stickerRecyclerView.setAdapter(stickerRecyclerViewAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(EditImage.this, LinearLayoutManager.HORIZONTAL, false);
        stickerRecyclerView.setLayoutManager(horizontalLayoutManagaer);

        stickerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(EditImage.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_01);
                }
                if (position == 1) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_02);
                }
                if (position == 2) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_03);
                }
                if (position == 3) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_04);
                }
                if (position == 4) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_05);
                }

                if (position == 5) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_06);
                }
                if (position == 6) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_07);
                }

                if (position == 7) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_08);
                }
                if (position == 8) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_09);
                }
                if (position == 9) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_10);
                }
                if (position == 10) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_11);
                }
                if (position == 11) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_12);
                }
                if (position == 12) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_13);
                }
                if (position == 13) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_14);
                }
                if (position == 14) {
                    ivSticker = new StickerImageView(EditImage.this);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_15);
                    ivStickerArraylist.add(ivSticker);
                    stickerFrameLayout.addView(ivSticker);
                }
                if (position == 15) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_16);
                }
                if (position == 16) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_17);
                }
                if (position == 17) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_18);
                }

                if (position == 18) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_19);
                }
                if (position == 19) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_20);
                }
                if (position == 20) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_21);
                }
                if (position == 21) {
                    ivSticker = new StickerImageView(EditImage.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.mipmap.ic_sticker_22);
                }
            }
        }));
    }

    /*
    method for change text color
    */

    private void changeTextEntityColor() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity == null) {
            return;
        }

        int initialColor = textEntity.getLayer().getFont().getColor();

        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose Color")
                .initialColor(initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(8) // magic number
                .setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setColor(selectedColor);
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    /*
    * add sticker in arraylist
    * */
    private void createEffectList() {
        effectImageList = new ArrayList<Integer>();
        effectImageList.add(R.mipmap.ic_sticker_01);
        effectImageList.add(R.mipmap.ic_sticker_02);
        effectImageList.add(R.mipmap.ic_sticker_03);
        effectImageList.add(R.mipmap.ic_sticker_04);
        effectImageList.add(R.mipmap.ic_sticker_05);
        effectImageList.add(R.mipmap.ic_sticker_06);
        effectImageList.add(R.mipmap.ic_sticker_07);
        effectImageList.add(R.mipmap.ic_sticker_08);
        effectImageList.add(R.mipmap.ic_sticker_09);
        effectImageList.add(R.mipmap.ic_sticker_10);
        effectImageList.add(R.mipmap.ic_sticker_11);
        effectImageList.add(R.mipmap.ic_sticker_12);
        effectImageList.add(R.mipmap.ic_sticker_13);
        effectImageList.add(R.mipmap.ic_sticker_14);
        effectImageList.add(R.mipmap.ic_sticker_15);
        effectImageList.add(R.mipmap.ic_sticker_16);
        effectImageList.add(R.mipmap.ic_sticker_17);
        effectImageList.add(R.mipmap.ic_sticker_18);
        effectImageList.add(R.mipmap.ic_sticker_19);
        effectImageList.add(R.mipmap.ic_sticker_20);
        effectImageList.add(R.mipmap.ic_sticker_21);
        effectImageList.add(R.mipmap.ic_sticker_22);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            mainMotionTextEntityEditPanel.setVisibility(View.INVISIBLE);
            try {
                ivSticker.setControlsVisibility(false);
                ivSticker.setControlsVisibility(false);
            } catch (NullPointerException e) {
            }
            hideControls();
            File mainFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "SmokeEffectPhotoMaker");
            if (!mainFolder.exists() && !mainFolder.isDirectory()) {
                mainFolder.mkdirs();
                mainFolder.setExecutable(true);
                mainFolder.setReadable(true);
                mainFolder.setWritable(true);
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{mainFolder.toString()}, null, null);
            }
            SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyyhhmmss");
            stickerFrameLayout.invalidate();
            stickerFrameLayout.buildDrawingCache();
            bitmap1 = stickerFrameLayout.getDrawingCache();
            bitmap2 = Bitmap.createScaledBitmap(bitmap1, stickerFrameLayout.getWidth(), stickerFrameLayout.getHeight(), false);
            File file = new File(Environment.getExternalStorageDirectory() + "/SmokeEffectPhotoMaker", "SmokeEffectPhotoMaker_" + sf.format(Calendar.getInstance().getTime()) + ".jpg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, "SmokeEffectPhotoMaker" + sf.format(Calendar.getInstance().getTime()));
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "SmokeEffectPhotoMaker" + sf.format(Calendar.getInstance().getTime()));
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            contentValues.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
            contentValues.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
            contentValues.put("_data", file.getAbsolutePath());
            ContentResolver contentResolver = getApplicationContext().getContentResolver();
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Toast.makeText(getApplicationContext(), "Image save to gellary", Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    /*
    * method for boottom tab
    * */

    public void bottomTab() {
        smokeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    flag = 1;
                    registerForContextMenu(relativeLayout);
                    smokeTv.setBackgroundColor(Color.BLACK);
                    shareTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    textTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    stickerRecyclerView.setVisibility(View.VISIBLE);
                    mainMotionTextEntityEditPanel.setVisibility(View.INVISIBLE);
                } else if (flag == 1) {
                    flag = 0;
                    flag1 = 0;
                    flag2 = 0;
                    stickerRecyclerView.setVisibility(View.GONE);

                }
            }
        });
        textTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTv.setBackgroundColor(Color.BLACK);
                smokeTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                shareTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                stickerRecyclerView.setVisibility(View.INVISIBLE);
                mainMotionTextEntityEditPanel.setVisibility(View.VISIBLE);
                registerForContextMenu(relativeLayout);
                addTextSticker();
                Toast.makeText(EditImage.this, "Double Click to Enter Text", Toast.LENGTH_SHORT).show();
            }
        });
        shareTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    /*
    * check permission of version status
    * */
    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(EditImage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditImage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request);

        } else {
            shareFile();
        }
    }

    /*
    * method for save file to gallery
    * */
    private void shareFile() {
        hideControls();
        shareTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        smokeTv.setBackgroundColor(getResources().getColor(R.color.black));
        textTv.setBackgroundColor(getResources().getColor(R.color.black));
        stickerRecyclerView.setVisibility(View.INVISIBLE);
        mainMotionTextEntityEditPanel.setVisibility(View.INVISIBLE);
        String applink = "Make Photo with \n Smoke Booth Photo Maker app by #dreamyinfotech \n https://goo.gl/r6A2FD";
        stickerFrameLayout.setDrawingCacheEnabled(true);
        mBitmap = Bitmap.createBitmap(stickerFrameLayout.getDrawingCache());
        stickerFrameLayout.setDrawingCacheEnabled(false);
        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent waIntent = new Intent(Intent.ACTION_SEND);
        waIntent.setType("image/*");
        waIntent.putExtra(android.content.Intent.EXTRA_STREAM, bitmapUri);
        waIntent.putExtra(android.content.Intent.EXTRA_TEXT, applink);
        startActivity(Intent.createChooser(waIntent, "Share with"));
    }

    @Override
    public void textChanged(@NonNull String text) {

        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextLayer textLayer = textEntity.getLayer();
            if (!text.equals(textLayer.getText())) {
                textLayer.setText(text);
                textEntity.updateEntity();
                this.motionView.invalidate();
            }
        }
    }

    /*
    * class for sticker item  click listener
    * */
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private final OnItemClickListener mListener;
        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }
    }

    /*
    * method for add sticker in  motionview
    * */

    protected void addTextSticker() {
        TextLayer textLayer = createTextLayer();
        TextEntity textEntity = new TextEntity(textLayer, motionView.getWidth(),
                motionView.getHeight(), fontProvider);
        motionView.addEntityAndPosition(textEntity);

        // move text sticker up so that its not hidden under keyboard
        PointF center = textEntity.absoluteCenter();
        center.y = center.y * 0.5F;
        textEntity.moveCenterTo(center);

        // redraw
        motionView.invalidate();

        startTextEntityEditing();
    }

    /*
    *  method for add text in motionview
    * */
    private TextLayer createTextLayer() {
        TextLayer textLayer = new TextLayer();
        Font font = new Font();

        font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);

        if (BuildConfig.DEBUG) {
            textLayer.setText("Hello, world :))");
        }

        return textLayer;
    }

    /*
    * class for change font
    * */
    private void changeTextEntityFont() {
        final List<String> fonts = fontProvider.getFontNames();
        FontsAdapter fontsAdapter = new FontsAdapter(EditImage.this, fonts, fontProvider);
        new AlertDialog.Builder(EditImage.this)
                .setTitle("Select Font")
                .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setTypeface(fonts.get(which));
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .show();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
