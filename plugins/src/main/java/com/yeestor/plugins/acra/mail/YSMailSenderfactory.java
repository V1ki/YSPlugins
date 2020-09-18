package com.yeestor.plugins.acra.mail;


import android.content.Context;

import com.yeestor.plugins.acra.mail.config.YSMailSenderConfig;

import org.acra.annotation.AcraCore;
import org.acra.config.CoreConfiguration;
import org.acra.plugins.HasConfigPlugin;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

public class YSMailSenderfactory extends HasConfigPlugin implements ReportSenderFactory {

    // NB requires a no arg constructor.
    public YSMailSenderfactory() {
        super(YSMailSenderConfig.class);
    }

    @Override
    public ReportSender create(Context context, CoreConfiguration config) {
        return new YSMailSender(config);
    }

}