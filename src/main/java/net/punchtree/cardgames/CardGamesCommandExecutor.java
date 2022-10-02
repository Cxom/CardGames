package net.punchtree.cardgames;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.punchtree.cardgames.blackjack.BlackJackGame;
import net.punchtree.cardgames.connect1.Connect1Game;
import net.punchtree.cardgames.connect4.Connect4Game;
import net.punchtree.cardgames.display.ScoreboardTesting;

public class CardGamesCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (! (sender instanceof Player)) {
			sender.sendMessage(CardGamesPlugin.CHAT_PREFIX_ERROR + "Must be a player to run this command!");
			return true;
		}
		
		if (args.length < 2) {
			sender.sendMessage(CardGamesPlugin.CHAT_PREFIX_ERROR + "/cg <game> <players>");
			return true;
		}
		
		String game = args[0];
		
		Set<Player> players = new LinkedHashSet<>();
		for (int i = 1; i < args.length; ++i) {
			Player p = Bukkit.getPlayer(args[i]);
			if (p != null) {
				players.add(p);
			} else {
				sender.sendMessage(CardGamesPlugin.CHAT_PREFIX_ERROR + "Couldn't find player '" + args[i] + "'.");
			}
		}
		
		if ("blackjack".equalsIgnoreCase(game) && players.size() >= 2) {
			CardGamesPlugin.getInstance().addGame(players, new BlackJackGame(players));
		} else if ("sbt".equalsIgnoreCase(game)) {
			if (players.size() >= 1) {
				CardGamesPlugin.getInstance().addGame(players, new ScoreboardTesting(players.iterator().next()));
			}
		} else if ("connect4".equalsIgnoreCase(game) && players.size() >= 2) {
			Iterator<Player> it = players.iterator();
			Connect4Game connect4 = new Connect4Game(it.next(), it.next());
			it.forEachRemaining(connect4::addSpectator);
			CardGamesPlugin.getInstance().addGame(players, connect4);
		} else if ("connect1".equalsIgnoreCase(game) && players.size() >= 2) {
			Iterator<Player> it = players.iterator();
			Connect1Game connect1 = new Connect1Game(it.next(), it.next());
			it.forEachRemaining(connect1::addSpectator);
			CardGamesPlugin.getInstance().addGame(players, connect1);
		} else {
			sender.sendMessage(CardGamesPlugin.CHAT_PREFIX_ERROR + "/cg <game> <players>");
		}
		
		return true;
	}

}
