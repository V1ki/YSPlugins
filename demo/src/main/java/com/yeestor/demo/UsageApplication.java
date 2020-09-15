package com.yeestor.demo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.yeestor.plugins.R;
import com.yeestor.plugins.acra.mail.YSMailSenderfactory;
import com.yeestor.plugins.acra.mail.config.YSMailSenderConfigBuilder;
import com.yeestor.plugins.store.SPFile;
import com.yeestor.plugins.store.SharedPrefrenceInjector;

import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.annotation.AcraCore;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;

@AcraCore(reportSenderFactoryClasses = YSMailSenderfactory.class)
public class UsageApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        ACRA.init(this);
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this)
                .setBuildConfigClass(BuildConfig.class);
        builder.getPluginConfigurationBuilder(YSMailSenderConfigBuilder.class)
                .setMailFrom("bugs.wan@yeestor.com")
                .setMailTo("bugs.wan@yeestor.com")
                .setUsername("bugs.wan@yeestor.com")
                .setPassword("xxxxx---- ")
                .setSMTPHost("smtp.mxhichina.com")
                .setEnabled(true);

        ACRA.DEV_LOGGING = true;
        ACRA.init(this, builder);
    }
}
