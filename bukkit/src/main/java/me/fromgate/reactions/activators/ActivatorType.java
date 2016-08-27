package me.fromgate.reactions.activators;

/*
 * TODO
 * TIME Параметры: time:10:00 repeat_time:5s (проверка на максимальное время) repeat_count:5
 * TIME_SERVER
 * TIME_REPEATER
 */


import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public enum ActivatorType {
    // алиас, класс активатора, класс события
    BUTTON("b", ButtonActivator.class, ButtonEvent.class),
    PLATE("plt", PlateActivator.class, PlateEvent.class),
    REGION("rg", RegionActivator.class, RegionEvent.class),
    REGION_ENTER("rgenter", RgEnterActivator.class, RegionEnterEvent.class),
    REGION_LEAVE("rgleave", RgLeaveActivator.class, RegionLeaveEvent.class),
    EXEC("exe", ExecActivator.class, ExecEvent.class),
    COMMAND("cmd", CommandActivator.class, CommandEvent.class),
    MESSAGE("msg", MessageActivator.class, MessageEvent.class),
    PVP_KILL("pvpkill", PVPKillActivator.class, PVPKillEvent.class),
    PLAYER_DEATH("PVP_DEATH", PlayerDeathActivator.class, PlayerWasKilledEvent.class),
    PLAYER_RESPAWN("PVP_RESPAWN", PlayerRespawnActivator.class, PlayerRespawnedEvent.class),
    LEVER("lvr", LeverActivator.class, LeverEvent.class),
    DOOR("door", DoorActivator.class, DoorEvent.class),
    JOIN("join", JoinActivator.class, JoinEvent.class),
    QUIT("quit", QuitActivator.class, QuitEvent.class),
    MOB_CLICK("mobclick", MobClickActivator.class, MobClickEvent.class),
    MOB_KILL("mobkill", MobKillActivator.class, MobKillEvent.class),
    MOB_DAMAGE("mobdamage", MobDamageActivator.class, MobDamageEvent.class),
    ITEM_CLICK("itemclick", ItemClickActivator.class, ItemClickEvent.class),
    ITEM_CONSUME("consume", ItemConsumeActivator.class, ItemConsumeEvent.class),
    ITEM_HOLD("itemhold", ItemHoldActivator.class, ItemHoldEvent.class),
    ITEM_WEAR("itemwear", ItemWearActivator.class, ItemWearEvent.class),
    FCT_CHANGE("faction", FactionActivator.class, FactionEvent.class),
    FCT_RELATION("fctrelation", FactionRelationActivator.class, FactionRelationEvent.class),
    FCT_CREATE("fctcreate", FactionCreateActivator.class, FactionCreateEvent.class),
    FCT_DISBAND("fctdisband", FactionDisbandActivator.class, FactionDisbandEvent.class),
    SIGN("sign", SignActivator.class, SignEvent.class),
    VARIABLE("var", VariableActivator.class, VariableEvent.class);

    private String alias;
    private Class<? extends Activator> aclass;
    private Class<? extends Event> eclass;

    ActivatorType(String alias, Class<? extends Activator> actclass, Class<? extends Event> evntclass) {
        this.alias = alias;
        this.aclass = actclass;
        this.eclass = evntclass;
    }

    public Class<? extends Activator> getActivatorClass() {
        return aclass;
    }

    public Class<? extends Event> getEventClass() {
        return eclass;
    }


    public String getAlias() {
        return this.alias;
    }

    public static boolean isValid(String str) {
        for (ActivatorType at : ActivatorType.values())
            if (at.name().equalsIgnoreCase(str) || at.alias.equalsIgnoreCase(str)) return true;
        return false;
    }

    public boolean isValidEvent(Event event) {
        return eclass.isInstance(event);
    }

    public static ActivatorType getByName(String name) {
        for (ActivatorType at : ActivatorType.values())
            if (at.name().equalsIgnoreCase(name) || at.getAlias().equalsIgnoreCase(name)) return at;
        return null;
    }

    public static void listActivators(CommandSender sender, int pageNum) {
        List<String> activatorList = new ArrayList<String>();
        for (ActivatorType activatorType : ActivatorType.values()) {
            String name = activatorType.name();
            String alias = activatorType.getAlias().equalsIgnoreCase(name) ? " " : " (" + activatorType.getAlias() + ") ";
            String description = ReActions.util.getMSGnc("activator_" + name);
            activatorList.add("&6" + name + "&e" + alias + "&3: &a" + description);
        }
        ReActions.util.printPage(sender, activatorList, pageNum, "msg_activatorlisttitle", "", false, sender instanceof Player ? 10 : 1000);
    }


}
