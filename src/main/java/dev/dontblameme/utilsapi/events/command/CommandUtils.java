package dev.dontblameme.utilsapi.events.command;

import java.util.ArrayList;

public class CommandUtils {

    private static ArrayList<CustomCommand> commands = new ArrayList<>();

    private CommandUtils() {}

    /**
     *
     * @param commandName Name of the command which should be registered
     * @param consumer The consumer which will handle the event
     * @apiNote Registers an / command for players to use. You can get the arguments by using Commandutils#getArguments. This method uses an PlayerCommandPreprocessEvent to register its commands. So the command will not show up in /TAB. You don't need to do anything else manually, the api will do the other stuff
     */
    public static void registerCommand(CustomCommand command) {
        commands.add(command);
    }

    /**
     *
     * @return A list every command
     */
    public static ArrayList<CustomCommand> getCommands() {
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
