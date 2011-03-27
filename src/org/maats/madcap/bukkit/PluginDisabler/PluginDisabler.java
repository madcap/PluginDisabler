package org.maats.madcap.bukkit.PluginDisabler;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class PluginDisabler extends JavaPlugin {

	protected static void print(String s){
		System.out.println("PluginDisabler: "+s);
	}
	
	private Plugin[] lastTarget;
	private PluginManager pm;
	
	@Override
	public void onEnable() {

		print("Plugin Enabled.");
		lastTarget = null;
		pm = this.getServer().getPluginManager();

	}

	@Override
	public void onDisable() {

		System.out.println("Plugin Disabled.");
		lastTarget = null;
		pm = null;
		
	}

	// rebuild the command into printable form
	private String cmdString(String name, String label, String[] args){
		String s = name+" - "+ label;
		for(int i = 0;i<args.length;i++){
			s = s + " " + args[i];
		}
		
		return s;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){ 

		// check that sender has access, must either be console or op
		boolean perm = false;
		String name = "";
		if(sender instanceof Player){
			name = ((Player)sender).getName();
			if(((Player) sender).isOp())
				perm = true;
		}
		else{
			// what else could this be besides console?
			perm = true;
			name = "Console";
		}
		
		if(!perm){
			sender.sendMessage("You are not allowed to use that command.");
			print("Unautherized attempt to change status of plugins: "+cmdString(name, label, args));
			return true;			
		}


		// process arguments

		Plugin[] targets = null;
		Plugin[] allPlugins = pm.getPlugins();
		String[] pluginNames = new String[allPlugins.length];

		// get the names of all plugins for comparison (so we can ignore case)
		for(int i = 0;i<allPlugins.length;i++){
			pluginNames[i]=allPlugins[i].getDescription().getName();
		}
		
		if(args.length==0){
			// no arguments, use the lastTarget 
			if(lastTarget==null){
				sender.sendMessage("No previous plugins used. You must specify at least one plugin.");
				return true;
			}
			targets = lastTarget;
		}
		else{

			List<Plugin> list = new LinkedList<Plugin>();
			boolean star = false;
			// create a list of target plugins
			for(int i = 0;i<args.length;i++){

				// if there is a *, use all plugins
				if(args[i].equals("*")){
					targets = pm.getPlugins();
					star = true;
					break;
				}
				
				// check plugin names for all plugins that start with the arg (ignore case)
				boolean found = false;
				for(int j = 0;j<pluginNames.length;j++){
					// fancy way of doing starts with ignore case
					if(pluginNames[j].regionMatches(true, 0, args[i], 0, args[i].length())){
						list.add(pm.getPlugin(pluginNames[j]));
						found = true;
					}
				}

				if(!found)
					sender.sendMessage("No such plugin: "+args[i]);
			}

			if(!star){
				targets = list.toArray(new Plugin[list.size()]);
			}
		}

		if(targets==null){
			// this shouldn't happen
			sender.sendMessage("oops, something went wrong, no targets");
			return true;
		}
		
		// remove this plugin from targets list
		int count = 0;
		for(int i = 0;i<targets.length;i++){
			if(targets[i] != null && targets[i].equals(this)){
				targets[i] = null;
			}
			else{
				count++;
			}
		}
		
		// after pruning, make sure there is at least one valid target
		if(count==0){
			sender.sendMessage("There are no that match your request.");
			return true;
		}

		// assign lastTarget to the new target list
		lastTarget = targets.clone();
		
		
		String cmd = command.getName();

		if (cmd.compareToIgnoreCase("penable") == 0){
			
			// turn on any targets that are not enabled
			
			print("Enabling plugins: "+cmdString(name, label, args));
			
			for(int i = 0;i<targets.length;i++){
				if(targets[i]!=null){
					if(pm.isPluginEnabled(targets[i])){
						sender.sendMessage("Plugin already enabled: "+targets[i].getDescription().getName());
					}
					else{
						sender.sendMessage("Enabling Plugin: "+targets[i].getDescription().getName());
						pm.enablePlugin(targets[i]);
					}
				}
			}
			
			return true;
		}

		if (cmd.compareToIgnoreCase("pdisable") == 0){

			// turn off any targets that are enabled
			
			print("Disabling plugins: "+cmdString(name, label, args));
			
			for(int i = 0;i<targets.length;i++){
				if(targets[i]!=null){
					if(!pm.isPluginEnabled(targets[i])){
						sender.sendMessage("Plugin already disabled: "+targets[i].getDescription().getName());
					}
					else{
						sender.sendMessage("Disabling Plugin: "+targets[i].getDescription().getName());
						pm.disablePlugin(targets[i]);
					}
				}
			}
			
			return true;
			
		}

		if (cmd.compareToIgnoreCase("ptoggle") == 0){

			// toggle targets from on to off and vice versa
			
			print("Toggling the status of plugins: "+cmdString(name, label, args));
			
			for(int i = 0;i<targets.length;i++){
				if(targets[i]!=null){
					if(pm.isPluginEnabled(targets[i])){
						sender.sendMessage("Disabling Plugin: "+targets[i].getDescription().getName());
						pm.disablePlugin(targets[i]);
					}
					else{
						sender.sendMessage("Enabling Plugin: "+targets[i].getDescription().getName());
						pm.enablePlugin(targets[i]);
					}
				}
			}

			return true;
			
		}

		if (cmd.compareToIgnoreCase("pstatus") == 0){

			// show the status of each target
			
			print("Listing status of plugins: "+cmdString(name, label, args));
			
			for(int i = 0;i<targets.length;i++){
				if(targets[i]!=null){
					if(pm.isPluginEnabled(targets[i])){
						sender.sendMessage("Enabled Plugin: "+targets[i].getDescription().getName());
					}
					else{
						sender.sendMessage("Disabled Plugin: "+targets[i].getDescription().getName());
					}
				}
			}

			return true;
			
		}


		return false;

	}

}