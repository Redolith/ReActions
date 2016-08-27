package me.fromgate.reactions.placeholders;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PlaceholderDefine {
    public String id();

    public boolean needPlayer() default false;

    public String[] keys();
}
