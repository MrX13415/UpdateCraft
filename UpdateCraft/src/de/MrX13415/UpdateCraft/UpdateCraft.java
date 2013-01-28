package de.MrX13415.UpdateCraft;


import java.net.URL;
import java.util.ArrayList;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import de.MrX13415.UpdateCraft.Command.Command_Restart;
import de.MrX13415.UpdateCraft.Command.Command_Updatecraft;
import de.MrX13415.UpdateCraft.Database.BuildDatabase;
import de.MrX13415.UpdateCraft.Net.Download;
import de.MrX13415.UpdateCraft.Net.Download.Destionation;
import de.MrX13415.UpdateCraft.Net.Download.NotInitializedException;

/**
 * 
 * @author Oliver
 *
 *c-log: * cmd code improvemens ...
 *
 */
public class UpdateCraft extends JavaPlugin{

	private static UpdateCraft updateCraft;
	
	private BuildDatabase buildDatabase = new BuildDatabase();
	
	
	
	public UpdateCraft() {
		super();
		updateCraft = this;
	}

	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onLoad() {
		
	}
	
	
	@Override
	public void onEnable() {  
		
        //register events ...
//        getServer().getPluginManager().registerEvents(pListener, this);
//        getServer().getPluginManager().registerEvents(bListener, this);
//        getServer().getPluginManager().registerEvents(eListener, this);

		
		
		
		
		/* /UpdateCraft database 
		 *                       update
		 *              
		 *              download
		 *                       buildnummber 
		 *                       rec...
		 *                       beta...
		 *                       devel...
		 *              
		 *              
		 *             
		 *              
		 */

		
		//register commands ...
		try {
			
			PluginCommand updatecraft;
			updatecraft = this.getCommand("updatecraft");
			updatecraft.setExecutor(new Command_Updatecraft());
			updatecraft.setUsage("");
			updatecraft.setDescription("");
			
			PluginCommand restart;
			restart = this.getCommand("restart");
			restart.setExecutor(new Command_Restart(this));
			restart.setUsage("");
			restart.setDescription("");
			
			ArrayList<String> aliases = new ArrayList<>();
			aliases.add("uc");
			aliases.add("ucraft");
			aliases.add("udc");
			aliases.add("upc");
			
			updatecraft.setAliases(aliases);
			
			
		} catch (Exception e) {
			getLogger().warning("[" + getDescription().getName() + "] Error: Commands not definated in 'plugin.yml'");
		}

		
		
	}
	
	
	
	
	
	
	
	
	
	
	
		
	
	public static void main(String[] aregs){
			
	UpdateCraft u = new UpdateCraft();

		u.getBuildDatabase().createDatabase();
	
//		System.out.println("Geting builds ...");
//		BuildDatabase db = new BuildDatabase();
//		db.updateDatabase();
//		
//		Build b = db.getLatestBuild(Build.Cannel.Recommended);
//		System.out.println("Latest build: ");
//		System.out.println(b.toString());
//
//		Downloade(b.getUrl());
		
	}

	
	public static void Downloade(URL url){
		
		Download d = null;
		try {
			d = new Download(url); //"http://download.opensuse.org/distribution/10.2/iso/dvd/openSUSE-10.2-GM-DVD-x86_64.iso");//https://github.com/MrX13415/Massband/raw/master/release/Massband.zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		d.setDestionation(Destionation.StringVar);
		d.initialize();
		
		try {
			d.start();
		} catch (NotInitializedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//d.start(1024*1024*1024);
		
		while (!d.isStoped()) {
			if (d != null) {
				System.out.println(Download.humanReadableByteCount(d
						.getByteProgress())
						+ "   "
						+ Download.humanReadableByteCount(d.getByteSize())
						+ "   "
						+ Math.round(d.getProgress() * 100d)/100d
						+ "%   "
						+ Download.humanReadableByteCount((long) d.getSpeed())
						+ "/s   "
						+ d.getState()
						+ "   "
						+ d.getRunTime()
						+ "ms   "
						+ (System.currentTimeMillis() - d.getStartTime()) 
						+ "ms   ");
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Done");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("---------------------------------");
		System.out.println(d.getContent());
		System.out.println("---------------------------------");
		
	}
	
	
	
	public BuildDatabase getBuildDatabase() {
		return buildDatabase;
	}

	
	public String getNameSpecial(){
		try {
			return getDescription().getName() + "/" + getDescription().getVersion();	
		} catch (Exception e) {
			return "UpdateCraft/<version>";
		}
	}

	public static UpdateCraft get() {
		return updateCraft;
	}
	
	public static void sendConsoleMessage(String message) {
		try {
			updateCraft.getServer().getConsoleSender().sendMessage("[" + updateCraft.getDescription().getName() + "] " + message);

		} catch (Exception e) {
			System.out.println("[UpdateCraft] " + message);
			
		}
	}
	
}
