package de.MrX13415.UpdateCraft.Command.SubCommand;

import java.text.DecimalFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.MrX13415.UpdateCraft.BuildDownload;
import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Command.UCCommand;
import de.MrX13415.UpdateCraft.Database.Build;
import de.MrX13415.UpdateCraft.Database.BuildDatabase;
import de.MrX13415.UpdateCraft.Database.Build.Cannel;
import de.MrX13415.UpdateCraft.Net.Download;
import de.MrX13415.UpdateCraft.Net.Download.NotInitializedException;
import de.MrX13415.UpdateCraft.Text.Text;

public class Command_Download extends UCCommand{

	public Command_Download() {
		super();
		
		setLabel("download");
		setAliases("dl");
		setUsage("<<buildChannel>|<buildNumber>>");
		setDescription("Download latest build from a channel or a specific one");
	}
	
	@Override
	public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
		boolean retval = false;
		if (args.length > 0){
			retval = onCommandDownload(sender, cmd, label, args);
		}
		
		return retval;
	}

	public boolean onCommandDownload(CommandSender sender, Command cmd, String label, String[] args) {
		Cannel cannel = Build.Cannel.get(args[0]);
		
		BuildDatabase db = UpdateCraft.get().getBuildDatabase();
		Build build = null;
		
		if (!cannel.equals(Cannel.Unknown)){
			
			build = db.getLatestBuild(cannel);
			
			UpdateCraft.sendMessageBoth(sender, Text.get(Text.DOWNLOAD_BUILD_LATEST, build.getCannel()));

		}else{
			try{
				int buildnr = Integer.valueOf(args[0]);
				
				build = db.getBuild(buildnr);
				
				if (build == null){
					UpdateCraft.sendMessageBoth(sender, Text.get(Text.DOWNLOAD_BUILD_NOTFOUND, buildnr));
					return true;
				}
			}catch(NumberFormatException e){
				UpdateCraft.sendMessageBoth(sender, Text.get(Text.DOWNLOAD_CANNEL_NOTFOUND, args[0]));
				return true;
			}
		}
		
		UpdateCraft.sendMessageBoth(sender,
				Text.get(Text.DOWNLOAD_BUILD_INFO,
				build.getBuildnumber(),
				build.getCannel(),
				build.getVersion()));
				
		download(build, sender);
		
		return true;
	}
	
	private void download(final Build build, final CommandSender sender){
		
		Thread dlT = new Thread(new Runnable() {
			@Override
			public void run() {

				Download d = null;
				try {
					d = new Download(build.getUrl()); //"http://download.opensuse.org/distribution/10.2/iso/dvd/openSUSE-10.2-GM-DVD-x86_64.iso");//https://github.com/MrX13415/Massband/raw/master/release/Massband.zip");
				} catch (Exception e) {
					if (UpdateCraft.get().isDebug()) e.printStackTrace();
				}
				
				d.initialize();
				
				BuildDownload bdl = new BuildDownload(build, d);
				UpdateCraft.get().addBuildDownload(bdl);
				
				try {
					d.start();
					UpdateCraft.sendMessageBoth(sender, Text.get(Text.DOWNLOAD_START, build.getBuildnumber()));
				} catch (NotInitializedException e1) {
					if (UpdateCraft.get().isDebug()) e1.printStackTrace();
				}
				
				
				int senderTimeCounter = 0;
				int msToWait = 1000;
				
				while (!d.isStoped()) {
					if (d != null) {
						String output = Text.get(Text.DOWNLOAD_PROGRESS, build.getBuildnumber(),
								new DecimalFormat("0.00").format(d.getProgress()),
								Download.humanReadableByteCount(d.getByteProgress()),
								Download.humanReadableByteCount(d.getByteSize()),
								Download.humanReadableByteCount((long) d.getSpeed()));
		
						UpdateCraft.sendConsoleMessage(output);
						
						
						if (senderTimeCounter >= 5000){
							senderTimeCounter = 0;
							if (sender != null) UpdateCraft.sendMessage(sender, output);
						}
					}
					
					try {Thread.sleep(msToWait);} catch (InterruptedException e) {}
					senderTimeCounter += msToWait;
				}
				
				UpdateCraft.sendMessageBoth(sender, Text.get(Text.DOWNLOAD_COMPLETED, build.getBuildnumber()));
				
			}
		});
		dlT.setName(UpdateCraft.get().getNameSpecial() + "#Download/");
		dlT.start();
		
	}
}
