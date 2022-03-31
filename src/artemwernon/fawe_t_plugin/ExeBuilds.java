package artemwernon.fawe_t_plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.bukkit.Bukkit.getServer;
import static java.nio.file.StandardCopyOption.*;

public class ExeBuilds implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String[] bldList = new String[0];
        org.bukkit.entity.Player player = (Player) sender;
        try {
            bldList = FileArrayProvider.readLines("D:\\schem_stor\\schem_na.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bldList.length; ++i){
            //here will be some type of schematics control
            String directory = "D:\\schem_stor\\";
            File na_bup = new File(directory + bldList[i] + "_na_bup.txt"); // bup = backup, na = names
            boolean ok = true;
            String source_na = directory + bldList[i] + "_na.txt";
            String target_na = directory + bldList[i] + "_na_bup.txt";
            String source_co = directory + bldList[i] + "_co.txt";
            String target_co = directory + bldList[i] + "_co_bup.txt";
            if (na_bup.exists()){
                try {
                    if(FileUtil.fileSame(source_co, target_co) && FileUtil.fileSame(source_na, target_na)){
                        ok = false;
                    }
                    else{
                        try {
                            Files.copy(Paths.get(source_na), Paths.get(target_na), REPLACE_EXISTING);
                            Files.copy(Paths.get(source_co), Paths.get(target_co), REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    Files.copy(Paths.get(source_na), Paths.get(target_na));
                    Files.copy(Paths.get(source_co), Paths.get(target_co));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!ok){
                continue;
            }

            //end of scheme control
            BuildLab TempB = new BuildLab();
            String[] tempo = new String[1];
            tempo[0] = bldList[i];
            String name = "bldreg";
            String alias = name.toLowerCase(java.util.Locale.ENGLISH);
            PluginCommand te_command = getServer().getPluginCommand(alias);
            TempB.onCommand(player, te_command, "bldreg", tempo);
        }
        return true;
    }
}
