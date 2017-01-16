package me.fromgate.reactions.util;

import me.fromgate.reactions.flags.Flags;

public class FlagVal {
    public String flag;
    public String value;
    public boolean not;

    public FlagVal(String f, String v, boolean not) {
        this.flag = f;
        this.value = v;
        this.not = not;
    }

    @Override
    public String toString() {
        String str = Flags.getValidName(flag) + "=" + value;
        if (this.not) str = "!" + str;
        return str;
    }
}
