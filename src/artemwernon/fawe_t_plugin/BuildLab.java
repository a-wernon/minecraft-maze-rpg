package artemwernon.fawe_t_plugin;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getServer;

public class BuildLab implements CommandExecutor {
    public static void wait(int ms){
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        org.bukkit.entity.Player player = (Player) sender;
        boolean allowUndo = true;
        if (args.length == 0){
            player.chat("no region specified");
            return true;
        }
        String Qua_name = args[0];
        player.chat(Qua_name);
        int[][] tcoords = new int[0][];
        try {
            tcoords = FileArrayProvider.read2dlines("D:\\schem_stor\\" + Qua_name + "_co.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.chat(tcoords.length + "");
        String[] tnames = new String[0];
        try {
            tnames = FileArrayProvider.readLines("D:\\schem_stor\\" + Qua_name + "_na.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.chat(tnames.length + "");
        player.chat(tnames[0]);
        int[][] coords = tcoords;//{{0, 100, 0}};
        String[] names = tnames;//{"way010.schem"};
        World world = FaweAPI.getWorld("world");
        //EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
        EditSession editSession = new EditSessionBuilder(world).fastmode(true).autoQueue(true).build();
        //editSession.enableQueue();

        for (int i = 0; i< names.length; ++i) {
            File file = new File("D:\\mcserv\\plugins\\FastAsyncWorldEdit\\schematics\\" + names[i]);
            BlockVector3 to = BlockVector3.at(coords[i][0], coords[i][1], coords[i][2]);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = null;
            try {
                reader = format.getReader(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Clipboard clipboard = null;
            try {
                clipboard = reader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(to)
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);

            //i % 32 == 31 ||
            if (i == names.length - 1){
                player.chat("wait for paste to complete " + i + "of " + names.length);
                editSession.flushQueue();

            }

            //EditSession editSession = ClipboardFormats.findByFile(file).load(file).paste(player.getWorld(), allowUndo, (Transform) null);
            //EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)
        }
        //editSession.flushQueue();
        return true;
    }


}
