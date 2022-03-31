package artemwernon.fawe_t_plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.google.common.io.CharStreams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class qgen implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        org.bukkit.entity.Player player = (Player) sender;
        try {
            //Process process = new ProcessBuilder("D:\\cpp_files\\mine_gen\\bin\\Release\\mine_gen.exe", args[0]);
            //Process p = process.start();
            /*Runtime rt = Runtime.getRuntime();
            String[] commands = {"D:\\cpp_files\\mine_gen\\bin\\Release\\" + args[0]};
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            // Read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                //System.out.println(s);
                player.chat(s);
            }

            // Read any errors from the attempted command
            //System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                //System.out.println(s);
                player.chat(s);
            }*/
            Process process = new ProcessBuilder("D:\\cpp_files\\mine_gen\\bin\\Release\\mine_gen.exe", args[0]).start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            player.chat(result);
        } catch (IOException e) {
            player.chat("aaaaa");
            e.printStackTrace();
        }
        player.chat("rg generated");
        return true;
    }
}
