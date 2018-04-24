package com.david.common.mode;

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

    public static LanguageMode getMode(String languageString) throws Exception {
        String languageUpper = languageString.toUpperCase();
        for (LanguageMode mode : values()) {
            if (mode.getName().equals(languageUpper)) {
                return mode;
            }
        }
        throw new Exception("Illegal language: " + languageString);
    }

    public String getName() {
        return name;
    }

    public byte getIndex() {
        return index;
    }
}