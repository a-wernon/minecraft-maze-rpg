package artemwernon.fawe_t_plugin;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import me.blackvein.quests.events.quest.QuestEvent;
import me.blackvein.quests.events.quest.QuestQuitEvent;
import me.blackvein.quests.events.quest.QuestTakeEvent;
import me.blackvein.quests.events.quester.QuesterPostCompleteQuestEvent;
import me.blackvein.quests.events.quester.QuesterPostStartQuestEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static artemwernon.fawe_t_plugin.RgTrigger.deleteSetBlocks;
import static artemwernon.fawe_t_plugin.fawe_t_plugin.HAS_AC_QUESTS_FLAG;
import static artemwernon.fawe_t_plugin.fawe_t_plugin.blocksPlaced;
import static org.bukkit.Bukkit.getServer;

public class QuestListener implements Listener {
    Quests qp = (Quests) getServer().getPluginManager().getPlugin("Quests");
    public static void failQuests(Player player){
        World world = FaweAPI.getWorld("world");
        Location loc = player.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);
        ApplicableRegionSet set = regions.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
        for (ProtectedRegion each : set) {
            each.setFlag(HAS_AC_QUESTS_FLAG, false);
        }
    }

    int g32(int t){
        if (t < 0){
            return (t - t%32) - 32;
        }
        else{
            return t - t%32;
        }
    }

    public ProtectedRegion getQuarter(Player player){
        Location loc = player.getLocation();
        int q_x = g32(loc.getBlockX());
        int q_y = 150;
        int q_z = g32(loc.getBlockZ());
        World world = FaweAPI.getWorld("world");
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);
        ProtectedRegion rg = regions.getRegion("reg_quar_" + q_x +"_150_" + q_z);
        return rg;

    }

    @EventHandler
    public void questTake(QuesterPostStartQuestEvent event) throws StorageException {
        System.out.println("heeey3");
        Player player = event.getQuester().getPlayer();
        player.setBedSpawnLocation(player.getLocation(), true);
        int q = 0;
        for (Quest quest : event.getQuester().getCurrentQuests().keySet()) {
            //Player player = Bukkit.getPlayer(event.getUUID());
            ++q;
        }
        player.chat("active quests: " + q);
        if (q > 0){
            ProtectedRegion rg = getQuarter(player);
            rg.setFlag(HAS_AC_QUESTS_FLAG, true);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            World world = FaweAPI.getWorld("world");
            RegionManager regions = container.get(world);
            regions.saveChanges();
        }
        //player.chat("heeey3");
    }

    @EventHandler
    public void questComp(QuesterPostCompleteQuestEvent event) throws StorageException{
        Player player = event.getQuester().getPlayer();
        player.chat("eventquestcomp");
        ProtectedRegion rg = getQuarter(player);
        player.chat(rg.getMinimumPoint().getX() + "");
        player.chat(rg.getMinimumPoint().getY() + "");
        player.chat(rg.getMinimumPoint().getZ() + "");
        rg.setFlag(HAS_AC_QUESTS_FLAG, false);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = FaweAPI.getWorld("world");
        RegionManager regions = container.get(world);
        regions.saveChanges();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) throws StorageException {
        Player player = event.getPlayer();
        System.out.println("player left, quest deleted");
        int q = 0;
        for (Quest quest : qp.getQuester(event.getPlayer().getUniqueId()).getCurrentQuests().keySet()) {
            //Player player = Bukkit.getPlayer(event.getUUID());
            quest.failQuest(qp.getQuester(event.getPlayer().getUniqueId()));
            ++q;
        }
        if (q >0){
            ProtectedRegion rg = getQuarter(player);
            rg.setFlag(HAS_AC_QUESTS_FLAG, false);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            World world = FaweAPI.getWorld("world");
            RegionManager regions = container.get(world);
            regions.saveChanges();
        }
        deleteSetBlocks(player);

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) throws StorageException {
        Player player = event.getEntity().getPlayer();
        System.out.println("player left, quest deleted");
        int q = 0;
        for (Quest quest : qp.getQuester(event.getEntity().getPlayer().getUniqueId()).getCurrentQuests().keySet()) {
            //Player player = Bukkit.getPlayer(event.getUUID());
            quest.failQuest(qp.getQuester(event.getEntity().getPlayer().getUniqueId()));
            ++q;
        }
        player.chat("active quests: " + q);
        if (q > 0){
            ProtectedRegion rg = getQuarter(player);
            rg.setFlag(HAS_AC_QUESTS_FLAG, false);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            World world = FaweAPI.getWorld("world");
            RegionManager regions = container.get(world);
            regions.saveChanges();
        }

        //deleteSetBlocks(player);
    }

    @EventHandler
    public void onPlayerLvlUp(PlayerLevelChangeEvent event)  {
        Player player = event.getPlayer();
        int old = event.getOldLevel();
        int neww = event.getNewLevel();
        int delt = neww - old;
        System.out.println(delt + "");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"questadmin givepoints " + player.getName() + " " + delt);
        System.out.println("questadmin givepoint" + player.getName() + " " + delt);
    }
}
