package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class CommandEvent extends RAEvent {
    private String command;

    public CommandEvent (Player p, String command) {
    	super(p);
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }
    
}
