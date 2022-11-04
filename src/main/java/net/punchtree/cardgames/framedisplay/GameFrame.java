package net.punchtree.cardgames.framedisplay;

import net.punchtree.cardgames.prototyping.CustomMapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class GameFrame {

	public static Player VIEWER;
	
	private final CardTable table;
	
	private final ItemFrame frame;
	
	private final int xIndex;
	private final int zIndex;
	
	public GameFrame(CardTable table, ItemFrame frame, int xIndex, int zIndex) {
		this.table = table;
		this.frame = frame;
		this.xIndex = xIndex;
		this.zIndex = zIndex;
	}
	
	public void render(BufferedImage image) {
		ItemStack mapItem = new ItemStack(Material.FILLED_MAP, 1);
		MapView mapView = Bukkit.createMap(frame.getWorld());

		mapView.removeRenderer(mapView.getRenderers().get(0));

		CustomMapRenderer mapRenderer = new CustomMapRenderer(image);
		mapView.addRenderer(mapRenderer);

		mapItem.editMeta(mapMeta -> ((MapMeta) mapMeta).setMapView(mapView));

		frame.setItem(mapItem);

//		VIEWER = Bukkit.getPlayer("Cxom");
//		MapWrapper mapWrapper = mapManager.wrapImage(image);
//		MapController mapController = mapWrapper.getController();
//		mapController.addViewer(VIEWER);
//		mapController.sendContent(VIEWER);
//		mapController.showInFrame(VIEWER, frame, true);
//		mapController.showInHand(VIEWER, true);
	}

	/**
	 *  x and z are coordinates within THIS frame
	 * @param player
	 * @param x
	 * @param z
	 * @param type
	 */
	public void hit(Player player, int x, int z, CardInteractionType type) {
		table.hit(player, 
					x + xIndex * CardTable.FRAME_SIZE,
					z + zIndex * CardTable.FRAME_SIZE,
					type);
	}
	
	public void remove() {
		frame.remove();
	}
	
}
