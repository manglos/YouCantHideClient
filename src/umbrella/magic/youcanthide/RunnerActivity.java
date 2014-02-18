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

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RunnerActivity extends Activity implements LocationListener {
	
	private static final String TAG = null;
	TextView userTV, xpTV;
	private String phoneNumber;
	private int gameID;
	private ArrayList<String> gameEvents;
	public String[] result;
	private LocationManager locationManager;
	private String location;
	private String provider;
	private AsyncGameUpdates gameUpdater;
	private boolean inPlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.inPlay = true;
		
		// Get the intent and extract the message delivered:
		Intent intent = getIntent();
		this.phoneNumber = intent.getStringExtra(Registration.EXTRA_PHONE_NUMBER);	// This value needs to be sent as an extra
		this.gameID = Integer.parseInt(intent.getStringExtra(GameList.EXTRA_GAME_ID));		// This value needs to be sent as an extra
		this.gameEvents = new ArrayList<String>();
		this.gameEvents.add("no updates");
		
		RunnerActivity[] activities = new RunnerActivity[1];
		activities[0] = this;
		gameUpdater = new AsyncGameUpdates();
		gameUpdater.execute(activities);
				
		setContentView(R.layout.activity_runner);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);

	    // Initialize the location fields
	    if (location != null) {
	      onLocationChanged(location);
	    } else {
	      this.location = "Lat:Not Available:Long:Not Available";
	    }
	}
	
	// Home -- called when tag button is pushed:
	public void initiateHome(View view){
		//Me.addXP(-50);
		RunnerActivity[] activities = new RunnerActivity[1];
		activities[0] = this;
		AsyncHome tagger = new AsyncHome();
		boolean successfulTag;
		try {
			successfulTag = tagger.execute(activities).get().booleanValue();
		} catch (InterruptedException e) {
			successfulTag = false;
		} catch (ExecutionException e) {
			successfulTag = false;
		}
		
		if (!successfulTag) {
			Toast myToast = Toast.makeText(getApplicationContext(), "You're not home, don't be an troll -50xp", Toast.LENGTH_LONG);
			myToast.show();
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
			this.gameUpdater.cancel(true);
			this.inPlay = false;
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
			Log.i(TAG, "It Location: " + this.location);
			return this.location;
		}
	
		// See if the game is still in play:
		public boolean inPlay() {
			return this.inPlay;
		}
	
		// Call the WebService to get game updates (called automatically):
	 	private class AsyncGameUpdates extends AsyncTask<RunnerActivity, Void, Void> {
	 		private static final String TAG ="AsyncCallWS";
	 		
	 		 @Override
	         protected Void doInBackground(final RunnerActivity...caller) {
	        	 Timer t = new Timer();
	        	 TimerTask task = new TimerTask() {
	                 public void run() {
	                	 if (caller[0].inPlay()) {
	                		 getUpdates(caller[0]); 
	                	 } else {
	                		 this.cancel();
	                	 }
	                 }
	        	 };
	             t.scheduleAtFixedRate(task, 1000, 5000); // Every 5 seconds
	        	 return null;
	         }

	 		private void getUpdates(RunnerActivity caller) {
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
	 	        	Log.i(TAG, "Home Updates Error: " + ex.toString());
	 	        }
	 		}
	    }
	 	
	 	// Call the WebService to Tag:
	 	private class AsyncHome extends AsyncTask<RunnerActivity, Void, Boolean> {
	 		private static final String TAG ="AsyncCallWS";
	 		
	         @Override
	         protected Boolean doInBackground(final RunnerActivity...caller) {
	        	 
	        	Boolean result = homeSOAP(caller[0]);
	        	if (result == null) {
	        		Log.i(TAG, "NULL ISSUE");
	        		result = false;
	        	}
	        	return result;
	         }

	 		private Boolean homeSOAP(RunnerActivity caller) {
	 			String SOAP_ACTION = "http://WS/DatabaseService/home";
	 	        String METHOD_NAME = "home";
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
	 	        	Log.i(TAG, "Home Error: " + ex.toString());
	 	        	return false;
	 	        }
	 		}
	     }
	 	
	 	// Requests Updates when Activity is started:
	 	@Override
	 	  protected void onResume() {
	 	    super.onResume();
	 	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	 	}
	 	
	 	@Override
	 	  protected void onPause() {
	 	    super.onPause();
	 	   Log.i(TAG, "Runner Activity Paused");
	 	    locationManager.removeUpdates(this);
	 	}

		@Override
		public void onLocationChanged(Location location) {
			double lat = location.getLatitude();
			double longe = location.getLongitude();
			this.location = "Lat:" + lat + ":Long:" + longe;
		}

		@Override
		public void onProviderDisabled(String arg0) {
			Log.i(TAG, "Provider Disabled");
		}

		@Override
		public void onProviderEnabled(String arg0) {
			Log.i(TAG, "Provider Enabled");
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			Log.i(TAG, "Status Changed");
		}
}