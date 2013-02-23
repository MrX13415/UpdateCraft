package de.MrX13415.UpdateCraft.Command;

import java.io.IOException;
import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Text.Text;



public class Command_Restart extends UCCommand{

	private Thread restartThread;
	private long time;
	
	public enum UNIT{
		seconds, minutes, houers, milliseconds;
		
		public static UNIT getUnit(String arg){
			for (UNIT unit : UNIT.values()) {
				if (unit.toString().toLowerCase().startsWith(arg.toLowerCase())) return unit;
			}		
			return UNIT.milliseconds;
		}
	}
	
	public Command_Restart() {
		super();

		setLabel("restart");
		setAliases("");
		setDescription("Restart the Server");
	}

	@Override
	public boolean command(final CommandSender sender, Command cmd, String label, String[] args) {
			
		UNIT unit = UNIT.milliseconds;
		long time = 0;	//in ms
		
		if (args.length > 0){
			try{
				if (args[0].contains(":")){
					String[] t = args[0].split(":");
					time += (Integer.valueOf(t[0]) * 60 * 60000);
					time += (Integer.valueOf(t[1]) * 60000);
					if (t.length > 2) time += Integer.valueOf(t[2]) * 1000;
				}else{
					time = Long.valueOf(args[0]);
					if (args.length >= 2) unit = UNIT.getUnit(args[1]);
				}
			
				if (unit == UNIT.seconds){
					time *= 1000;
				}
				if (unit == UNIT.minutes){
					time *= 1000 * 60;
				}
				if (unit == UNIT.houers){
					time *= 1000 * 60 * 60;
				}
			}catch(Exception e){}
		}
		
		this.time = time;
		
		if (restartThread == null){
			restartThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					restart(sender);
				}
			});
		}		
		
		restartThread.start();
		
		return true;
	}
	
	private void restart(CommandSender sender){
		while (time > 0 || restartThread == null){
			if (time < 60000)
				sender.sendMessage(Text.get(Text.RESTART_TIME, String.format("%s %s", time/1000, UNIT.seconds))); 
			else
				sender.sendMessage(Text.get(Text.RESTART_TIME, String.format("%tT%n", new Date(time - 60000 * 60))));
			
			long c = (time <= 1000 ? time : time <= 10000 ? 1000 : time <= 60000 ? 10000 : 60000);
			
			try {
				Thread.sleep(c);
			} catch (InterruptedException e) {}
			
			time -= c;
		}
		
		sender.sendMessage(Text.RESTART);
		
		UpdateCraft.get().getServer().dispatchCommand(sender, "stop");

	    try {
			Runtime.getRuntime().exec(UpdateCraft.get().getUpdateConfig().getServerRestartServiceCommand());
		} catch (IOException e) {
			if (UpdateCraft.get().isDebug()) e.printStackTrace();
		}
	}
		
}
