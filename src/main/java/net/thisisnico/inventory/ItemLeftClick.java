package net.thisisnico.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface ItemLeftClick {

    Material material();
    String name();

}
