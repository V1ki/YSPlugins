package com.yeestor.plugins.acra.mail.config;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.auto.service.AutoService;

import org.acra.config.ConfigurationBuilder;
import org.acra.config.ConfigurationBuilderFactory;
import org.acra.config.CoreConfiguration;

@AutoService(ConfigurationBuilderFactory.class)
public class YSMailSenderConfigBuilderFactory implements ConfigurationBuilderFactory {
    @NonNull
    @Override
    public ConfigurationBuilder create(@NonNull Context annotatedContext) {
        return new YSMailSenderConfigBuilderImpl();
    }

    @Override
    public boolean enabled(@NonNull CoreConfiguration config) {
        return config.enabled();
    }
}
