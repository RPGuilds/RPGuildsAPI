package me.XxXYaJrAbXxX.Characters;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.XxXYaJrAbXxX.Characters.FileWriter.*;
import me.XxXYaJrAbXxX.Characters.DataCollect.*;
import me.XxXYaJrAbXxX.Characters.FileWriter.Config;
import me.XxXYaJrAbXxX.Characters.Commands.CharCommand;
import me.XxXYaJrAbXxX.Characters.Commands.RollCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Utilities extends JavaPlugin {
	public GetData GetData = new GetData(this);
	public SetData SetData = new SetData(this, GetData);
	public RollCommand RollCommand = new RollCommand(GetData);
	private CharCommand CharCommand = new CharCommand(this);
	Logger logger = Logger.getLogger("Minecraft");
	public JavaPlugin RPGuildsAPI = this;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		logger.log(Level.INFO, "RPGuildsAPI by " + this.getDescription().getAuthors());
		new File(this.getDataFolder(), "PlayerData").mkdirs();
		File file = new File("/plugins/RPGuildsAPI/");
		for (Player player : Bukkit.getOnlinePlayers()) {
			Config PlayerData = new Config(this, player.getName());
			try {
				if (PlayerData.get("pregenerated") == null) {
					logger.log(Level.INFO, player.getName() + "'s data does not exist! Generating...");
					SetData.generatePlayerData(player.getName());
				} else {
					logger.log(Level.INFO, player.getName() + "'s data already exists!");
				}
			} catch (Exception e) {

			}
		}
		if (!file.exists()) {
			new File("/plugins/RPGuildsAPI/").mkdirs();
		}
		try {
			this.getServer().getPluginManager().registerEvents(SetData, this);
		} catch (Exception e) {

		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		try {
			if (CommandLabel.equalsIgnoreCase("char") && sender instanceof Player) {
				CharCommand.onCommand(sender, cmd, CommandLabel, args);
				return true;
			} else if (CommandLabel.equalsIgnoreCase("roll") && sender instanceof Player) {
				RollCommand.onCommand(sender, cmd, CommandLabel, args);
				return true;
			}
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "Type '/char help' for help!");
			return true;
		}
		return false;
	}
}
