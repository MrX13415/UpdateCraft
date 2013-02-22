package de.MrX13415.UpdateCraft.Text;

import java.util.Arrays;

import org.bukkit.ChatColor;


public abstract class Text {

	private Text() {}
	
	public static String DONE = ChatColor.GREEN + "Done";
	
	public static String CONNECTION_ERROR1 = ChatColor.RED + "Error: Can't initialize download connection: %s";
	public static String CONNECTION_ERROR2 = ChatColor.RED + "Error: Can't initialize download destination: %s";
	
	public static String DATABASE_UPDATE = "Updating build database ... %3s%%";
	public static String DATABASE_INVALID_URL = ChatColor.RED + "Error: Can't update build database: Invalid URL: \"%s\"";
	public static String DATABASE_INVALID_BUILD = ChatColor.YELLOW + "Warning: Invalid build found! %s";
	public static String DATABASE_NOMOREBUILD = ChatColor.YELLOW + "Warning: Can't dertmine further build informations!";
	public static String DATABASE_LOAD = "Loading build database ...";
	public static String DATABASE_LOAD_DONE = ChatColor.GREEN + "Loading build database complete!";
	public static String DATABASE_SAVE = "Saving build database ...";
	public static String DATABASE_SAVE_DONE = ChatColor.GREEN + "Saving build database complete!";
	public static String DATABASE_LOAD_ERROR = ChatColor.RED + "Error: Can't load the build database from disk ...";
	public static String DATABASE_LOAD_INV_BUILD_POS = ChatColor.RED + "(%s: line %s)";
	public static String DATABASE_SAVE_ERROR = ChatColor.RED + "Error: Can't save the build database to disk ...";
	
	public static String CONFIG_LOAD = "Loading config ...";
	public static String CONFIG_LOAD_DONE = ChatColor.GREEN + "Loading config complete!";
	public static String CONFIG_SAVE = "Saving config ...";
	public static String CONFIG_SAVE_DONE = ChatColor.GREEN + "Saving config complete!";
	public static String CONFIG_LOAD_ERROR = ChatColor.RED + "Error: Can't load the config from disk ...";
	public static String CONFIG_SAVE_ERROR = ChatColor.RED + "Error: Can't save the config to disk ...";
	
	public static String DOWNLOAD_PROGRESS = "Downloading build #%s ... %6s%%  %9s / %9s  %9s/s";
	public static String DOWNLOAD_BUILD_INFO = "Build: #%s   Cannel: %s   Version: %s";
	public static String DOWNLOAD_BUILD_LATEST = "Latest build in cannel: %s";
	public static String DOWNLOAD_BUILD_NOTFOUND = ChatColor.RED + "Error: Build not found in database: Build #%s";
	public static String DOWNLOAD_CANNEL_NOTFOUND = ChatColor.RED + "Error: Unknow cannel: %s";

	public static String RESTART = ChatColor.DARK_PURPLE + "Restarting the Server ...";
	public static String RESTART_TIME = ChatColor.DARK_PURPLE + "Server restart in %s";
	
	public static String get(String text, Object ...arg){
		return String.format(text, arg);
	}
	
	public static String toString(String[] arg){
		return Arrays.asList(arg).toString();
	}
}
