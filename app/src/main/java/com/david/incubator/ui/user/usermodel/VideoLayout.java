package com.david.incubator.ui.user.usermodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.view.View;

import com.david.R;
import com.david.common.dao.UserModel;
import com.david.common.data.SelectedUser;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.camera.Camera2Config;
import com.david.common.util.Constant;
import com.david.databinding.LayoutVideoBinding;
import com.david.incubator.control.MainApplication;
import com.david.incubator.util.FragmentPage;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class VideoLayout extends BindingConstraintLayout<LayoutVideoBinding> {

    @Inject
    SelectedUser selectedUser;

    ObservableInt navigationView;

    private List<String> itemList;
    private List<String> pathList;

    public VideoLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        MainApplication.getInstance().getApplicationComponent().inject(this);

        init();
        binding.setViewModel(this);

        itemList = new ArrayList<>();
        pathList = new ArrayList<>();

        RxView.clicks(binding.btReturn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (binding.vvFile.getVisibility() == View.VISIBLE) {
                        binding.vvFile.stopPlayback();
                        binding.vvFile.setVisibility(View.GONE);
                    } else {
                        navigationView.set(FragmentPage.USER_MODEL_DETAIL);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_video;
    }

    @Override
    public void attach() {
        String filePath = Camera2Config.buildFile(Camera2Config.VIDEO_DIRECTORY, "");

        File directory = new File(filePath);
        File[] files = directory.listFiles();

        UserModel userModel = selectedUser.userModel;
        if (userModel != null) {
            for (int index = files.length - 1; index >= 0; index--) {
                File file = files[index];
                if (itemList.size() < 10) {
                    String fileName = file.getName();
                    String createTimeString = fileName.substring(0, fileName.indexOf('.'));

                    try {
                        long createTime = Long.parseLong(createTimeString);
                        if (createTime > userModel.getStartTimeStamp()) {
                            if (userModel.getEndTimeStamp() == 0 || createTime < userModel.getEndTimeStamp()) {
                                itemList.add(file.getName());
                                pathList.add(file.getPath());
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
            FileAdapter fileAdapter = new FileAdapter(getContext(), itemList, pathList, R.drawable.ic_ondemand_video_black);
            binding.gvFiles.setAdapter(fileAdapter);
            binding.gvFiles.setOnItemClickListener((parent, view, position, id) -> {
                String fileName = (String) view.getTag();

                Uri uri = Uri.parse(fileName);
                binding.vvFile.setVideoURI(uri);
                binding.vvFile.start();
                binding.vvFile.setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void detach() {
    }

    private void init() {
        binding.title.setTitle(R.string.video_data);
    }
}
