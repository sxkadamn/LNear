don`t abusseeeeeee pls , test pl


it was create for test api, u can also use 

CommandProcessingAPI commandProcessingAPI = new SimpleCommandProcessing();
commandProcessingAPI.registerCommand("your command", this::processNearCommand);

private void processNearCommand(CommandContext context) {
     Command logic............
}
