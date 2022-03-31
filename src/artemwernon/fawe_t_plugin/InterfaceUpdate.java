package artemwernon.fawe_t_plugin;

import javafx.print.PageLayout;
import javafx.util.Pair;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InterfaceUpdate extends BukkitRunnable implements Listener {  //this is an extension of hp tracker for sword hit cooldowns
    private HashMap<Player,WeaponManager> stamina = new HashMap();
    private fawe_t_plugin fawe_t_plugin;
    InterfaceUpdate(fawe_t_plugin fawe_t_plugin){
        this.fawe_t_plugin = fawe_t_plugin;

    }

    @Override
    public void run() {
        Iterator<Map.Entry<Player,WeaponManager>> it = stamina.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Player,WeaponManager> os = it.next();
            WeaponManager wm = os.getValue();
            wm.tick();
            //os.getKey().chat(te.getValue().getValue() + "");
            //te.getKey().setProgress(Math.max(0,te.getKey().getProgress() - 1/te.getValue().getValue()));
        }
    }

    @EventHandler
    private void playerInit(PlayerJoinEvent evt){
        Player player = evt.getPlayer();
        WeaponManager wm = stamina.get(player);
        if (wm == null){
            player.chat("new wm");
            wm = new WeaponManager(player, fawe_t_plugin);
            stamina.put(player, wm);
            /*BossBar plasta =  Bukkit.getServer().createBossBar("Stamina", BarColor.YELLOW, BarStyle.SOLID);
            stamina.put(player,  new Pair <BossBar, DivDouble> (plasta, new DivDouble(10.0)));
            plasta.setVisible(true);
            plasta.addPlayer(player);*/
        }
    }

    @EventHandler
    private void playerDelete(PlayerQuitEvent evt){
        stamina.remove(evt.getPlayer());
    }

    /*private Double getcooldown(Player player){
        ItemStack main = player.getInventory().getItemInMainHand();
        net.minecraft.server.v1_15_R1.ItemStack nmste = CraftItemStack.asNMSCopy(main);
        NBTTagCompound nmstecompound = (nmste.hasTag()) ? nmste.getTag() : new NBTTagCompound();
        String cooldown = nmstecompound.getString("stamina");
        //player.chat(cooldown);
        if (cooldown.isEmpty()){
            player.chat("Basic cd time used");
            return 10.0;
        }
        else{
            return Double.parseDouble(cooldown);
        }
    }*/

    @EventHandler
    private void playerHit(EntityDamageByEntityEvent evt){
        if (evt.getDamager() instanceof Player){
            Player player = (Player) evt.getDamager();
            if(evt.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || evt.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
                WeaponManager wm = stamina.get(player);
                if (!wm.canHit()){
                    evt.setCancelled(true);
                    player.chat("Wait before hitting!");
                }
                else{
                    player.chat(wm.cooldowns.get(wm.currenthit) + " " + wm.currenthit);
                    evt.setDamage(evt.getDamage()*(1 + wm.hit(player)));
                }

                /*Pair <BossBar, DivDouble> te = stamina.get(player);
                BossBar st = te.getKey();
                if(st.getProgress() > 0.001){
                    player.chat("Wait before attacking again!");
                    evt.setCancelled(true);
                }
                else{
                    st.setProgress(1);
                    DivDouble cd = te.getValue();
                    cd.setValue(getcooldown(player));
                    player.chat(cd.getValue() + " cd");

                }*/
            }
        }
    }


    @EventHandler
    private void annnulCombo(PlayerItemHeldEvent evt){
        stamina.get(evt.getPlayer()).clear();
    }


    //need to add new method that deletes player when he is offline for 10 seconds
}
