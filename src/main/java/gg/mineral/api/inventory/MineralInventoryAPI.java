package gg.mineral.api.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MineralInventoryAPI implements Listener {
    static boolean registered = false;

    public static void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new MineralInventoryAPI(), plugin);
        registered = true;
    }

    /**
     * 
     * @param title The title for the menu.
     * 
     * @return a new menu.
     */
    public static MineralInventory createMenu(String title) {
        if (!registered) {
            throw new IllegalStateException("The Inventory API has not been registered.");
        }

        return new MineralMenu(title);
    }

    /**
     * 
     * @param menu The menu to be cloned.
     * 
     * @return the cloned menu.
     */
    public static MineralInventory cloneMenu(MineralInventory menu) {
        if (!registered) {
            throw new IllegalStateException("The Inventory API has not been registered.");
        }

        if (menu.getInventoryType() != InventoryType.MENU) {
            throw new IllegalStateException("This inventory is not a menu.");
        }

        return new MineralMenu((MineralMenu) menu);
    }

    /**
     * Gets the player's current inventory. If it doesn't already exist, it will be
     * created.
     * 
     * @param player The player that the inventory belongs to.
     * 
     * @return the player inventory.
     */
    public static MineralInventory getOrCreatePlayerInventory(Player player) {
        if (!registered) {
            throw new IllegalStateException("The Inventory API has not been registered.");
        }

        return MineralPlayerInventory.create(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerQuit(PlayerQuitEvent e) {
        MineralPlayerInventory.destroy(e.getPlayer());
        MineralMenu.destroy(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInventoryClose(InventoryCloseEvent e) {
        MineralMenu.destroy((Player) e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() instanceof MineralPlayerInventory) {
            MineralPlayerInventory inv = (MineralPlayerInventory) e.getClickedInventory();
            e.setCancelled(inv.getInteractionPredicate(e.getSlot())
                    .test(new Interaction((Player) e.getWhoClicked(), Interaction.Type.INVENTORY_CLICK)));
            return;
        }

        MineralInventory menu = MineralMenu.getMenu(e.getWhoClicked().getUniqueId());

        if (menu == null) {
            return;
        }

        e.setCancelled(menu.getInteractionPredicate(e.getSlot())
                .test(new Interaction((Player) e.getWhoClicked(), Interaction.Type.INVENTORY_CLICK)));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerInteract(PlayerInteractEvent e) {
        MineralPlayerInventory inv = MineralPlayerInventory.get(e.getPlayer());
        Interaction.Type type;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            type = Interaction.Type.HAND_RIGHT_CLICK;
        } else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            type = Interaction.Type.HAND_LEFT_CLICK;
        } else {
            return;
        }

        e.setCancelled(inv.getInteractionPredicate(inv.getHeldItemSlot())
                .test(new Interaction(e.getPlayer(), type)));
    }

}
