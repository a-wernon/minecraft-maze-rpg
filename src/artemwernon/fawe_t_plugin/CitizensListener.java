package artemwernon.fawe_t_plugin;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CitizensListener implements Listener {
    fawe_t_plugin plugin = (fawe_t_plugin) Bukkit.getServer().getPluginManager().getPlugin("fawetest");
    @EventHandler
    private void openMerchantInv(NPCRightClickEvent event) {
        System.out.println(event.getNPC().getId() + "");
        boolean tr = event.getNPC().hasTrait(StringTrait.class);
        if (tr){
            plugin.ShopInv.openShop(event.getClicker());
        }
    }
}
