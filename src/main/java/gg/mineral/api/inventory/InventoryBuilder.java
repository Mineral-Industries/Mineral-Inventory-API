package gg.mineral.api.inventory;

public interface InventoryBuilder<T> {
  public MineralInventory build(T obj);
}
