package net.punchtree.cardgames.framedisplay;

import java.awt.image.BufferedImage;

import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.inventivetalent.mapmanager.MapManagerPlugin;
import org.inventivetalent.mapmanager.controller.MapController;
import org.inventivetalent.mapmanager.manager.MapManager;
import org.inventivetalent.mapmanager.wrapper.MapWrapper;

public class GameFrame {

	public static Player VIEWER;
	
	private final CardTable table;
	
	private final ItemFrame frame;
	
	private final int xIndex;
	private final int zIndex;
	
	private static final MapManager mapManager = ((MapManagerPlugin) Bukkit.getPluginManager().getPlugin("MapManager")).getMapManager();
	
	public GameFrame(CardTable table, ItemFrame frame, int xIndex, int zIndex) {
		this.table = table;
		this.frame = frame;
		this.xIndex = xIndex;
		this.zIndex = zIndex;
	}
	
	public void render(BufferedImage image) {
		VIEWER = Bukkit.getPlayer("Cxom");
		MapWrapper mapWrapper = mapManager.wrapImage(image);
		MapController mapController = mapWrapper.getController();
		mapController.addViewer(VIEWER);
		mapController.sendContent(VIEWER);
		mapController.showInFrame(VIEWER, frame, true);
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
