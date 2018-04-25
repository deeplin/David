package com.david.common.util;

import android.view.animation.AlphaAnimation;

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
}
