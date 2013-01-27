package de.MrX13415.UpdateCraft.Command.SubCommand.Database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.UCCommand;


public class Command_Remove extends UCCommand{

	public Command_Remove() {
		super();
		
		setAliases("remove", "r", "rm");
	}
	
	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		//TODO: improve code ...
		
		if (args.length > 0){
			
			for (int i = 0; i < args.length; i++) {
				UpdateCraft.get().getBuildDatabase().removeBuild(UpdateCraft.get().getBuildDatabase().getBuild(Integer.valueOf(args[i])));
			}		
		}
			
		return false;
	}

}
