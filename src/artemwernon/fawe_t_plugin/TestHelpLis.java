package artemwernon.fawe_t_plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class TestHelpLis implements Listener {
    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        Inventory inv =e.getInventory();
        if (true) {
            e.setCancelled(true);
            if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }


            if (e.getSlot() == 5 && (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6§lClick to get help"))) {

                p.sendMessage("§a >> /shop - to visit shop");
                p.sendMessage("§a >> /home - go to home!");
                p.closeInventory();

                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);

            }

        }
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }
    }

}
