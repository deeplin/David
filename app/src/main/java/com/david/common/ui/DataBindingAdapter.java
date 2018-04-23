package com.david.common.ui;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

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
        if (resource > 0) {
            imageView.setImageResource(resource);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
