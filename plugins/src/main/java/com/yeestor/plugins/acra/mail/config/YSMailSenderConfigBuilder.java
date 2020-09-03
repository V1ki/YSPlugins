package com.yeestor.plugins.acra.mail.config;

import androidx.annotation.NonNull;

import org.acra.config.ConfigurationBuilder;

public interface YSMailSenderConfigBuilder extends ConfigurationBuilder {

    @NonNull
    YSMailSenderConfigBuilder setEnabled(boolean enabled) ;

    @NonNull
    YSMailSenderConfigBuilder setMailFrom(String mailFrom);

    @NonNull
    YSMailSenderConfigBuilder setUsername(String username);

    @NonNull
    YSMailSenderConfigBuilder setPassword(String password);

    @NonNull
    YSMailSenderConfigBuilder setMailTo(String mailTo);

    @NonNull
    YSMailSenderConfigBuilder setSMTPHost(String smtpHost);

    @NonNull
    YSMailSenderConfigBuilder setSMTPPort(int smtpPort);

}
