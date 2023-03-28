package me.ryguy.paperanno.anno.load;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface LoadAfter {

    Plugin[] value();

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @Repeatable(LoadAfter.class)
    @interface Plugin {
        String name();

        boolean bootstrap() default false;
    }
}
