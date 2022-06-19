# UtilsAPI
UtilsAPI is a powerful java plugin api. It provides many features. examples: simple event registration, multi page inventories and more

### EventSystem

The `EventSystem` Is a simple way to register events without needing to create multiple methods

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

The `ConfigUtils` Is a simple way to create your custom config without needing to code a lot
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

The `TextParser` Is a simple way to replace every hex code and/or color code in a string

**Example**:

```java
// Replaces every hex code and color code for a string  
TextParser.parseHexAndCodesForChat("&cThis would be red and #00ff00");

// Replaces every color code in a message starting with a special predefined character
TextParser.parseColorCodes("%aThis is green", '%');

// Replaces every hex code code for a string  
TextParser.parseHexForChat("#00ff00Bright green");
```

Copyright https://DontBlameMe.dev
