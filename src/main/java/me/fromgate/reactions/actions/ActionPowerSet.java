package me.fromgate.reactions.actions;

import java.util.Map;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class ActionPowerSet extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        Location loc = Util.parseLocation(ParamUtil.getParam(params, "loc", ""));
        setMessageParam("UNKNOWN");
        if (loc == null) return false;
        Block b = loc.getBlock();
        setMessageParam(b.getType().name());
        if (!isPowerBlock(b)) return false;
        String state = ParamUtil.getParam(params, "power", "on");
        boolean power = getPower(b, state);
        return setPower(b,power);
    }
    


    private boolean getPower (Block b, String state){
        boolean power = state.equalsIgnoreCase("on")||state.equalsIgnoreCase("true");
        if (state.equalsIgnoreCase("toggle")){
            if (b.getType()==Material.LEVER){
                Lever lever = (Lever) b.getState().getData();
                power = lever.isPowered();
            } if (isDoorBlock(b)){
                power = Util.isOpen(b);
            } else power = true;
        }
        return power;        
    }

    @SuppressWarnings("deprecation")
    private boolean setPower(Block b, boolean power){
        if (b.getType()==Material.LEVER){
            BlockState state = b.getState();
            Lever lever = (Lever) state.getData();
            lever.setPowered(power);
            b.setData(lever.getData(),true);
            state.update();
        } else if (isDoorBlock (b)){
            Util.setOpen(b, power);
        } else return false;
        return true;
    }

    public boolean isPowerBlock (Block b){
        if (b.getType() == Material.LEVER) return true;
        return isDoorBlock(b);
    }
    
    public boolean isDoorBlock (Block b){
        if (b.getType() == Material.WOODEN_DOOR) return true;
        if (b.getType() == Material.TRAP_DOOR) return true;
        if (b.getType() == Material.FENCE_GATE) return true;
        if (b.getType() == Material.IRON_DOOR_BLOCK) return true;
        return false;
    }


}
