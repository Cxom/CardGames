package net.punchtree.cardgames.prototyping;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.inventivetalent.mapmanager.MapManagerPlugin;
import org.inventivetalent.mapmanager.controller.MapController;
import org.inventivetalent.mapmanager.manager.MapManager;
import org.inventivetalent.mapmanager.wrapper.MapWrapper;

import net.punchtree.cardgames.StandardCard;
import net.punchtree.cardgames.StandardDeck;
import net.punchtree.cardgames.display.CardSprites;

public class CardTableTest implements CommandExecutor {

	private static MapManager mapManager = ((MapManagerPlugin) Bukkit.getPluginManager().getPlugin("MapManager")).getMapManager();
	
	private static int IMAGE_SIZE = 128;
	
	private static BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
	private static Graphics g = image.getGraphics();
	
	private static Image resetBackground() {
	    g.setColor(new Color(0, 0, 0, 0));
	    g.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
	    
	    return image;
	}
	
	public static void createCardTableAndSpawnSomeCards(Player viewer) {
		
		Location corner = viewer.getLocation();
		World world = corner.getWorld();
		
		ItemFrame frame = world.spawn(corner, ItemFrame.class, itemFrame -> {
			itemFrame.setFacingDirection(BlockFace.UP, true);
			itemFrame.setVisible(false);
		});
		
		resetBackground();
		StandardDeck deck = StandardDeck.getNewDeck().shuffle();
		int baseOffset = 5;
		for ( int i = 0; i < 7; ++i ) {
			StandardCard card = deck.get(i);
			int xOffset = 9 * i * 2;
			g.drawImage(card.getSprite(), baseOffset + xOffset, baseOffset, CardSprites.CARD_WIDTH * 2, CardSprites.CARD_HEIGHT * 2, null);
		}
		baseOffset += CardSprites.CARD_HEIGHT * 2 + 1;
		for ( int i = 0; i < 7; ++i ) {
			int xOffset = 9 * i;
			g.drawImage(CardSprites.getCardBacking(), baseOffset + xOffset, baseOffset, null);
		}
		
		MapWrapper mapWrapper = mapManager.wrapImage(image);
		MapController mapController = mapWrapper.getController();
		mapController.addViewer(viewer);
		mapController.sendContent(viewer);
		mapController.showInFrame(viewer, frame, true);
//		mapController.showInHand(viewer, true);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ( ! ( sender instanceof Player )) {
			return true;
		}
		
		Player player = (Player) sender;
		createCardTableAndSpawnSomeCards(player);
		
		return true;
	}
	
}
