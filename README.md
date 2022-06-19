# UtilsAPI
UtilsAPI is a powerful java plugin api. It provides many features. examples: simple event registration, multi page inventories and more

### EventSystem

The `EventSystem` is a simple way to register events without needing to create multiple methods

**Example**:

```java
// Registering a AsyncPlayerChatEvent
// AsyncPlayerChatEvent.class > The Event which should be called as a class
// e > The event which is called
EventUtils.registerEvent(AsyncPlayerChatEvent.class, e -> {
  // Send the player of the event a message
  e.getPlayer().sendMessage("Hello World");
});
```


### Config

The `ConfigUtils` is a simple way to create your custom config without needing to code a lot
You need to provide a config.yml (or your choosen name) in the src folder & the build of your project (prefilled with your default values).

**Example**:

```java
// this > Instance of your main class
// "config.yml" > Name and file extension of your config file
ConfigUtils exampleConfig = new ConfigUtils(this, "config.yml");
        
// Getting a value by the key "test"
String value = exampleConfig.getValue("test");

// Setting the value "exampleKey" in the section "exampleSection" to value + " My Config is working"
exampleConfig.setValue("exampleSection", "exampleKey", value + " My Config is working");

// Reloading the entire config updating its values, not needed to be called in most cases
exampleConfig.refresh();

// Getting the file of the config, not needed in most cases
File configFile = exampleConfig.getConfigFile();
```

### TextParser

The `TextParser` is a simple way to replace every hex code and/or color code in a string

**Example**:

```java
// Replaces every hex code and color code for a string  
TextParser.parseHexAndCodesForChat("&cThis would be red and #00ff00");

// Replaces every color code in a message starting with a special predefined character
TextParser.parseColorCodes("%aThis is green", '%');

// Replaces every hex code code for a string  
TextParser.parseHexForChat("#00ff00Bright green");
```

### ParticleBuilder

The `ParticleBuilder` allows to easily create and spawn particles to players

**Example**:

```java
// Creating a new ParticleBuilder with the particle type FLAME
new ParticleBuilder(Particle.FLAME)
  // Setting the amount of particles to 20
  .amount(20)
  // Adding a player who can see the particles
  .addPlayer(Bukkit.getPlayer("Notch"))
  // Defining the location where the particles should spawn
  .location(Bukkit.getPlayer("Notch").getLocation())
  // Adding an offset to the particles (Spawning it 10 blocks above the players location)
  .offset(0, 10, 0)
  // Defining the speed of which the particles should fly away
  .speed(0.2f)
  // Sending the particles to the player(s)
  .send();
```

### InventoryBuilder

The `InventoryBuilder` allows to easily create custom inventories and manage every item event without additional methods or classes

**Example**:

```java
// Creating a new InventoryBuilder with the gui title "Title of the &cGUI" and a size of 27
Inventory inventoryBuilder = new InventoryBuilder("Title of the &cGUI", 27)
  // Adding an item stack at the position 0 with an event we handle (Click / Drag etc.)
  .addItem(new ItemStack(Material.GLASS), 0, e -> {
    Bukkit.broadcastMessage("Clicked on glass!");
  })
  // Adding an InventoryItem at the position 2 with an event we handle (Click / Drag etc.)
  .addItem(new InventoryItem(new ItemStack(Material.CLOCK), 2, e -> {
    Bukkit.broadcastMessage("You clicked on my special InventoryItem");
  }))
  // Adding a border (Surrounding the outer row) of the gui with this ItemStack
  .border(new ItemStack(Material.BARRIER))
  // Adding a placeholder which will be put at every empty slot in the inventory
  .placeholder(new ItemStack(Material.PLAYER_HEAD))
  // Allowing users to put in items (Taking / Putting is not allowed by default)
  .addOption(InventoryBuilder.Option.PUT_ITEM)
  // Building the gui to receive an inventory
  .build();
```

### ItemBuilder

The `ItemBuilder` allows to easily create custom items

**Example**:

```java
// Creating a new ItemBuilder with the material DIAMOND_BLOCK
ItemStack myCustomItem = new ItemBuilder(Material.DIAMOND_BLOCK)
  // Changing the display name using hex and color codes
  .name("&cMy cool red #ff0000Custom Item")
  // Adding a custom lore
  .addLore("Hello there")
  // Setting the amount of items
  .amount(3)
  // Adding an enchantment with the level 7
  .enchant(Enchantment.ARROW_INFINITE, 7)
  // Adding an item flag to hide the enchantments
  .flag(ItemFlag.HIDE_ENCHANTS)
  // Making the item unbreakable
  .unbreakable()
  // Building / Creating the ItemStack
  .build();
```

### MultiPageInventory

The `MultiPageInventory` allows to easily create inventories with multiple pages, buttons, items with events & more

**Example**:

```java
// Creating a new MultiPageInventory
Inventory multiPageInventory = new MultiPageInventory()
  // Adding the first page with an inventory size of 27
  .addPage(new InventoryBuilder("Page 1", 27))
  // Adding another inventory with a size of 36 which contains an item with a special event
  .addPage(new InventoryBuilder("Page 2", 36).addItem(new ItemStack(Material.DIAMOND_BLOCK), 0, e -> {
    System.out.println("You clicked on my item in a multi paged inventory");
   }))
  // Adding a button of a custom item stack which should be at slot 1 in inventory 1. It redirects to page 2
  .button(new MultiPageButton(new ItemStack(Material.STONE_BUTTON), 1, 1, 2))
  // Adding a button of a custom item stack which should be at slot 0 in inventory 1. It redirects to the next page
  .button(new MultiPageButton(new ItemStack(Material.STONE_BUTTON), 0, 1, MultiPageButton.ButtonType.NEXT))
  // Adding a global button of a custom item stack which should be at slot 10 in every inventory. It redirects to the last page and disappears if it is invalid
  // shouldStayWhenInvalid means, that for example if it is false and a button to the next page would be on the last page of the MultiInventory, it would disappear because it makes no sense for it to be there (Because there is no next page)
  .globalButton(new MultiPageButton(new ItemStack(Material.CLOCK), 10, MultiPageButton.ButtonType.LAST, false))
  // Building the inventory
  .build();

// Now we could open this inventory to a player and the api would handle everything else
```

Copyright https://DontBlameMe.dev
