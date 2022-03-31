package artemwernon.fawe_t_plugin;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;

import static artemwernon.fawe_t_plugin.fawe_t_plugin.HAS_AC_QUESTS_FLAG;
import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.Bukkit.getServer;

public class MobSpawnTask extends BukkitRunnable {
    private final JavaPlugin plugin;

    private ProtectedRegion region;
    private int[][] MobCoords;
    private Player player;
    private World world;
    private int sx;
    private int sy;
    private int sz;
    private int level;
    private String[] mobs;

    public MobSpawnTask(World world,JavaPlugin plugin,Player player ,ProtectedRegion region, int[][] mbcord, String[] mobs,int sx, int sy, int sz, int level) {
        this.plugin = plugin;
        this.region = region;
        this.MobCoords = mbcord;
        this.player = player;
        this.world = world;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.level = level;
        this.mobs = mobs;
    }
    /*@Override
    public void run() {
        // What you want to schedule goes here
        plugin.getServer().broadcastMessage("Welcome to Bukkit! Remember to read the documentation!");
    }*/

    @Override
    public void run() {
        // What you want to schedule goes here
        //System.out.println("runnning");
        //System.out.println(this.region.getFlag(HAS_AC_QUESTS_FLAG));
        //System.out.println("runnning2");
        if (this.region.getFlag(HAS_AC_QUESTS_FLAG)){
            //select random place in mpbcoords and spawn zomb here
            //System.out.println("runned");
            //player.chat("boo");
            Random r = new Random();
            int ra = r.nextInt(MobCoords.length);
            int[] te = MobCoords[ra];
            //Location lloc = new Location(world,te[0],te[1],te[2]);
            //Location lloc = new Location(world,te[0] + sx,te[1] + sy,te[2] + sz);
            int tex = te[0] + sx;
            int tey = te[1] + sy;
            int tez = te[2] + sz;
            int tet = te[3];
            Bukkit.getServer().dispatchCommand(getConsoleSender(),"mm mobs spawn " + mobs[tet] + ":"+ this.level + " 1 world," + tex + "," + tey + "," + tez);
            //world.spawnEntity(lloc, EntityType.ZOMBIE);
        }
        else{
            this.cancel();
        }
    }
}
