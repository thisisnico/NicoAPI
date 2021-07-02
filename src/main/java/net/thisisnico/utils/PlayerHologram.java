package net.thisisnico.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.thisisnico.API;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerHologram {

    public static PlayerHologram create(Player player, Location loc) {
        return new PlayerHologram(player, loc);
    }

    private final Hologram holo;
    private final Player player;

    private PlayerHologram(Player p, Location loc) {
        holo = HologramsAPI.createHologram(API.getInstance(), loc);
        VisibilityManager vis = holo.getVisibilityManager();
        player = p;
        vis.showTo(p);
        vis.setVisibleByDefault(false);
    }

    public void setLine(int n, String text) {
        if (holo.getLine(n) != null)
            holo.getLine(n).removeLine();

        holo.insertTextLine(n,
                ChatColor.translateAlternateColorCodes('&',
                        PlaceholderAPI.setPlaceholders((OfflinePlayer) player, text)));
    }

    public void setLine(int n, Material item) {
        if (holo.getLine(n) != null)
            holo.getLine(n).removeLine();

        holo.insertItemLine(n, new ItemStack(item));
    }

    public void closeButton(String text) {
        holo.appendTextLine(ChatColor.translateAlternateColorCodes('&',
                PlaceholderAPI.setPlaceholders((OfflinePlayer) player, text))).setTouchHandler(p -> {
                    holo.delete();
        });
    }

    public Hologram getHologram() {
        return holo;
    }
}
