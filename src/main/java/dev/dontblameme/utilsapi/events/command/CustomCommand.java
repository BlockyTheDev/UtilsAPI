package dev.dontblameme.utilsapi.events.command;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class CustomCommand {

    private final String commandName;
    private final String permission;
    private final Consumer<CommandEvent> consumer;
    private final String permissionMessage;

    /**
     *
     * @param commandName Name of the command (no / needed)
     * @param permission Permission for the command
     * @param permissionMessage Message for the player if he doesn't have the permission
     * @param consumer Consumer of the command execute event
     */
    public CustomCommand(String commandName, String permission, String permissionMessage, Consumer<CommandEvent> consumer) {
        this.commandName = commandName;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.consumer = consumer;
    }

    /**
     *
     * @param commandName Name of the command (no / needed)
     * @param consumer Consumer of the command execute event
     * @apiNote This creates a command without a permission. You can use this if you don't want to use a permission
     */
    public CustomCommand(String commandName, Consumer<CommandEvent> consumer) {
        this(commandName, "", "", consumer);
    }

}
