package com.morelap.tagskill.utils;

public class LogUtil {
    public static boolean Debug = true;

    public static void v(String tag, String msg) {
        if (Debug)
            android.util.Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable t) {
        if (Debug)
            android.util.Log.v(tag, msg, t);
    }

    public static void d(String tag, String msg) {
        if (Debug)
            android.util.Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (Debug)
            android.util.Log.d(tag, msg, t);
    }

    public static void i(String tag, String msg) {
        if (Debug)
            android.util.Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        if (Debug)
            android.util.Log.i(tag, msg, t);
    }

    public static void w(String tag, String msg) {
        if (Debug)
            android.util.Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        if (Debug)
            android.util.Log.w(tag, msg, t);
    }

    public static void e(String tag, String msg) {
        if (Debug)
            android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (Debug)
            android.util.Log.e(tag, msg, t);
    }
}
