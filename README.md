[![](https://jitpack.io/v/Mineral-Industries/Mineral-Inventory-API.svg)](https://jitpack.io/#Mineral-Industries/Mineral-Inventory-API)

# Mineral Inventory API

Examples:

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
	
<dependency>
	<groupId>com.github.Mineral-Industries</groupId>
	<artifactId>Mineral-Inventory-API</artifactId>
	<version>v1.0.3</version>
</dependency>
```

```java
public class YourPlugin extends JavaPlugin {
    public void onEnable() {
        MineralInventoryAPI.register(this);
    }
}
```

```java

Player player = Bukkit.getPlayer(uuid);

// Menu.

MineralInventory menu = MineralInventoryAPI.createMenu("Title");


menu.set(/*X Position*/ 0, /*Y Position*/ 0, new ItemStack(Material.WOOL), interaction -> {
    
    if (interaction.getType() == Interaction.Type.INVENTORY_CLICK) {
        Player playerWhoClicked = interaction.getPlayer();
        playerWhoClicked.setAllowFlight(!playerWhoClicked.getAllowFlight());
    }

    return true; // True if the event should be cancelled, false otherwise
});



menu.open(player); // Open the menu for the player.


// Player Inventory
MineralInventory playerInventory = MineralInventoryAPI.getOrCreatePlayerInventory(player);

playerInventory.set(/*X Position*/ 0, /*Y Position*/ 0, new ItemStack(Material.WOOL), interaction -> {
    
    if (interaction.getType() == Interaction.Type.HAND_LEFT_CLICK) {
        Player playerWhoClicked = interaction.getPlayer();
        playerWhoClicked.setAllowFlight(!playerWhoClicked.getAllowFlight());
    }

    return true; // True if the event should be cancelled, false otherwise
});


```
