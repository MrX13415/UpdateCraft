package de.MrX13415.UpdateCraft;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.MrX13415.UpdateCraft.Command.Command_Restart;
import de.MrX13415.UpdateCraft.Command.Command_Updatecraft;
import de.MrX13415.UpdateCraft.Command.SubCommand.Database.Command_Save;
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
	
	//**** DEBUG ****
	private boolean debug = false;
	//***************
	
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
		buildDatabase.save();
	}
	
	@Override
	public void onLoad() {
		config.load();
		buildDatabase.load();
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
					if (UpdateCraft.get().isDebug()) e.printStackTrace();
				}
			}
		}
		
	}

	@Deprecated
	public static void Downloade(URL url){
		
		Download d = null;
		try {
			d = new Download(url); //"http://download.opensuse.org/distribution/10.2/iso/dvd/openSUSE-10.2-GM-DVD-x86_64.iso");//https://github.com/MrX13415/Massband/raw/master/release/Massband.zip");
		} catch (Exception e) {
			if (UpdateCraft.get().isDebug()) e.printStackTrace();
		}
		
		d.setDestionation(Destionation.StringVar);
		d.initialize();
		
		try {
			d.start();
		} catch (NotInitializedException e1) {
			// TODO Auto-generated catch block
			if (UpdateCraft.get().isDebug()) e1.printStackTrace();
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
				if (UpdateCraft.get().isDebug()) e.printStackTrace();
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

	/** Send a message to the server console<br><br>
	 * Full color support with <code>ChatColor</code>
	 * Multi-line support<br><br>
	 * The message looks like: <br>
	 *    <code>[UpdateCraft] This is a message!</code><br>
	 * @param message
	 */
	public static void sendConsoleMessage(String message) {
		try {
			
			String[] lines = message.split("\n");
			
			for (String line : lines) {
				updateCraft.getServer().getConsoleSender().sendMessage("[" + updateCraft.getDescription().getName() + "] " + line);
			}
			
		} catch (Exception e) {
			System.out.println("[" + updateCraft.getDescription().getName() + "] " + message);
		}
	}
	
	/** Sends a message to the given sender<br>
	 * (Player or CONSOLE)<br><br>
	 * Full color support with <code>ChatColor</code>
	 * @param sender The Sender 
	 * @param message The Message
	 */
	public static void sendMessage(CommandSender sender, String message) {
		if (sender.equals(sender.getServer().getConsoleSender())) 
			UpdateCraft.sendConsoleMessage(message);
		else
			sender.sendMessage(message);		
	}
	
	/**Send a message to the console and the sender
	 * 
	 * @param sender
	 * @param message
	 */
	public static void sendMessageBoth(CommandSender sender, String message) {
		if (sender.equals(sender.getServer().getConsoleSender())) 
			UpdateCraft.sendConsoleMessage(message);
		else{
			sendConsoleMessage(message);
			sender.sendMessage(message);
		}
	}
	
	public static void sendMessageAll(String message) {
		get().getServer().broadcastMessage(message);
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

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
}
