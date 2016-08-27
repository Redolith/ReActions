package me.fromgate.reactions.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CmdDefine {
    public String command();

    public String[] subCommands();

    public String permission();

    public boolean allowConsole() default false;

    public String description();

    public String shortDescription();
}

