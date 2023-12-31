package net.lnear.commands;

import java.util.HashMap;
import java.util.Map;

public class SimpleCommandProcessing implements CommandProcessingAPI {

    private final Map<String, CommandHandler> commands = new HashMap<>();

    @Override
    public void registerCommand(String command, CommandHandler handler) {
        commands.put(command, handler);
    }

    @Override
    public void processCommand(String command, String[] args, CommandContext context) {
        CommandHandler handler = commands.get(command);
        if (handler != null) {
            CommandContext commandContext = new CommandContext(context.getSender(), args);
            handler.handleCommand(commandContext);
        }
    }
}