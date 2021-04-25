package net.punchtree.cardgames.freecell;

import org.bukkit.entity.Player;

import net.punchtree.cardgames.CardGame;

public class Freecell implements CardGame {

	List<Card>[] columns = new ArrayList<Card>[7];
	
	@Override
	public void onPlayerQuit(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
