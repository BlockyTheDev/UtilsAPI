# UtilsAPI
UtilsAPI is a powerful java plugin api. It provides many features. examples: simple event registration, multi page inventories, easy scoreboards, configs and more

It works on minecraft version 1.18.2 and requires java 18

**You need to add api-version: 1.18 to your plugin.yml**
**You maybe want to add depend: [UtilsAPI] to your plugin.yml**

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

### CommandSystem

The `CommandSystem` is a simple way to register commands without needing to create multiple methods

**Example**:

```java
// Registering my custom command with the name "MyCommand" and consuming the event
CommandUtils.registerCommand("MyCommand", e -> {
  // Getting the args by using an util method which will return a String[] of arguments
  String[] args = e.getArgs();

  // Sending the player a message that he used the command and also providing the length / how many arguments were provided
  e.getEvent().getPlayer().sendMessage("Wow you used an custom command! You also provided so many arguments: " + args.length);
});
```

### ScoreboardBuilder

The `ScoreboardBuilder` is a simple way to create scoreboards without needing to mess around with the spigot api

```java
// Creating a ScoreboardBuilder with the title My Scoreboard
ScoreboardBuilder sb = new ScoreboardBuilder("My Scoreboard");

// Adding a line without any color
sb.addLine("Line 1", 0);
// Adding a line with a red color code
sb.addLine("&cRed Line 2", 1);
// Adding a line with a green hex color
sb.addLine("#00ff00Line 3 in green", 2);
// Adding a player who can see this scoreboard
sb.addPlayer(Bukkit.getPlayer("DrachenfeuerHD"));
// Starting the scoreboard and updating it every 20 ticks (1 second)
sb.start(20);

// Scheduling a delayed task to execute after 200 ticks
Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
  // Updating line 0 to now display the text Now the line is updated
  sb.updateLine("Now the line is updated", 0);
}, 200);

// Destroying the scoreboard which will result into the scoreboard being removed from every player
sb.destroy();
```

### MultiScoreboard

The `MultiScoreboard` provides a simple way to cycle across different scoreboards and display this to players

```java
// Creating a ScoreboardBuilder with the title My Scoreboard
ScoreboardBuilder sb = new ScoreboardBuilder("My Scoreboard");

// Adding a line without any color
sb.addLine("Line 1", 0);

// Creating a ScoreboardBuilder with the title Another Scoreboard
ScoreboardBuilder anotherSb = new ScoreboardBuilder("Another Scoreboard");

// Adding a line without any color
sb.addLine("Another Line", 0);

// Creating a multi scoreboard
MultiScoreboard msb = new MultiScoreboard();

// Adding a player who will see the cycling scoreboards
msb.addPlayer(Bukkit.getPlayer("DrachenfeuerHD"));
// Adding the scoreboards to the multi scoreboard
msb.addScoreboards(sb, anotherSb);
// Starting the multi scoreboard with a delay of 1 second between scoreboard changes
msb.start(20);

// Stopping and destroying every scoreboard in the multi scoreboard
msb.stop(true);
```

### Config

The `ConfigUtils` is a simple way to create your custom config without needing to code a lot
You need to provide the config.yml (or your choosen name) in the src folder of your project (prefilled with your default values)
When you export your plugin you need to add your config.yml to your export. (You need to export it along with your other files)

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

**Example Config**:
```
test: 'Value of test'
exampleSection:
  exampleKey: 'This is my value'
```

### TextParser

The `TextParser` is a simple way to replace every hex code and/or color code in a string

**Example**:

```java
// Replaces every hex code and color code for a string  
TextParser.parseHexAndCodesForChat("&cThis would be red and #00ff00Green");

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
  // Allowing users to put in items (Taking / Putting items is not allowed by default)
  .addOption(InventoryBuilder.Option.PUT_ITEM)
  // Building the gui to receive an inventory which we could open to a player
  .build();
```

**Example for custom Inventory-Type:**

```java
// Creating a new InventoryBuilder with the gui title "Title of the &cGUI" and my custom inventory type BREWING (which will result in a brewing stand inventory)
Inventory inventoryBuilder = new InventoryBuilder("Title of the &cGUI", CustomInventoryType.BREWING)
  // Adding an item stack at the position 0 with an event we handle (Click / Drag etc.)
  .addItem(new ItemStack(Material.GLASS), 0, e -> {
    Bukkit.broadcastMessage("Clicked on glass!");
  })
  // Adding an InventoryItem at the position 1 with an event we handle (Click / Drag etc.)
  .addItem(new InventoryItem(new ItemStack(Material.CLOCK), 1, e -> {
    Bukkit.broadcastMessage("You clicked on my special InventoryItem");
  }))
  // Adding a placeholder which will be put at every empty slot in the inventory
  .placeholder(new ItemStack(Material.PLAYER_HEAD))
  // Allowing users to put in items (Taking / Putting items is not allowed by default)
  .addOption(InventoryBuilder.Option.PUT_ITEM)
  // Building the gui to receive an inventory which we could open to a player
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
