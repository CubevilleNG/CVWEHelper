package org.cubeville.cvwehelper;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandParameterList;
import org.cubeville.commons.commands.CommandParameterType;
import org.cubeville.commons.commands.CommandParameterListEnum;
import org.cubeville.commons.commands.CommandParameterWorld;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandParameterVector;
import org.cubeville.commons.utils.BlockUtils;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

public class CopyCuboid extends BaseCommand {

    public CopyCuboid() {
        super("copy cuboid");
        setPermission("cvwehelper.copycuboid");

        addFlag("-e");
        addFlag("-a");

        addParameter("omit", true, new CommandParameterListEnum(Material.class));
        addParameter("mask", true, new CommandParameterListEnum(Material.class));

        List<CommandParameterType> slocpars = new ArrayList<>();
        slocpars.add(new CommandParameterWorld());
        slocpars.add(new CommandParameterVector());
        slocpars.add(new CommandParameterVector());
        addParameter("sloc", true, new CommandParameterList(slocpars));

        List<CommandParameterType> srglocpars = new ArrayList<>();
        srglocpars.add(new CommandParameterWorld());
        srglocpars.add(new CommandParameterString());
        addParameter("srg", true, new CommandParameterList(srglocpars));
        
        List<CommandParameterType> tlocpars = new ArrayList<>();
        tlocpars.add(new CommandParameterWorld());
        tlocpars.add(new CommandParameterVector());
        addParameter("tloc", false, new CommandParameterList(tlocpars));
    }

    private BlockVector3 vec2vec(Vector vector) {
        return BlockVector3.at(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) 
        throws CommandExecutionException {

        if(parameters.containsKey("srg") == parameters.containsKey("sloc"))
            throw new CommandExecutionException("&cExactly one of srg and sloc needed");

        World sourceWorld;
        Vector sourcePos1, sourcePos2;

        if(parameters.containsKey("sloc")) {
            List<Object> sloc = (List<Object>) parameters.get("sloc");
            sourceWorld = (World) sloc.get(0);
            sourcePos1 = (Vector) sloc.get(1);
            sourcePos2 = (Vector) sloc.get(2);
        }
        else {
            List<Object> sloc = (List<Object>) parameters.get("srg");
            sourceWorld = (World) sloc.get(0);
            String regionName = (String) sloc.get(1);
            sourcePos1 = BlockUtils.getWGRegionMin(sourceWorld, regionName);
            sourcePos2 = BlockUtils.getWGRegionMax(sourceWorld, regionName);
        }
        
        List<Object> tloc = (List<Object>) parameters.get("tloc");
        World targetWorld = (World) tloc.get(0);
        Vector targetPos = (Vector) tloc.get(1);
        
        CuboidRegion sourceRegion = new CuboidRegion(vec2vec(sourcePos1), vec2vec(sourcePos2));
        //BlockArrayClipboard clipboard = new BlockArrayClipboard(sourceRegion);

        try {
            ForwardExtentCopy copy = new ForwardExtentCopy(
              new BukkitWorld(sourceWorld), sourceRegion, new BukkitWorld(targetWorld), vec2vec(targetPos));
            copy.setCopyingEntities(flags.contains("-e"));

            Set<Material> omitMaterials = new HashSet<>();
            if(flags.contains("-a")) omitMaterials.add(Material.AIR);
            if(parameters.containsKey("omit")) {
                List<Material> l = (List<Material>) parameters.get("omit");
                for(Material m: l) omitMaterials.add(m);
            }

            if(omitMaterials.size() > 0) {
                List<BlockType> blocktypes = new ArrayList<BlockType>();
                for(Material m: omitMaterials)
                    blocktypes.add(BukkitAdapter.asBlockType(m));

                copy.setSourceMask(new MaskInvert(new BlockTypeMask(new BukkitWorld(sourceWorld), blocktypes)));
            }

            if(parameters.containsKey("mask")) {
                if(omitMaterials.size() > 0)
                    throw new CommandExecutionException("&cOmitting and masking types are exclusive operations");

                List<Material> l = (List<Material>) parameters.get("mask");
                List<BlockType> blocktypes = new ArrayList<BlockType>();
                for(Material m: l)
                    blocktypes.add(BukkitAdapter.asBlockType(m));

                copy.setSourceMask(new BlockTypeMask(new BukkitWorld(sourceWorld), blocktypes));
            }
            
            Operations.complete(copy);
        }
        catch (WorldEditException e) {
            throw new CommandExecutionException(e.getMessage());
        }
        
        return null;
    }
}
