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

Copyright https://DontBlameMe.dev
