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
            HandItemRightClick annotation = method.getDeclaredAnnotation(HandItemRightClick.class);
            if (annotation == null) continue;

            itemRightClickEvent.put(annotation, method);
            classes.put(method, asd);
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
        if (e.getHand() == EquipmentSlot.HAND
                && e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
//            if (item.getType() != Material.AIR) return;
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
    }

    @EventHandler
    private void onInvClick(org.bukkit.event.inventory.InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (e.isLeftClick()) {
            invLeftClickEvent.forEach((event, method) -> {
                if (e.getInventory().getHolder() != e.getWhoClicked().getInventory().getHolder()
                        && item.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', event.name()))
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
        if (e.isRightClick()) {
            invRightClickEvent.forEach((event, method) -> {
                if (e.getInventory().getHolder() != e.getWhoClicked().getInventory().getHolder()
                        && item.getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', event.name()))
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

}
