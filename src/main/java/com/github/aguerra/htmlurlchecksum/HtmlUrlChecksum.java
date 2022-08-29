package com.github.aguerra.htmlurlchecksum;

import com.github.aguerra.htmlurlchecksum.commands.CommandRegistry;
import com.github.aguerra.htmlurlchecksum.commands.htmlchecksum.HtmlChecksumInDirectory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlUrlChecksum {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        CommandRegistry commandRegistry = new CommandRegistry();
        commandRegistry.register(
                new HtmlChecksumInDirectory()
        );

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg != null && !arg.isBlank() && arg.startsWith("-")) {
                List<String> cmdArgs = new ArrayList<>();
                var additionalAccessors = commandRegistry.getAdditionalAccessors(arg);
                for (i = i + 1; i < args.length; i++) {
                    var cmdArg = args[i];
                    if (cmdArg.startsWith("-") && !additionalAccessors.contains(cmdArg)) {
                        break;
                    }
                    cmdArgs.add(cmdArg);
                }
                commandRegistry.call(arg, cmdArgs);
            }
        }
//        DirectoryScanner scanner = new DirectoryScanner();

    }
}
