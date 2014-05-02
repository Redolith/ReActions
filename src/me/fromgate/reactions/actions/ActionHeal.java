package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.externals.RAEffects;
import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class ActionHeal extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        int hp = ParamUtil.getParam(params, "hp", 0);
        boolean playhearts = ParamUtil.getParam(params, "hearts", true);
        if (params.containsKey("params")) hp=ParamUtil.getParam(params, "params", 0);
        double health = BukkitCompatibilityFix.getEntityHealth(p);
        double healthMax = BukkitCompatibilityFix.getEntityMaxHealth(p);
        if ((hp>0)&&(health<healthMax)) BukkitCompatibilityFix.setEntityHealth(p, Math.max(hp+health, healthMax));
        //if ((hp>0)&&(p.getHealth()<p.getMaxHealth())) p.setHealth(Math.max(hp+p.getHealth(), p.getMaxHealth()));
        if (playhearts&&RAEffects.isPlayEffectConnected()) RAEffects.playEffect(p.getEyeLocation(), "HEART", "offset:0.5 num:4 speed:0.7");
        setMessageParam(Integer.toString(hp));
        return true;
    }
}
