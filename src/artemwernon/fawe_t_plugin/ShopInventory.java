package artemwernon.fawe_t_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ShopInventory implements CommandExecutor {
    public boolean openShop (CommandSender sender){
        System.out.println("opening inv");
        Player p = (Player) sender;
        // Here we create our named help "inventory"
        Inventory shopinv = Bukkit.getServer().createInventory(p, 27, "Shop");
        //shopinv.addItem(new ItemStack(Material.APPLE));
        //shopinv.setItem(3, new ItemStack(Material.APPLE));
        ItemStack conf = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta conf_meta = conf.getItemMeta();

        conf_meta.setDisplayName("Sell these items");
        conf.setItemMeta(conf_meta);
        shopinv.setItem(26, conf);

        //Here opens the inventory
        p.openInventory(shopinv);
        return true;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return openShop(sender);
    }
}
