package me.ceramictitan.iWantToApply;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	/**
	 * TODO: Make Colors configurable add Groupmanager better tutorial
	 * playerlist in config --> change so it checks from there if should load
	 * something needs to be done I forgot atm
	 * */

	public Set<String> applymode = new HashSet<String>(); // Is

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {

		getCommand("appply").getExecutor(new applyCommand());
		getCommand("age").getExecutor(new ageCommand());
		getCommand("gender").getExecutor(new genderCommand());
		getCommand("irl").getExecutor(new irlCommand());
		getCommand("desc").getExecutor(new descCommand());
		getCommand("done").getExecutor(new doneCommand());
		getCommand("checkstatus").getExecutor(new statusCommand());
		getCommand("check").getExecutor(new checkCommand());
		getCommand("accept").getExecutor(new acceptCommand());
		getCommand("reject").getExecutor(new rejectCommand());
		getCommand("checklist").getExecutor(new checklistCommand());
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		Player p = (Player) sender;

		String s = "";

		/** all application commands */

		// this message will show up after you type /apply:
		// "hello there %playername%, i see you want to apply for staff.. this
		// will help you:
		if (cmd.getName().equalsIgnoreCase("apply")
				&& p.hasPermission("apply.apply")) {

			applymode.add(p.getName());
			API.createAppFile(p);
			// block to send update when someone applies
			// end of that block
			p.sendMessage("Application process started!");
		}
		if (args.length < 0) {
			p.sendMessage("You wouldn't need to add arguments, but that's ok.");
		}

		/**
		 * i want these commands to be only available after you type /apply:
		 * [/age] [/gender male and gender female] [/irl] [/whyme] [/moredesc]
		 * [/done]
		 * */

		// -/age -to set your age,
		// (after you type /age this message will appear: your age is now
		// %age%!)
		if (cmd.getName().equalsIgnoreCase("age")
				&& p.hasPermission("apply.apply")
				&& applymode.contains(p.getName())) {
			if (args.length == 1) {
				if (isInt(args[0])) {
					AGE.put(p, getInt(args[1]));
					p.sendMessage("Your age is: " + args[0]);
				}
			} else if (args.length == 0) {
				p.sendMessage("Please add an argument. The command is '/age <age>'.");
			} else if (args.length > 1) {
				p.sendMessage("Please remove an argument. The command is '/age <age>'.");
			}
		}

		// -/gender male or /gender female - this will set your gender to
		// male/female
		// (after you type /gender this message will apeear: your gender is
		// %gender%!)
		if (cmd.getName().equalsIgnoreCase("gender")
				&& p.hasPermission("apply.apply") && (applymode.get(p) == true)) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("male")) {
					GENDER.put(p, 0);
					p.sendMessage("Your gender is: " + args[0]);
				} else if (args[0].equalsIgnoreCase("female")) {
					GENDER.put(p, 1);
				}
			} else if (args.length == 0) {
				p.sendMessage("Please add an argument. The command is '/gender <male/female>'.");
			} else if (args.length > 1) {
				p.sendMessage("Please remove an argument. The command is '/gender <male/female>'.");
			}
		}

		// -/IRL %name% - this will be used so that admins will know your real
		// name.
		// (after you type /IRL this message will appear: your name is %name%!)
		if (cmd.getName().equalsIgnoreCase("irl")
				&& p.hasPermission("apply.apply")) {
			if (args.length == 1) {
				IRL.put(p, args[0]);
				p.sendMessage("Your name is: " + args[0]);
			} else if (args.length == 0) {
				p.sendMessage("Please add an argument. The command is '/irl <name>'.");
			} else if (args.length > 1) {
				p.sendMessage("Please remove an argument. The command is '/irl <name>'.");
			}
		}

		// -/whyme - here you will type why YOU should be accepted!
		// (after you type /whyme whis message will appear: description set!
		// if you want to add to the description type /moredesc and continue
		// typing!)
		if (cmd.getName().equalsIgnoreCase("whyme")
				&& p.hasPermission("apply.apply") && (applymode.get(p) == true)) {
			if (args.length == 0) {
				for (int i = 0; i < args.length; i++) {
					s = s + args[i] + " ";
				}
				WHYME.put(p, s);
			}
		}

		// //-/done - to finish applying!
		// (customizble message will be shown after you type /done)
		if (cmd.getName().equalsIgnoreCase("done")
				&& p.hasPermission("apply.apply") && (applymode.get(p) == true)) {
			if (args.length < 0) {
				p.sendMessage("You wouldn't need to add arguments, but that's ok.");
			}

			p.sendMessage(this.getConfig().get("DoneMessage").toString());
			applymode.put(p, false);
		}

		// Check if your app was rejected/approved or hasn't been looked at yet
		if (cmd.getName().equalsIgnoreCase("checkstatus")
				&& p.hasPermission("apply.apply") && (applymode.get(p) == true)) {
			if (args.length == 0) {
				if (STATUS.get(p) == null) {
					p.sendMessage("Your application hasn't been looked at yet!");
				} else if (STATUS.get(p) == true) {
					p.sendMessage("Your application got approved! Congratz!");
				} else if (STATUS.get(p) == false) {
					p.sendMessage("Your application got rejected. We are sorry.");
				}

			} else {
				p.sendMessage("You wouldn't need to add arguments, but that's ok.");
			}
		}

		/** end application block **/

		// returns list of players who applied
		if (cmd.getName().equalsIgnoreCase("checkapps")
				&& p.hasPermission("apply.check")) {
			p.sendMessage("Current Applications:");
			for (Player app : APPS) {
				p.sendMessage("- " + app.toString());
			}
		}

		// Check a certain app
		if (cmd.getName().equalsIgnoreCase("check")
				&& p.hasPermission("apply.check")) {
			p.sendMessage("Current Applications:");
			if (args.length == 1) {
				Player info = getServer().getPlayerExact(args[0]);
				p.sendMessage("Information about the application by: "
						+ info.toString());
				p.sendMessage(info + "'s age is: " + AGE.get(info));
				p.sendMessage(info + "'s gender is: " + GENDER.get(info));
				p.sendMessage(info + "'s name is: " + IRL.get(info));
				p.sendMessage(info + "'s reason to get accepted is: "
						+ WHYME.get(info));
				p.sendMessage("'/accept " + info.toString()
						+ "' to accept his application");
				p.sendMessage("'/reject " + info.toString()
						+ "' to reject his application");
			} else if (args.length == 0) {
				p.sendMessage("Please add an argument. The command is '/check <player>'.");
			} else if (args.length > 1) {
				p.sendMessage("Please remove an argument. The command is '/check <player>'.");
			}
		}

		// accept app
		if (cmd.getName().equalsIgnoreCase("accept")
				&& p.hasPermission("apply.accept")) {
			if (args.length == 1) {
				Player statusplayer = Bukkit.getPlayer(args[0]);
				if (APPS.contains(statusplayer)) {
					STATUS.put(statusplayer, true);
					APPS.remove(statusplayer);
					HANDLER.put(statusplayer, p.toString());
					// gm.setGroup(statusplayer,
					// this.getConfig().getString("GroupToPromote"));
				}
			} else if (args.length == 0) {
				p.sendMessage("Please add an argument. The command is '/accept <player>'.");
			} else if (args.length > 1) {
				p.sendMessage("Please remove an argument. The command is '/accept <player>'.");
			}
		}

		// reject app
		if (cmd.getName().equalsIgnoreCase("reject")
				&& p.hasPermission("apply.reject")) {
			if (args.length == 1) {
				Player statusplayer = Bukkit.getPlayer(args[0]);
				if (APPS.contains(args[0])) {
					STATUS.put(statusplayer, false);
					APPS.remove(statusplayer);
					HANDLER.put(statusplayer, p.toString());
				}
			} else if (args.length == 0) {
				p.sendMessage("Please add an argument. The command is '/reject <player>'.");
			} else if (args.length > 1) {
				p.sendMessage("Please remove an argument. The command is '/reject <player>'.");
			}
		}

		return false;
	}

	/**
	 * also i want all applies to be saved in a log (even after they accepted)
	 * with the name of the person who applied and the name of the person who
	 * accepted
	 * 
	 * PERMISSIONS: apply.apply - to let people to apply (use all commands
	 * above) apply.check - to see new applies apply.accept - used to accept the
	 * apply (command: /app accept %playername% apply.reject - used to reject
	 * applies (command: /app reject %playername%)
	 * 
	 * also i'd like the plugin to auto promote the applier after his app was
	 * accepted.
	 * */

}
