package de.MrX13415.UpdateCraft.Command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public abstract class UCCommand implements CommandExecutor{

	protected String[] aliases;
	protected String description;
	protected ArrayList<UCCommand> subCommands = new ArrayList<>();
	
	public boolean isLabel(String label){
		for (String alias : aliases) {
			if (alias.equalsIgnoreCase(label)) return true;
		}
		return false;
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
		
		command(sender, cmd, label, args);
		
		return false;
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
		subCommands.add(command);
	}
	
	public abstract boolean command(CommandSender sender, Command cmd, String label, String[] args);

	public String[] getAliases() {
		return aliases;
	}
	
}
