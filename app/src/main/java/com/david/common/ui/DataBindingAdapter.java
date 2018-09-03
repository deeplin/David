package com.david.common.ui;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.david.R;

/**
 * author: Ling Lin
 * created on: 2017/7/29 14:50
 * email: 10525677@qq.com
 * description:
 */

public class DataBindingAdapter {

    @BindingAdapter("android:isSelected")
    public static void isSelected(View view, boolean status) {
        view.setSelected(status);
    }

    @BindingAdapter("android:imageSrc")
    public static void setImageSrc(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("android:layout_margin")
    public static void setMargin(View view, float margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(Math.round(margin), Math.round(margin), Math.round(margin),
                Math.round(margin));
        view.setLayoutParams(layoutParams);
    }
}