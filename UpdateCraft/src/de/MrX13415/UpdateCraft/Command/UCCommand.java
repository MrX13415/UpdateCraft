package de.MrX13415.UpdateCraft.Command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Text.Text;


public abstract class UCCommand implements CommandExecutor{

	protected PluginCommand pluginCommand;
	protected String label = "";
	protected String[] aliases = {""};
	protected String description = "";
	protected String usage = "";
	
	protected ArrayList<UCCommand> subCommands = new ArrayList<>();
	protected UCCommand parentCommand = null;
		
	public boolean isLabel(String label){
		if (label.equalsIgnoreCase(this.label)) return true;
		for (String alias : aliases) {
			if (alias.equalsIgnoreCase(label)) return true;
		}
		return false;
	}
	
	public void initPluginCommand(){
		pluginCommand = UpdateCraft.get().getCommand(label);
		pluginCommand.setAliases(Arrays.asList(aliases));
		pluginCommand.setExecutor(this);
		pluginCommand.setUsage(usage);
		pluginCommand.setDescription(description);
	}
	
	public PluginCommand getPluginCommand() {
		return pluginCommand;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if (args.length > 0){			
			for (UCCommand scmd : subCommands) {	
				if (scmd.isLabel(args[0])){
					args = removeCommandFromArgs(args);
					return scmd.onCommand(sender, cmd, label, args);
				}
			}		
		}
		//basic permissions:
		if (!sender.isOp()){
			sendCommandPermissionDenied(sender, cmd, label, args);
			return false;
		}
		
		boolean returnval = false;
		
		if (args.length == 1 && args[0].equals("?"))
			sendCommandHelp(sender, cmd, label, args);
		else{
			//process command
			returnval = command(sender, cmd, label, args);
			//send help ...
			if (!returnval) sendCommandHelp(sender, cmd, label, args);
		}
		
		return returnval;
	}

	/**
	 * Removes the first argument of the array, because it is the current command
	 * @param args
	 * @return
	 */
	private String[] removeCommandFromArgs(String[] args){
		String[] newArgsArray = new String[args.length - 1];
		
		for (int i = 1; i < args.length; i++) {
			newArgsArray[i - 1] = args[i];
		}
		
		return newArgsArray;
	}
	
	public void setAliases(String...alias){
		aliases = alias;
	}
	
	public void add(UCCommand...command){
		for (UCCommand ucCommand : command) {
			add(ucCommand);
		}
	}
	
	public void add(UCCommand command){
		command.setParentCommand(this);
		subCommands.add(command);
	}
	
	public ArrayList<UCCommand> getSubCommands() {
		return subCommands;
	}

	public void setParentCommand(UCCommand parentCommand) {
		this.parentCommand = parentCommand;
	}

	public UCCommand getParentCommand() {
		return parentCommand;
	}

	public abstract boolean command(CommandSender sender, Command cmd, String label, String[] args);
	
	public boolean sendCommandHelp(CommandSender sender, Command cmd, String label, String[] args){
		
		if (sender.equals(sender.getServer().getConsoleSender())) 
			UpdateCraft.sendConsoleMessage(getCommandHelpText());
		else
			sender.sendMessage(getCommandHelpText());
		
		return false;
	}
	
	public boolean sendCommandPermissionDenied(CommandSender sender, Command cmd, String label, String[] args){
		
		sender.sendMessage(getCommandPermissionDeniedText());
		
		
		return false;
	}
	
	public String[] getAliases() {
		return aliases;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}


	public String getCommandPermissionDeniedText(){
	String format = ChatColor.RED + "DENIED " + ChatColor.GREEN + "Command: /%s" + ChatColor.RED + "%s " + ChatColor.GREEN + "Permission: " + ChatColor.RED + "%s";
		
		String parents = "";

		UCCommand parent = getParentCommand();
		
		while (parent != null) {
			parents = parent.getLabel() + " " + parents;
			parent = parent.getParentCommand();
		}
		
		String finalT = String.format(format, parents, label, "OP");

		return finalT;
	}
		
	public String getCommandHelpText(){
		String format = ChatColor.GREEN + "Command: /%s" + ChatColor.RED + "%s " + ChatColor.GREEN + "Aliases: " + ChatColor.RED + "%s";
		
		String parents = "";

		UCCommand parent = getParentCommand();
		
		while (parent != null) {
			parents = parent.getLabel() + " " + parents;
			parent = parent.getParentCommand();
		}
		
		String finalT = String.format(format, parents, label, Text.toString(aliases));
		String descLine = "";

		if (usage.length() > 0){
			if (!usage.endsWith(" ")) usage += " ";
		}else if (subCommands.size() > 0 ){
			usage = "<subCommand> ";
		}
				
		if (description.length() > 0){
			descLine = String.format("\n" + ChatColor.GREEN + "Usage: " + ChatColor.GOLD + "%s" + ChatColor.GRAY + "%s", usage, description); 
		}
		
		String cmdFormat = ChatColor.WHITE + " - " + ChatColor.RED + "%s " + ChatColor.GOLD + "%s " + ChatColor.GRAY + "%s";
		String cmdFinalT = "";
		
		for (UCCommand uccmd : subCommands) {
			cmdFinalT += String.format(cmdFormat, uccmd.getLabel(), uccmd.getUsage(), uccmd.getDescription());
			if (subCommands.size() - subCommands.indexOf(uccmd) > 1) cmdFinalT += "\n";
		}
		
		return finalT + descLine + "\n" + cmdFinalT;
	}
	
}
