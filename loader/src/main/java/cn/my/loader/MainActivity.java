package cn.my.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import dalvik.system.DexClassLoader;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.InMemoryDexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "dlog";
    private TextView tv;

    private Context context = null;
    private final String dir = "/data/local/tmp";


    private File dexFile = null;
    private File optimizedDirectory = null;
    private String librarySearchPath = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textViewId);

        context = MainActivity.this;
        PermissionUtils.getInstance().requestPermissions(context);

        dexFile = new File(dir, "classes.dex");
        optimizedDirectory = context.getDir("dex", 0);
        librarySearchPath = context.getCacheDir().getPath();
        Log.d(TAG, "optimizedDirectory:" + optimizedDirectory.getPath());
    }

    @SuppressLint("SetTextI18n")
    public void load1(View view) {
        /*
            参数:
            1.待加载的dex文件路径,如果是外存路径,一定要加上读外存文件的权限
            2.odex(优化的dex)存放的路径,此位置一定要是可读写且仅该应用可读写
            3.指向包含本地库(so)的文件夹路径,可以设为null
            4.父级类加载器,一般可以通过context.getClassLoader()获取到,也可以通过ClassLoader.getSystemClassLoader()取到
        **/
//        ClassLoader classLoader = new BaseDexClassLoader(dexFile.getPath(), optimizedDirectory, librarySearchPath, ClassLoader.getSystemClassLoader());
        ClassLoader classLoader = new DexClassLoader(dexFile.getPath(), optimizedDirectory.getPath(), librarySearchPath, context.getClassLoader());
        invokeCustomMethod(classLoader);
        tv.setText("从dex文件中加载类");
    }

    @SuppressLint("SetTextI18n")
    public void load2(View view) {
        byte[] bytes = FileUtils.getInstance().readFile(dexFile);

        ByteBuffer byteBuffers = ByteBuffer.allocate(bytes.length);
        byteBuffers.put(bytes);
        byteBuffers.position(0);

        ClassLoader classLoader = new InMemoryDexClassLoader(byteBuffers, ClassLoader.getSystemClassLoader());
        invokeCustomMethod(classLoader);
        tv.setText("从内存中加载类");
    }

    @SuppressLint("SetTextI18n")
    public void load3(View view) {
        File apkFile = new File(dir, "makedex-debug.apk");

        ClassLoader classLoader = new BaseDexClassLoader(apkFile.getPath(), optimizedDirectory, librarySearchPath, ClassLoader.getSystemClassLoader());
//        ClassLoader classLoader = new DexClassLoader(apkFile.getPath(), optimizedDirectory.getPath(), librarySearchPath, ClassLoader.getSystemClassLoader());
        invokeCustomMethod(classLoader);
        tv.setText("从apk文件中加载类");
    }

    @SuppressLint({"SetTextI18n", "SdCardPath"})
    public void load4(View view) {
        File dexFile = new File("/sdcard/Download/", "classes.dex");
//        File dexFile = new File("/sdcard/Download/", "makedex-debug.apk"); // or
        ClassLoader classLoader = new PathClassLoader(dexFile.getPath(), ClassLoader.getSystemClassLoader());
        invokeCustomMethod(classLoader);
        tv.setText("PathClassLoader 加载类");
    }

    public void invokeCustomMethod(ClassLoader classLoader) {
        try {
            // 获取类
            Class<?> clz = classLoader.loadClass("cn.my.dex.Start");

            // 反射调用方法
            Method method = clz.getMethod("start", Context.class);
            method.invoke(clz.newInstance(), context);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            Log.d(TAG, "Exception:" + e.getMessage());
        }
    }
}
