package dev.larrox.headOrNumberGame;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HeadOrNumberCommand implements CommandExecutor {

    private Map<Player, Player> challenges = new HashMap<>();
    HeadOrNumberGame plugin = HeadOrNumberGame.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String prefix = "§8[§aHoN§8] §7";

        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "This command can only be executed by a player...");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(prefix + "Please provide a player's name.");
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(prefix + "Player not found.");
            return true;
        }

        if (challenges.containsKey(target)) {
            player.sendMessage(prefix + "This player is already in a challenge.");
            return true;
        }

        challenges.put(player, target);
        player.sendMessage(prefix + "You have challenged §e" + target.getName() + "§7 to a Head or Number battle!");
        target.sendMessage(prefix + "§e" + player.getName() + "§7 has challenged you to a Head or Number battle! Type /accept to accept or /decline to decline.");

        return true;
    }

    public boolean onAcceptCommand(Player player) {
        Player challenger = null;

        for (Map.Entry<Player, Player> entry : challenges.entrySet()) {
            if (entry.getValue().equals(player)) {
                challenger = entry.getKey();
                break;
            }
        }

        if (challenger == null) {
            player.sendMessage("You were not challenged.");
            return false;
        }

        player.sendMessage("§7You accepted the challenge from §e" + challenger.getName() + "§7!");
        challenger.sendMessage("§7Your challenge has been accepted by §e" + player.getName() + "§7!");

        final Player finalChallenger = challenger;

        new BukkitRunnable() {
            @Override
            public void run() {
                Random random = new Random();
                boolean isHeads = random.nextBoolean(); // true = Kopf, false = Zahl

                if (isHeads) {
                    finalChallenger.sendMessage("§7The result is: §aHeads§7! You win!");
                    player.sendMessage("§7The result is: §aHeads§7! You lose!");
                    player.damage(1.0);
                } else {
                    finalChallenger.sendMessage("§7The result is: §cTails§7! You lose!");
                    player.sendMessage("§7The result is: §cTails§7! You win!");
                    finalChallenger.damage(1.0);
                }

                challenges.remove(finalChallenger);
                challenges.remove(player);
            }
        }.runTaskLater(plugin, 60L);

        return true;
    }

    public boolean onDeclineCommand(Player player) {
        Player challenger = null;

        for (Map.Entry<Player, Player> entry : challenges.entrySet()) {
            if (entry.getValue().equals(player)) {
                challenger = entry.getKey();
                break;
            }
        }

        if (challenger == null) {
            player.sendMessage("You were not challenged.");
            return false;
        }

        player.sendMessage("§7You declined the challenge from §e" + challenger.getName() + "§7!");
        challenger.sendMessage("§7Your challenge has been declined by §e" + player.getName() + "§7.");
        challenges.remove(challenger);
        challenges.remove(player);

        return true;
    }
}
