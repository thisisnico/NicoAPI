package net.thisisnico.inventory;

import net.thisisnico.API;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InventoryEvents implements Listener {
    private final Map<HandItemRightClick, Method> itemRightClickEvent = new HashMap<>();
    private final Map<HandItemLeftClick, Method> itemLeftClickEvent = new HashMap<>();
    private final Map<ItemLeftClick, Method> invLeftClickEvent = new HashMap<>();
    private final Map<ItemRightClick, Method> invRightClickEvent = new HashMap<>();

    private final Map<Method, Object> classes = new HashMap<>();

    /**
     * Registers Hand click events from class.
     * @param asd Class to register.
     */
    public void register(HandClickEvent asd) {
        Class<? extends HandClickEvent> c = asd.getClass();
        Method[] methods = c.getMethods();

        for (Method method : methods) {
            HandItemRightClick right = method.getDeclaredAnnotation(HandItemRightClick.class);
            HandItemLeftClick left = method.getDeclaredAnnotation(HandItemLeftClick.class);

            if (right != null) {
                itemRightClickEvent.put(right, method);
                classes.put(method, asd);
            }
            if (left != null) {
                itemLeftClickEvent.put(left, method);
                classes.put(method, asd);
            }
        }
    }

    /**
     * Registers Inventory click events from class.
     * @param asd Class to register.
     */
    public void register(InventoryClickEvent asd) {
        Class<? extends InventoryClickEvent> c = asd.getClass();
        Method[] methods = c.getMethods();

        for (Method method : methods) {
            ItemLeftClick left = method.getDeclaredAnnotation(ItemLeftClick.class);
            ItemRightClick right = method.getDeclaredAnnotation(ItemRightClick.class);
            if (left != null) {
                invLeftClickEvent.put(left, method);
                classes.put(method, asd);
            }
            if (right != null) {
                invRightClickEvent.put(right, method);
                classes.put(method, asd);
            }
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR || item.getItemMeta() == null) return;
        if (e.getHand() == EquipmentSlot.HAND
                && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            itemRightClickEvent.forEach((event, method) -> {
                if (item.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', event.name()))
                        && item.getType() == event.material()) {
                    try {
                        e.setCancelled(true);
                        method.invoke(classes.get(method), e);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        if (e.getHand() == EquipmentSlot.HAND
                && (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR)) {
            itemLeftClickEvent.forEach((event, method) -> {
                if (item.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', event.name()))
                        && item.getType() == event.material()) {
                    try {
                        e.setCancelled(true);
                        method.invoke(classes.get(method), e);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    @EventHandler
    private void onInvClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!(e.getClickedInventory().getHolder() instanceof AbstractInventory)) return;
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        e.setCancelled(true);
        if (e.isLeftClick()) {
            invLeftClickEvent.forEach((event, method) -> {
                if (item.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', event.name()))
                        && item.getType() == event.material()) {
                    try {
                        method.invoke(classes.get(method), e);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        if (e.isRightClick()) {
            invRightClickEvent.forEach((event, method) -> {
                if (item.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', event.name()))
                        && item.getType() == event.material()) {
                    try {
                        method.invoke(classes.get(method), e);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

}
