package net.thisisnico.utils;

import net.thisisnico.API;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerRunner extends BukkitRunnable {

    private final Player player;

    public PlayerRunner(Player sender) {
        player = sender;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }
        execute(player);
    }

    @NotNull
    public synchronized BukkitTask runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskLater(API.getInstance(), delay);
    }

    @NotNull
    public synchronized BukkitTask runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskTimer(API.getInstance(), delay, period);
    }

    public abstract void execute(Player player);

    public boolean isRunning() {
        return player.isOnline() && !isCancelled();
    }

    @Override
    public void cancel() {
        if (isRunning())
            super.cancel();
    }

}
