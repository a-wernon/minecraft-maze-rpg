package artemwernon.fawe_t_plugin;

import org.bukkit.scheduler.BukkitRunnable;

public class DelayedUpdater extends BukkitRunnable {

    WeaponManager wm;
    DelayedUpdater(WeaponManager wm){
        this.wm = wm;
    }
    @Override
    public void run() {
        wm.updateBB(1);
        this.cancel();
    }
}
