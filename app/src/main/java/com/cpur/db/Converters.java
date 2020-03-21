package com.cpur.db;

import androidx.room.TypeConverter;
import android.text.TextUtils;

import com.cpur.data.Story;

import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Story.Status statusFromString(String value) {
        return value == null ? null : Story.Status.valueOf(value);
    }

    @TypeConverter
    public static String statusToString(Story.Status status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static List<String> listFromString(String value) {
        return value == null ? null : Arrays.asList(value.split(","));
    }

    @TypeConverter
    public static String listStringToString(List<String> strings) {
        return strings == null ? null : TextUtils.join(",", strings);
    }
}

