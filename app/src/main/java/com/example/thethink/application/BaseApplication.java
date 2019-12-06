package com.example.thethink.application;

/*
 *  描述：    Application
 */

import android.app.Application;
import com.example.thethink.utils.StaticClass;
import cn.bmob.v3.Bmob;

public class BaseApplication extends Application {

    //创建
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

    }

}
