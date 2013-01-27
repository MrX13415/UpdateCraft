package de.MrX13415.UpdateCraft.Database;
import java.net.URL;


public class Build {

	public enum Cannel{
		Recommended, Beta, Development, Unknown;
		
		public static Cannel get(String arg0){
			if (arg0.trim().toLowerCase().startsWith("r")) return Recommended;
			if (arg0.trim().toLowerCase().startsWith("b")) return Beta;
			if (arg0.trim().toLowerCase().startsWith("d")) return Development;
			else return Unknown;
		}
	}
	
	private int buildnumber;
	private String version;
	private Cannel cannel;
	private URL url;
	
	public Build(int buildnumber, String version, Cannel cannel, URL url) {
		super();
		this.buildnumber = buildnumber;
		this.version = version;
		this.cannel = cannel;
		this.url = url;
	}

	public int getBuildnumber() {
		return buildnumber;
	}

	public String getVersion() {
		return version;
	}

	public Cannel getCannel() {
		return cannel;
	}

	public URL getUrl() {
		return url;
	}
	
	public String toString(){
		return super.toString() + "[buildnumber=" + buildnumber + ";version=" + version + ";cannel=" + cannel + ";url=" + url +"]";
	}
}
