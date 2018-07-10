package com.cpur.db;

import android.arch.persistence.room.TypeConverter;

import com.cpur.models.Story;

public class Converters {
    @TypeConverter
    public static Story.Status fromInt(String value) {
        return value == null ? null : Story.Status.valueOf(value);
    }

    @TypeConverter
    public static String statusToString(Story.Status status) {
        return status == null ? null : status.name();
    }
}

