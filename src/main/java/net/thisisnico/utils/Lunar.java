package net.thisisnico.utils;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketModSettings;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTitle;
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;
import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.lunarclient.bukkitapi.serverrule.LunarClientAPIServerRule;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Lunar {
    public static LunarClientAPI Client;

    private static final ModSettings.ModSetting modDisabled = new ModSettings.ModSetting(false, new HashMap<>());
    private static final ModSettings.ModSetting modEnabled = new ModSettings.ModSetting(true, new HashMap<>());

    private static final ModSettings mods = new ModSettings()
            .addModSetting("one_seven_visuals", modDisabled)
            .addModSetting("hypixel_mod", modDisabled)
            .addModSetting("uhc_overlay", modDisabled)
            .addModSetting("time_changer", modDisabled)
            .addModSetting("particleMultiplierMod", modDisabled)
            .addModSetting("scoreboard", modEnabled)
            .addModSetting("cooldowns", modEnabled);

    public static boolean isRunning(Player p) {
        return Client.isRunningLunarClient(p);
    }

    public static void sendMods(Player p) {
        Client.sendPacket(p, new LCPacketModSettings(mods));
    }

    private static void setupRules() {
        LunarClientAPIServerRule.setRule(ServerRule.SERVER_HANDLES_WAYPOINTS, false);
        LunarClientAPIServerRule.setRule(ServerRule.VOICE_ENABLED, true);
        LunarClientAPIServerRule.setRule(ServerRule.MINIMAP_STATUS, false);
    }

    public static void subtitle(Player p, String text, float scale, long fadeIn, long stay, long fadeOut) {
        Client.sendPacket(p, new LCPacketTitle("subtitle", text, scale, fadeIn*1000L, stay*1000L, fadeOut*1000L));
    }

    public static void sendRules(Player p) {
        LunarClientAPIServerRule.sendServerRule(p);
    }

    public static void enable(LunarClientAPI lcapi) {
        Client = lcapi;
        setupRules();
    }
    
    public static Set<Player> getOnline() {
        return Client.getPlayersRunningLunarClient();
    }

    public static LCWaypoint getWaypoint(String name, Location loc, Color color) {
        return new LCWaypoint(name, loc, color.asRGB(), true, true);
    }

    private static final Map<Player, Set<LCWaypoint>> waypoints = new HashMap<>();

    public static void sendWaypoint(Player p, LCWaypoint waypoint) {
        Client.sendWaypoint(p, waypoint);
        if (!waypoints.containsKey(p)) waypoints.put(p, new HashSet<>());
        Set<LCWaypoint> list = waypoints.get(p);
        list.add(waypoint);
        waypoints.replace(p, list);
    }

    public static void removeWaypoint(Player p, String name) {
        if (!waypoints.containsKey(p)) return;
        Set<LCWaypoint> list = waypoints.get(p);
        LCWaypoint w = null;
        for (LCWaypoint lcWaypoint : list) {
            if (lcWaypoint.getName().equalsIgnoreCase(name)) {
                w = lcWaypoint;
                break;
            }
        }
        if (w == null) return;
        list.remove(w);
        waypoints.replace(p, list);
        Client.removeWaypoint(p, w);
    }

    public static void removeAllWaypoints(Player p) {
        if (waypoints.containsKey(p))
            waypoints.get(p).forEach(w -> Client.removeWaypoint(p, w));
        waypoints.remove(p);
    }
}
