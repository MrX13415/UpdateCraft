package de.MrX13415.UpdateCraft.Text;

import org.bukkit.ChatColor;

public abstract class Text {

	private Text() {}
	
	public static String DONE = ChatColor.GREEN + "Done";
	
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
	
	public static String DOWNLOAD_PROGRESS = "Downloading build #%s ... %6s%%  %9s / %9s  %9s";
	public static String DOWNLOAD_BUILD_INFO = "Build: #%s   Cannel: %s   Version: %s";
	public static String DOWNLOAD_BUILD_LATEST = "Latest build in cannel: %s";
	public static String DOWNLOAD_BUILD_NOTFOUND = ChatColor.RED + "Error: Build not found in database: Build #%s";
	public static String DOWNLOAD_CANNEL_NOTFOUND = ChatColor.RED + "Error: Unknow cannel: %s";
	
	
	public static String get(String text, Object ...arg){
		return String.format(text, arg);
	}
}
