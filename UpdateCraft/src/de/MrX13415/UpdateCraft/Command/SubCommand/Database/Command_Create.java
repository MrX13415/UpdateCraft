package de.MrX13415.UpdateCraft.Command.SubCommand.Database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.UCCommand;


public class Command_Create extends UCCommand{

	public Command_Create() {
		super();
		
		setLabel("create");
		setAliases("cr", "crt");
		setDescription("(Re)Create the database");
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		
		UpdateCraft.get().getBuildDatabase().createDatabase();

		return true;
	}

}
