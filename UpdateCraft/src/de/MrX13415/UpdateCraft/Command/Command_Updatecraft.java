package de.MrX13415.UpdateCraft.Command;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.SubCommand.Command_Database;
import de.MrX13415.UpdateCraft.Command.SubCommand.Command_Download;
import de.MrX13415.UpdateCraft.Text.Text;


public class Command_Updatecraft extends UCCommand{

	public Command_Updatecraft() {
		super();

		setLabel("updatecraft");
		setAliases("uc", "ucraft", "udc", "upc");
		setDescription("Manage UpdateCraft");

		add(new Command_Database(), new Command_Download());
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {

		//**** DEBUG ****
		if (args.length == 2 && args[0].equalsIgnoreCase("debug")){
			if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("1")){
				UpdateCraft.get().setDebug(true);
				UpdateCraft.get().getServer().broadcastMessage(Text.get(Text.DEBUG_TRUE, UpdateCraft.get().getDescription().getName()));
			}else{
				UpdateCraft.get().setDebug(false);
				UpdateCraft.get().getServer().broadcastMessage(Text.get(Text.DEBUG_FALSE, UpdateCraft.get().getDescription().getName()));
			}
			return true;
		}
		//***************
		
		//**** EASTEREGG ****
		if (args.length == 1 && (args[0].equalsIgnoreCase("cat") || args[0].equalsIgnoreCase("meow") || args[0].equalsIgnoreCase(":3"))){
			UpdateCraft.get().getServer().broadcastMessage(Text.get(Text.MEOW, UpdateCraft.get().getDescription().getName()));
			return true;
		}
		//*******************
		
		return false;
	}


		
}
