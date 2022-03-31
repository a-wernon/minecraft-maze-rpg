package artemwernon.fawe_t_plugin;

import javafx.util.Pair;
import net.Indyuce.mmocore.api.event.PlayerRegenResourceEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.resource.PlayerResource;
import net.Indyuce.mmocore.api.player.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;

import static java.lang.Double.min;

public class HpTracker implements Listener {
    private HashMap<Player, Pair<BossBar, BossBar>> resour = new HashMap();
    /*@EventHandler
    public void hpup(EntityRegainHealthEvent evt){
        if (evt.getEntity() instanceof Player){
            updateResources((Player) evt.getEntity());
        }
    }

    @EventHandler
    public void hpdown(EntityDamageEvent evt){
        if (evt.getEntity() instanceof Player){
            updateResources((Player) evt.getEntity());
        }
    }*/

    @EventHandler
    public void pldeat(PlayerRespawnEvent evt){
        evt.getPlayer().setHealth(evt.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        updateResources(evt.getPlayer());
    }

    @EventHandler
    public void manachange(PlayerRegenResourceEvent evt){
        updateResources(evt.getPlayer());
    }

    private void updateResources(Player player){
        PlayerData pdata = PlayerData.get(player.getUniqueId());
        Pair <BossBar,BossBar> bar = resour.get(player);
        BossBar bs_health;
        BossBar bs_mana;
        if (bar == null){
            player.chat("new");
            bs_health = Bukkit.getServer().createBossBar("Health", BarColor.RED, BarStyle.SEGMENTED_10);
            bs_mana = Bukkit.getServer().createBossBar("Mana", BarColor.BLUE, BarStyle.SEGMENTED_10);
            resour.put(player, new Pair<>(bs_health,bs_mana));
            bs_health.setVisible(true);
            bs_health.addPlayer(player);
            bs_mana.setVisible(true);
            bs_mana.addPlayer(player);
        }
        else{
            bs_health = bar.getKey();
            bs_mana = bar.getValue();
        }
        bs_health.setTitle("Current Health: " + Math.floor(player.getHealth()));
        bs_health.setProgress(min(1,player.getHealth()/ player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        bs_mana.setTitle("Current Mana: " + Math.floor(pdata.getMana()));
        bs_mana.setProgress(min(1,pdata.getMana()/pdata.getStats().getStat(StatType.MAX_MANA)));

    }
}
