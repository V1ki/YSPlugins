package com.yeestor.plugins.store;

import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPFile {

    String name() default "plugins" ;

    int mode() default Context.MODE_PRIVATE ;

    Class<?>[] stores();
}
