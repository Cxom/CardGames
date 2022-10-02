package net.punchtree.cardgames.prototyping;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class VanillaMapTesting implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if ( ! (sender instanceof Player player)) return false;

        ItemStack mapItem = new ItemStack(Material.FILLED_MAP, 1);
        MapView mapView = Bukkit.createMap(player.getWorld());

        mapView.removeRenderer(mapView.getRenderers().get(0));
        mapView.addRenderer(new CustomMapRenderer());

        mapItem.editMeta(mapMeta -> ((MapMeta) mapMeta).setMapView(mapView));

        player.getInventory().addItem(mapItem);

        return true;
    }
}
