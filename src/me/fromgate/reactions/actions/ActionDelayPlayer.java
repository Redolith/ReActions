package me.fromgate.reactions.actions;

import java.util.Map;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.RAFlagDelay;

import org.bukkit.entity.Player;

public class ActionDelayPlayer extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String str = setPersonalDelay (p, ParamUtil.getParam(params, "param-line", ""));
        if (str.isEmpty()) return false;
        setMessageParam(str);
        return true;
    }
    
    
    private String setPersonalDelay(Player p, String mstr){
        String seconds = "";
        String varname = p.getName();
        if (mstr.isEmpty()) return "";
        if (mstr.contains("/")){
            String[] m = mstr.split("/");
            if (m.length>=2){
                seconds = m[0];
                varname = m[1];
            }
        } else seconds = mstr;
        if (seconds.isEmpty()) return "";
        Long sec = u().parseTime(seconds);
        if (sec == 0) return "";
        RAFlagDelay.setPersonalDelay(p,varname, sec);
        return seconds;
    }

}
