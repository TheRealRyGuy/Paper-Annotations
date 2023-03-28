package me.ryguy.paperanno.anno.dependency;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@DefaultQualifier(NonNull.class)
@Repeatable(Dependencies.class)
public @interface Dependency {
    String name();

    boolean required() default false;

    boolean bootstrap() default false;
}
