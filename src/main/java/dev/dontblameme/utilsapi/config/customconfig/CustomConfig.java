package dev.dontblameme.utilsapi.config.customconfig;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CustomConfig {

    private final JavaPlugin instance;
    private FileConfiguration fileConfig;
    private final String configName;
    private File configFile;

    /**
     *
     * @param instance Instance of your plugin to use
     * @param configName The name of the config to use (with file ending. Example: config.yml)
     */
    public CustomConfig(JavaPlugin instance, String configName) {
        this.instance = instance;
        this.configName = configName;
        refresh();
    }

    /**
     * @apiNote Refreshes the entire config. This means: Create the file if it is not existing and reload it
     */
    public void refresh() {

        configFile = new File(instance.getDataFolder(), configName);

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            instance.saveResource(configName, false);
        }

        fileConfig = new YamlConfiguration();

        try {
            fileConfig.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param key String to search for
     * @param section Section of key (not needed)
     * @return Value of key as list
     */
    public List<?> getList(String key, String... section) {
        if(section.length == 0)
            return fileConfig.getList(key);

        ConfigurationSection configSection = getConfigSection(section);

        if(configSection == null)
            return new ArrayList<>();

        return Objects.requireNonNull(configSection.getList(key));
    }

    /**
     *
     * @param key String to search for
     * @param section Section of key (not needed)
     * @return Value of key as object
     */
    public Object getObject(String key, String... section) {

        if(section.length == 0)
            return fileConfig.get(key);

        ConfigurationSection configSection = getConfigSection(section);

        if(configSection == null)
            return new Object();

        return Objects.requireNonNull(configSection.get(key));
    }

    /**
     *
     * @param key String to search for
     * @param section Section of key
     * @return Value of key as string
     */
    public String getValue(String key, String... section) {
        if(section.length == 0)
            return fileConfig.getString(key);

        ConfigurationSection configSection = getConfigSection(section);

        if(configSection == null)
            return "";

        return Objects.requireNonNull(configSection.getString(key));
    }

    /**
     *
     * @param section Section of key
     * @param key Identifier
     * @param value Value of identifier
     */
    public void setValue(String key, Object value, String... section) {
        if(section.length == 0) {
            fileConfig.set(key, value);
            save();
            refresh();
            return;
        }

        ConfigurationSection configSection = getConfigSection(section);

        if(configSection == null)
            return;

        configSection.set(key, value);
        save();
        refresh();
    }

    /**
     *
     * @return Raw file used
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     *
     * @param deep Should it search deep
     * @return Set of available keys
     */
    public Set<String> getKeys(boolean deep) {
        return fileConfig.getKeys(deep);
    }

    private void save() {
        try {
            fileConfig.save(configFile);
        } catch (IOException e) {
            System.out.println("Error while saving config: ");
            e.printStackTrace();
        }
    }

    private ConfigurationSection getConfigSection(String... section) {
        if(section.length == 0)
            throw new IllegalArgumentException("Section cannot be empty!");

        ConfigurationSection configSection = null;

        for(String sectionName : section) {
            if(configSection == null)
                configSection = fileConfig.getConfigurationSection(sectionName);
            else
                configSection = configSection.getConfigurationSection(sectionName);
        }

        return configSection;
    }

}
