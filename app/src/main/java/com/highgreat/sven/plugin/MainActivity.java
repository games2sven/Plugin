package com.highgreat.sven.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    static final String ACTION = "com.highgreat.sven.plugin.Receive1.PLUGIN_ACTION";

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, " 我是宿主，收到你的消息,握手完成!", Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mReceiver, new IntentFilter(ACTION));
    }
    
    public void load(View view){
        loadPlugin();
    }

    private void loadPlugin() {
        File filesDir = this.getDir("plugin", Context.MODE_PRIVATE);
        Log.i("Sven","path == "+filesDir.getAbsolutePath());
        String name = "plugin.apk";
        String filePath = new File(filesDir, name).getAbsolutePath();
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        InputStream is = null;
        FileOutputStream os = null;
        try {
            Log.i("Sven", "加载插件 " + new File(Environment.getExternalStorageDirectory(), name).getAbsolutePath());
            is = new FileInputStream(new File(Environment.getExternalStorageDirectory(),name));
            os = new FileOutputStream(filePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            while((len = is.read(buffer)) != -1){
                os.write(buffer,0,len);
            }
            File f = new File(filePath);
            if(f.exists()){
                Toast.makeText(this, "dex overwrite", Toast.LENGTH_SHORT).show();
            }
            PluginManager.getInstance().loadPath(this);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void click(View view) {
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("className", PluginManager.getInstance().getPackageInfo().activities[0].name);
        startActivity(intent);
    }

    public void sendBroadCast(View view) {
        Intent intent = new Intent();
        intent.setAction("com.highgreat.sven.taopiaopiao.StaticReceiver");
        sendBroadcast(intent);
    }

}
