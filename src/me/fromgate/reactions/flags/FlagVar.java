package me.fromgate.reactions.flags;

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Variables;

import org.bukkit.entity.Player;

public class FlagVar extends Flag{
    private int flagType = -1;
    private boolean personalVar = false;

    public FlagVar(int flagType, boolean personalVar) {
        this.flagType = flagType;
        this.personalVar = personalVar;
    }

    @Override
    public boolean checkFlag(Player player, String param) {
        Player p = player;
        Map<String,String> params = ParamUtil.parseParams(param, "param-line");
        String var;
        String value;
        
        if (ParamUtil.isParamExists(params, "id")){
            var = ParamUtil.getParam(params, "id", "");
            if (var.isEmpty()) return false;
            value = ParamUtil.getParam(params, "value", "");
        } else {
            String [] ln = ParamUtil.getParam(params, "param-line", "").split("/",2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length>1) ? ln[1] : "";
        }

        
        /*String var = ParamUtil.getParam(params, "id", "");
        if (var.isEmpty()) return false;
        String value = ParamUtil.getParam(params, "value", "");*/
        
        if (!this.personalVar) p = null;
        else if (p == null) return false;
        
        
        switch (this.flagType){
        case 0: // VAR_EXIST
            return Variables.existVar(p, var);
        case 1: 
            return Variables.cmpVar(p, var, value);
        case 2: 
            return Variables.cmpGreaterVar(p, var, value);
        case 3: 
            return Variables.cmpLowerVar(p, var, value);
        }
        return false;
    }
    
    
    

}
