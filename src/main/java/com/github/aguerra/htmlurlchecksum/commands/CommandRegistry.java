package com.github.aguerra.htmlurlchecksum.commands;

import com.github.aguerra.htmlurlchecksum.exceptions.CommandNotFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, AbstractCommand> commands;

    public CommandRegistry() {
        this.commands = new HashMap<>();
    }

    public void register(AbstractCommand command) {
        var accessors = command.accessors();
        for (String accessor : accessors) {
            this.commands.put(accessor, command);
        }
    }

    private AbstractCommand getCommand(String accessor) {
        var abstractCommand = this.commands.get(accessor);
        if (abstractCommand == null) throw new CommandNotFoundException(accessor);
        return abstractCommand;
    }

    public void call(String accessor, List<String> args) {
        AbstractCommand abstractCommand = this.getCommand(accessor);
        abstractCommand.run(args);
    }
    public List<String> getAdditionalAccessors(String accessor){
        AbstractCommand abstractCommand = this.getCommand(accessor);
        return Arrays.asList(abstractCommand.additionalAccessors());
    }

}
