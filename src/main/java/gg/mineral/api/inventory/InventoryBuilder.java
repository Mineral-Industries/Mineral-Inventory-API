package gg.mineral.api.inventory;

import java.util.function.Predicate;

public interface InventoryBuilder<T> {
  public MineralInventory build(T obj);

  public MineralInventory build(T obj, Predicate<Interaction> interactionPredicate);
}
