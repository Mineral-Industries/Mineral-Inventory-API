package gg.mineral.api.inventory;

import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class MineralPlayerInventory extends CraftInventoryPlayer implements MineralInventory {
    static Object2ObjectOpenHashMap<UUID, MineralPlayerInventory> playerInventoryMap = new Object2ObjectOpenHashMap<>();
    Int2ObjectOpenHashMap<Predicate<Interaction>> interactionMap = new Int2ObjectOpenHashMap<>();
    CraftPlayer holder;
    boolean fullClear = false;

    public static MineralPlayerInventory create(Player player) {
        MineralPlayerInventory inv = playerInventoryMap.get(player.getUniqueId());

        if (inv == null) {
            playerInventoryMap.put(player.getUniqueId(), inv = new MineralPlayerInventory(player.getInventory()));
        }
        return inv;

    }

    public static MineralPlayerInventory get(Player player) {
        return playerInventoryMap.get(player.getUniqueId());
    }

    public MineralPlayerInventory(org.bukkit.inventory.PlayerInventory playerInventory) {
        super(((CraftInventoryPlayer) playerInventory).getInventory());
        holder = (CraftPlayer) this.getHolder();
    }

    public static void destroy(Player player) {
        playerInventoryMap.remove(player.getUniqueId());
    }

    public void clearHotbar() {
        for (int it = 0; it < 9; it++) {
            setItem(it, null);
        }
    }

    @Override
    public void setItem(int slot, ItemStack itemstack) {
        try {
            interactionMap.remove(slot);
            super.setItem(slot, itemstack);
        } catch (Exception e) {

        }
    }

    @Override
    public void set(int x, int y, ItemStack itemstack) {
        try {
            int slot = x + (y * 9);
            setItem(slot, itemstack);
        } catch (Exception e) {

        }
    }

    @Override
    public void set(int x, int y, ItemStack itemstack, Predicate<Interaction> interactionFunction) {
        try {
            int slot = x + (y * 9);
            interactionMap.put(slot, interactionFunction);
            super.setItem(slot, itemstack);
        } catch (Exception e) {

        }
    }

    @Override
    public void add(ItemStack itemstack) {
        try {
            setItem(findUnusedSlot(), itemstack);
        } catch (Exception e) {

        }
    }

    @Override
    public void add(ItemStack itemstack, Predicate<Interaction> interactionFunction) {
        try {
            int slot = findUnusedSlot();
            interactionMap.put(slot, interactionFunction);
            super.setItem(slot, itemstack);
        } catch (Exception e) {

        }
    }

    @Override
    public Integer findUnusedSlot() {
        net.minecraft.server.v1_8_R3.ItemStack[] mcItems = this.getInventory().getContents();
        for (int i = 0; i <= mcItems.length; i++) {
            if (mcItems[i] == null) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Predicate<Interaction> getInteractionPredicate(int slot) {
        return interactionMap.get(slot);
    }

    @Override
    public void clear() {
        interactionMap.clear();

        if (!fullClear) {
            clearHotbar();
            return;
        }

        fullClear = false;
        super.clear();
        setHelmet(null);
        setChestplate(null);
        setLeggings(null);
        setBoots(null);
    }

    @Override
    public void setContents(ItemStack[] items) {

        net.minecraft.server.v1_8_R3.ItemStack[] mcItems = this.getInventory().getContents();
        for (int i = 0; i < mcItems.length; ++i) {
            if (i >= items.length) {
                setItem(i, null);
                continue;
            }

            if (i < 8) {
                fullClear = true;
            }

            setItem(i, items[i]);
        }

        holder.updateInventory();
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.PLAYER;
    }

    @Override
    public void open(Player player) {
    }

    @Override
    public boolean contains(Predicate<ItemStack> itemstackFunction) {
        for (ItemStack itemstack : getContents()) {
            if (itemstackFunction.test(itemstack)) {
                return true;
            }
        }

        return false;
    }
}
