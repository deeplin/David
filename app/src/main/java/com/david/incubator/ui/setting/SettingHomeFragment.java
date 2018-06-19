package com.david.incubator.ui.setting;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.ui.TabHomeFragment;
import com.david.databinding.FragmentSettingBinding;
import com.david.common.data.SelectedUser;

import javax.inject.Inject;


/**
 * author: Ling Lin
 * created on: 2018/1/3 17:49
 * email: 10525677@qq.com
 * description:
 */
public class SettingHomeFragment extends TabHomeFragment<FragmentSettingBinding> {

    @Inject
    public SelectedUser selectedUser;

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void attach() {
        binding.sllLeft.attach();
        binding.clChart.attach();

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

    @Override
    public void detach() {
        super.detach();
        selectedUser.showDetail.set(false);
        binding.clChart.detach();
        binding.sllLeft.detach();
    }
}