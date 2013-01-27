package de.MrX13415.UpdateCraft.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;


public class Command_Restart extends UCCommand{

	public Command_Restart(UpdateCraft updateCraft) {
		super();

		setAliases("");
	}

	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		
		UpdateCraft.sendConsoleMessage("RESTART ...");
		UpdateCraft.get().getServer().dispatchCommand(sender, "stop");
		
		return false;
	}
		
}
