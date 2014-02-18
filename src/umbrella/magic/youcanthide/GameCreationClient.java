package umbrella.magic.youcanthide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import umbrella.magic.youcanthide.youcanthide.Player;

public class GameCreationClient {
	/* This is a client template for the Android side of You Can't Hide
	 * The class provides methods for sending and receiving via Sockets
	 */
		String debug="";
	 	final int PORT = 9846;
		Socket smtpSocket = null;  
		ObjectInputStream ois = null;
		InputStream is = null;
		ObjectOutputStream oos=null;
		OutputStream os=null;
		String hostname = "moxie.oswego.edu";
		
		String lo,si;
		int nI, gL, id;
	
		Player p;
		
		PrintWriter out;
		Socket clientSocket = null;
		BufferedReader in=null;
		Socket echoServer = null;
		int numRequest=0;

	    public GameCreationClient(){
			openSocket();
	    }
	    
	    public void openSocket(){
			try {
				System.out.println("opening socket: " + hostname);
			   echoServer = new Socket(hostname, PORT);
			   System.out.println("opened socket");
			}
			catch (IOException e) {
			   System.out.println("Hold on IO Exception creating socket \n" + e);
			} 
				
			try {
				in = new BufferedReader(new InputStreamReader(echoServer.getInputStream()));
				os = echoServer.getOutputStream();
				oos = new ObjectOutputStream(os);
				is = echoServer.getInputStream();
				ois = new ObjectInputStream(is);
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: " + hostname);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: " + hostname);
			}
		}
	    
	    boolean createGame(String location, String size, int numIt, int gameLength){
	    	
	    	//communication stub until beans are up
	    	
	    	
	    	//check for null values
	    	if(location==null && size==null){
	    		debug="null values!";
	    		return false;
	    	}
	    	
	    	String gameString="newgame:" + location + " " + size + " " + numIt + " " + gameLength;
	    	
	    	try{
	    		oos.writeObject((String)gameString);
	    		gameString=(String)ois.readObject();
	    		
	    		String parems[] = gameString.split(":", 2);
	    		
	    		if(parems[0].equals("error")){
	    			debug=parems[1];
	    			return false;
	    		}
	    		else{
	    			parseLine(parems[1]);
	    			
	    			new MyGame(id, lo, si, nI, gL);
	    			debug="Game created successfully!";
	    			return true;
	    		}
	    		
	    	}catch (UnknownHostException e) {
				System.err.println("UnkownHostException: " + e);
			}catch (IOException e) {
				System.err.println("IOException:  " + e);
			}catch (ClassNotFoundException cnfe) {
				System.err.println("CNFE : " + cnfe);
			}	    	
	    	
	    	return false;
	    }
	    
	    void parseLine(String s){
	        String parems[] = s.split(" ", 5);
	        
	        id = Integer.parseInt(parems[0]);
	        lo = parems[1];
	        si = parems[2];
	        nI = Integer.parseInt(parems[3]);
	        gL = Integer.parseInt(parems[4]);
	        
	    }
	    
	    public void closeSocket(){
	    	try{
		    	is.close();
				ois.close();
				oos.close();
				os.close();
				in.close();
				echoServer.close();
	    	}catch (UnknownHostException e) {
				System.err.println("UnkownHostException: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
	    }
	    
	    public String getDebug(){
	    	return debug;
	    }
}
