package com.david.incubator.ui.setting;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.ui.BindingConstraintLayout;
import com.david.databinding.LayoutSettingIntroductionBinding;
import com.github.barteksc.pdfviewer.PDFView;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:03
 * email: 10525677@qq.com
 * description:
 */
public class SettingIntroductionLayout extends BindingConstraintLayout<LayoutSettingIntroductionBinding> {

    @Inject
    ModuleHardware moduleHardware;

    public SettingIntroductionLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setting_introduction;
    }

    @Override
    public void attach() {
        try {
            String deviceModel = moduleHardware.getDeviceModel();
            PDFView.Configurator configurator = binding.introductionPdfView.fromAsset(deviceModel + ".pdf")
                    .defaultPage(1).enableSwipe(false).enableDoubletap(false).enableAnnotationRendering(false);
            configurator.onRender((pages, pageWidth, pageHeight) -> {
                binding.introductionPdfView.fitToWidth(); // optionally pass page number
            });
            configurator.load();
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    @Override
    public void detach() {
    }
}
