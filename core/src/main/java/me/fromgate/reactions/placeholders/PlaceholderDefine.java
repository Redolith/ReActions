package me.fromgate.reactions.placeholders;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PlaceholderDefine {
    String id();

    boolean needPlayer() default false;

    String[] keys();
}
