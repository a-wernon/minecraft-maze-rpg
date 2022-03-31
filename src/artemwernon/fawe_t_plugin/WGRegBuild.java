package artemwernon.fawe_t_plugin;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import javafx.util.Pair;
import me.blackvein.quests.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.trait.Gravity;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.BlockVector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import static artemwernon.fawe_t_plugin.fawe_t_plugin.CONTENT_TYPE_FLAG;
import static artemwernon.fawe_t_plugin.fawe_t_plugin.REG_TYPE_FLAG;

public class WGRegBuild implements CommandExecutor {

    int g32(int t){
        if (t < 0){
            return (t - t%32) - 32;
        }
        else{
            return t - t%32;
        }
    }


    public static int npid(String name, Player player){
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        int id = -1;
        for(NPC npc : registry){
            player.chat(npc.getName());
            if (npc.getName().equals(name)){
                return npc.getId();
            }
            if (id != -1){
                break;
            }
        }
        return id;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        org.bukkit.entity.Player player = (Player) sender;
        World world = FaweAPI.getWorld("world");
        BlockVector3 min = BlockVector3.at( Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        BlockVector3 max = BlockVector3.at( Integer.parseInt(args[0]) + 31, Integer.parseInt(args[1]) + 31, Integer.parseInt(args[2]) + 31);
        Integer[] s_co = {Integer.parseInt(args[0])/32, Integer.parseInt(args[2])/32};
        player.chat("quarter lowest point " + args[0] + " " + args[1] + " " + args[2]);
        String q_filename = "D:\\schem_stor\\quest_info\\quest_" + s_co[0].toString() + "_" + s_co[1].toString() + ".txt";

        ProtectedRegion region = new ProtectedCuboidRegion("reg_quar_" + args[0] + "_" +args[1] + "_" + args[2], min, max);
        region.setFlag(Flags.PASSTHROUGH, StateFlag.State.ALLOW);
        region.setFlag(Flags.GREET_MESSAGE, region.getId());
        region.setFlag(REG_TYPE_FLAG, "quarter");




        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);
        regions.addRegion(region);
        int[][] roomcoords = {};
        player.chat(q_filename);
        String qtype = "";
        String[] type = {};
        try {
            qtype = fawe_t_plugin.getQuarType(min.getX(), min.getZ());
            player.chat(qtype + "qtype");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Pair<int[][], String[]> tempp;
            tempp = FileArrayProvider.readquestlines(q_filename, player);
            roomcoords = tempp.getKey();
            type = tempp.getValue();
            player.chat("hjk" + roomcoords.length);
        } catch (IOException e) {
            player.chat("hueta");
            e.printStackTrace();

        }
        Integer r_q = 1;
        for (int i = 0; i < roomcoords.length; ++i){
            //player.chat(i + "");

            BlockVector3 tmin = min.add(roomcoords[i][0], roomcoords[i][1], roomcoords[i][2]);
            BlockVector3 tmax = min.add(roomcoords[i][3], roomcoords[i][4], roomcoords[i][5]);
            String nam = "room_" + tmin.getX() + "_" + tmin.getY() + "_" + tmin.getZ();
            //String te = (cas4(roomcoords[i][6]) + "" + cas3(roomcoords[i][7]) + "" + cas4(roomcoords[i][8]));
            //int id = Integer.parseInt(te);
            ProtectedRegion tr = new ProtectedCuboidRegion(nam, tmin, tmax);
            tr.setFlag(Flags.PASSTHROUGH, StateFlag.State.ALLOW);
            tr.setFlag(Flags.GREET_MESSAGE, tr.getId());
            tr.setFlag(REG_TYPE_FLAG, qtype + "room");
            tr.setFlag(CONTENT_TYPE_FLAG, type[i]);
            regions.addRegion(tr);
            r_q += 1;
            if (qtype.equals("delivery") || qtype.equals("hub")){
                player.chat("npc");

                NPCRegistry registry = CitizensAPI.getNPCRegistry();
                int idd = npid(nam, player);
                NPC npc;
                player.chat("" + idd);
                if (idd != -1){
                    npc = registry.getById(idd);
                }
                else{
                    npc = registry.createNPC(EntityType.VILLAGER, nam);
                    player.chat("newnpc");
                }

                org.bukkit.World bworld = Bukkit.getServer().getWorld("world");
                Location loc = new Location(bworld, min.getX() + roomcoords[i][6], min.getY() + roomcoords[i][7], min.getZ() + roomcoords[i][8]);
                //player.chat( "" + roomcoords[i][6] + roomcoords[i][7] + roomcoords[i][8]);
                //Location loc = new Location(bworld, 16,170,6);
                npc.setFlyable(true);
                npc.setProtected(true);
                if (qtype.equals("hub")){
                    StringTrait str_te = new StringTrait();
                    npc.addTrait(str_te);
                }
                LookClose lc = new LookClose();
                lc.setRandomLook(true);
                lc.lookClose(true);
                lc.setRealisticLooking(true);
                npc.addTrait(lc);
                Gravity gr = new Gravity();
                gr.gravitate(true);
                npc.addTrait(gr);
                npc.spawn(loc);
                npc.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);

            }
            //NPCManager.register(<name of the NPC>, <the NPC location>, <name of the player who owns the NPC>, NPCCreateReason reason) ; // for the last one, check the API which reasons are available.
        }

        if (!qtype.equals("delivery")) {
            String nam = "announ_" + min.getX() + "_" + min.getY() + "_" + min.getZ();
            NPCRegistry registry = CitizensAPI.getNPCRegistry();
            int idd = npid(nam, player);
            NPC npc;
            player.chat("" + idd);
            if (idd != -1) {
                npc = registry.getById(idd);
            } else {
                npc = registry.createNPC(EntityType.VILLAGER, nam);
                player.chat("newnpc");
            }

            org.bukkit.World bworld = Bukkit.getServer().getWorld("world");
            Random r = new Random();
            int ra1 = r.nextInt(1);
            int ra2 = r.nextInt(1);
            Location loc = new Location(bworld, min.getX() + 29 + ra1, min.getY(), min.getZ() + 29 + ra2);
            npc.spawn(loc);
            //npc.setFlyable(true);
            //npc.setProtected(true);
            npc.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            int npcid = npc.getId();
            Text te = new Text();
            te.add("Hello, stranger!");
            te.add("What do you want from me?");
            npc.addTrait(te);
            LookClose lc = new LookClose();
            lc.setRandomLook(true);
            lc.lookClose(true);
            lc.setRealisticLooking(true);
            npc.addTrait(lc);
            Gravity gr = new Gravity();
            gr.gravitate(true);
            npc.addTrait(gr);
            //npc.getTrait(LookClose.class).setRandomLook(true);
        }

        try {
            regions.saveChanges();
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return true;
    }
}
