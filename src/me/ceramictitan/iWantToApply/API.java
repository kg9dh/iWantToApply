package me.ceramictitan.iWantToApply;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class API {

	public static Main plugin;
	public static DatabaseHandler handler;

	public API(Main plugin, DatabaseHandler handler) {
		API.plugin = plugin;
		API.handler = handler;
	}

	public static Integer getAge(Player p) {
		return API.getApplication(p).getInt(p.getName() + ".age");

	}

	public static String getHandler(Player p) {
		return API.getApplication(p).getString(p.getName() + ".handler");

	}

	public static List<String> getReason(Player p) {
		return API.getApplication(p).getStringList(p.getName() + ".reason");

	}

	public static String getRealName(Player p) {
		return API.getApplication(p).getString(p.getName() + ".realname");

	}

	public static String getName(Player p) {
		return p.getName();

	}

	public static String getGender(Player p) {
		return API.getApplication(p).getString(p.getName() + ".gender");

	}

	public static String getStatus(Player p) {
		return API.getApplication(p).getString(p.getName() + ".status");

	}

	public static void setAge(Player p, int age) {
		API.getApplication(p).set(p.getName() + ".age", age);

	}

	public static void setHandler(Player p, String handler) {
		API.getApplication(p).set(p.getName() + ".handler", handler);

	}

	public static void setReason(Player p, List<String> reason) {
		API.getApplication(p).set(p.getName() + ".age", reason);

	}

	public static void setRealName(Player p, String name) {
		API.getApplication(p).set(p.getName() + ".realname", name);

	}

	public static void setGender(Player p, String gender) {
		API.getApplication(p).set(p.getName() + ".gender", gender);

	}

	public static void setStatus(Player p, String accepted) {
		API.getApplication(p).set(p.getName() + ".status", accepted);

	}

	public static File getFile(Player p) {
		return handler.getFile(p.getName());

	}

	public static void createAppFile(Player p) {
		handler.createAppFile(p.getName());
	}

	public static FileConfiguration getApplication(Player p) {
		return handler.getConfig(API.getFile(p));
	}
}
