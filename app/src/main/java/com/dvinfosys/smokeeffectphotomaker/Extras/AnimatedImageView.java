package com.dvinfosys.smokeeffectphotomaker.Extras;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dvinfosys.smokeeffectphotomaker.R;

/**
 * Created by vaksys-1 on 31/7/17.
 */

public class AnimatedImageView extends ImageView{

    public AnimatedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimatedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedImageView(Context context) {
        super(context);
    }

    public void initializeAnimation(){
        setImageDrawable(null);
        setBackgroundAnimation();
        AnimationDrawable da = (AnimationDrawable) getBackground();
        da.start();
    }

    public void setBackgroundAnimation() {
        setBackgroundResource(R.drawable.app_hub_img); // this is an animation-list
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Handler handler = new Handler();
        final AnimatedImageView me = this;
        handler.post(new Runnable(){
            public void run() {
                me.initializeAnimation();
            }
        });
    }
}
