package net.thisisnico.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.thisisnico.API;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class PlayerScoreboard {

    private static final Map<Player, PlayerScoreboard> everything = new HashMap<>();

    public static PlayerScoreboard get(Player p) {
        return everything.get(p);
    }

    public static PlayerScoreboard register(Player p) {
        return new PlayerScoreboard(p);
    }

    private final Scoreboard sb = API.getInstance().getServer().getScoreboardManager().getNewScoreboard();
    private final Objective sidebar;
    private final Team team;
    private final Player player;

    private final Map<Integer, String> lines = new HashMap<>();

    private PlayerScoreboard(Player p) {
        player = p;
        p.setScoreboard(sb);

        String sname = "sb_"+p.getName();
        if (sname.length() > 16) sname = sname.substring(0, 16);
        sidebar = sb.registerNewObjective(sname, "dummy",
                Component.text(API.getConf().getString("server.name", "ServerName here")));

        String tname = "t_"+p.getUniqueId();
        if (tname.length() > 16) tname = tname.substring(0, 16);
        team = sb.registerNewTeam(tname);
        team.addEntry(p.getName());

        everything.put(p, this);
    }

    /**
     * Hides player sidebar.
     */
    public void hideSidebar() {
        if (sidebar.getDisplaySlot() == DisplaySlot.SIDEBAR)
            sidebar.setDisplaySlot(null);
    }

    /**
     * Shows player sidebar.
     */
    public void showSidebar() {
        if (sidebar.getDisplaySlot() != DisplaySlot.SIDEBAR)
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Changes sidebar line
     * @param line Line number
     * @param component New text on line. (removes line if null)
     */
    public void setLine(int line, String component) {
        String prev = lines.get(line);
        if (prev != null) {
            if (prev.equals(PlaceholderAPI.setPlaceholders(player, component))) return;
            lines.remove(line);
            sb.resetScores(prev);
        }
        if (component != null) {
            sidebar.getScore(PlaceholderAPI.setPlaceholders(player, component)).setScore(line*-1);
            lines.put(line, PlaceholderAPI.setPlaceholders(player, component));
        }
    }

    /**
     * Changes player prefix
     * @param prefix New prefix
     */
    public void setPrefix(String prefix) {
        if (!team.getPrefix().equals(prefix)) {
            team.setPrefix(prefix);
            updateTeams();
        }
    }

    public ChatColor getColor() { return team.getColor(); }

    /**
     * Changes player nickname color
     * @param color New color
     */
    public void setColor(ChatColor color) {
        if (team.getColor() != color) {
            team.setColor(color);
            updateTeams();
        }
    }

    /**
     * Unregisters player scoreboard
     */
    public void unregister() {
        sidebar.unregister();
        everything.forEach((p, s) -> s.unregisterTeam(team));
        sb.getTeams().forEach(Team::unregister);
        player.setScoreboard(Bukkit.getServer().getScoreboardManager().getMainScoreboard());
        everything.remove(player);
    }

    /**
     * Only for internal use.
     * @deprecated
     */
    public void updateTeams() {
        everything.forEach((p, s) -> s.updateTeam(team, player));
    }

    /**
     * Only for internal use.
     * @deprecated
     */
    public void updateTeam(Team upd, Player p) {
        String teamName = upd.getName();
        Team reg = sb.getTeam(teamName);
        if (reg == null) reg = sb.registerNewTeam(teamName);

        reg.prefix(upd.prefix());
        reg.setColor(upd.getColor());
        reg.addEntry(p.getName());
    }

    /**
     * Only for internal use.
     * @deprecated
     */
    public void unregisterTeam(Team name) {
        if (name == team) return;
        Team reg = sb.getTeam(name.getName());
        if (reg == null) return;
        reg.unregister();
    }

}
