package gg.mineral.api.inventory;

import org.bukkit.entity.Player;

public class Interaction {
    Player player;
    Type type;

    public enum Type {
        INVENTORY_CLICK, HAND_LEFT_CLICK, HAND_RIGHT_CLICK, INVENTORY_OPEN
    }

    public Interaction(Player player, Type type) {
        this.player = player;
        this.type = type;
    }

    /**
     * @return The type of interaction. (INVENTORY_CLICK, HAND_LEFT_CLICK,
     *         HAND_RIGHT_CLICK)
     */
    public Type getType() {
        return type;
    }

    /**
     * @return The player that caused the interaction.
     */
    public Player getPlayer() {
        return player;
    }
}
