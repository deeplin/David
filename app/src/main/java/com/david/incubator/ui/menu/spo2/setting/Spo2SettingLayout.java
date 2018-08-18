package com.david.incubator.ui.menu.spo2.setting;

import android.content.Context;
import android.util.AttributeSet;

import com.david.R;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeLayout;
import com.david.databinding.LayoutSpo2SettingBinding;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;


/**
 * author: Ling Lin
 * created on: 2018/1/2 22:31
 * email: 10525677@qq.com
 * description:
 */
public class Spo2SettingLayout extends TabHomeLayout<LayoutSpo2SettingBinding> {

    @Inject
    ShareMemory shareMemory;

    public Spo2SettingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_spo2_setting;
    }

    @Override
    public void attach() {
        Spo2SettingPagerAdapter pagerAdapter = new Spo2SettingPagerAdapter();
        binding.vpSpo2Setting.setAdapter(pagerAdapter);
        binding.tlSpo2Setting.setupWithViewPager(binding.vpSpo2Setting);

        binding.tlSpo2Setting.removeAllTabs();
        binding.tlSpo2Setting.addTab(binding.tlSpo2Setting.newTab().setText("SENS"));
        binding.tlSpo2Setting.addTab(binding.tlSpo2Setting.newTab().setText("AVG Time"));
        binding.tlSpo2Setting.addTab(binding.tlSpo2Setting.newTab().setText("FastSAT"));

        binding.vpSpo2Setting.addOnPageChangeListener(super.getPageChangeListener(binding.vpSpo2Setting));
        binding.tlSpo2Setting.getTabAt(0).select();
        shareMemory.functionTag.set(0);
    }

    @Override
    public void detach() {
        super.detach();
    }

    @Override
    protected void pageSelected() {
        shareMemory.functionTag.set(super.currentPosition);
    }
}
