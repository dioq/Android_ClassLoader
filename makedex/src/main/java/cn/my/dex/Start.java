package cn.my.dex;

import android.content.Context;
import android.util.Log;

public class Start {
    private static final String TAG = "dlog";
    static {
        Log.d(TAG, "auto running");
    }

    public static void start(Context context) {
        String packageName = context.getPackageName();
        Log.d(TAG, "packageName:" + packageName);
    }
}
