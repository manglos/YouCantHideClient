package umbrella.magic.youcanthide;

import java.io.*;
import java.net.*;

import android.os.AsyncTask;

import umbrella.magic.youcanthide.youcanthide.*;


/* This is a client template for the Android side of You Can't Hide
 * The class provides methods for sending and receiving via Sockets
 */
public class RegistrationClient{
		String debug="";
	 	final int PORT = 9845;
		Socket smtpSocket = null;  
		ObjectInputStream ois = null;
		InputStream is = null;
		ObjectOutputStream oos=null;
		OutputStream os=null;
		String hostname = "moxie.oswego.edu";
		String un, pw, fn, ln, pn;
		int xp;
		
		PrintWriter out;
		Socket clientSocket = null;
		BufferedReader in=null;
		Socket echoServer = null;
		int numRequest=0;

	    public RegistrationClient(){
			//openSocket();
	    }
	    
	    
	    //Open a socket for communication with the server
	    public void openSocket(){
			
			try {
			System.out.println("opening socket: " + hostname + ":"+PORT);
			   echoServer = new Socket(hostname, PORT);
			   debug="Connected to the You Can't Hide Server!";
			}
			catch (IOException e) {
			   System.out.println("Hold on IO Exception creating socket \n" + e);
			   debug="Could not connect to the You Can't Hide Server!";
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
	    
	    //This method takes the players phone number and checks if they're already registered
	    public boolean userExists(String phoneNumber){
	    	String playerString;
	    	
	    	//make sure we have a valid number
	    	if(phoneNumber==null){
	    		debug="null phoneNumber";
	    		return false;
	    	}
	    	
	    	try{
	    		//sending the phonenumber with the 'search' function
		    	oos.writeObject(new String("search:" + phoneNumber));
		    	//receive the result from the server
		    	playerString=(String)ois.readObject();
		    	
		    	//parse the line about the function and value
		    	String parems[] = playerString.split(":", 2);
		    	
		    	
		    	//if nouser function received, set the debug field and return false
		    	if(parems[0].equals("nouser")){
		    		debug=parems[1];
		    		return false;
				}
		    	//else populate a player with the users info, return true
		    	else{
		    		parseLine(parems[1]);
					new Me(fn,ln,un,pw,pn, xp, false);
					debug="User exists";
		    		return true;
		    	}
	    	}catch(IOException ioe){
	    		System.err.println(ioe);
	    		debug="io exception";
	    	}catch(ClassNotFoundException cnfe){
				System.err.println(cnfe);
				debug="class not found";
			}
	    	return false;
	    }
	    
	    //This method sends a string to the server in order to add a player to it's database
	    public boolean createPlayer(String userName, String password, String passwordConfirm, String firstName, String lastName, String phonenumber){
	    	
	    		if(userName.contains(" ") || password.contains(" ") || firstName.contains(" ") || lastName.contains(" ")){
	    			debug="No spaces allowed, epic fail!";
	    			return false;	    			
	    		}
	    		
	    		if(userName.length()<1 || password.length()<1 || firstName.length()<1 || lastName.length()<1){
	    			debug="You gotta type something, dummy!";
	    			return false;	    			
	    		}
	    			
	    		//if passwords do not match, return before server call
	    		if(!password.equals(passwordConfirm)){
	    			debug="Passwords do not match";
	    			return false;
	    		}
	    		
	    		//create a string for sending to server
	    		String playerString="create:" + userName + " " + password + " " + firstName + " " + lastName + " " + phonenumber;
				
				try {
					//send our request
					oos.writeObject((String)playerString);
					//receive server response
					playerString = (String)ois.readObject();
					
					String parems[] = playerString.split(":", 2);
					
					//check for error function and update debug string
					if(parems[0].equalsIgnoreCase("error")){
						debug=parems[1];
						return false;
					}
					
					//parse out player info from server response and create player
					parseLine(parems[1]);
					new Me(fn, ln, un, pw, pn, xp, false);
					
					debug="Player created";
					return true;	
					
				} catch (UnknownHostException e) {
					System.err.println("Trying to connect to unknown host: " + e);
				} catch (IOException e) {
					System.err.println("IOException:  " + e);
				} catch (ClassNotFoundException cnfe) {
					System.err.println("CNFE : " + cnfe);
				}
				
				return false;
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
	    
	    void parseLine(String s){
	        String parems[] = s.split(" ", 6);
	        
	        un = parems[0];
	        pw = parems[1];
	        fn = parems[2];
	        ln = parems[3];
	        pn = parems[4];
	        xp = Integer.parseInt(parems[5]);
	        
	    }
	    public String getDebug(){
	    	return debug;
	    }

}

