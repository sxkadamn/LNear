package net.lnear.commands;

public interface CommandProcessingAPI {

    void registerCommand(String command, CommandHandler handler);

    void processCommand(String command, String[] args, CommandContext context);
}