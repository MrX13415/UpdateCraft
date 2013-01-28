package de.MrX13415.UpdateCraft.Command;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.SubCommand.Command_Database;
import de.MrX13415.UpdateCraft.Command.SubCommand.Command_Download;

public class Command_Updatecraft extends UCCommand{

	public Command_Updatecraft() {
		super();

		setAliases("");
		add(new Command_Database(), new Command_Download());
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		UpdateCraft.get().getBuildDatabase().save();
		return false;
	}
		
}
