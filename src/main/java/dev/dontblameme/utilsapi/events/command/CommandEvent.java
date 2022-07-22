package dev.dontblameme.utilsapi.events.command;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public record CommandEvent(Player player, PlayerCommandPreprocessEvent event, String[] args) {}
