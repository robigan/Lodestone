package com.robigan.lodestone;

import de.tr7zw.nbtapi.NBTCompound;
import me.NoChance.PvPManager.PvPManager;
import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
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

public class CompassListener implements Listener {
    private static final String chatPrefix = ChatColor.YELLOW + "[" + ChatColor.DARK_GRAY + "Lodestone" + ChatColor.YELLOW + "]" + ChatColor.RESET + " ";
    private static final String pvpChatPrefix = ChatColor.YELLOW + "[" + ChatColor.DARK_GRAY + "PvPManager" + ChatColor.YELLOW + "]" + ChatColor.RESET + " ";
    private boolean isPvP = false;

    public CompassListener() {
        final PvPManager pvpInstance = Lodestone.getPvpInstance();
        if (pvpInstance != null) {
            isPvP = true;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void Compass(final @NotNull PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (event.getHand() == EquipmentSlot.HAND && itemStack.getType() == Material.COMPASS && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            if (isPvP) {
                @NotNull final PvPlayer pvPlayer = PvPlayer.get(player);

                if (pvPlayer.isInCombat()) {
                    player.sendMessage(pvpChatPrefix + ChatColor.RED + "Lodestone teleporting is not allowed while in combat!");
                    event.setCancelled(true);
                    return;
                }
            }

            final NBTItem nbti = new NBTItem(itemStack);

            if (nbti.hasKey("LodestonePos")) {
                final NBTCompound pos = nbti.getCompound("LodestonePos");

                final NamespacedKey worldNamespace = NamespacedKey.fromString(nbti.getString("LodestoneDimension"));
                if (worldNamespace == null) {
                    Bukkit.getLogger().warning("The field LodestoneDimension cannot be converted to a valid NamespacedKey (is null) in compass for player " + player.getName());
                    Bukkit.getLogger().warning("The LodestoneDimension value returned by NBTAPI is " + nbti.getString("LodestoneDimension"));
                    Bukkit.getLogger().warning("Lodestone teleport aborted due to error");
                    player.sendMessage(chatPrefix + ChatColor.RED + "There was an error processing the data in your Lodestone teleport");
                    return;
                }

                final World world = Bukkit.getServer().getWorld(worldNamespace);
                if (world == null) {
                    Bukkit.getLogger().warning("The field LodestoneDimension is not an actual world (is null) in compass for player " + player.getName());
                    Bukkit.getLogger().warning("The LodestoneDimension value returned by NBTAPI is " + nbti.getString("LodestoneDimension"));
                    Bukkit.getLogger().warning("Lodestone teleport aborted due to error");
                    player.sendMessage(chatPrefix + ChatColor.RED + "There was an error processing the data in your Lodestone teleport");
                    return;
                }

                player.sendMessage(chatPrefix + ChatColor.BLUE + "Whoosh!");
                final Location loc = new Location(world, pos.getInteger("X"), pos.getInteger("Y") + 2, pos.getInteger("Z"));
                player.teleport(loc);
                event.setCancelled(true);
            }
        }
    }
}
