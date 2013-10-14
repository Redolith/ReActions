package me.fromgate.reactions.actions;

import java.util.List;
import java.util.Map;

import me.fromgate.reactions.EventManager;
import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.ActVal;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum Actions{
    //"tp,velocity,sound,potion,rmvpot,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,invitemrmv,itemgive,cmdplr,cmdop,cmdsrv,moneypay,moneygive,delay,pdelay,back,mob,effect,run,rgclear";
    TP ("tp",true,new ActionTp()),
    VELOCITY ("velocity",true,new ActionVelocity()),
    SOUND ("sound",false,new ActionSound()),    //добавить параметр location
    POTION ("potion",true,new ActionPlayerPotion()),
    POTION_REMOVE ("rmvpot",true,new ActionPlayerPotionRemove()),
    GROUP_ADD ("grpadd",true,new ActionGroupAdd()),
    GROUP_REMOVE ("grprmv",true,new ActionGroupRemove()),
    MESSAGE ("msg",true,new ActionMessage()), //или всё-таки добавить параметры ??
    BROADCAST ("msgall",true,new ActionBroadcast()), //или всё-таки добавить параметры ??
    DAMAGE ("dmg",true,new ActionDamage()),
    TOWN_SET ("townset",true,new ActionTownSet()),
    TOWN_KICK ("townkick",true,new ActionTownKick()),
    ITEM_REMOVE ("itemrmv",true,new ActionItemRemove()),
    ITEM_REMOVE_INVENTORY ("invitemrmv",true,new ActionItemRemoveInv()),
    ITEM_GIVE ("itemgive",true,new ActionItemGive()),
    ITEM_DROP ("itemdrop",true,new ActionItemDrop()),
    CMD ("cmdplr",true,new ActionCommand()),
    CMD_OP("cmdop",false,new ActionCommandOp()),
    CMD_CONSOLE ("cmdsrv",false,new ActionCommandConsole()),
    MONEY_PAY ("moneypay",true,new ActionMoneyPay()),
    MONEY_GIVE ("moneygive",true,new ActionMoneyGive()),
    DELAY ("delay",false,new ActionDelay()),
    DELAY_PLAYER ("pdelay",true,new ActionDelayPlayer()),
    BACK("back",true,new ActionBack()),
    MOB_SPAWN ("mob",false,new ActionMobSpawn()),
    EFFECT("effect",false,new ActionEffect()),
    EXECUTE ("run",false,new ActionExecute()),  /// ???? не уверен
    REGION_CLEAR("rgclear",false,new ActionClearRegion()),
    HEAL("heal",true,new ActionHeal());


    private String alias;
    private boolean requireplayer;
    private Action action;

    Actions (String alias, boolean requireplayer, Action action){
        this.alias = alias;
        this.requireplayer = requireplayer;
        this.action = action;
        this.action.init(this);
    }

    static ReActions plg(){
        return ReActions.instance;
    }

    static RAUtil u(){
        return ReActions.util;
    }

    public String getAlias(){
        return this.alias;
    }


    public static Actions getByName(String name){
        for (Actions at : Actions.values())
            if (at.name().equalsIgnoreCase(name)
                    ||at.getAlias().equalsIgnoreCase(name)) return at;
        return null;
    }
    
    public static String getValidName (String name){
        for (Actions at : Actions.values())
            if (at.getAlias().equalsIgnoreCase(name)) return at.name();
       return name;
    }
    
    /*
     *  не знаю ещё зачем..... 
     */
    public static boolean executeActivator (Player p, Activator act){
        boolean action = Flags.checkFlags(p, act);
        boolean rst = true;
        List<ActVal> actions = action ? act.getActions() : act.getReactions();
        if (actions.isEmpty()) return true;
        for (int i = 0; i<actions.size(); i++){
            if (!Actions.isValid(actions.get(i).flag)) continue;
            Actions at = Actions.getByName(actions.get(i).flag);
            Map<String,String> params = Util.replaceAllPlaceholders(p, act, Util.parseActionParam(actions.get(i).value));
            if (!at.performAction(p, act, action, params)) rst = false;
        }
        return rst;
    }


    public boolean performAction(Player p, Activator a, boolean action, Map<String,String> params){
        if ((p==null)&&this.requireplayer) return false;
        return this.action.executeAction(p, a, action, params);
    }

    public static boolean isValid(String name){
        for (Actions at : Actions.values()){
            if (at.name().equalsIgnoreCase(name)) return true;
            if (at.getAlias().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    
    public static void execActivator(final Player p, final Player targetPlayer, final String id, long delay_ticks){
        Activator act = plg().getActivator(id);
        if (act == null) {
            u().logOnce("wrongact_"+id, "Failed to run exec activator "+id+". Activator not found.");
            return;
        }

        if (!act.getType().equalsIgnoreCase("exec")){
            u().logOnce("wrongactype_"+id, "Failed to run exec activator "+id+". Wrong activator type.");
            return;
        }

        Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
            @Override
            public void run() {
                EventManager.raiseExecEvent(p, targetPlayer, id);
            }
        }, Math.max(1, delay_ticks));
    }


}
