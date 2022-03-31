package artemwernon.fawe_t_plugin;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.skill.Skill;
import net.Indyuce.mmocore.listener.SpellCast;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DivSpellCast extends SpellCast implements CommandExecutor{
    fawe_t_plugin plugin;
    DivSpellCast(fawe_t_plugin plugin){
        this.plugin = plugin;
    }
    /*@Override
    @EventHandler
    public void a(PlayerSwapHandItemsEvent var1) {
        Player var2 = var1.getPlayer();
        if (var2.getGameMode() != GameMode.CREATIVE && var2.getGameMode() != GameMode.SPECTATOR) {
            PlayerData var3 = PlayerData.get(var2);
            var1.setCancelled(true);
            if (var2.isSneaking() && MMOCore.plugin.configManager.hotbarSwap) { //hotbar swap feature
                var2.playSound(var2.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);

                for(int var4 = 0; var4 < 9; ++var4) {
                    ItemStack var5 = var2.getInventory().getItem(var4 + 27);
                    var2.getInventory().setItem(var4 + 27, var2.getInventory().getItem(var4));
                    var2.getInventory().setItem(var4, var5);
                }

            } else {
                if (!var3.isCasting() && var3.getBoundSkills().size() > 0) {
                    var3.skillCasting = new SpellCast.SkillCasting(var3);
                    var2.playSound(var2.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 2.0F);
                }

            }
        }
    }*/

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;
        if (args.length != 1){
            player.chat("Incorrect command usage");
            return false;
        }
        int skillNum = Integer.parseInt(args[0]);

        PlayerData pdata = PlayerData.get(player);
        if (!pdata.isCasting() && pdata.getBoundSkills().size() > 0) {
            pdata.skillCasting = new DivSpellCast.DivSkillCasting(pdata);
            player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 2.0F);
            pdata.skillCasting.onSkillCast(new PlayerItemHeldEvent(player,0,skillNum));
        }
        return true;
    }


    public class DivSkillCasting extends SkillCasting {
        private final PlayerData playerData;
        private final String ready;
        private final String onCooldown;
        private final String noMana;
        private final String split;
        private int j;
        public DivSkillCasting(PlayerData playerData) {
            super(playerData);
            super.cancel();
            this.ready = MMOCore.plugin.configManager.getSimpleMessage("casting.action-bar.ready", new String[0]).message();
            this.onCooldown = MMOCore.plugin.configManager.getSimpleMessage("casting.action-bar.on-cooldown", new String[0]).message();
            this.noMana = MMOCore.plugin.configManager.getSimpleMessage("casting.action-bar.no-mana", new String[0]).message();
            this.split = MMOCore.plugin.configManager.getSimpleMessage("casting.split", new String[0]).message();
            this.playerData = playerData;
            System.out.println(ready);
            //this.runTaskTimer(Bukkit.getPluginManager().getPlugin("fawe_t_plugin"), 0L, 1L);
            BukkitTask task = new UpdateSkillCooldowns(playerData,ready,onCooldown,noMana,split).runTaskTimer(plugin,0L, 1L);
            HandlerList.unregisterAll(this);
        }

        @Override
        public void onSkillCast(PlayerItemHeldEvent evt){

            Player player = playerData.getPlayer();
            int var3 = evt.getNewSlot();
            if (var3 >= 0 && this.playerData.hasSkillBound(var3)) {
                System.out.println("da");
                this.playerData.cast(this.playerData.getBoundSkill(var3));
            }
            System.out.println("da2");
            stopCasting();
        }

        /*@EventHandler(
                ignoreCancelled = false
        )
        public void onSkillCast(PlayerItemHeldEvent var1) {
            Player var2 = var1.getPlayer();
            if (var1.getPlayer().equals(this.playerData.getPlayer())) {
                if (var1.getPreviousSlot() != var1.getNewSlot()) {
                    var1.setCancelled(true);
                    int var3 = var1.getNewSlot() + (var1.getNewSlot() >= var2.getInventory().getHeldItemSlot() ? -1 : 0);
                    if (var3 >= 0 && this.playerData.hasSkillBound(var3)) {
                        this.playerData.cast(this.playerData.getBoundSkill(var3));
                    }

                }
            }
        }*/

        public void stopCasting(){
            System.out.println("da3");
            playerData.getPlayer().playSound(playerData.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 2.0F);
            this.close();
        }

        /*@EventHandler
        public void stopCasting(PlayerSwapHandItemsEvent var1) {
            Player var2 = var1.getPlayer();
            if (var1.getPlayer().equals(this.playerData.getPlayer()) && !var2.isSneaking()) {
                var2.playSound(var2.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0F, 2.0F);
                MMOCore.plugin.configManager.getSimpleMessage("casting.no-longer", new String[0]).send(this.playerData.getPlayer());
                this.close();
            }

        }*/

        private void close() {
            this.playerData.skillCasting = null;
            System.out.println("da4");
            this.playerData.displayActionBar(this.getFormat(this.playerData));
            //this.cancel();
        }

        private String getFormat(PlayerData var1) {
            String var2 = "";

            for(int var3 = 0; var3 < var1.getBoundSkills().size(); ++var3) {
                Skill.SkillInfo var4 = var1.getBoundSkill(var3);
                var2 = var2 + (var2.isEmpty() ? "" : this.split) + (this.onCooldown(var1, var4) ? this.onCooldown.replace("{cooldown}", "" + var1.getSkillData().getCooldown(var4) / 1000L) : (this.noMana(var1, var4) ? this.noMana : this.ready)).replace("{index}", "" + (var3 + 1 + (var1.getPlayer().getInventory().getHeldItemSlot() <= var3 ? 1 : 0))).replace("{skill}", var1.getBoundSkill(var3).getSkill().getName());
            }

            return var2;
        }

        private boolean onCooldown(PlayerData var1, Skill.SkillInfo var2) {
            return var2.getSkill().hasModifier("cooldown") && var1.getSkillData().getCooldown(var2) > 0L;
        }

        private boolean noMana(PlayerData var1, Skill.SkillInfo var2) {
            return var2.getSkill().hasModifier("mana") && var2.getModifier("mana", var1.getSkillLevel(var2.getSkill())) > var1.getMana();
        }

        public void run() {
            if (!this.playerData.isOnline() || this.playerData.getPlayer().isDead()) {
                this.close();
            }

            if (this.j % 20 == 0) {
                this.playerData.displayActionBar(this.getFormat(this.playerData));
                this.close();
            }

            for(int var1 = 0; var1 < 2; ++var1) {
                double var2 = (double)(this.j++) / 5.0D;
                this.playerData.getProfess().getCastParticle().display(this.playerData.getPlayer().getLocation().add(Math.cos(var2), 1.0D + Math.sin(var2 / 3.0D) / 1.3D, Math.sin(var2)));
            }

        }
    }
}
