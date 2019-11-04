package com.highgreat.sven.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.highgreat.sven.pluginstand.PayInterfaceBroadcast;

import java.lang.reflect.Constructor;

public class ProxyBroadCast extends BroadcastReceiver {

    //需要加载插件的全类名
    private String className;
    private PayInterfaceBroadcast payInterfaceBroadcast;

    public ProxyBroadCast(String className, Context context) {
        this.className = className;

        Class<?> aClass = null;
        try {
            aClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor constructor = aClass.getConstructor(new Class[]{});
            Object in = constructor.newInstance(new Object[]{});
            payInterfaceBroadcast = (PayInterfaceBroadcast) in;
            payInterfaceBroadcast.attach(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        payInterfaceBroadcast.onReceive(context, intent);
    }
}
