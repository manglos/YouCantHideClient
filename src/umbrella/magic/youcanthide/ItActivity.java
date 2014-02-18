package umbrella.magic.youcanthide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItActivity extends Activity {
	
	TextView userTV, xpTV;
	private String phoneNumber;
	private int gameID;
	private ArrayList<String> gameEvents;
	public String[] result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the intent and extract the message delivered:
		Intent intent = getIntent();
		this.phoneNumber = intent.getStringExtra(Registration.EXTRA_PHONE_NUMBER);	// This value needs to be sent as an extra
		this.gameID = Integer.parseInt(intent.getStringExtra(GameList.EXTRA_GAME_ID));		// This value needs to be sent as an extra
		this.gameEvents = new ArrayList<String>();
		this.gameEvents.add("no updates");
		
		ItActivity[] activities = new ItActivity[1];
		activities[0] = this;
		AsyncGameUpdates gameUpdater = new AsyncGameUpdates();
		gameUpdater.execute(activities);
				
		setContentView(R.layout.activity_it);
		
		userTV = (TextView)findViewById(R.id.username_string);
		//userTV.setText(Me.getUsername());
		xpTV = (TextView)findViewById(R.id.current_xp);
		//xpTV.setText(Me.getExperience()+"xp");
	}
	
	// Tag -- called when tag button is pushed:
	public void initiateTag(View view){
		//Me.addXP(-50);
		ItActivity[] activities = new ItActivity[1];
		activities[0] = this;
		AsyncTag tagger = new AsyncTag();
		boolean successfulTag;
		try {
			successfulTag = tagger.execute(activities).get().booleanValue();
		} catch (InterruptedException e) {
			successfulTag = false;
		} catch (ExecutionException e) {
			successfulTag = false;
		}
		
		if (!successfulTag) {
			Toast myToast = Toast.makeText(getApplicationContext(), "Troll Tag, don't be an troll -50xp", Toast.LENGTH_LONG);
			myToast.show();
			//xpTV.setText(Me.getExperience()+"xp");
		}
	}
	
	// Update NewsFeed -- called every 5 seconds:
	public void updateNewsFeed(String[] updates) {
		boolean gameOver = false;
		// Add the new events to the running list:
		if(updates[0]!=null){
		this.gameEvents = new ArrayList<String>(Arrays.asList(updates));
			if(gameEvents!=null){
				if(gameEvents.contains("Game Over"))
					gameOver=true;
				
				Collections.reverse(gameEvents);
			}
		}
		// Update the list view:
		ListView gameUpdates = (ListView) findViewById(R.id.gameUpdates);
		
		//Will have to remake adapter for Game objects
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.gameEvents);
		gameUpdates.setAdapter(adapter);
		// If game over signaled, go to results:
		if (gameOver) {
			//Go forth!
			Log.i("Game END", "yeahhhh");
			Intent intent = new Intent(this, GameResults.class);
	    	intent.putExtra(Registration.EXTRA_PHONE_NUMBER, phoneNumber);
	    	intent.putExtra(GameList.EXTRA_GAME_ID, gameID);
	    	startActivity(intent);
		}
	}
	
	// Get the Game Id:
	public int getGameID() {
		return this.gameID;
	}
	
	// Get the Phone Number:
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	// Get the Location:
	public String getLocation() {
		return "here"; // A place holder. You said you have code for this?
	}
	
	// Call the WebService to get game updates (called automatically):
	 	private class AsyncGameUpdates extends AsyncTask<ItActivity, Void, Void> {
	 		private static final String TAG ="AsyncCallWS";
	 		
	 		 @Override
	         protected Void doInBackground(final ItActivity...caller) {
	        	 Timer t = new Timer();
	        	 TimerTask task = new TimerTask() {
	                 public void run() {
	                	 getUpdates(caller[0]); 
	                 }
	        	 };
	             t.scheduleAtFixedRate(task, 1000, 5000); // Every 5 seconds
	        	 return null;
	         }

	 		private void getUpdates(ItActivity caller) {
	 			String SOAP_ACTION = "http://WS/DatabaseService/getUpdatesById";	// Place-holder
	 	        String METHOD_NAME = "getUpdatesById";								// Place-holder
	 	        String NAMESPACE = "http://WS/";
	 	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	 	        
	 	        try { 
	 	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	 	            Log.i("Updated ID", caller.getGameID()+"");
	 	            Request.addProperty("id", caller.getGameID());

	 	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	 	            soapEnvelope.setOutputSoapObject(Request);

	 	            HttpTransportSE transport= new HttpTransportSE(URL);

	 	           transport.call(SOAP_ACTION, soapEnvelope);
	 	           SoapPrimitive soapResult = (SoapPrimitive)soapEnvelope.getResponse();
	 	           if (soapResult.toString() != null) {
	 	        	   Log.i("soapResult=", soapResult.toString());
	 	        	   result = soapResult.toString().split(":"); 
	 	        	   runOnUiThread(new Runnable() {
		 	        	     public void run() {
	 	        	    	 	updateNewsFeed(result);
	
		 	        	    }
		 	        	});
	 	        	   
	 	           }	
	 	        } catch(Exception ex) {
	 	        	Log.i(TAG, "It Updates Error: " + ex.toString());
	 	        }
	 		}
	     }
	 	
	 // Call the WebService to Tag:
	 	private class AsyncTag extends AsyncTask<ItActivity, Void, Boolean> {
	 		private static final String TAG ="AsyncCallWS";
	 		
	         @Override
	         protected Boolean doInBackground(final ItActivity...caller) {
	        	 Boolean result = tagSOAP(caller[0]);
	        	 if (result == null) {
	        		 Log.i(TAG, "NULL ISSUE");
	        		 result = false;
	        	 }
	             return result;
	         }

	 		private Boolean tagSOAP(ItActivity caller) {
	 			String SOAP_ACTION = "http://WS/DatabaseService/tag";
	 	        String METHOD_NAME = "tag";
	 	        String NAMESPACE = "http://WS/";
	 	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	 	        
	 	        try { 
	 	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	 	            Request.addProperty("location", caller.getLocation());
	 	            Request.addProperty("phone", caller.getPhoneNumber());
	 	            Request.addProperty("game", caller.getGameID());

	 	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	 	            soapEnvelope.setOutputSoapObject(Request);

	 	            HttpTransportSE transport= new HttpTransportSE(URL);

	 	            transport.call(SOAP_ACTION, soapEnvelope);
	 	            SoapPrimitive soapResult = (SoapPrimitive)soapEnvelope.getResponse();
	 	            if (soapResult.toString().equalsIgnoreCase("false")) {
	 	        	   return false;
	 	            } else {
	 	        	   return true;
	 	            }
	 	           		
	 	        } catch(Exception ex) {
	 	        	Log.i(TAG, "Tag Error: " + ex.toString());
	 	        	return false;
	 	        }
	 		}
	     }
}