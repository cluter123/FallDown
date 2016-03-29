package me.cluter.falldown.commands;

import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.cluter.falldown.InventoryToBase64;
import me.cluter.falldown.Main;

public class fds implements CommandExecutor {
	Main pl;

	public fds(Main inst) {
		pl = inst;
	}

	// String error = pl.prefix + ChatColor.RED + "An error has occured. Please
	// check the console.";
	private Selection s;

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("fds")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only.");
				return false;
			}
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage(pl.toColor("&7&m+---------------------------------------------------+"));
				p.sendMessage(pl.toColor("&b FallDown:&7 The goal of the game is simple- don't fall down. "
						+ "You can leave at any time, but the longer you stay the better the rewards. "
						+ "If you fall down, its Game Over! The first round will begin soon!"));
				p.sendMessage(pl.toColor("&7&m+---------------------------------------------------+"));
				return false;
			}
			if (args[0].equalsIgnoreCase("help")) {
				// do permissions
				p.sendMessage(pl.toColor("&7&m+-------------------&r&8[&bFallDown Help&8]&7&m-------------------+"));
				p.sendMessage(pl.toColor("&7- &b/fds setarena &7| Sets up the arena."));
				p.sendMessage(pl.toColor("&7- &b/fds join &7| Join the arena"));
				p.sendMessage(pl.toColor("&7- &b/fds leave &7| Leave the arena"));
				p.sendMessage(pl.toColor("&7- &b/fds start <block> <inc> &7| Starts falldown!"));
				p.sendMessage(pl.toColor("&7- &b/fds help &7| Opens up this menu"));
				p.sendMessage(pl.toColor("&7&m+---------------------------------------------------+"));
				return true;
			}
			if (args[0].equalsIgnoreCase("setarena")) {
				s = pl.getWorldEdit().getSelection(p);
				if (s == null) {
					p.sendMessage(pl.prefix + ChatColor.RED + "Please specify a selection.");
					return true;
				}
				try {
					pl.getConfig().set("arena1", s.getMinimumPoint());
					pl.getConfig().set("arena2", s.getMaximumPoint());
					pl.saveConfig();
				} catch (Exception e) {
					p.sendMessage(pl.prefix + ChatColor.RED + "An error has occured. Please check the console.");
					e.printStackTrace();
				}
				p.sendMessage(pl.prefix + "Game region has been set!");
				return true;
			}
			if (args[0].equalsIgnoreCase("join")) {
				pl.pinv.set(p.getName() + ".inv", InventoryToBase64.toBase64(p.getInventory()));
				pl.pinv.set(p.getName() + ".armor", p.getInventory().getArmorContents());
				pl.saveData(pl.pinv, pl.pinvfile);
				p.getInventory().clear();
				pl.joined.add(p.getName());
				p.sendMessage(pl.prefix + "You have joined the arena.");
				return true;
			}
			if (args[0].equalsIgnoreCase("leave")) {
				if (pl.joined.contains(p.getName())) {
					try {
						@SuppressWarnings("unchecked")
						List<ItemStack> inven = (List<ItemStack>) InventoryToBase64
								.fromBase64(pl.pinv.getString(p.getName() + ".inv"));
						p.getInventory().setContents((ItemStack[]) inven.toArray());
						p.sendMessage(pl.prefix + "You have left the arena.");
						return true;
					} catch (IOException e) {
						p.sendMessage(pl.prefix + ChatColor.RED + "Unable to load inventory. Please contact an admin.");
						e.printStackTrace();
						return false;
					}
				}
				p.sendMessage(pl.prefix + "You have not joined an arena!");
				return false;
			}
			p.sendMessage(pl.prefix + "Invalid arguments. Please use /fds help for help.");
			return false;
		}
		return false;
	}

}
