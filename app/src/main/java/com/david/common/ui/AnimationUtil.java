package com.david.common.ui;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * author: Ling Lin
 * created on: 2018/1/25 12:21
 * email: 10525677@qq.com
 * description:
 */

public class AnimationUtil {

    public static AlphaAnimation getAlphaAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0.9f, 0.2f);
        animation.setDuration(2000);
        animation.setFillAfter(false);
        animation.setRepeatMode(AlphaAnimation.REVERSE);
        animation.setRepeatCount(AlphaAnimation.INFINITE);
        return animation;
    }

    public static RotateAnimation getRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);//设置动画持续周期
        rotateAnimation.setRepeatCount(Animation.INFINITE);//设置重复次数
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        return rotateAnimation;
    }

    public static ColorFilter getGrayFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(50);//饱和度 0灰色 100过度彩色，50正常
        return new ColorMatrixColorFilter(matrix);
    }
}
