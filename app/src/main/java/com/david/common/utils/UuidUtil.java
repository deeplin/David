package com.david.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * author: Ling Lin
 * created on: 2017/10/16 12:34
 * email: 10525677@qq.com
 * description:
 */

public class UuidUtil {
    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";

    private static UUID uuid;

    public static String getUuid(Context context) {
        if (uuid == null) {
            synchronized (UuidUtil.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = UUID.fromString(id);

                    } else {
                        uuid = UUID.randomUUID();
                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply();
                    }
                }
            }
        }
        return uuid.toString().substring(0, 18);
    }
}
