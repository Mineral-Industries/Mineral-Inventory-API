package gg.mineral.api.inventory;

public interface MenuBuilder<T> {
  public MineralInventory buildMenu(T obj);
}
