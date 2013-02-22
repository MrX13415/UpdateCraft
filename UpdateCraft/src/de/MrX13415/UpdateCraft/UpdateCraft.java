package de.MrX13415.UpdateCraft;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import de.MrX13415.UpdateCraft.Command.Command_Restart;
import de.MrX13415.UpdateCraft.Command.Command_Updatecraft;
import de.MrX13415.UpdateCraft.Config.Config;
import de.MrX13415.UpdateCraft.Database.BuildDatabase;
import de.MrX13415.UpdateCraft.Net.Download;
import de.MrX13415.UpdateCraft.Net.Download.Destionation;
import de.MrX13415.UpdateCraft.Net.Download.NotInitializedException;

/**
 * 
 * @author Oliver
 *
 *c-log: * cmd code improvemens ...
 *  * add: config
 *  * working restart cmd...
 *  
 *
 */
public class UpdateCraft extends JavaPlugin{

	private String pluginFilesDir = "plugins/UpdateCraft/";
	
	private static UpdateCraft updateCraft;
	
	private BuildDatabase buildDatabase = new BuildDatabase();
	private ArrayList<BuildDownload> buildDLs = new ArrayList<>(); 
	private Config config = new Config();
	
	
	public UpdateCraft() {
		super();
		updateCraft = this;
	}

	@Override
	public void onDisable() {
		config.save();
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
		
		//register commands ...
		try {
			
			Command_Updatecraft cmd_UC = new Command_Updatecraft();
			cmd_UC.initPluginCommand();
			Command_Restart cmd_R = new Command_Restart();
			cmd_R.initPluginCommand();
			
		} catch (Exception e) {
			getLogger().warning("[" + getDescription().getName() + "] Error: Commands not definated in 'plugin.yml'");
		}

		
		
	}
	
	public static void main(String[] args){

		if (args.length > 0){
			if (args[0].equalsIgnoreCase("--restart_server")){
			
				System.out.println("");
				System.out.println("[Minecraft][Bukkit][UpdateCraft] Starting in 10 seconds ...");
				System.out.println("");
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
				
				System.out.println("[Minecraft][Bukkit][UpdateCraft] Starting in 05 seconds ...");
				System.out.println("");
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
				
				System.out.println("");
				System.out.println("[Minecraft][Bukkit][UpdateCraft] Starting server ...");
				System.out.println("");
				
				System.out.println("[Minecraft][Bukkit][UpdateCraft] Executing: " + new Config().getServerStartCommand());
				
			    try {
					Runtime.getRuntime().exec(new Config().getServerStartCommand());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
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
	
	public String getNameSpecial(){
		try {
			return getDescription().getName() + "/" + getDescription().getVersion();	
		} catch (Exception e) {
			return "UpdateCraft/<version>";
		}
	}

	public static void sendConsoleMessage(String message) {
		try {
			
			String[] lines = message.split("\n");
			
			for (String line : lines) {
				updateCraft.getServer().getConsoleSender().sendMessage("[" + updateCraft.getDescription().getName() + "] " + line);
			}
			
		} catch (Exception e) {
			System.out.println("[UpdateCraft] " + message);
		}
	}

	public static UpdateCraft get() {
		return updateCraft;
	}
	
	public String getPluginFilesDir() {
		return pluginFilesDir;
	}

	public void setPluginFilesDir(String pluginFilesDir) {
		this.pluginFilesDir = pluginFilesDir;
	}

	public BuildDatabase getBuildDatabase() {
		return buildDatabase;
	}

	public Config getUpdateConfig() {
		return config;
	}

	public ArrayList<BuildDownload> getBuildDownloads() {
		return buildDLs;
	}

	public void addBuildDownload(BuildDownload buildDL) {
		this.buildDLs.add(buildDL);
	}

	public void removeBuildDownload(BuildDownload buildDL) {
		this.buildDLs.remove(buildDL);
	}
	
	public void removeBuildDownload(int index) {
		this.buildDLs.remove(index);
	}
	
}
