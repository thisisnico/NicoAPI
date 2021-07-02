package net.thisisnico.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Collections;
import java.util.List;

public abstract class AbstractInventory {

    public AbstractInventory(Player player) {
        generate(player);
        player.openInventory(inv);
    }

    Inventory inv = null;

    /**
     * Called one time in <b>generate()</b> function. Sets the size of inventory/
     * @param i Size
     */
    protected void setSize(int i) {
        if (inv != null) return;
        inv = Bukkit.createInventory(null, i);
    }

    protected ItemStack item(Material material, String name) {
        return item(material, 1, name, "");
    }
    protected ItemStack item(Material material, int count, String name) {
        return item(material, count, name, "");
    }
    protected ItemStack item(Material material, String name, String... lore) {
        return item(material, 1, name, lore);
    }
    protected ItemStack item(Material material, int count, String name, String... lore) {
        ItemStack is = new ItemStack(material, count);
        is.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (!lore[0].isEmpty()) {
            ItemMeta meta = is.getItemMeta();
            List<Component> nl = new java.util.ArrayList<>(Collections.emptyList());
            for (String s: lore) {
                nl.add(Component.text(ChatColor.translateAlternateColorCodes('&', s)));
            }
            meta.lore(nl);
            is.setItemMeta(meta);
        }
        return is;
    }

    protected void add(ItemStack is) {
        if (inv == null) return;
        inv.addItem(is);
    }
    protected void add(int slot, ItemStack is) {
        if (inv == null) return;
        inv.setItem(slot, is);
    }

    public abstract void generate(Player player);

}
