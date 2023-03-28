package me.ryguy.paperanno.anno;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@DefaultQualifier(NonNull.class)
@Target(ElementType.TYPE)
public @interface PaperPlugin {
    String name();

    String desc();

    String version() default "1.0";

    String apiVersion() default "1.19";

    boolean hasOpenClassLoader() default false;
}
