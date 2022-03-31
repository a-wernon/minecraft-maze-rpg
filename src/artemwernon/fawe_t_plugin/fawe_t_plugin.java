package artemwernon.fawe_t_plugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionsLeftEvent;
import org.apache.logging.log4j.core.LifeCycle;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

//import static artemwernon.fawe_t_plugin.PlayerMMORewards.getDropChances;


public class fawe_t_plugin extends JavaPlugin{
    public PlayerMMORewards MMORew;
    public static StringFlag REG_TYPE_FLAG;
    public static StringFlag CONTENT_TYPE_FLAG;
    public static BooleanFlag HAS_AC_QUESTS_FLAG;
    public static Map<Player, Vector<Block>> blocksPlaced = new HashMap<>();
    public Listener QueL;
    public Listener ShopL;
    public Listener CitL;
    public ShopInventory ShopInv;
    public DivSpellCast DivSpCast;
    private HpTracker HpTrack;
    public InterfaceUpdate InterfaceUpdate;
    public BowManager BowMang;

    public static String getQuarType(int x, int z) throws FileNotFoundException {
        int n_x = x / 32;
        int n_z = z / 32;
        String nam = "D:\\schem_stor\\in_rooms\\" + "room_out_in_quar_" + n_x + "_" + n_z + ".txt";
        Scanner sc = new Scanner(new BufferedReader(new FileReader(nam)));
        return sc.nextLine();
    }

    @Override
    public void onLoad() {
        // ... do your own plugin things, etc

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StringFlag flag = new StringFlag("regtype", "none");
            StringFlag flag2 = new StringFlag("roomctype", "none");
            BooleanFlag flag3 = new BooleanFlag("activequest");
            registry.register(flag);
            registry.register(flag2);
            registry.register(flag3);

            HAS_AC_QUESTS_FLAG = flag3;
            REG_TYPE_FLAG = flag; // only set our field if there was no error
            CONTENT_TYPE_FLAG = flag2; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("my-custom-flag");
            if (existing instanceof StateFlag) {
                REG_TYPE_FLAG = (StringFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
    }

    @Override
    public void onEnable() {
        //this.getCommand("bld").setExecutor(new ExeBuilds());
        this.getCommand("bldreg").setExecutor(new BuildLab());
        //this.getCommand("qgen").setExecutor(new qgen());
        //this.getCommand("blcl").setExecutor(new ComClear());
        DivSpCast = new DivSpellCast(this);
        ShopInv = new ShopInventory();
        this.getCommand("quarbuild").setExecutor(new BuildFullQuarter());
        this.getCommand("questbasegen").setExecutor(new WGRegBuild());
        this.getCommand("questgen").setExecutor(new QuestGen());
        this.getCommand("hlep").setExecutor(new TestHelp());
        this.getCommand("shop").setExecutor(ShopInv);
        this.getCommand("cast").setExecutor(DivSpCast);

        //getServer().getPluginManager().registerEvents(new RgTrigger(), this);
        new RgTrigger(this);
        QueL = new QuestListener();
        ShopL = new ShopListener();
        CitL = new CitizensListener();
        HpTrack = new HpTracker();
        InterfaceUpdate = new InterfaceUpdate(this);
        BowMang = new BowManager(this);

        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(StringTrait.class).withName("merchant"));
        getServer().getPluginManager().registerEvents(QueL, this);
        //getServer().getPluginManager().registerEvents(new TestHelpLis(), this);
        getServer().getPluginManager().registerEvents(ShopL, this);
        getServer().getPluginManager().registerEvents(CitL, this);
        getServer().getPluginManager().registerEvents(HpTrack, this);
        getServer().getPluginManager().registerEvents(InterfaceUpdate, this);
        getServer().getPluginManager().registerEvents(BowMang, this);
        MMORew = new PlayerMMORewards();
        try {
            MMORew.getDropChances();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        BukkitTask task = InterfaceUpdate.runTaskTimer(this,0,5); //period 20 for like 1 second

    }

    @Override
    public void onDisable(){

    }


}
