package artemwernon.fawe_t_plugin;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionsLeftEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Map;
import java.util.Set;

public class ObjReg extends CustomObjective implements Listener {

    // Get the Quests plugin
    Quests qp = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");

    // Construct the objective
    public ObjReg() {
        this.setName("RG reach objective");
        this.setAuthor("Artem Wernon");
        this.setShowCount(true);
        this.setCountPrompt("Enter the amount that the player must drop:");
        this.setDisplay("Drop %Item Name%: %count%");
        this.addStringPrompt("rgnam","Enter region in that player should finish run", "");
    }

    // Catch the Bukkit event for a player gaining/losing exp
    @EventHandler
    public void HasFinished(RegionEnteredEvent evt){
        // Make sure to evaluate for all of the player's current quests
        for (Quest quest : qp.getQuester(evt.getPlayer().getUniqueId()).getCurrentQuests().keySet()) {
            Player player = Bukkit.getPlayer(evt.getUUID());
            Map<String, Object> map = getDataForPlayer(evt.getPlayer(), this, quest);
            String regionname = evt.getRegionName();
            // Check if the player gained exp, rather than lost
            String tar_reg =  (String) map.get("rgnam");
            if (regionname.equals(tar_reg)) {
                // Add to the objective's progress, completing it if requirements were met
                incrementObjective(evt.getPlayer(), this, 1, quest);
            }
            else{
                player.chat(tar_reg);
            }
        }
    }
}