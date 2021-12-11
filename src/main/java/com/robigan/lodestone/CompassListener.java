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

import java.util.Objects;

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
                final NamespacedKey worldNamespace = NamespacedKey.fromString(nbti.getString("LodestoneDimension"));
                if (Objects.isNull(worldNamespace)) {
                    Bukkit.getLogger().warning("The field LodestoneDimension cannot be converted to a valid NamespacedKey (is null) in compass for player " + p.getName());
                    Bukkit.getLogger().warning("The LodestoneDimension value returned by NBTAPI is " + nbti.getString("LodestoneDimension"));
                    Bukkit.getLogger().warning("Lodestone teleport aborted due to error");
                    p.sendMessage("There was an error processing the data in your Lodestone teleport");
                    return;
                }
                final World world = Bukkit.getServer().getWorld(worldNamespace);
                if (Objects.isNull(world)) {
                    Bukkit.getLogger().warning("The field LodestoneDimension is not an actual world (is null) in compass for player " + p.getName());
                    Bukkit.getLogger().warning("The LodestoneDimension value returned by NBTAPI is " + nbti.getString("LodestoneDimension"));
                    Bukkit.getLogger().warning("Lodestone teleport aborted due to error");
                    p.sendMessage("There was an error processing the data in your Lodestone teleport");
                    return;
                }
                final Location loc = new Location(world, pos.getInteger("X"), pos.getInteger("Y") + 2, pos.getInteger("Z"));
                p.teleport(loc);
                e.setCancelled(true);
            }
        }
    }
}
