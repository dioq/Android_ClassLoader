package cn.my.loader;

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

public class MainActivity extends AppCompatActivity {

    private final String TAG = "dlog";
    private TextView showText;

    private Context context = null;
    private final String dir = "/data/local/tmp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showText = findViewById(R.id.textBloardId);

        context = MainActivity.this;
    }

    public void load1(View view) {
        File dexOutputDir = this.getDir("dex", 0);// 无法直接从外部路径加载.dex文件，需要指定APP内部路径作为缓存目录（.dex文件会被解压到此目录）
        Log.d(TAG, "dexOutputDir:" + dexOutputDir.getPath());

        File dexFile = new File(dir, "classes.dex");
        String libSearchPath = context.getFilesDir().getPath();

        //1.待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限,
        //2.解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写
        //3.指向包含本地库(so)的文件夹路径，可以设为null
        //4.父级类加载器，一般可以通过context.getClassLoader()获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
//        ClassLoader classLoader = new DexClassLoader(dexFile.getPath(), dexOutputDir.getAbsolutePath(), libSearchPath, getClassLoader());
        ClassLoader classLoader = new BaseDexClassLoader(dexFile.getPath(), dexOutputDir, libSearchPath, getClassLoader());
        invokeCustomMethod(classLoader);
    }

    public void load2(View view) {
        File dexFile = new File(dir, "classes.dex");
        byte[] bytes = FileUtils.getInstance().readFile(dexFile);

        ByteBuffer byteBuffers = ByteBuffer.allocate(bytes.length);
        byteBuffers.put(bytes);
        byteBuffers.position(0);

        //            classLoader = new InMemoryDexClassLoader(byteBuffers, null, context.getClassLoader());
        ClassLoader classLoader = new InMemoryDexClassLoader(byteBuffers, context.getClassLoader());
        invokeCustomMethod(classLoader);
    }

    public void invokeCustomMethod(ClassLoader classLoader) {
        try {
            // 获取类
            Class<?> clz = classLoader.loadClass("cn.my.study.Test");

            // 反射调用方法
            Method method = clz.getMethod("getMsgFromDexFile", String.class);
            Object result = method.invoke(clz.newInstance(), "TestMsg");
            String msg = String.format("result:%s", result);
            Log.d(TAG, msg);
            showText.setText(msg);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            Log.d(TAG, "Exception:" + e.getMessage());
        }
    }
}
