package dev.dontblameme.utilsapi.config.ingameconfig;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class IngameConfigUpdateEvent {

    private final String key;
    private final String oldValue;
    private final String newValue;
    private final Player player;

    public IngameConfigUpdateEvent(String key, String oldValue, String newValue, Player player) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.player = player;
    }

}
