package net.thisisnico.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerToggle {

    private final List<Player> toggle = new ArrayList<>();

    public boolean is(Player player) {
        return toggle.contains(player);
    }

    public boolean toggle(Player player) {
        if (!is(player))
            setEnabled(player);
        else
            setDisabled(player);
        return is(player);
    }

    public void enable(Player player) { setEnabled(player); }
    public void disable(Player player) { setDisabled(player); }

    private void setEnabled(Player player) {
        if (is(player)) return;

        toggle.add(player);
    }

    private void setDisabled(Player player) {
        if (!is(player)) return;

        toggle.remove(player);
    }

}
