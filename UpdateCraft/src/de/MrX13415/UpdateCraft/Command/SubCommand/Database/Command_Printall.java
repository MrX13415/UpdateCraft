package de.MrX13415.UpdateCraft.Command.SubCommand.Database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.UCCommand;


public class Command_Printall extends UCCommand{

	public Command_Printall() {
		super();
		
		setLabel("printall");
		setAliases("pa", "prtall");
		setDescription("Prints the database content to the console");
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		
		UpdateCraft.get().getBuildDatabase().printDB();

		return true;
	}


	
}
