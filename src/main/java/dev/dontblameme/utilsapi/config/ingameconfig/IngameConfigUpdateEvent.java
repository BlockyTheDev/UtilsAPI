package dev.dontblameme.utilsapi.config.ingameconfig;

import org.bukkit.entity.Player;

public record IngameConfigUpdateEvent(String key, String oldValue, String newValue, Player player) {}
