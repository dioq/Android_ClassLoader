package cn.my.loader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;

public class PermissionUtils {
    private static PermissionUtils instance;

    //构造器私有化
    private PermissionUtils() {
    }

    //方法同步，调用效率低
    public static synchronized PermissionUtils getInstance() {
        if (instance == null) {
            instance = new PermissionUtils();
        }
        return instance;
    }

    public void requestPermissions(Context context) {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }
}
