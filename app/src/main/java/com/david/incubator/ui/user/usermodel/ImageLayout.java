package com.david.incubator.ui.user.usermodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.david.R;
import com.david.common.dao.UserModel;
import com.david.common.data.SelectedUser;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.camera.Camera2Config;
import com.david.common.util.Constant;
import com.david.common.util.TimeUtil;
import com.david.databinding.LayoutImageBinding;
import com.david.incubator.control.MainApplication;
import com.david.incubator.util.FragmentPage;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ImageLayout extends BindingConstraintLayout<LayoutImageBinding> {

    @Inject
    SelectedUser selectedUser;

    ObservableInt navigationView;

    private List<String> itemList;
    private List<String> pathList;

    public ImageLayout(Context context, ObservableInt navigationView) {
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
                    if (binding.ivFile.getVisibility() == View.VISIBLE) {
                        ((BitmapDrawable) binding.ivFile.getDrawable()).getBitmap().recycle();
                        binding.ivFile.setImageBitmap(null);
                        binding.ivFile.setVisibility(View.GONE);
                    } else {
                        navigationView.set(FragmentPage.USER_MODEL_DETAIL);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_image;
    }

    @Override
    public void attach() {
        String filePath = Camera2Config.buildFile(Camera2Config.IMAGE_DIRECTORY, "");

        File directory = new File(filePath);
        File[] files = directory.listFiles();

        UserModel userModel = selectedUser.userModel;
        if (userModel != null) {
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
            FileAdapter fileAdapter = new FileAdapter(getContext(), itemList, pathList, R.drawable.ic_image_black);
            binding.gvFiles.setAdapter(fileAdapter);
            binding.gvFiles.setOnItemClickListener((parent, view, position, id) -> {
                if (binding.ivFile.getVisibility() == View.GONE) {
                    String fileName = (String) view.getTag();
                    Bitmap bitmap = BitmapFactory.decodeFile(fileName);
                    binding.ivFile.setImageBitmap(bitmap);
                    binding.ivFile.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void detach() {
        itemList.clear();
        pathList.clear();
    }

    private void init() {
        binding.title.setTitle(R.string.image_data);
    }
}
