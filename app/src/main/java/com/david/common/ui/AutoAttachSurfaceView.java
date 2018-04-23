package com.david.common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

/**
 * author: Ling Lin
 * created on: 2017/8/3 11:17
 * email: 10525677@qq.com
 * description:
 */

public abstract class AutoAttachSurfaceView extends SurfaceView implements IViewModel {

    public AutoAttachSurfaceView(Context context) {
        super(context);
    }

    public AutoAttachSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        switch (visibility) {
            case View.VISIBLE:
                attach();
                break;
            default:
                detach();
                break;
        }
    }
}