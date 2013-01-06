package me.ceramictitan.iWantToApply;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DatabaseHandler {

	private static Main main;

	public DatabaseHandler(Main main) {
		DatabaseHandler.main = main;
	}

	/**
	 * config.set("in-game Name", playername); config.set("Status",
	 * STATUS.get(status)); config.set("age", AGE.get(age));
	 * config.set("Gender", GENDER.get(gender)); config.set("Name",
	 * IRL.get(name)); config.set("Reason", WHYME.get(reason));
	 * config.set("Handler", HANDLER.get(handler)); try { config.save(file); }
	 * catch (Exception e) { e.printStackTrace(); } }
	 **/
	public FileConfiguration getConfig(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}

	public void createAppFile(String playername) {
		File file = new File(main.getDataFolder() + File.separator + "players"
				+ File.separator + playername + ".yml");
		if (!file.exists()) {
			main.getDataFolder().mkdir();
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public File getFile(String playername) {
		return new File(main.getDataFolder() + File.separator + "Application"
				+ File.separator + playername + ".yml");
	}
}
