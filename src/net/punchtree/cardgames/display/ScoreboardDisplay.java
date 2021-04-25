package net.punchtree.cardgames.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardDisplay {

	public static final int MAX_SCOREBOARD_LENGTH = 40;
	
	private String title;
	private Set<UUID> players = new HashSet<>();
	
	private final Map<Integer, String> activeRows = new HashMap<>();
	
	public final Scoreboard scoreboard;
	public Objective sidebar;
	
	public ScoreboardDisplay(String title) {
		this.title = title;
		
		scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		sidebar = scoreboard.registerNewObjective("scoreboard", "dummy", title);
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public void addPlayer(Player player){
		players.add(player.getUniqueId());
		player.setScoreboard(scoreboard);
	}
	
	public void removePlayer(Player player){
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		players.remove(player.getUniqueId());
	}
	
	public void removeAll(){
		for (UUID uuid : players){
			Bukkit.getPlayer(uuid).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}
		players.clear();
	}
	
	public void showMessage(String[] messageRows) {
		for (int i = 0; i < messageRows.length; ++i) {
			setRow(messageRows.length + 1 - i, messageRows[i]);
		}
	}
	
	public void showMessage(List<String> messageRows) {
		for (int i = 0; i < messageRows.size(); ++i) {
			setRow(messageRows.size() + 1 - i, messageRows.get(i));
		}
	}
	
	public void setRow(int i, String message) {
		resetRow(i);
		activeRows.put(i, message);
		sidebar.getScore(message).setScore(i);
	}

	public void resetRow(int i) {
		if (activeRows.containsKey(i)) {
			scoreboard.resetScores(activeRows.get(i));
			activeRows.remove(i);
		}
	}
	
	// TODO make rows limited here;
	public int showWrappedMessage(String message) {
		List<String> rows = new ArrayList<>();
		while (message.length() > MAX_SCOREBOARD_LENGTH) {
			int spaceIndex = message.substring(0, 40).lastIndexOf(" ");
			if (spaceIndex == -1) {
				rows.add(message.substring(0, MAX_SCOREBOARD_LENGTH - 1).concat("-"));
				message = message.substring(MAX_SCOREBOARD_LENGTH - 1);
			} else {
				rows.add(message.substring(0, spaceIndex));
				message = message.substring(spaceIndex + 1);
			}
		}
		rows.add(message);
		for (int i = 0; i < rows.size(); ++i) {
			Score score = sidebar.getScore(rows.get(i));
			activeRows.put(rows.size() + 1 - i, rows.get(i));
			score.setScore(rows.size() + 1 - i);
		}
		return rows.size();
	}
	
	public void clear() {
		for (String row : activeRows.values()) {
			scoreboard.resetScores(row);	
			activeRows.remove(row);
		}
	}
	
}
