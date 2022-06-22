package dev.dontblameme.utilsapi.events.command;

import lombok.Getter;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@Getter
public class CommandEvent {

    private final PlayerCommandPreprocessEvent event;
    private final String[] args;

    public CommandEvent(PlayerCommandPreprocessEvent e, String[] args) {
        this.event = e;
        this.args = args;
    }
}
