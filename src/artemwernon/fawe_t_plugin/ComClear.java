package artemwernon.fawe_t_plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class ComClear implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        org.bukkit.entity.Player player = (Player) sender;

        player.chat("clearing first");
        int[] bot = {Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])};
        int[] top = {bot[0] + 31, bot[1] + 31, bot[2] +31};
        /*CuboidRegion region = new CuboidRegion(world, bot, top);
                    editSession.setBlock();
        editSession.fall(region, false, );*/
        getServer().dispatchCommand(getServer().getConsoleSender(), "sudo wernon /"+ "/pos1 " + bot[0]+ "," + bot[1] + "," + bot[2]);
        getServer().dispatchCommand(getServer().getConsoleSender(), "sudo wernon /" +"/pos2 " + top[0] + "," + top[1] + "," + top[2]);
        getServer().dispatchCommand(getServer().getConsoleSender(), "sudo wernon /" +"/set air");
        player.chat("now paste");
        return true;
    }
}
