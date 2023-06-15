package org.cubeville.cvwehelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;

public class CVWEHelper extends JavaPlugin
{
    CommandParser commandParser;

    @Override
    public void onEnable() {
        commandParser = new CommandParser();

        // commandParser.addCommand(new LoadSchematic());
        commandParser.addCommand(new CopyCuboid());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cvwe")) {
            return commandParser.execute(sender, args);
        }
        return false;
    }
}
