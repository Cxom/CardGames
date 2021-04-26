package net.punchtree.cardgames.freecell;

import java.util.ArrayList;
import java.util.List;

import net.punchtree.cardgames.StandardCard;

public class FreecellMain {

	List<List<StandardCard>> columns = new ArrayList<>();
	{
		for(int i = 0; i < 8; ++i) {
			columns.add(new ArrayList<>());
		}
	}
	
	public FreecellMain() {
		
	}
	
}
