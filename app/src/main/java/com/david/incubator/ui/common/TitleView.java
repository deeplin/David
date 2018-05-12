package com.david.incubator.ui.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutTitleBinding;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class TitleView extends FrameLayout {

    LayoutTitleBinding layoutTitleBinding;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutTitleBinding = LayoutTitleBinding.inflate(layoutInflater, this, true);
    }

    public void setTitle(int title) {
        layoutTitleBinding.tvTitle.setText(ResourceUtil.getString(title));
    }
}
