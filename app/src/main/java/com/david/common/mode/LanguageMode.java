package com.david.common.mode;

import java.util.Objects;

/**
 * author: Ling Lin
 * created on: 2017/7/20 11:23
 * email: 10525677@qq.com
 * description:
 */

public enum LanguageMode {

    English("ENGLISH", (byte) 1), Chinese("CHINESE", (byte) 2);

    private final String name;
    private final byte index;

    LanguageMode(String name, byte index) {
        this.name = name;
        this.index = index;
    }

    public static LanguageMode getMode(String languageString){
        String languageUpper = languageString.toUpperCase();
        for (LanguageMode mode : values()) {
            if (Objects.equals(mode.getName(), languageUpper)) {
                return mode;
            }
        }
        return LanguageMode.Chinese;
    }

    public String getName() {
        return name;
    }

    public byte getIndex() {
        return index;
    }
}