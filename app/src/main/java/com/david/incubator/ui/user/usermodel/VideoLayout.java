package com.david.incubator.ui.user.usermodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.view.View;

import com.david.R;
import com.david.common.control.AutomationControl;
import com.david.common.dao.UserModel;
import com.david.common.data.SelectedUser;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.camera.Camera2Config;
import com.david.common.util.Constant;
import com.david.common.util.TimeUtil;
import com.david.databinding.LayoutVideoBinding;
import com.david.incubator.control.MainApplication;
import com.david.incubator.util.FragmentPage;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class VideoLayout extends BindingConstraintLayout<LayoutVideoBinding> implements Consumer<Long> {

    @Inject
    SelectedUser selectedUser;
    @Inject
    AutomationControl automationControl;
    @Inject
    ShareMemory shareMemory;

    public ObservableBoolean recordIcon = new ObservableBoolean();
    public ObservableField<String> recordString = new ObservableField<>();

    ObservableInt navigationView;

    private List<String> itemList;
    private List<String> pathList;

    private int startTime;

    public VideoLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        MainApplication.getInstance().getApplicationComponent().inject(this);
        shareMemory.layoutLockable.set(false);

        init();
        binding.setViewModel(this);

        itemList = new ArrayList<>();
        pathList = new ArrayList<>();

        RxView.clicks(binding.btReturn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (binding.vvFile.getVisibility() == View.VISIBLE) {
                        recordIcon.set(false);
                        automationControl.removeConsumer(VideoLayout.this);
                        binding.vvFile.stopPlayback();
                        binding.vvFile.setVisibility(View.GONE);
                    } else {
                        navigationView.set(FragmentPage.USER_MODEL_DETAIL);
                        shareMemory.layoutLockable.set(true);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_video;
    }

    @Override
    public void attach() {
        recordIcon.set(false);

        String filePath = Camera2Config.buildFile(Camera2Config.VIDEO_DIRECTORY, "");

        File directory = new File(filePath);
        File[] files = directory.listFiles();

        UserModel userModel = selectedUser.userModel;
        if (userModel != null && files != null) {
            for (int index = files.length - 1; index >= 0; index--) {
                File file = files[index];
                if (itemList.size() <= 15) {
                    long createTime = file.lastModified();
                    if (TimeUtil.getTimeInSecond(createTime) > userModel.getStartTimeStamp()) {
                        if (userModel.getEndTimeStamp() == 0 || createTime < userModel.getEndTimeStamp()) {
                            String timeString = TimeUtil.getTime(createTime, TimeUtil.FullTime);
                            itemList.add(timeString);
                            pathList.add(file.getPath());
                        }
                    }
                }
            }
            FileAdapter fileAdapter = new FileAdapter(getContext(), itemList, pathList, R.drawable.ic_ondemand_video_black);
            binding.gvFiles.setAdapter(fileAdapter);
            binding.gvFiles.setOnItemClickListener((parent, view, position, id) -> {
                if (!recordIcon.get()) {
                    recordIcon.set(true);

                    String fileName = (String) view.getTag();
                    Uri uri = Uri.parse(fileName);
                    binding.vvFile.setVideoURI(uri);
                    binding.vvFile.start();
                    binding.vvFile.setVisibility(View.VISIBLE);
                    recordString.set("00:00:00");
                    startTime = 0;

                    automationControl.addConsumer(VideoLayout.this);
                    binding.vvFile.setOnCompletionListener(mp -> {
                        automationControl.removeConsumer(VideoLayout.this);
                    });
                }
            });
        }
    }

    @Override
    public void detach() {
    }

    private void init() {
        binding.title.setTitle(R.string.video_data);
    }

    @Override
    public void accept(Long aLong) {
        recordString.set(String.format(Locale.US, "%02d:%02d:%02d",
                startTime / 3600 % 24, startTime / 60 % 60, startTime % 60));
        startTime++;
    }
}