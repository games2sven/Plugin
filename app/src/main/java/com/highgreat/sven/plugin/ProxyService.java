package com.highgreat.sven.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.highgreat.sven.pluginstand.PayInterfaceService;

import java.lang.reflect.Constructor;

public class ProxyService extends Service {
    private String serviceName;
    private PayInterfaceService payInterfaceService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    private void init(Intent intent) {

        serviceName = intent.getStringExtra("serviceName");

        try {
            //插件oneService
            Class<?> aClass = getClassLoader().loadClass(serviceName);
            Constructor<?> constructor = aClass.getConstructor(new Class[]{});
            Object in = constructor.newInstance(new Object[]{});

            payInterfaceService = (PayInterfaceService) in;
            payInterfaceService.attach(this);

            Bundle bundle = new Bundle();
            bundle.putInt("from", 1);
            payInterfaceService.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(payInterfaceService == null){
            init(intent);
        }
        return payInterfaceService.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        payInterfaceService.onUnbind(intent);
        return super.onUnbind(intent);
    }

}
