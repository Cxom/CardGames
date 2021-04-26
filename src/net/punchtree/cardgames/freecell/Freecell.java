package net.punchtree.cardgames.freecell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.punchtree.cardgames.CardGame;
import net.punchtree.cardgames.StandardCard;

public class Freecell implements CardGame {
	
	Player player;
	List<List<StandardCard>> columns = new ArrayList<>();
	{
		for(int i = 0; i < 8; ++i) {
			columns.add(new ArrayList<>());
		}
	}
	
	@Override
	public void onPlayerQuit(Player player) {
		shutdown();
	}

	@Override
	public void shutdown() {
		CardGame.deregisterPlayers(player);
	}

}
