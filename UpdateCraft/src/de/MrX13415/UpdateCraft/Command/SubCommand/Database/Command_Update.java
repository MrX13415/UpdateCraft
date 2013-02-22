package de.MrX13415.UpdateCraft.Command.SubCommand.Database;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.UCCommand;


public class Command_Update extends UCCommand{	

	public Command_Update() {
		super();
		
		setLabel("update");
		setAliases("u", "ud", "up");
		setDescription("Check for and add newer builds");
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		
		UpdateCraft.get().getBuildDatabase().updateDatabase();
		
		return false;
	}

}
