package net.thisisnico.events;

import net.thisisnico.API;
import net.thisisnico.utils.PlayerScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BasicHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (API.getInstance().getConfig().getBoolean("server.hide-stream"))
            e.joinMessage(null);

        PlayerScoreboard.register(e.getPlayer())
            .setLine(9, API.getConf().getString("server.ip", "Your IP!"));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (API.getInstance().getConfig().getBoolean("server.hide-stream"))
            e.quitMessage(null);

        PlayerScoreboard.get(e.getPlayer()).unregister();
    }

}
