package net.punchtree.cardgames.framedisplay;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class HitDetection implements Listener {

//	private final String GAME_FRAME_METADATA_KEY = "CARD_GAME_TABLE_FRAME";
	
	private static Map<ItemFrame, GameFrame> gameFrameMap = new WeakHashMap<>();
	
	// We use a singleton just to prevent registering the event twice (we only want one after all)
	private HitDetection(){}
	private static final HitDetection instance = new HitDetection();
	public static HitDetection getInstance() { return instance; }

	public void registerTableFrame(ItemFrame frame, GameFrame gameFrame) {
		gameFrameMap.put(frame, gameFrame);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) return;
	}
	
//	@EventHandler
//	public void onMapInteract(MapInteractEvent event) {
//		ItemFrame frame = event.getItemFrame();
//		Player player = event.getPlayer();
////		Bukkit.broadcastMessage(gameFrameMap.size() + " card table frames registered!");
//
//		GameFrame gameFrame = gameFrameMap.get(frame);
//		if (gameFrame == null) return;
//
//		Point pointOnMap = getMapPointPlayerIsLookingAt(player);
//		gameFrame.hit(player, pointOnMap.x, pointOnMap.y, CardInteractionType.CLICK);
//	}

	@EventHandler
	public void onEntityInteractEvent(HangingBreakByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.ITEM_FRAME) return;
		ItemFrame frame = (ItemFrame) event.getEntity();
		if (gameFrameMap.containsKey(frame)) {
			event.setCancelled(true);
			Point p = getMapPointPlayerIsLookingAt((Player) event.getRemover());
			Bukkit.broadcastMessage("Cancelled breaking a card table frame! (" + p.getX() + ", " + p.getY() + ")");
		}
	}
	
//	private boolean isGameFrame(ItemFrame frame) {
//		return frameMap.containsKey(frame.getUniqueId());
//	}
	
	private static final Set<Material> TRANSPARENT = Arrays.asList(Material.values()).stream().filter(Material::isTransparent).collect(Collectors.toSet());
	
	private Point getMapPointPlayerIsLookingAt(Player player) {
		Block sec = null;
		Block fin = null;
		for(Block b : player.getLineOfSight(TRANSPARENT, 1000)) {
			if (b.getType() == Material.AIR) {
				sec = b;
				continue;
			}
			fin = b;
		}
		if ( sec == null ) {
			throw new IllegalStateException("sec is null!!!");
		} else if ( fin == null ) {
			throw new IllegalStateException("fin is null!!!");
		} else if ( !fin.getType().isSolid() ) {
			throw new IllegalStateException("fin is not solid!!!");
		}
		Location secL = sec.getLocation();
		Location finL = fin.getLocation();
		Location eyeLoc = player.getEyeLocation();
		Vector eyeDir = eyeLoc.getDirection();
		
		World world = secL.getWorld();
		BlockFace facingDirection = null;
		
		char planeaxis = '?';
		int plane = Integer.MIN_VALUE;
		
		if ( secL.getBlockX() > finL.getBlockX() ) {
			planeaxis = 'x';
			plane = Math.max(secL.getBlockX(), finL.getBlockX() );
			facingDirection = BlockFace.EAST;
		} else if ( secL.getBlockX() < finL.getBlockX() ) {
			planeaxis = 'x';
			plane = Math.max(secL.getBlockX(), finL.getBlockX() );
			facingDirection = BlockFace.WEST;
		}
		if ( secL.getBlockY() > finL.getBlockY() ) {
			planeaxis = 'y';
			plane = Math.max(secL.getBlockY(), finL.getBlockY() );
			facingDirection = BlockFace.UP;
		} else if ( secL.getBlockY() < finL.getBlockY() ) {
			planeaxis = 'y';
			plane = Math.max(secL.getBlockY(), finL.getBlockY() );
			facingDirection = BlockFace.DOWN;
		}
		if ( secL.getBlockZ() > finL.getBlockZ() ) {
			planeaxis = 'z';
			plane = Math.max(secL.getBlockZ(), finL.getBlockZ() );
			facingDirection = BlockFace.SOUTH;
		} else if ( secL.getBlockZ() < finL.getBlockZ() ) {
			planeaxis = 'z';
			plane = Math.max(secL.getBlockZ(), finL.getBlockZ() );
			facingDirection = BlockFace.NORTH;
		}
		final BlockFace facingDirectionFinal = facingDirection;
		
		double eyeLocPlaneComp = planeaxis == 'x' ? eyeLoc.getX() : planeaxis == 'y' ? eyeLoc.getY() : eyeLoc.getZ();
		double eyeDirPlaneComp = planeaxis == 'x' ? eyeDir.getX() : planeaxis == 'y' ? eyeDir.getY() : eyeDir.getZ();
		
		double planeEyeLocDiff = Math.abs(plane - eyeLocPlaneComp);
		double planeSteps = planeEyeLocDiff / eyeDirPlaneComp;
	
		Location intersection = eyeLoc.clone().add(eyeDir.clone().multiply(Math.abs(planeSteps)));
		
		double r = 0;
		double d = 0;
		
		// TODO I might be able to replace the dense directional component (Dot product) checks with just explicitly checking the facingDirection
		// Less mathematically rigorous but easier to read?
		
		Location diffs = intersection.clone().subtract(intersection.getBlock().getLocation());
		if ( planeaxis == 'x' ) {
			// Testing the x component is like dot producting against EAST
			if ( eyeDir.getX() < 0 ) {
				diffs.setZ(1d - diffs.getZ());
			}
			r = diffs.getZ();
			d = 1 - diffs.getY();
		}
		if ( planeaxis == 'y' ) {
			r = diffs.getX();
			d = diffs.getZ();
			if (facingDirectionFinal == BlockFace.DOWN) {
				// Why does this fix it/ is necessary?
				d = 1 - d;
			}
		}
		if ( planeaxis == 'z' ) {
			// Testing the z component is like dot producting against SOUTH
			if ( eyeDir.getZ() > 0 ) {
				diffs.setX(1d - diffs.getX());
			}
			r = diffs.getX();
			d = 1 - diffs.getY();
		}
		
		int x = (int) Math.round(r * 128);
		int y = (int) Math.round(d * 128);
		
		return new Point(x, y);
	}
	
}
