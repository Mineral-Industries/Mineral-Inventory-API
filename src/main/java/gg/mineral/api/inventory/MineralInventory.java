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
     * @param items     the array of items.
     * @param cancelled if all events related to the item are cancelled.
     */
    public void setContents(ItemStack[] items, boolean cancelled);

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
     * @param cancelled if all events related to the item are cancelled.
     */
    public void set(int x, int y, ItemStack itemstack, boolean cancelled);

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

    public void set(int slot, ItemStack itemstack, Predicate<Interaction> interactionFunction);

    /**
     * Adds the item in the inventory.
     * 
     * @param itemstack the itemstatck to be set in the slot.
     * @param cancelled if all events related to the item are cancelled.
     */
    public void add(ItemStack itemstack, boolean cancelled);

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
     * 
     * @param player the player to open the menu for.
     */
    public void open(Player player);

    /**
     * Calls the event when opened.
     * 
     * @param interactionFunction the function that gets executed when the item is
     *                            interacted with.
     */
    public void whenOpened(Predicate<Interaction> interactionFunction);

    /**
     * Check if the inventory contains an itemstack where the predicate is true.
     * 
     * @param itemstackFunction the itemstack predicate.
     * 
     * @return if it exists in the inventory.
     */
    public boolean contains(Predicate<ItemStack> itemstackFunction);

    /**
     * Checks the number of itemstacks in the inventory that satisfy the predicate.
     * 
     * @param itemstackFunction the itemstack predicate.
     * 
     * @return the amount.
     */
    public long count(Predicate<ItemStack> itemstackFunction);

}
