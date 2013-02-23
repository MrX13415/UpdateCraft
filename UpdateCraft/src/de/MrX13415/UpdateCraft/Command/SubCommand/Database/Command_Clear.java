package de.MrX13415.UpdateCraft.Command.SubCommand.Database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.UCCommand;


public class Command_Clear extends UCCommand{

	public Command_Clear() {
		super();
		
		setLabel("clear");
		setAliases("clr", "cl");
		setDescription("Clear the database");
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		//TODO: improve code ...
		
		UpdateCraft.get().getBuildDatabase().clearDatabase();
		
		return true;
	}

}
