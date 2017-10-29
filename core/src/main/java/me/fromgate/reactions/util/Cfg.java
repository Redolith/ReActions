package me.fromgate.reactions.util;


import me.fromgate.reactions.ReActions;
import org.bukkit.configuration.file.FileConfiguration;

public class Cfg {
    private static FileConfiguration config;

    public static boolean saveEmptySections = false;
    public static String actionMsg = "tp,grpadd,grprmv,townset,townkick,itemrmv,invitemrmv,itemgive,moneypay,moneygive"; //отображать сообщения о выполнении действий
    public static String language = "english";
    public static boolean languageSave = false;
    public static boolean checkUpdates = false;
    public static boolean centerTpCoords = true;
    public static int worlduardRecheck = 2;
    public static int itemHoldRecheck = 2;
    public static int itemWearRecheck = 2;
    public static boolean horizontalPushback = false;
    public static int chatLength = 55;
    public static boolean playerSelfVarFile = false;
    public static boolean playerAsynchSaveSelfVarFile = false;

    public static void save() {
        config.set("general.language", language);
        config.set("general.check-updates", checkUpdates);
        config.set("general.player-self-variable-file", playerSelfVarFile);
        config.set("general.player-asynch-save-self-variable-file", playerAsynchSaveSelfVarFile);
        config.set("reactions.save-empty-actions-and-flags-sections", saveEmptySections);
        config.set("reactions.show-messages-for-actions", actionMsg);
        config.set("reactions.center-player-teleport", centerTpCoords);
        config.set("reactions.region-recheck-delay", worlduardRecheck);
        config.set("reactions.item-hold-recheck-delay", itemHoldRecheck);
        config.set("reactions.item-wear-recheck-delay", itemWearRecheck);
        config.set("reactions.horizontal-pushback-action", horizontalPushback);
        config.set("reactions.default-chat-line-length", chatLength);
        config.set("actions.shoot.break-block", Shoot.actionShootBreak);
        config.set("actions.shoot.penetrable", Shoot.actionShootThrough);

        ReActions.getPlugin().saveConfig();
    }

    public static void load() {
        language = config.getString("general.language", "english");
        checkUpdates = config.getBoolean("general.check-updates", true);
        languageSave = config.getBoolean("general.language-save", false);
        playerSelfVarFile = config.getBoolean("general.player-self-variable-file", false);
        playerAsynchSaveSelfVarFile = config.getBoolean("general.player-asynch-save-self-variable-file", false);
        chatLength = config.getInt("reactions.default-chat-line-length", 55);
        saveEmptySections = config.getBoolean("reactions.save-empty-actions-and-flags-sections", false);
        centerTpCoords = config.getBoolean("reactions.center-player-teleport", true);
        actionMsg = config.getString("reactions.show-messages-for-actions", "tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive");
        worlduardRecheck = config.getInt("reactions.region-recheck-delay", 2);
        itemHoldRecheck = config.getInt("reactions.item-hold-recheck-delay", 2);
        itemWearRecheck = config.getInt("reactions.item-wear-recheck-delay", 2);
        horizontalPushback = config.getBoolean("reactions.horizontal-pushback-action", false);
        Shoot.actionShootBreak = config.getString("actions.shoot.break-block", Shoot.actionShootBreak);
        Shoot.actionShootThrough = config.getString("actions.shoot.penetrable", Shoot.actionShootThrough);
    }

    static {
        config = ReActions.getPlugin().getConfig();
    }
}
