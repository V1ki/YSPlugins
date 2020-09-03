package com.yeestor.plugins.acra.mail.config;

import android.util.Log;

import androidx.annotation.NonNull;

import org.acra.config.ACRAConfigurationException;
import org.acra.config.Configuration;
import org.acra.config.ConfigurationBuilder;

public class YSMailSenderConfigBuilderImpl implements YSMailSenderConfigBuilder {


    private boolean enabled;

    @NonNull
    private String mailFrom;

    @NonNull
    private String username;
    @NonNull
    private String password;

    @NonNull
    private String mailTo;
    @NonNull
    private String smtpHost;

    /**
     * SMTP Port 。默认为465
     */
    private int smtpPort = 465;


    public YSMailSenderConfigBuilderImpl() {
        Log.i("ACRA", " ==== YSMailSenderConfigBuilderImpl") ;
    }

    @NonNull
    @Override
    public YSMailSenderConfigBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }



    @NonNull
    @Override
    public YSMailSenderConfigBuilder setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
        return this;
    }

    @NonNull
    @Override
    public YSMailSenderConfigBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    @NonNull
    @Override
    public YSMailSenderConfigBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    @NonNull
    @Override
    public YSMailSenderConfigBuilder setMailTo(String mailTo) {
        this.mailTo = mailTo;
        return this;
    }

    @NonNull
    @Override
    public YSMailSenderConfigBuilder setSMTPHost(String smtpHost) {
        this.smtpHost = smtpHost;
        return this;
    }

    @NonNull
    @Override
    public YSMailSenderConfigBuilder setSMTPPort(int smtpPort) {
        this.smtpHost = smtpHost;
        return this;
    }

    @NonNull
    @Override
    public Configuration build() throws ACRAConfigurationException {

        if (!this.checkParameter()) {
            throw new ACRAConfigurationException("Please check your parameter!");
        }

        return new YSMailSenderConfig(this);
    }


    private boolean checkParameter() {


        if(this.enabled && this.mailFrom == null ) {
            return false ;
        }

        if(this.enabled && this.username == null) {
            return false ;
        }

        if(this.enabled && this.password == null) {
            return false ;
        }
        if(this.enabled && this.mailTo == null) {
            return false ;
        }
        if(this.enabled && this.smtpHost == null) {
            return false ;
        }
        return true ;
    }


    boolean enabled() {
        return enabled;
    }

    @NonNull
    String mailFrom() {
        return mailFrom;
    }

    @NonNull
    String username() {
        return username;
    }

    @NonNull
    String password() {
        return password;
    }

    @NonNull
    String mailTo() {
        return mailTo;
    }

    @NonNull
    String smtpHost() {
        return smtpHost;
    }

    int smtpPort() {
        return smtpPort;
    }
}
