package me.fromgate.reactions.actions;

import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 * Created by MaxDikiy on 2017-05-17.
 */
public class ActionIfElse extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        String condition;
        String then_;
        String else_;
        String suffix;
        if (params.isParamsExists("if", "then", "else")) {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");

            condition = params.getParam("if", "");
            then_ = params.getParam("then", "");
            else_ = params.getParam("else", "");
            suffix = params.getParam("suffix", "");

            try {
                Boolean result = (Boolean) engine.eval(condition.toLowerCase());
                if (!executeActivator(p, condition.toLowerCase(), (result) ? then_ : else_))
                    Variables.setTempVar("ifelseresult" + suffix, (result) ? then_ : else_);
            } catch (ScriptException e) {
                Variables.setTempVar("ifelsedebug", e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    private static Boolean executeActivator(Player p, String condition, String paramStr) {
        Param param = Param.parseParams(paramStr);
        if (!param.hasAnyParam("run")) return false;
        param = Param.parseParams(param.getParam("run"));
        if (param.isEmpty() || !param.hasAnyParam("activator", "exec")) return false;
        param.set("player", p == null ? "null" : p.getName());
        Param tempVars = new Param();
        tempVars.set("condition", condition);
        EventManager.raiseExecEvent(p, param, tempVars);
        return true;
    }

}
