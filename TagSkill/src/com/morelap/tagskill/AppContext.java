package com.morelap.tagskill;

import com.morelap.tagskill.eventbus.BusProvider;
import com.morelap.tagskill.net.HttpMediator;

import android.app.Application;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BusProvider.getInstance().register(
                new HttpMediator(getApplicationContext()));
    }

}
