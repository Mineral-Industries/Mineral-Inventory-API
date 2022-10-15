package gg.mineral.api.inventory;

import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MineralInventory {

    /**
     * @return The inventory type. (PLAYER, MENU)
     */
    public InventoryType getInventoryType();

    /**
     * @return An unused slot in the inventory.
     */
    public Integer findUnusedSlot();

    /**
     * Sets the contents of the inventory.
     * 
     * @param items the array of items.
     */
    public void setContents(ItemStack[] items);

    /**
     * Clears the inventory.
     */
    public void clear();

    /**
     * Sets the item in the inventory.
     * 
     * @param x         the x position the item should be set to.
     * @param y         the y position the item should be set to.
     * @param itemstack the itemstatck to be set in the slot.
     */
    public void set(int x, int y, ItemStack itemstack);

    /**
     * Sets the item in the inventory.
     * 
     * @param x                   the x position the item should be set to.
     * @param y                   the y position the item should be set to.
     * @param itemstack           the itemstatck to be set in the slot.
     * @param interactionFunction the function that gets executed when the item is
     *                            interacted with.
     * 
     *                            <pre>
     * {@code
     * inventory.set(x, y, itemstack, interaction -> {
     *      if (interaction.getType() == Interaction.Type.INVENTORY_CLICK) { 
     *          Player player = interaction.getPlayer();
     *          // do stuff
     *          return true;
     * }
     * 
     * return false; // true to cancel interaction, false to not cancel interaction
     * });
     * }
     * </pre>
     * 
     */
    public void set(int x, int y, ItemStack itemstack, Predicate<Interaction> interactionFunction);

    /**
     * Adds the item in the inventory.
     * 
     * @param itemstack the itemstatck to be set in the slot.
     */
    public void add(ItemStack itemstack);

    /**
     * Sets the item in the inventory.
     * 
     * @param itemstack           the itemstatck to be set in the slot.
     * @param interactionFunction the function that gets executed when the item is
     *                            interacted with.
     * 
     *                            <pre>
     * {@code
     * inventory.add(itemstack, interaction -> {
     *      if (interaction.getType() == Interaction.Type.INVENTORY_CLICK) { 
     *          Player player = interaction.getPlayer();
     *          // do stuff
     *          return true;
     * }
     * 
     * return false; // true to cancel interaction, false to not cancel interaction
     * });
     * }
     * </pre>
     * 
     */
    public void add(ItemStack itemstack, Predicate<Interaction> interactionFunction);

    /**
     * @return the interaction predicate.
     */
    public Predicate<Interaction> getInteractionPredicate(int slot);

    /**
     * Opens the inventory.
     */
    public void open(Player player);

}
