package gg.mineral.api.inventory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class MineralMenu implements MineralInventory {
	static Object2ObjectOpenHashMap<UUID, MineralMenu> menuMap = new Object2ObjectOpenHashMap<>();
	String title;
	Predicate<Interaction> openPredicate = null;
	Int2ObjectOpenHashMap<Predicate<Interaction>> interactionMap = new Int2ObjectOpenHashMap<>();
	Int2ObjectOpenHashMap<ItemStack> itemMap = new Int2ObjectOpenHashMap<>();
	int size = 9;

	public MineralMenu(String title) {
		this.title = title;
	}

	public MineralMenu(MineralMenu menu) {
		this.title = menu.title;
		this.interactionMap = menu.interactionMap;
		this.itemMap = menu.itemMap;
		this.size = menu.size;
	}

	public static MineralInventory getMenu(UUID uuid) {
		return menuMap.get(uuid);
	}

	@Override
	public void set(int x, int y, ItemStack itemstack, boolean cancelled) {
		set(x + (y * 9), itemstack, cancelled ? interaction -> {
			return true;
		} : null);
	}

	@Override
	public void set(int slot, ItemStack itemstack, Predicate<Interaction> interactionFunction) {

		if (itemstack == null || slot < 0) {
			return;
		}

		itemMap.put(slot, itemstack);

		if (interactionFunction != null) {
			interactionMap.put(slot, interactionFunction);
		} else {
			interactionMap.remove(slot);
		}

		if (slot > size - 1) {
			size = slot + 1;
		}

		if (inv == null) {
			return;
		}

		inv.setItem(slot, itemstack);
	}

	@Override
	public void set(int x, int y, ItemStack itemstack, Predicate<Interaction> interactionFunction) {
		int slot = x + (y * 9);
		set(slot, itemstack, interactionFunction);
	}

	@Override
	public void add(ItemStack itemstack, boolean cancelled) {
		set(findUnusedSlot(), itemstack, cancelled ? interaction -> {
			return true;
		} : null);
	}

	@Override
	public void add(ItemStack itemstack, Predicate<Interaction> interactionFunction) {
		int slot = findUnusedSlot();
		set(slot, itemstack, interactionFunction);
	}

	@Override
	public Integer findUnusedSlot() {
		for (int i = 0; i <= itemMap.keySet().size(); i++) {
			if (itemMap.get(i) == null) {
				return i;
			}
		}

		return -1;
	}

	protected Inventory toInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(player, Math.max(roundUp(size, 9), 9), title.toString());

		for (Map.Entry<Integer, ItemStack> e : itemMap.int2ObjectEntrySet()) {
			inventory.setItem(e.getKey(), e.getValue());
		}

		return inventory;
	}

	private static int roundUp(int val, int multiple) {
		int mod = val % multiple;
		return mod == 0 ? val : val + multiple - mod;
	}

	protected Inventory inv;

	@Override
	public void open(Player player) {
		if (openPredicate != null && openPredicate.test(new Interaction(player, Interaction.Type.INVENTORY_OPEN))) {
			return;
		}

		if (inv == null) {
			inv = toInventory(player);
		}

		player.openInventory(inv);
		menuMap.put(player.getUniqueId(), this);
	}

	public static void destroy(Player player) {
		menuMap.remove(player.getUniqueId());
		player.closeInventory();
	}

	@Override
	public void setContents(ItemStack[] contents, boolean cancelled) {
		Predicate<Interaction> interactionFunction = cancelled ? interaction -> {
			return true;
		} : null;
		for (int i = 0; i < contents.length; i++) {
			set(i, contents[i], interactionFunction);
		}
	}

	@Override
	public Predicate<Interaction> getInteractionPredicate(int slot) {
		return interactionMap.get(slot);
	}

	@Override
	public void clear() {
		itemMap.clear();
	}

	@Override
	public InventoryType getInventoryType() {
		return InventoryType.MENU;
	}

	@Override
	public boolean contains(Predicate<ItemStack> itemstackFunction) {
		for (ItemStack itemstack : itemMap.values()) {
			if (itemstackFunction.test(itemstack)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void whenOpened(Predicate<Interaction> interactionFunction) {
		this.openPredicate = interactionFunction;
	}

	@Override
	public long count(Predicate<ItemStack> itemstackFunction) {
		return itemMap.values().stream().filter(itemstackFunction).count();
	}
}
