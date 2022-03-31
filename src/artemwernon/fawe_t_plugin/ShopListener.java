package artemwernon.fawe_t_plugin;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopListener implements Listener {
    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        //System.out.println("click");
        if (e.getView().getTitle().equalsIgnoreCase("shop")) {
            //e.setCancelled(true);
            if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }
            if (e.getSlot() == 26)  {
                e.setCancelled(true);
                //p.sendMessage("§a >> /shop - to visit shop");
                //p.sendMessage("§a >> /home - go to home!");
                ItemStack[] items = inv.getContents();
                if (items==null){
                    System.out.println("hueta");
                }
                int q = 0;
                ItemStack te;
                int price;
                String spr;
                for(int i = 0; i < items.length - 1; ++i){
                    if (items[i] != null){
                        //System.out.println(i + "not null'");
                        if (!items[i].getType().equals(Material.AIR)){
                            //++q;
                            te = items[i];
                            net.minecraft.server.v1_15_R1.ItemStack nmste = CraftItemStack.asNMSCopy(te);
                            NBTTagCompound nmstecompound = (nmste.hasTag()) ? nmste.getTag() : new NBTTagCompound();
                            spr = nmstecompound.getString("price");
                            if(!spr.isEmpty()){
                                //p.chat(spr);
                                price = Integer.parseInt(spr);
                                q += price;
                            }
                            else {
                                p.chat("price not set, contact admin");
                            }
                        }
                    }
                }
                if (q > 0){
                    p.chat(q + " is a quantity of items");
                    p.chat("You received " + q + " gems for selling useless items!");
                    ItemStack rew = new ItemStack(Material.DIAMOND,q);
                    p.getInventory().addItem(rew);
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                }
                p.closeInventory();

            }
            ItemStack te = e.getCurrentItem();
            List<String> tes = te.getItemMeta().getLore();
            if (tes == null && !(e.getCurrentItem().getType().equals(Material.GREEN_STAINED_GLASS_PANE))){
                //p.chat(tes.size() + "");
                e.setCancelled(true);
            }
            //p.chat(e.getSlot() + " slot");
            //p.chat(e.getCurrentItem().toString() + " item type");
        }
    }

}
