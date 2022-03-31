package artemwernon.fawe_t_plugin;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class BowManager implements Listener {
    fawe_t_plugin fawe_t_plugin;
    BowManager(fawe_t_plugin fawe_t_plugin){
        this.fawe_t_plugin = fawe_t_plugin;
    }

    private Double getBowData(ItemStack is, String tag){
        net.minecraft.server.v1_15_R1.ItemStack nmste = CraftItemStack.asNMSCopy(is);
        NBTTagCompound nmstecompound = (nmste.hasTag()) ? nmste.getTag() : new NBTTagCompound();
        String cooldown = nmstecompound.getString(tag);
        //player.chat(cooldown);
        if (cooldown.isEmpty()){
            System.out.println("No data with that name was found.");
            return 0D;
        }
        else{
            return Double.parseDouble(cooldown);
        }
    }

    private void setArrowMeta(Entity ar, String key,Double data){
        ar.setMetadata(key, new FixedMetadataValue(fawe_t_plugin, data));
    }

    @EventHandler
    private void addArrow(EntityShootBowEvent evt){
        if (evt.getEntityType().equals(EntityType.PLAYER)){
            ItemStack bow = evt.getBow();
            Double data = getBowData(bow, "crit");
            setArrowMeta(evt.getProjectile(),"crit",data);
        }
    }

    @EventHandler
    private void resolveArrow(EntityDamageByEntityEvent evt){
        if (evt.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
            if(evt.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow) evt.getDamager();
                MetadataValue critmod = arrow.getMetadata("crit").get(0);
                if(critmod != null){
                    evt.setDamage(evt.getDamage() +  critmod.asDouble());
                    System.out.println(critmod.asDouble());
                    System.out.println(evt.getDamage() + "damage");
                }
                else{
                    System.out.println("Epic fail");
                }
            }
        }
    }
}
