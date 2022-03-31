package artemwernon.fawe_t_plugin;

import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import javax.swing.*;
import java.util.Vector;

import static java.lang.Double.max;

public class WeaponManager implements Listener{
    public BossBar stamina;
    public Vector<Double> cooldowns;
    public Vector<Double> empcooldowns = new Vector<Double>();
    public Vector<Double> crits;
    public Vector<Double> empcrits = new Vector<Double>();
    public int currenthit;
    public int combocapacity;
    private fawe_t_plugin fawe_t_plugin;
    private Vector<Double> basiccd = new Vector<Double>();
    private Vector<Double> basiccr = new Vector<Double>();

    WeaponManager(Player player, fawe_t_plugin fawe_t_plugin){
        initBossbar(player);
        basiccd.add(10.0);
        basiccr.add(0.0);
        this.fawe_t_plugin = fawe_t_plugin;
        cooldowns = basiccd;
        crits = basiccr;
    }

    public void tick(){
        //updateBB(0);
        //updateBB(max(stamina.getProgress() - 1.0/10.0,0));
        updateBB(max(stamina.getProgress() - 1.0/cooldowns.get(currenthit),0));
    }

    private void initBossbar(Player player){
        stamina =  Bukkit.getServer().createBossBar("Stamina", BarColor.YELLOW, BarStyle.SOLID);
        //stamina.put(player,  new Pair <BossBar, DivDouble> (plasta, new DivDouble(10.0)));
        stamina.setVisible(true);
        stamina.addPlayer(player);
    }

    public void loadWeapon(ItemStack is){
        cooldowns = empcooldowns;
        cooldowns.clear();
        crits = empcrits;
        crits.clear();
        net.minecraft.server.v1_15_R1.ItemStack nmste = CraftItemStack.asNMSCopy(is);
        NBTTagCompound nmstecompound = (nmste.hasTag()) ? nmste.getTag() : new NBTTagCompound();
        combocapacity = getIntNBT(nmstecompound,"combo", 1);
        for (int i = 0; i < combocapacity; ++i){
            cooldowns.add(getDoubleNBT(nmstecompound, "stamina" + i, 10.0));
            System.out.println(cooldowns);
            crits.add(getDoubleNBT(nmstecompound,"crit" + i, 0));
        }
    }

    public double hit(Player player){
        if (cooldowns == basiccd){
            loadWeapon(player.getInventory().getItemInMainHand());
            player.chat("loading cds");
        }
        double crit = crits.get(currenthit);
        BukkitTask task = new DelayedUpdater(this).runTaskLaterAsynchronously(fawe_t_plugin, 1);
        //updateBB(1); //delay here
        ++currenthit;
        currenthit %= combocapacity;
        return crit;
    }

    public void updateBB(double value){
        stamina.setProgress(value);
    }

    public void clear(){
        cooldowns = basiccd;
        crits = basiccr;
        currenthit = 0;
        combocapacity = 1;
    }

    public boolean canHit(){
        return stamina.getProgress() <= 0.001;
    }

    private int getIntNBT (NBTTagCompound nmstecompound, String tag,int defaultvalue){
        //net.minecraft.server.v1_15_R1.ItemStack nmste = CraftItemStack.asNMSCopy(is);
        //NBTTagCompound nmstecompound = (nmste.hasTag()) ? nmste.getTag() : new NBTTagCompound();
        String cooldown = nmstecompound.getString(tag);
        //player.chat(cooldown);
        if (cooldown.isEmpty()){
            System.out.println("Basic cd time used");
            return defaultvalue;
        }
        else{
            return Integer.parseInt(cooldown);
        }
    }

    private double getDoubleNBT (NBTTagCompound nmstecompound, String tag, double defaultvalue){
        //net.minecraft.server.v1_15_R1.ItemStack nmste = CraftItemStack.asNMSCopy(is);
        //NBTTagCompound nmstecompound = (nmste.hasTag()) ? nmste.getTag() : new NBTTagCompound();
        String cooldown = nmstecompound.getString(tag);
        //player.chat(cooldown);
        if (cooldown.isEmpty()){
            System.out.println("Basic cd time used");
            return defaultvalue;
        }
        else{
            return Double.parseDouble(cooldown);
        }
    }


}
