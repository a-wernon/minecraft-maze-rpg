package artemwernon.fawe_t_plugin;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quests;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionsLeftEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import sun.java2d.pipe.Region;

import static artemwernon.fawe_t_plugin.QuestListener.failQuests;
import static artemwernon.fawe_t_plugin.fawe_t_plugin.*;
import static org.bukkit.Bukkit.getServer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.*;

public class RgTrigger implements Listener {
    private final fawe_t_plugin plugin;
    public RgTrigger(fawe_t_plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    int n32(int n){
        if(n>0){
            return n - n % 32;
        }
        else{
            return n - 32 - n%32;
        }
    }
    Quests qp = (Quests) getServer().getPluginManager().getPlugin("Quests");
    boolean hasActiveQuest(Player player){
        int c = 0;
        for (Quest quest : qp.getQuester(player.getUniqueId()).getCurrentQuests().keySet()){
            c++;
        }
        if (c > 0)
            return true;
        else{
            return false;
        }
    }

    int activeQuestLevel(Player player){
        int c = 0;
        for (Quest quest : qp.getQuester(player.getUniqueId()).getCurrentQuests().keySet()){
            c = quest.getRequirements().getQuestPoints();
        }
        return c;
    }

    void spawnMobs(Player player,ProtectedRegion region, String nam, World world) throws IOException {
        BlockVector3 minp = region.getMinimumPoint();
        String fnam_on = "D:\\schem_stor\\mobs_loc\\once\\" + nam;
        String fnam_reg = "D:\\schem_stor\\mobs_loc\\regular\\" + nam;
        int[][] mob_co_on = FileArrayProvider.read2d4lines(fnam_on);
        int[][] mob_co_reg = FileArrayProvider.read2d4lines(fnam_reg);

        /*for (int i = 0; i < mob_co.length; ++i){
            BlockVector3 loc = BlockVector3.at(mob_co[i][0] + minp.getX(),mob_co[i][1] + minp.getY(),mob_co[i][2] + minp.getZ());
            Location lloc = new Location(world,loc.getX(),loc.getY(),loc.getZ());
            getServer().getWorld("world").spawnEntity(lloc, EntityType.ZOMBIE);
        }*/
        int q_x = n32(minp.getBlockX());
        int q_y = 150;
        int q_z = n32(minp.getBlockZ());
        com.sk89q.worldedit.world.World fworld = FaweAPI.getWorld("world");
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(fworld);
        ProtectedRegion rg = regions.getRegion("reg_quar_" + q_x +"_150_" + q_z);
        System.out.println("reg_quar_" + q_x +"_150_" + q_z);
        if (hasActiveQuest(player)){
            int lv = activeQuestLevel(player);
            player.chat( lv +"");
            BukkitTask task = new MobSpawnTask(world, plugin, player, rg, mob_co_reg, new String[]{"LvlZombie", "LvlSkeleton"}, minp.getX(), minp.getY(), minp.getZ(), lv).runTaskTimer(plugin,0,150);
            BukkitTask task2 = new MobSpawnOnce(world, plugin, player, rg, mob_co_on, new String[]{"LvlZombie", "LvlSkeleton"},minp.getX(), minp.getY(), minp.getZ(), lv).runTaskLater(plugin, 10);
        }
        System.out.println("spawnmobs enter");
        System.out.println("" + mob_co_on.length);


        if (rg.getFlag(HAS_AC_QUESTS_FLAG)){

            player.playSound(player.getLocation(), "custom.examp",1,1);
        }
        //player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK,1,1);

    }

    void despawnMobs(ProtectedRegion region, Player player,World world){
        BlockVector3 minp = region.getMinimumPoint();
        Location lc = new Location(world,n32(minp.getX()),n32(minp.getY()),n32(minp.getZ()));
        Chunk ch = lc.getChunk();
        player.chat("" + (minp.getZ() - minp.getZ()%32));
        for (Entity entities : ch.getEntities()) {
            if (entities.getType().equals(EntityType.ZOMBIE)|| entities.getType().equals(EntityType.SKELETON)) {
                entities.remove();
            }
        }
        lc = new Location(world,n32(minp.getX()) + 16,n32(minp.getY()),n32(minp.getZ()));
        ch = lc.getChunk();
        player.chat("" + (minp.getZ() - minp.getZ()%32));
        for (Entity entities : ch.getEntities()) {
            if (entities.getType().equals(EntityType.ZOMBIE)|| entities.getType().equals(EntityType.SKELETON)) {
                entities.remove();
            }
        }
        lc = new Location(world,n32(minp.getX()),n32(minp.getY()),n32(minp.getZ()) + 16);
        ch = lc.getChunk();
        player.chat("" + (minp.getZ() - minp.getZ()%32));
        for (Entity entities : ch.getEntities()) {
            if (entities.getType().equals(EntityType.ZOMBIE) || entities.getType().equals(EntityType.SKELETON)) {
                entities.remove();
            }
        }
        lc = new Location(world,n32(minp.getX()) + 16,n32(minp.getY()),n32(minp.getZ()) + 16);
        ch = lc.getChunk();
        player.chat("" + (minp.getZ() - minp.getZ()%32));
        for (Entity entities : ch.getEntities()) {
            if (entities.getType().equals(EntityType.ZOMBIE)|| entities.getType().equals(EntityType.SKELETON)) {
                entities.remove();
            }
        }




    }

    org.bukkit.World world = Bukkit.getServer().getWorld("world");
    @EventHandler
    public void onRegionEntered(RegionEnteredEvent event)
    {
        Player player = Bukkit.getPlayer(event.getUUID());
        if (player == null) return;

        String rflag = event.getRegion().getFlag(REG_TYPE_FLAG);
        if(rflag.equals("mobroom"))
        {
            player.sendMessage("You entered fight room! Be careful!");
            getServer().getWorld("world").spawnEntity(player.getLocation(), EntityType.ZOMBIE);
            System.out.println("in");
            try {
                spawnMobs(player, event.getRegion(), event.getRegion().getFlag(CONTENT_TYPE_FLAG), world); //somewhere here error with null pointer
                System.out.println("in");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteSetBlocks(Player player){
        Vector<org.bukkit.block.Block> te = blocksPlaced.get(player);
        if (te != null) {
            for (int i = 0; i < te.size(); ++i) {
                //player.chat(i + "");
                te.get(i).setType(Material.AIR);
            }
            te.removeAllElements();
        }
    }

    @EventHandler
    public void onRegionsLeft(RegionsLeftEvent event) throws StorageException {
        Player player = Bukkit.getPlayer(event.getUUID());
        if(player == null){
            event.setCancelled(true);
        }
        Boolean qr = false;

        Set<ProtectedRegion> regionsNames = event.getRegions();
        Iterator it = regionsNames.iterator();
        while (it.hasNext()){
            Object elem = it.next();
            ProtectedRegion el = (ProtectedRegion) elem;
            String type = el.getFlag(REG_TYPE_FLAG);
            //player.chat(regionsNames.size() +" " + elem + " " + type);
            if (type.equals("quarter")){
                qr = true;
                break;
            }
            if (type.equals("mobroom")){
                if (hasActiveQuest(player)){
                    player.chat("Defeat Mobs first!");
                    event.setCancelled(true);
                }
                else{
                    despawnMobs((ProtectedRegion) elem, player, world);
                }
                break;
            }
        }

        if(qr){
            for (Quest quest : qp.getQuester(event.getPlayer().getUniqueId()).getCurrentQuests().keySet()) {
                //Player player = Bukkit.getPlayer(event.getUUID());
                quest.failQuest(qp.getQuester(event.getPlayer().getUniqueId()));
            }
            //failQuests(player);
            for (ProtectedRegion trg : event.getRegions()){
                trg.setFlag(HAS_AC_QUESTS_FLAG, false);
            }
            player.chat("you left the quarter");
            /*Vector<org.bukkit.block.Block> te = blocksPlaced.get(player);
            if (te != null) {
                for (int i = 0; i < te.size(); ++i) {
                    //player.chat(i + "");
                    te.get(i).setType(Material.AIR);
                }
            }*/
            deleteSetBlocks(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            com.sk89q.worldedit.world.World world = FaweAPI.getWorld("world");
            RegionManager regions = container.get(world);
            regions.saveChanges();
        }

        //player.sendMessage(String.valueOf(regionsNames.size()));
        //event.setCancelled(true);

    }

    @EventHandler
    public void onBlockSet(BlockPlaceEvent event){
        //System.out.println("blockplace");
        //blocksPlaced.put(event.getPlayer(), event.getBlock());
        //org.bukkit.block.Block te = event.getBlock();

        Vector<Block> temp = blocksPlaced.get(event.getPlayer());
        if (temp == null){
            //temp = new Vector<Block>();
            temp = new Vector<Block>();
            /*temp.set(0,event.getBlock());
            blocksPlaced.put(event.getPlayer(),temp);
            System.out.println("new");*/
        }

        temp.add(event.getBlock());

        blocksPlaced.put(event.getPlayer(), temp);

        //System.out.println(temp.size());
        //System.out.println();
    }



}
