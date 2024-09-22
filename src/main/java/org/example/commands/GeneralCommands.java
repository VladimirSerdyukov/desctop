package org.example.commands;

public class GeneralCommands {

    @AppBotCommand(name="/hello", description = "when request hello", showInHelp = true)
    public String hello(){
        return "Hello, User";
    }

    @AppBotCommand(name="/bye", description = "when request bye", showInHelp = true)
    public String bye(){
        return "Good bye, User";
    }

    @AppBotCommand(name="/help", description = "menu help", showInMenuPhotoCommands = true)
    public String help(){
        return "HEEEELP";
    }

    @AppBotCommand(name="/start", description = "menu help", showInMenuPhotoCommands = true)
    public String start(){
        return "start";
    }
}
