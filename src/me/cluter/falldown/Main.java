package me.cluter.falldown;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.cluter.falldown.commands.fds;

public class Main extends JavaPlugin {
	public String prefix;
	public File pinvfile;
	public FileConfiguration pinv;
	public ArrayList<String> joined = new ArrayList<String>();

	public void onEnable() {
		saveDefaultConfig();
		getCommand("fds").setExecutor(new fds(this));
		setup();
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix")) + ChatColor.WHITE + " ";
	}

	public void setup() {
		pinvfile = new File(getDataFolder(), "playerinv.yml");
		if (!pinvfile.exists()) {
			try {
				pinvfile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "PlayerInv.yml could not be created!");
			}
		}
		pinv = YamlConfiguration.loadConfiguration(pinvfile);
	}

	public WorldEditPlugin getWorldEdit() {
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (p instanceof WorldEditPlugin)
			return (WorldEditPlugin) p;
		else
			return null;
	}

	public void saveData(FileConfiguration ymlConfig, File ymlFile) {
		try {
			ymlConfig.save(ymlFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "PlayerInv.yml could not be saved!");
		}
	}
	
	public String toColor(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
