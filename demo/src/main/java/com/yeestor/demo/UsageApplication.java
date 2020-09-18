package com.yeestor.demo;

import android.app.Application;
import android.content.Context;

import com.yeestor.plugins.acra.mail.YSMailSenderfactory;
import com.yeestor.plugins.acra.mail.config.YSMailSenderConfigBuilder;
import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.annotation.AcraCore;
import org.acra.config.CoreConfigurationBuilder;

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
                .setPassword("Wan0806@")
                .setSMTPHost("smtp.mxhichina.com")
                .setEnabled(true);

        ACRA.DEV_LOGGING = true;
        ACRA.init(this, builder);
    }
}
