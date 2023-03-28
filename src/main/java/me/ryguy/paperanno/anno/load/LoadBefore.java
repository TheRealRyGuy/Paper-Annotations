package me.ryguy.paperanno.anno.load;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface LoadBefore {
    LoadBefore.Plugin[] value();

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @Repeatable(LoadBefore.class)
    @interface Plugin {
        String name();

        boolean bootstrap() default false;
    }
}
