package de.MrX13415.UpdateCraft.Command.SubCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.Command.UCCommand;
import de.MrX13415.UpdateCraft.Command.SubCommand.Database.Command_Clear;
import de.MrX13415.UpdateCraft.Command.SubCommand.Database.Command_Create;
import de.MrX13415.UpdateCraft.Command.SubCommand.Database.Command_Printall;
import de.MrX13415.UpdateCraft.Command.SubCommand.Database.Command_Remove;
import de.MrX13415.UpdateCraft.Command.SubCommand.Database.Command_Update;


public class Command_Database extends UCCommand{
	
	public Command_Database() {
		super();
		
		setAliases("database", "db");
		
		add(new Command_Update(),
			new Command_Clear(),
			new Command_Create(),
			new Command_Printall(),
			new Command_Remove());
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {

		return false;
	}

}


