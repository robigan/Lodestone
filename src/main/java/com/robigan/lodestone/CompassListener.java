package com.robigan.lodestone;

import de.tr7zw.nbtapi.NBTCompound;
import org.bukkit.*;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

//import java.util.UUID;

public class CompassListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void Compass(final @NotNull PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final ItemStack i = p.getInventory().getItemInMainHand();
        if (e.getHand() == EquipmentSlot.HAND && i.getType() == Material.COMPASS && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            final NBTItem nbti = new NBTItem(i);
            if (nbti.hasKey("LodestonePos")) {
                p.sendMessage(ChatColor.BLUE + "Whoosh!");
                final NBTCompound pos = nbti.getCompound("LodestonePos");
                final NamespacedKey world = NamespacedKey.fromString(nbti.getString("LodestoneDimension"));
                final Location loc = new Location(Bukkit.getServer().getWorld(world), pos.getInteger("X"), pos.getInteger("Y") + 2, pos.getInteger("Z"));
                p.teleport(loc);
                e.setCancelled(true);
            }
        }
        /*if (p.getUniqueId().equals(UUID.fromString("678d4e909e464d65b4e1703a468349ac"))) {
            p.sendMessage(ChatColor.RED + "GAY DETECTED!");
        }*/
    }
}
