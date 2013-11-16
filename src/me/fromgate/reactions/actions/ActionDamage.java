package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;

public class ActionDamage extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        int dmg = ParamUtil.getParam(params, "param-line", 0);
        if (dmg>0) p.damage(dmg);
        else p.playEffect(EntityEffect.HURT);
        setMessageParam(Integer.toString(dmg));
        return true;
    }

}
