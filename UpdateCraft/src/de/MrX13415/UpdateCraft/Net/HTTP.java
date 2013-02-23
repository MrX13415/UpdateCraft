package de.MrX13415.UpdateCraft.Net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

@Deprecated
public class HTTP {

	boolean pout = true;
	HTTP thiso = this;
	
	@Deprecated
	public String getPage(String urlStr){
//		System.out.print("Receave the file: ");
		try{
			Thread pot = new Thread(new Runnable() {
				@Override
				public void run() {
					while (pout){
//						System.out.print("#");
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
					}
//					System.out.println("");
				}
			});
			pot.start();
			
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// allow both GZip and Deflate (ZLib) encodings
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			String encoding = conn.getContentEncoding();
			
	
			InputStream inStr = null;
	
			// create the appropriate stream wrapper based on
			// the encoding type
			if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
			    inStr = new GZIPInputStream(conn.getInputStream());
			} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
			    inStr = new InflaterInputStream(conn.getInputStream(),
			      new Inflater(true));
			} else {
			    inStr = conn.getInputStream();
			}
			
			

			int ib = 0;
			String out = "";
			
			do{
				ib = inStr.read();
				out += (char)ib;
			}while (ib != -1);
			pout = false;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			return out;
		}catch (Exception e){
			System.err.println("Can't receave the file ...");
			e.printStackTrace();
		}

		return "";
	}
}
