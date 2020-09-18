package com.yeestor.plugins.acra.mail.config;

import androidx.annotation.NonNull;

import org.acra.config.Configuration;

import java.io.Serializable;

public class YSMailSenderConfig implements Serializable, Configuration {


    private final boolean enabled;

    @NonNull
    private final String mailFrom;

    @NonNull
    private final String username;
    @NonNull
    private final String password;



    @NonNull
    private final String mailTo;
    @NonNull
    private final String smtpHost;

    /**
     * SMTP Port 。默认为465
     */
    private final int smtpPort;

    @NonNull
    private final String socketFactoryClass =  "javax.net.ssl.SSLSocketFactory" ;

    private final int socketFactoryPort = 465 ;

    private final boolean auth = true ;


    public YSMailSenderConfig(@NonNull YSMailSenderConfigBuilderImpl builder) {
        this.enabled = builder.enabled();
        this.mailFrom = builder.mailFrom();
        this.username = builder.username();
        this.password = builder.password();
        this.mailTo = builder.mailTo();
        this.smtpHost = builder.smtpHost();
        this.smtpPort = builder.smtpPort();
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @NonNull
    public String mailFrom() {
        return mailFrom;
    }

    @NonNull
    public String username() {
        return username;
    }

    @NonNull
    public String password() {
        return password;
    }

    @NonNull
    public String mailTo() {
        return mailTo;
    }

    @NonNull
    public String smtpHost() {
        return smtpHost;
    }

    public int smtpPort() {
        return smtpPort;
    }

    @NonNull
    public String socketFactoryClass() {
        return socketFactoryClass;
    }

    public int socketFactoryPort() {
        return socketFactoryPort;
    }

    public boolean auth() {
        return auth;
    }
}
