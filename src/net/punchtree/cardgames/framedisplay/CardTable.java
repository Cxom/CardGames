package net.punchtree.cardgames.framedisplay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import net.punchtree.cardgames.CardGamesPlugin;
import net.punchtree.cardgames.display.CardSprites;

public class CardTable {
	
	public static int FRAME_SIZE = 128;
	
	// TODO one class(place) for loading resource images
	private static final String CARD_TABLE_TEXTURE_FILENAME = "/card-table-texture.png";
	private static final BufferedImage CARD_TABLE_TEXTURE;
	static {
		BufferedImage cardTableTexture = new BufferedImage(FRAME_SIZE, FRAME_SIZE, BufferedImage.TYPE_INT_ARGB);
		try {
			cardTableTexture = ImageIO.read(CardSprites.class.getResourceAsStream(CARD_TABLE_TEXTURE_FILENAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		CARD_TABLE_TEXTURE = cardTableTexture;
	}		
			
	private final int xDim;
	private final int zDim;
	private GameFrame[][] tableFrames; 
	
	private BufferedImage baseTableTexture;
	
	private BufferedImage image;
	private Graphics g;
	
	public CardTable(Block minCorner, int xDim, int zDim) {
		this.xDim = xDim;
		this.zDim = zDim;
		this.tableFrames = new GameFrame[xDim][zDim];
		this.baseTableTexture = new BufferedImage(FRAME_SIZE * xDim, FRAME_SIZE * zDim, BufferedImage.TYPE_INT_ARGB);
		Graphics baseTableTextureG = baseTableTexture.getGraphics();
		this.image = new BufferedImage(FRAME_SIZE * xDim, FRAME_SIZE * zDim, BufferedImage.TYPE_INT_ARGB);
		this.g = image.getGraphics();
		
		HitDetection hitDetection = HitDetection.getInstance();
		
		for (int x = 0; x < xDim; ++x) {
			for (int z = 0; z < zDim; ++z) {
				Location loc = minCorner.getLocation().clone().add(x, 1, z);
				ItemFrame frame = spawnItemFrame(loc);
				tableFrames[x][z] = new GameFrame(this, frame, x, z);
				hitDetection.registerTableFrame(frame, tableFrames[x][z]);
				
				baseTableTextureG.drawImage(CARD_TABLE_TEXTURE, x * FRAME_SIZE, z * FRAME_SIZE, null);
			}
		}
		// Add border to base tableTexture;
		baseTableTextureG.setColor(new Color(0, 66, 0));
		baseTableTextureG.fillRect(0, 0, 4, zDim * FRAME_SIZE);
		baseTableTextureG.fillRect(0, 0, xDim * FRAME_SIZE, 4);
		baseTableTextureG.fillRect(xDim * FRAME_SIZE - 4, 0, 4, zDim * FRAME_SIZE);
		baseTableTextureG.fillRect(0, zDim * FRAME_SIZE - 4, xDim * FRAME_SIZE, 4);
		baseTableTextureG.dispose();
		render();
	}

	private ItemFrame spawnItemFrame(Location spawnLocation) {
		ItemFrame frame = spawnLocation.getWorld().spawn(spawnLocation, ItemFrame.class, itemFrame -> {
			itemFrame.setFacingDirection(BlockFace.UP, true);
			// Since we're drawing an opaque background, this just moves the plane down to be parallel to the block instead of 1/16th up
			itemFrame.setVisible(false);
		});
		return frame;
	}
	
	private void render() {
		g.drawImage(baseTableTexture, 0, 0, null);
		for (int x = 0; x < xDim; ++x) {
			for (int z = 0; z < zDim; ++z) {
				tableFrames[x][z].render(image.getSubimage(x * FRAME_SIZE, z * FRAME_SIZE, FRAME_SIZE, FRAME_SIZE));
			}
		}
		for (int x = 0; x < xDim; ++x) {
			for (int z = 0; z < zDim; ++z) {
				tableFrames[x][z].render(image.getSubimage(x * FRAME_SIZE, z * FRAME_SIZE, FRAME_SIZE, FRAME_SIZE));
			}
		}
	}

	public void hit(Player player, int x, int z, CardInteractionType type) {
		Bukkit.broadcastMessage(String.format("Player %s interacted-%s with the card table at %d, %d", player.getName(), type.name(), x, z));
	}
	
	public void remove() {
		Arrays.stream(tableFrames).forEach(row -> Arrays.stream(row).forEach(GameFrame::remove));
	}
	
}
