package me.fromgate.reactions.event;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityClickEvent extends RAEvent {
    private Entity entity;

    public EntityClickEvent(Player p, Entity entity) {
        super(p);
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

}
