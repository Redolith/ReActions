package me.fromgate.reactions.util;

import me.fromgate.reactions.actions.Actions;

public class ActVal {
    public String flag;
    public String value;

    public ActVal(String f, String v) {
        this.flag = f;
        this.value = v;
    }

    public ActVal(String f) {
        this.flag = f;
        this.value = "";
    }

    @Override
    public String toString() {
        return Util.join(Actions.getValidName(flag), "=", value);
    }
}