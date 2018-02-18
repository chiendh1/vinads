package com.vinsofts.adlibraries;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by macOS on 2/12/18.
 */

public class VinAdView extends RelativeLayout {
    public VinAdView(@NonNull Context context) {
        super(context);
    }

    public VinAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VinAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_ad, this, true);
    }
}
