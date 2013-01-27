package de.MrX13415.UpdateCraft.Net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import de.MrX13415.UpdateCraft.UpdateCraft;


public class Download implements Runnable{

	public enum Destionation{
		BinFile, StringVar;
	}
	
	public enum State{
		New, Initialized, Starting, Skiping, Downloading, Stoped, Paused;
	}
	
	// **** Source / Destination ****
	private URL sourceUrl;
	private Destionation destionation = Destionation.BinFile;
	private String destionationDir = "downloads/";
	private String destionationFile = "file.bin"; 
	private File destionationPath = new File(destionationDir, destionationFile);
	private String content;

	// **** Connection ****
	private BufferedInputStream input;
	private BufferedOutputStream output; 
	
	// **** Process ****
	private Thread dlThread;
	private final int bufferSize = 65536;	//in bytes (64K)
	private byte[] buffer;
	private boolean paused;
	@SuppressWarnings("unused")
	private long startFromByte = 0;
	
	// **** Progress ****
	private State state = State.New;
	private long byteSize;			// in bytes
	private long byteProgress;		// in bytes
	private double progress;		// in &
	private long startTime;			// in ms
	private long endTime;			// in ms
	private long runTime;		// in ms
	private double speed;			// in bytes
	
	// **** Information ****
	private String encoding = "";
	
	public Download(String url) throws MalformedURLException{
		this(new URL(url));
	}
	
	public Download(URL url){
		this.sourceUrl = url;
		initThread();
	}
	
	
	public void initialize(){
		try {
			initConnection();
		} catch (IOException e) {
			System.err.println("Can not initialize connection: ");
			e.printStackTrace();
		}
		
		try {
			initDestination();
		} catch (IOException e) {
			System.err.println("Can not initialize destination: ");
			e.printStackTrace();
		}
		
		state = State.Initialized;
	}

	private HttpURLConnection createConnection() throws IOException{

		//initialize a connection to the given URL ...
		HttpURLConnection conn = (HttpURLConnection) sourceUrl.openConnection();
 		conn.setInstanceFollowRedirects(false);
		
		// Set request properties
 		conn.setRequestProperty("User-Agent", UpdateCraft.get().getNameSpecial() + " " + 
 												UpdateCraft.get().getServer().getName()+"/"+
 												UpdateCraft.get().getServer().getVersion());
 		
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		conn.connect();
		
//		for (String h : conn.getHeaderFields().keySet()) {
//			System.out.println(h + ": " + conn.getHeaderFields().get(h));
//		}
		
		String location = conn.getHeaderField("Location");
		
		if ((conn.getResponseCode() / 100) == 3 && location != null){
			sourceUrl = new URL(location);
			conn = createConnection();
		}
		return conn;
	}
	
	/** Initializes a connection to download the content of the given URL
	 * (Supported encodings: GZip, Deflate (ZLib))
	 * @throws IOException
	 */
	public void initConnection() throws IOException{
		
		HttpURLConnection conn = createConnection();

		//determine file length ...
		byteSize = conn.getContentLengthLong();
			
		if (destionation.equals(Destionation.BinFile)){
			//determine the filename ...
			String raw = conn.getHeaderField("Content-Disposition");
			// raw = "attachment; filename=abc.bin"
			if(raw != null && raw.indexOf("=") != -1) {
			    String fileName = raw.split("=")[1];
			    destionationPath = new File(destionationDir, fileName);
			}else{
				String fileName = sourceUrl.toString().substring(sourceUrl.toString().lastIndexOf('/')+1, sourceUrl.toString().length());
				destionationPath = new File(destionationDir, fileName);
			}
		}
		
		encoding = conn.getContentEncoding();
		
		// create the appropriate stream wrapper based on
		// the encoding type
		if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
		    input = new BufferedInputStream(new GZIPInputStream(conn.getInputStream()));
		    
		} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
		    input = new BufferedInputStream(new InflaterInputStream(conn.getInputStream(), new Inflater(true)));
		    
		} else {
		    input = new BufferedInputStream(conn.getInputStream());
		    
		}
	}
	
	//bugy
	public void initDestination() throws IOException{
		if (destionation.equals(Destionation.StringVar)){
			content = "";
		}else{
			new File(destionationDir).mkdirs();
			destionationPath.createNewFile(); 
			output = new BufferedOutputStream(new FileOutputStream(destionationPath));
		}
	}
	
	private void initThread(){
		dlThread = new Thread(this);
		dlThread.setName(this.toString());
	}

	public void start() throws NotInitializedException{
		start(0);
	}
	
	public void start(long bytePostion) throws NotInitializedException{
		
		if (state != State.Initialized) throw new NotInitializedException(); 
		
		startFromByte = bytePostion;
		state = State.Starting;
		dlThread.start();
	}
	
	public void stop(){
		
	} 
	
	public void pause(){
		paused = !paused;
		
		synchronized (dlThread){ 
			if (!paused){
				dlThread.notify();
			}
		}
	}
		
	public void calculatePrecentage(){
		 progress = (100d / byteSize) * byteProgress;
	}
	
	public void calculateSpeed(){
		 speed = ((double)byteProgress) / ( ((double)runTime) / 1000d );
	}

	public void close(){
		try {
			if (input != null) input.close();
			if (output != null) output.close();
		} catch (IOException e) {}
	}
	
	public void initDownload(){
		byteProgress = 0;
		buffer = new byte[bufferSize];
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		try {
			
			initDownload();
			int bytesReaded = 0;
			long runStartTime = startTime;
			long lastRunTime = 0;	//runtime before the last break ...
			long packageCounter = 0;
			long packageCounterMax = 1;
			long bytesAtPackageCounterStart = 0;
			long packageCounterStartTime = 0;
			
			do{	
				while (paused){
					speed = 0;
					state = State.Paused;
					synchronized (dlThread) {
						try {dlThread.wait();} catch (InterruptedException e) {}
					}
				}
				
				if (isPaused()){
					runStartTime = now();
					lastRunTime = runTime;
				}

				state = State.Downloading;
				
				if (packageCounter == 0){
					packageCounterStartTime = now();
					bytesAtPackageCounterStart = byteProgress;
				}
				
				
				//read one package ...
				bytesReaded = input.read(buffer);
				
				if (bytesReaded >= 0){
					//write one package ...
					if (destionation.equals(Destionation.BinFile))
						output.write(buffer, 0, bytesReaded);
					
					if (destionation.equals(Destionation.StringVar))
						content += new String(buffer);
					
						
					packageCounter++;
					byteProgress += bytesReaded;
				}
				
				//calculate current run time ...
				runTime = (now() - runStartTime) + lastRunTime;
				
				//wait until 20 packages, before calculate speed ...
				if (packageCounter >= packageCounterMax){
					if (packageCounterMax < 20){
						packageCounterMax++;	
					}
					packageCounter = 0;
					
					//calculate speed ...
					speed = ((double)(byteProgress - bytesAtPackageCounterStart)) / ( ((double)(now() - packageCounterStartTime)) / 1000d);
				}
				
				calculatePrecentage();
				
			}while (bytesReaded >= 0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close();
			state = State.Stoped;
		}
	}

	/** retuns the given bytes human readable
	 *  
	 * @param bytes	the bytes count
	 * @return the human readable bytes ...
	 */
	public static String humanReadableByteCount(long bytes) {
		return humanReadableByteCount(bytes, true);
	}
	
	/** retuns the given bytes human readable
	 *  
	 * @param bytes	the bytes count
	 * @param si	return as SI Units ?
	 * @return the human readable bytes ...
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	private long now(){
		return System.currentTimeMillis();
	}
	
	public boolean isNew() {
		return state == State.New;
	}
	
	public boolean isInitialized() {
		return state == State.Initialized;
	}
	
	public boolean isDownloading() {
		return state == State.Downloading;
	}
	
	public boolean isPaused() {
		return state == State.Paused;
	}
	
	public boolean isStoped() {
		return state == State.Stoped;
	}

	public Destionation getDestionation() {
		return destionation;
	}

	public void setDestionation(Destionation destionation) {
		this.destionation = destionation;
	}

	public String getDestionationDir() {
		return destionationDir;
	}

	public void setDestionationDir(String destionationDir) {
		this.destionationDir = destionationDir;
	}

	public String getDestionationFile() {
		return destionationFile;
	}

	public void setDestionationFile(String destionationFile) {
		this.destionationFile = destionationFile;
	}

	public URL getSourceUrl() {
		return sourceUrl;
	}

	public File getDestionationPath() {
		return destionationPath;
	}

	public String getContent() {
		return content;
	}

	public Thread getDlThread() {
		return dlThread;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public State getState() {
		return state;
	}

	public long getByteSize() {
		return byteSize;
	}

	public long getByteProgress() {
		return byteProgress;
	}

	public double getProgress() {
		return progress;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getRunTime() {
		return runTime;
	}

	public double getSpeed() {
		return speed;
	}

	public String getEncoding() {
		return encoding;
	}
	
	public class NotInitializedException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8461063977016495767L;

		@Override
		public String getMessage() {
			return "Download is not initialized yet: call \"initialize()\" first";
		}
	}
}
