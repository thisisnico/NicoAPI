package net.thisisnico.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.thisisnico.API;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandAlias("napi")
public class BasicCommand extends BaseCommand {

    @Subcommand("reload")
    @CommandPermission("napi.admin")
    public static void reloadCmd(Player p) {
        API.getInstance().reloadConfig();
        p.sendActionBar(Component.text("§d§lNicoAPI §f| §aConfig reloaded."));
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100f, 2f);
    }

    @Subcommand("servername")
    @CommandPermission("napi.admin")
    public static void serverNameCommand(Player p, @Optional String newName) {
        if (newName == null)
            p.sendMessage("§d§lNicoAPI §f| §eCurrent server name: §r"+API.getInstance().getConfig().getString("server.name", "null"));
        else {
            API.getInstance().getConfig().set("server.name", ChatColor.translateAlternateColorCodes('&', newName));
            p.sendMessage("§d§lNicoAPI §f| §eNew name set!");
            API.getInstance().saveConfig();
        }
    }

    @Subcommand("ip")
    @CommandPermission("napi.admin")
    public static void serverIpCommand(Player p, @Optional String newName) {
        if (newName == null)
            p.sendMessage("§d§lNicoAPI §f| §eCurrent server IP: §r"+API.getInstance().getConfig().getString("server.ip", "null"));
        else {
            API.getInstance().getConfig().set("server.ip", ChatColor.translateAlternateColorCodes('&', newName));
            p.sendMessage("§d§lNicoAPI §f| §eNew IP set!");
            API.getInstance().saveConfig();
        }
    }

    @Subcommand("stream")
    @CommandPermission("napi.admin")
    public static void streamCommand(Player p) {
        API.getInstance().getConfig().set("server.hide-stream", !API.getInstance().getConfig().getBoolean("server.hide-stream"));
        p.sendMessage("§d§lNicoAPI §f| §eStream: "
                + (API.getInstance().getConfig().getBoolean("server.hide-stream") ? "§cfalse" : "§atrue"));
        API.getInstance().saveConfig();
    }

}
