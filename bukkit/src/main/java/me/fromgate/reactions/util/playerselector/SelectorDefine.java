package me.fromgate.reactions.util.playerselector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SelectorDefine {
    public String key();
}
