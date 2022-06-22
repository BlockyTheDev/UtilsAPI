package dev.dontblameme.utilsapi.events.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CommandUtils {

    private static HashMap<String, Consumer<CommandEvent>> commands = new HashMap<>();

    private CommandUtils() {}

    /**
     *
     * @param commandName Name of the command which should be registered
     * @param consumer The consumer which will handle the event
     * @apiNote Registers an / command for players to use. You can get the arguments by using Commandutils#getArguments. This method uses an PlayerCommandPreprocessEvent to register its commands. So the command will not show up in /TAB. You don't need to do anything else manually, the api will do the other stuff
     */
    public static void registerCommand(String commandName, Consumer<CommandEvent> consumer) {
        commands.put(commandName, consumer);
    }

    /**
     *
     * @return The map of every command with its consumer
     */
    public static Map<String, Consumer<CommandEvent>> getCommands() {
        return commands;
    }

    /**
     *
     * @param commandName Name of the command
     * @apiNote Removes the command from the system which will result in it no longer working
     */
    public static void removeCommand(String commandName) {
        commands.remove(commandName);
    }
}
