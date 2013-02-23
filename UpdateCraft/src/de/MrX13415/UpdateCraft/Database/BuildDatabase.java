package de.MrX13415.UpdateCraft.Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import de.MrX13415.UpdateCraft.UpdateCraft;
import de.MrX13415.UpdateCraft.Database.Build.Cannel;
import de.MrX13415.UpdateCraft.Net.Download;
import de.MrX13415.UpdateCraft.Net.Download.Destionation;
import de.MrX13415.UpdateCraft.Net.Download.NotInitializedException;
import de.MrX13415.UpdateCraft.Text.Text;



public class BuildDatabase {

	private String bdbFileName = "BuildDB.yml";
	
	private ArrayList<Build> buildDBContent = new ArrayList<Build>();
	private int pageCount = 1;

	private Thread uDBT = new Thread();
	
	public Build getBuild(int buildnummber){
		for (Build build : buildDBContent) {
			if (build.getBuildnumber() == buildnummber) return build;
		}
		return null;
	}
	
	
	public Build getLatestBuild(){
		return getLatestBuild(null);
	}
	
	public Build getLatestBuild(Build.Cannel cannal){
		int latestBuildNummber = 0;
		Build latestBuild = null;
		
		for (Build build : buildDBContent) {
			if (cannal == null || build.getCannel() == cannal){
				if (build.getBuildnumber() > latestBuildNummber){
					latestBuildNummber = build.getBuildnumber();
					latestBuild = build;
				}
			}
		}
		return latestBuild;
	}	
	
	public void updateDatabaseFull(){
		createDatabase(false, true);
	}
	
	public void updateDatabase(){
		createDatabase(false, false);
	}
	
	public void createDatabase(){
		createDatabase(true, false);
	}
	
	public void createDatabase(final boolean reCreate, final boolean fullUpdate){
		
		uDBT = new Thread(new Runnable() {
			
			@Override
			public void run() {
				UpdateCraft.sendMessageAll(Text.DATABASE_UPDATE_START);
				
				if (reCreate) buildDBContent.clear();
						
				UpdateCraft.sendConsoleMessage(Text.get(Text.DATABASE_UPDATE_PROCESS, 0));
				
				for (int i = 1; i <= pageCount; i++) {
					String url = "http://dl.bukkit.org/downloads/craftbukkit/?page="+i;
					
					Download dl;
					try {
						dl = new Download(url);
					} catch (MalformedURLException e) {
						UpdateCraft.sendConsoleMessage(Text.get(Text.DATABASE_INVALID_URL, url));
						if (UpdateCraft.get().isDebug()) e.printStackTrace();
						break;
					}
					dl.getDlThread().setName(UpdateCraft.get().getNameSpecial()+"#Download"+dl.toString());
					dl.setDestionation(Destionation.StringVar);
					dl.initialize();
					try {
						dl.start();
					} catch (NotInitializedException e) {}	
					
					
					while (!dl.isStoped()){
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
					}
					
					String page = dl.getContent();
					
					findPageCount(page);
					if (!AddBulidsFromPageToDatabase(page, reCreate, fullUpdate)) break;
					
					int perc = (int) Math.round((100d / (double)pageCount) * i);
					UpdateCraft.sendConsoleMessage(Text.get(Text.DATABASE_UPDATE_PROCESS, perc));
				}
				UpdateCraft.sendMessageAll(Text.DATABASE_UPDATE_COMPLETED);
				save();
			}
		});
		
		if (!uDBT.isAlive()){
			uDBT.setName(UpdateCraft.get().getNameSpecial() + "#UpdateDatabase");
			uDBT.start();
		}
	}
	
	public void findPageCount(String page){
		int ls = page.indexOf("<span class=\"paginatorPageCount\">");
		int srts = page.indexOf("\">", ls) + 2;
		int stre = page.indexOf("</", srts);
		String pcStr = page.substring(srts, stre);
		pcStr = pcStr.split(" of ")[1];
		pageCount = Integer.valueOf(pcStr);
	}
	
	public boolean AddBulidsFromPageToDatabase(String page, boolean all, boolean fullUpdate){
	
		//grep out the version table out of the page ...
		int p1 = page.indexOf("<table class=\"versionsTable\">");
		int p2 = page.indexOf("</table>", p1);
		
		String versionTable = page.substring(p1, p2).trim();

		//grep build infos out of the version table ...
		int buildInfoIndex = 0;
		String buildInfos = "";

		do{
			try {
				int buildInfoP1 = versionTable.indexOf("<tr class=\"chan-", buildInfoIndex);
				int buildInfoP2 = versionTable.indexOf("</tr>", buildInfoP1);
				
				buildInfoIndex = buildInfoP2;
				
				buildInfos = new String(versionTable.substring(buildInfoP1, buildInfoP2).trim());
				
				/*
				 * WARNING: Bug work around:
				 * 
				 * Sometimes there are NULL chars (ASCII value 0) in the String
				 * The Problem is: The length() function returns a higher value,
				 * than the Text actually is ...
				 * This removes this chars ... 
				 */
				buildInfos = buildInfos.replace(String.valueOf((char)0), "");

				int bip1 = buildInfos.indexOf("<th><a href=\"");
				int bip2 = buildInfos.indexOf("\">#", bip1);
				bip1 = bip2;
				bip2 = buildInfos.indexOf("</a>", bip2);
				String build = buildInfos.substring(bip1 + 3, bip2);

				bip1 = buildInfos.indexOf("<td>", bip2);
				bip2 = buildInfos.indexOf("</td>", bip1);
				String version = buildInfos.substring(bip1 + 4, bip2);
			
				bip1 = buildInfos.indexOf("<td><a href=\"", bip2);
				bip2 = buildInfos.indexOf("\">", bip1);
				bip1 = bip2;
				bip2 = buildInfos.indexOf("</a>", bip2);
				String type = buildInfos.substring(bip1 + 2, bip2);
						
				bip1 = buildInfos.indexOf("<td class=\"downloadLink\">", bip2);
				bip2 = buildInfos.indexOf("<a class=\"tooltipd\"", bip1);
				bip1 = buildInfos.indexOf(" href=\"", bip2);
				bip2 = buildInfos.indexOf("\">", bip1);
				String url = buildInfos.substring(bip1 + 7, bip2);
				
				try {
					Build b = new Build(Integer.valueOf(build),
							            version,
							            Build.Cannel.get(type),
							            new URL("http://dl.bukkit.org" + url));
					
					if (!all){
						
						Build buildInDB = getBuild(b.getBuildnumber());
						
						if (fullUpdate){
							if (buildInDB == null){
								buildDBContent.add(b);
							}else {
								continue; 
							}
						}else{
							if (buildInDB != null){
								UpdateCraft.sendConsoleMessage(Text.get(Text.DATABASE_UPDATE_PROCESS, 100));
								return false;
							}else buildDBContent.add(b);
						}
							
					}else buildDBContent.add(0, b);
					
				} catch (Exception e) {
					UpdateCraft.sendConsoleMessage(Text.DATABASE_INVALID_BUILD);
					if (UpdateCraft.get().isDebug()) e.printStackTrace();
				}
			} catch (Exception e) {
				UpdateCraft.sendConsoleMessage(Text.DATABASE_NOMOREBUILD);
				if (UpdateCraft.get().isDebug()) e.printStackTrace();
			}

		}while ((buildInfoIndex + buildInfos.length()) < versionTable.length());
		
		return true;  
	}
	
	public void printDB(){
		for (Build build : buildDBContent) {
			System.out.println(build.toString());
		}
	}

	public void clearDatabase() {
		buildDBContent.clear();
	}

	public boolean removeBuild(Build build) {
		return buildDBContent.remove(build);
	}
	
	public void load(){
		
		UpdateCraft.sendConsoleMessage(Text.DATABASE_LOAD);
		
		BufferedReader br = null;
		
		try {
			File bdbf = new File(UpdateCraft.get().getPluginFilesDir() + bdbFileName);
			br = new BufferedReader(new FileReader(bdbf));
	
			int lineIndex = 0;
			
			while (br.ready()) {
				String line = br.readLine();
				lineIndex++;
				
				if (!line.startsWith(":")) continue;
				
				String[] build = line.trim().substring(1).split(";");
				
				Build b = null;
				int bN = 0;
				String bV = "";
				Cannel bC = Cannel.Unknown;
				URL bU = null;
				
				try {
					bN = Integer.valueOf(build[0].trim());
					bV = build[1].trim();
					bC = Build.Cannel.valueOf(build[2].trim());
					bU = new URL(build[3].trim());
					
					b = new Build(bN, bV, bC, bU);		
				} catch (Exception e) {
					UpdateCraft.sendConsoleMessage(Text.get(
							Text.DATABASE_INVALID_BUILD,
							Text.get(Text.DATABASE_LOAD_INV_BUILD_POS,
									bdbf.getName(), lineIndex)));
					continue;
				}			
				
				if (getBuild(bN) != null) continue;
				buildDBContent.add(b);
			}
			
			UpdateCraft.sendConsoleMessage(Text.DATABASE_LOAD_DONE);
		} catch (Exception e) {
			UpdateCraft.sendConsoleMessage(Text.get(Text.DATABASE_LOAD_ERROR, e));
			if (UpdateCraft.get().isDebug()) e.printStackTrace();
		}finally{
			try {
				if(br != null) br.close();
			} catch (IOException e) {}
		}
		

	}
	
	public void save(){
		
		UpdateCraft.sendConsoleMessage(Text.DATABASE_SAVE);
		
		PrintWriter pw = null;
		
		try {
			File directory = new File(UpdateCraft.get().getPluginFilesDir());
			if (! directory.exists()) directory.mkdir();
			
			pw = new PrintWriter(new FileWriter(UpdateCraft.get().getPluginFilesDir() + bdbFileName));
			
			pw.write("#\r\n");
			pw.write(String.format("# %s\r\n", UpdateCraft.get().getDescription().getName()));
			pw.write(String.format("# Version: %s\r\n", UpdateCraft.get().getDescription().getVersion()));
			pw.write(String.format("# Author: %s\r\n", UpdateCraft.get().getDescription().getAuthors()));
			pw.write("#\r\n");
			pw.write("# Build Database\r\n");
			pw.write("#\r\n");
			pw.write("#   Version: 1.0\r\n");
			pw.write(String.format("#   Date:    %1$te/%1$tm/%1$tY at %1$tH:%1$tM:%1$tS\r\n", new Date()));
			pw.write("#\r\n");
			
			for (Build build : buildDBContent) {
				
				//:   2555 ; 1.4.7-R0.4      ; development     ; http://dl.[...].org[...]   
				
				String fileFormatLine = ": %6s ; %-15s ; %-15s ; %s \r\n";
				String dbFileLine = String.format(fileFormatLine, 
						build.getBuildnumber(),
						build.getVersion(),
						build.getCannel(),
						build.getUrl());
				
				pw.write(dbFileLine);
			}
			pw.write("#\r\n");
			pw.write("# EOF\r\n");
			pw.write("#\r\n");
			
			UpdateCraft.sendConsoleMessage(Text.DATABASE_SAVE_DONE);
		} catch (Exception e) {
			UpdateCraft.sendConsoleMessage(Text.get(Text.DATABASE_SAVE_ERROR, e));
		}finally{
			try {
				if (pw != null) pw.close();
			} catch (Exception e) {}
		}
		
	}
	
}
