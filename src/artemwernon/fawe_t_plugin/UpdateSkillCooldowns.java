package artemwernon.fawe_t_plugin;

import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.skill.Skill;
import net.minecraft.server.v1_15_R1.IPlayerFileData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateSkillCooldowns extends BukkitRunnable {
    private final PlayerData playerData;
    private final String ready;
    private final String onCooldown;
    private final String noMana;
    private final String split;
    private int j = 0;

    UpdateSkillCooldowns(PlayerData playerData, String ready, String onCooldown, String noMana, String Split){
        this.playerData = playerData;
        this.ready = ready;
        this.onCooldown = onCooldown;
        this.noMana = noMana;
        this.split = Split;
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


    @Override
    public void run(){
        if(j > 100){
            this.cancel();
        }
        if (!this.playerData.isOnline() || this.playerData.getPlayer().isDead()) {
            this.cancel();
        }

        if (this.j % 20 == 0) {
            this.playerData.displayActionBar(this.getFormat(this.playerData));
            //this.cancel();
        }


        for(int var1 = 0; var1 < 2; ++var1) {
            double var2 = (double)(this.j++) / 5.0D;
            this.playerData.getProfess().getCastParticle().display(this.playerData.getPlayer().getLocation().add(Math.cos(var2), 1.0D + Math.sin(var2 / 3.0D) / 1.3D, Math.sin(var2)));
        }
    }
}
