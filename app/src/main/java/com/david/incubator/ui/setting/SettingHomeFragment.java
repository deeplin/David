package com.david.incubator.ui.setting;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.UserModelData;
import com.david.common.ui.TabHomeFragment;
import com.david.databinding.FragmentSettingBinding;

import javax.inject.Inject;


/**
 * author: Ling Lin
 * created on: 2018/1/3 17:49
 * email: 10525677@qq.com
 * description:
 */
public class SettingHomeFragment extends TabHomeFragment<FragmentSettingBinding> {

    @Inject
    UserModelData userModelData;

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        // Inflate the layout for this fragment
        binding.setViewModel(userModelData);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void attach() {
        SettingPagerAdapter pagerAdapter = new SettingPagerAdapter();
        binding.vpSetting.setAdapter(pagerAdapter);
        binding.tlSetting.setupWithViewPager(binding.vpSetting);

        binding.tlSetting.removeAllTabs();
        binding.tlSetting.addTab(buildIcon(binding.tlSetting, R.mipmap.user_small));
        binding.tlSetting.addTab(buildIcon(binding.tlSetting, R.mipmap.comfort_zone));
        binding.tlSetting.addTab(buildIcon(binding.tlSetting, R.mipmap.cal));
        binding.tlSetting.addTab(buildIcon(binding.tlSetting, R.mipmap.introduction));
        binding.tlSetting.addTab(buildIcon(binding.tlSetting, R.mipmap.device));
        binding.tlSetting.addTab(buildIcon(binding.tlSetting, R.mipmap.login));
        binding.vpSetting.addOnPageChangeListener(super.getPageChangeListener(binding.vpSetting));
        binding.tlSetting.getTabAt(0).select();
    }
}
