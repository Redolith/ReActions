package me.fromgate.reactions.flags;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.FlagVal;
import me.fromgate.reactions.util.RADebug;

import org.bukkit.entity.Player;

public enum Flags {
    GROUP ("group",true,new FlagGroup()),
    PERM ("perm",true,new FlagPerm()),
    TIME ("time",false,new FlagTime()),
    ITEM ("item",true,new FlagItem()),
    ITEM_INVENTORY ("invitem",true,new FlagItemInventory()),
    TOWN ("town",true,new FlagTown()),
    MONEY ("money",true,new FlagMoney()),
    CHANCE("chance",false,new FlagChance()),
    PVP("pvp",true,new FlagPVP()),
    ONLINE("online",false,new FlagOnline()),
    DELAY("delay",false,new FlagDelay()),
    DELAY_PLAYER("pdelay",true,new FlagDelayPlayer()),
    REGION("region",true,new FlagRegion()),
    STATE("pose",true,new FlagState()),
    REGION_PLAYERS("rgplayer",false,new FlagRegionPlayers()),
    GAMEMODE("gamemode",true,new FlagGameMode()),
    FOODLEVEL("food",true,new FlagFoodlevel()),
    XP("xp",true,new FlagXP()),
    LEVEL("level",true,new FlagLevel());
    
    
    private String alias;
    private boolean require_player = true;
    private Flag flag;
    
    
    Flags (String alias, boolean needplayer, Flag flag){
        this.alias = alias;
        this.require_player=needplayer;
        this.flag= flag;
    }
    
    public boolean check(Player player, String param){
        if (this.require_player&&(player==null)) return false;
        return flag.checkFlag(player, param);
    }
    
    
    public static boolean isValid(String name){
        for (Flags ft : Flags.values()){
            if (ft.name().equalsIgnoreCase(name)) return true;
            if (ft.getAlias().equalsIgnoreCase(name)) return true;
        }
        return false;
    }
    public static Flags getByName(String name){
        for (Flags ft : Flags.values()){
            if (ft.name().equalsIgnoreCase(name)) return ft;
            if (ft.getAlias().equalsIgnoreCase(name)) return ft;
        }
        return null;
    }
   
    public String getAlias(){
        return this.alias;
    }
    
    public static boolean checkFlag (Player p, String flag, String param, boolean not){
        Flags ft = Flags.getByName(flag);
        if (ft == null) return false;
        boolean check = ft.check(p, param);
        if (not) return !check;
        return check;
    }
    
    public static boolean checkFlags (Player p, Activator c){
        return RADebug.checkFlagAndDebug(p, checkAllFlags (p, c));
    }

    public static boolean checkAllFlags (Player p, Activator c){
        if (c.getFlags().size()>0)
            for (int i = 0; i<c.getFlags().size();i++){
                FlagVal f = c.getFlags().get(i);
                if (!checkFlag (p, f.flag, f.value, f.not)) return false;
            }
        return true;
    }
    
    public static String getFtypes(){
        String str = "";
        for (Flags f : Flags.values()){
            str = (str.isEmpty() ? f.name() : str+","+f.name());
            str = (str.isEmpty() ? f.getAlias() : str+","+f.getAlias());
        }
        return str;
    }

    public static String getValidName(String flag) {
        for (Flags f : Flags.values())
            if (f.getAlias().equalsIgnoreCase(flag)) return f.name();
       return flag;
    }
}
