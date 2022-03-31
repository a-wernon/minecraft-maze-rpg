package artemwernon.fawe_t_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TestHelp implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        System.out.println("opening inv");
        Player p = (Player) sender;
        // Here we create our named help "inventory"
        Inventory help = Bukkit.getServer().createInventory(p, 9, "Help");

        //Here you define our item
        ItemStack ref1 = new ItemStack(Material.BOOK);
        ItemMeta metaref1 = ref1.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();

        lore.add(" ");
        lore.add("§for visit our site");
        lore.add(" ");
        lore.add("§atest.net/help");

        metaref1.setLore(lore);
        metaref1.setDisplayName("§6§lClick to get help");


        ref1.setItemMeta(metaref1);


        //Here opens the inventory
        p.openInventory(help);
        return true;
    }


}