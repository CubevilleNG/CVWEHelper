package org.cubeville.cvwehelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.World;

import org.cubeville.commons.commands.CommandParameterVector;
import org.cubeville.commons.commands.CommandParameterWorld;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandParameterString;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;

public class LoadSchematic extends BaseCommand {

    public LoadSchematic() {
        super("loadschematic");
        setPermission("cvwehelper.loadschematic");
        addBaseParameter(new CommandParameterString()); // Schematic name
        addBaseParameter(new CommandParameterWorld()); // Target world
        addBaseParameter(new CommandParameterVector()); // Location where to paste
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) 
        throws CommandExecutionException {

        World targetWorld = (World) baseParameters.get(0);
        
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(new BukkitWorld(targetWorld)).build();

        // TODO...
        
        return null;
    }
    
}
