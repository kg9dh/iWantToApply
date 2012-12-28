package ch.kg9dh.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	/**TODO:
	 * Make Colors configurable
	 * add Groupmanager 
	 * something needs to be done I forgot atm
	 * */
	
	Logger log = this.getLogger();
	
	public Map<Player, Boolean>APPLY = new HashMap<Player, Boolean>();	//Is the player in apply-mode?
	public Map<Player, Boolean>STATUS = new HashMap<Player, Boolean>();  //rejected or accepted?
	public Map<Player, Integer>AGE = new HashMap<Player, Integer>();	//stores player age
	public Map<Player, Integer>GENDER = new HashMap<Player, Integer>();	//stores gender: 0=male, 1=female
	public Map<Player, String>IRL = new HashMap<Player, String>();		//stores irl-name
	public Map<Player, String>WHYME = new HashMap<Player, String>();	//stores whyme-data
	public Map<Player, String>HANDLER = new HashMap<Player, String>();	//stores handler-name
	
	public List<Player>APPS = new ArrayList<Player>(); 					//stores all apps
	
 	@Override
	public void onEnable() {
 		
        this.saveDefaultConfig();
        
        APPS = (List<Player>) this.getConfig().getList("PlayerAps");
        
        for(Player startup : APPS){
        	getLog(startup);
        }
        
 		log.info("Plugin Enabled!");
 	}
	
 	@Override
    public void onDisable() {
 		 		
 		this.getConfig().set("PlayerApps", APPS);
 		
 		for(Player savelog : APPS){
 			setUpLog(savelog, GENDER.get(savelog), AGE.get(savelog), IRL.get(savelog), WHYME.get(savelog), STATUS.get(savelog), HANDLER.get(savelog));
 		}
 		
 		APPLY.clear();
 		STATUS.clear();
 		AGE.clear();
 		GENDER.clear();
 		IRL.clear();
 		WHYME.clear();
 		HANDLER.clear();
 		
 		log.info("Plugin Disabled!");
 		
	}
 	
 	@Override
 	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){			
 		
 		Player p =(Player) sender;
 		
 		
 		String s = "";
 		
 		/**all application commands*/
 		
 			//this message will show up after you type /apply: 
 			//"hello there %playername%, i see you want to apply for staff.. this will help you:
	 		if(cmd.getName().equalsIgnoreCase("apply")&&p.hasPermission("apply.apply")){
	 			if(args.length<0){
	 				p.sendMessage("You wouldn't need to add arguments, but that's ok.");
	 			}
	 			
	 			APPS.add(p);
	 				//block to send update when someone applies
			 	 		for(Player checker : Bukkit.getOnlinePlayers()){
			 	 	 		if(checker.hasPermission("apply.check")||checker.isOp()){
			 	 	 			checker.sendMessage("Player" + p + "has handed in an application!");
			 	 	 		}
			 	 		}
			 	 	//end of that block
	 			APPLY.put(p, true);
	 			p.sendMessage("Application process started!");
	 			
	 		}
	 		
	 		/** 
	 		 * i want these commands to be only available after you type /apply: 
	 		 * [/age]
	 		 * [/gender male and gender female]
	 		 * [/irl]
	 		 * [/whyme]
	 		 * [/moredesc] 
	 		 * [/done] 
	 		 * */
	 		
	 		//-/age -to set your age, 
	 		//(after you type /age this message will appear: your age is now %age%!)
	 		if(cmd.getName().equalsIgnoreCase("age")&&p.hasPermission("apply.apply")&&(APPLY.get(p)==true)){
	 			if(args.length==1){
	 				if(isInt(args[0])){
	 					AGE.put(p, getInt(args[1]));
		 				p.sendMessage("Your age is: " + args[0]);
	 				}
	 			}else if(args.length==0){
	 				p.sendMessage("Please add an argument. The command is '/age <age>'.");
	 			}else if(args.length>1){
	 				p.sendMessage("Please remove an argument. The command is '/age <age>'.");
	 			}
	 		}
	 		
	 		//-/gender male or /gender female - this will set your gender to male/female
	 		//(after you type /gender this message will apeear: your gender is %gender%!)
	 		if(cmd.getName().equalsIgnoreCase("gender")&&p.hasPermission("apply.apply")&&(APPLY.get(p)==true)){
	 			if(args.length==1){
	 				if(args[0].equalsIgnoreCase("male")){
	 					GENDER.put(p, 0);
		 				p.sendMessage("Your gender is: " + args[0]);
	 				}else if(args[0].equalsIgnoreCase("female")){
	 					GENDER.put(p, 1);
	 				}	 					
	 			}else if(args.length==0){
	 				p.sendMessage("Please add an argument. The command is '/gender <male/female>'.");
	 			}else if(args.length>1){
	 				p.sendMessage("Please remove an argument. The command is '/gender <male/female>'.");
	 			}
	 		}
	 		
	 		//-/IRL %name% - this will be used so that admins will know your real name. 
	 		//(after you type /IRL this message will appear: your name is %name%!)
	 		if(cmd.getName().equalsIgnoreCase("irl")&&p.hasPermission("apply.apply")){
	 			if(args.length==1){
	 				IRL.put(p, args[0]);
	 				p.sendMessage("Your name is: " + args[0]);
	 			}else if(args.length==0){
	 				p.sendMessage("Please add an argument. The command is '/irl <name>'.");
	 			}else if(args.length>1){
	 				p.sendMessage("Please remove an argument. The command is '/irl <name>'.");
	 			}
	 		}
	 		
	 		//-/whyme - here you will type why YOU should be accepted! 
	 		//(after you type /whyme whis message will appear: description set! 
	 		//if you want to add to the description type /moredesc and continue typing!)
	 		if(cmd.getName().equalsIgnoreCase("whyme")&&p.hasPermission("apply.apply")&&(APPLY.get(p)==true)){
	 			if(args.length==0){
	 				for(int i=0; i < args.length; i++){
	 					s = s+args[i]+" ";
	 				}
	 					WHYME.put(p, s);
	 			}
	 		}
	 		
	 		//add to description
	 		if(cmd.getName().equalsIgnoreCase("moredesc")&&p.hasPermission("apply.apply")&&(APPLY.get(p)==true)){
	 			if(args.length==0){
	 				String temp = WHYME.get(p);
	 				
	 				for(int i=0; i < args.length; i++){
	 					temp = temp+args[i]+" ";
	 				}
	 					
	 					WHYME.put(p, temp);
	 			}else{
	 				p.sendMessage("Please add atleast one argument. The command is '/moredesc <text>'.");
	 			}
	 		}
	 		
	 		////-/done - to finish applying! 
	 		//(customizble message will be shown after you type /done)
	 		if(cmd.getName().equalsIgnoreCase("done")&&p.hasPermission("apply.apply")&&(APPLY.get(p)==true)){
	 			if(args.length<0){
	 				p.sendMessage("You wouldn't need to add arguments, but that's ok.");
	 			}
	 			
	 			p.sendMessage(this.getConfig().get("DoneMessage").toString());
	 			APPLY.put(p, false);
	 		}
	 		
	 		//Check if your app was rejected/approved or hasn't been looked at yet
	 		if(cmd.getName().equalsIgnoreCase("checkstatus")&&p.hasPermission("apply.apply")&&(APPLY.get(p)==true)){
	 			if(args.length==0){
	 				if(STATUS.get(p)==null){
	 					p.sendMessage("Your application hasn't been looked at yet!");
	 				}else if(STATUS.get(p)==true){
	 					p.sendMessage("Your application got approved! Congratz!");
	 				}else if(STATUS.get(p)==false){
	 					p.sendMessage("Your application got rejected. We are sorry.");
	 				}
	 					
	 			}else{
	 				p.sendMessage("You wouldn't need to add arguments, but that's ok.");
	 			}
	 		}
 			
 		/**end application block*/
 		
	 		//returns list of players who applied
 			if(cmd.getName().equalsIgnoreCase("checkapps")&&p.hasPermission("apply.check")){
 				p.sendMessage("Current Applications:");
 				for(Player app : APPS){
 					p.sendMessage("- " + app.toString());
 				}
 			}
 			
 			//Check a certain app
 			if(cmd.getName().equalsIgnoreCase("check")&&p.hasPermission("apply.check")){
 				p.sendMessage("Current Applications:");
 				if(args.length==1){
	 				Player info = Bukkit.getPlayer(args[0]);
	 				p.sendMessage("Information about the application by: " + info.toString());
	 				p.sendMessage(info + "'s age is: " + AGE.get(info));
	 				p.sendMessage(info + "'s gender is: " + GENDER.get(info));
	 				p.sendMessage(info + "'s name is: " + IRL.get(info));
	 				p.sendMessage(info + "'s reason to get accepted is: " + WHYME.get(info));
	 				p.sendMessage("'/accept " + info.toString() + "' to accept his application");
	 				p.sendMessage("'/reject " + info.toString() + "' to reject his application");
	 			}else if(args.length==0){
	 				p.sendMessage("Please add an argument. The command is '/check <player>'.");
	 			}else if(args.length>1){
	 				p.sendMessage("Please remove an argument. The command is '/check <player>'.");
	 			}
 			}
 		
 			//accept app
 			if(cmd.getName().equalsIgnoreCase("accept")&&p.hasPermission("apply.accept")){	
	 			if(args.length==1){
	 				Player statusplayer = Bukkit.getPlayer(args[0]);
	 				if(APPS.contains(statusplayer)){
		 				STATUS.put(statusplayer, true);
		 				APPS.remove(statusplayer);
		 				HANDLER.put(statusplayer, p.toString());
	 				}
	 			}else if(args.length==0){
	 				p.sendMessage("Please add an argument. The command is '/accept <player>'.");
	 			}else if(args.length>1){
	 				p.sendMessage("Please remove an argument. The command is '/accept <player>'.");
	 			}
 			}
 		
 			//reject app
 			if(cmd.getName().equalsIgnoreCase("reject")&&p.hasPermission("apply.reject")){ 				
	 			if(args.length==1){
	 				Player statusplayer = Bukkit.getPlayer(args[0]);
	 				if(APPS.contains(args[0])){
		 				STATUS.put(statusplayer, false);
		 				APPS.remove(statusplayer);
		 				HANDLER.put(statusplayer, p.toString());
	 				}
	 			}else if(args.length==0){
	 				p.sendMessage("Please add an argument. The command is '/reject <player>'.");
	 			}else if(args.length>1){
	 				p.sendMessage("Please remove an argument. The command is '/reject <player>'.");
	 			}
 			}
 		
 			
		return false;	
 	}	
 	
	public static int getInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException nFE) {
			return 0;
		} 
	}	
	
	public static boolean isInt(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException nFE) {
			return false;
		}
	return true;
	}
	
	public void setUpLog(Player ply, int gender, int age, String name, String reason, boolean status, String handler){
		this.getConfig().createSection(ply.toString());
		this.getConfig().createSection(ply.toString()+"."+"gender");
		this.getConfig().createSection(ply.toString()+"."+"age");
		this.getConfig().createSection(ply.toString()+"."+"irl-name");
		this.getConfig().createSection(ply.toString()+"."+"reason");
		this.getConfig().createSection(ply.toString()+"."+"status");
		this.getConfig().createSection(ply.toString()+"."+"handler");
		
		this.getConfig().addDefault(ply.toString()+"."+"gender", gender);
		this.getConfig().addDefault(ply.toString()+"."+"age", age);
		this.getConfig().addDefault(ply.toString()+"."+"name", name);
		this.getConfig().addDefault(ply.toString()+"."+"reason", reason);
		this.getConfig().addDefault(ply.toString()+"."+"status", status);
		this.getConfig().addDefault(ply.toString()+"."+"handler", handler);
	}
	
	public void getLog(Player ply){
		//check if need to load(not completed)
		if(this.getConfig().getBoolean(ply.toString()+".status")!=false||true){
			STATUS.put(ply, this.getConfig().getBoolean(ply.toString()+".status"));
			AGE.put(ply, this.getConfig().getInt(ply.toString()+".age"));
			GENDER.put(ply, this.getConfig().getInt(ply.toString()+".gender"));
			IRL.put(ply, (String) this.getConfig().get(ply.toString()+".name"));
			WHYME.put(ply, (String) this.getConfig().get(ply.toString()+".reason"));
			HANDLER.put(ply, (String) this.getConfig().get(ply.toString()+".handler"));
		}
	}
 	
 	
 	/**
 	 * also i want all applies to be saved in a log (even efter they eccepted)
 	 * with the name of the person who applied and the name of the person who accepted
 	 * 
 	 * PERMISSIONS:
 	 * apply.apply - to let people to apply (use all commands above)
 	 * apply.check - to see new applies
 	 * apply.accept - used to accept the apply (command: /app accept %playername%
 	 * apply.reject - used to reject applies (command: /app reject %playername%)
 	 * 
 	 * also i'd like the plugin to auto promote the applier after his app was accepted.
 	 * */
 	
}
