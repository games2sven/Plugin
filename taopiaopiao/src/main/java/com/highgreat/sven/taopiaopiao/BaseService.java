package com.highgreat.sven.taopiaopiao;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.highgreat.sven.pluginstand.PayInterfaceService;

public class BaseService extends Service implements PayInterfaceService {


    private Service that;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void attach(Service proxyService) {
        this.that = proxyService;
    }
}
