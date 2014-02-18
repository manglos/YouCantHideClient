package umbrella.magic.youcanthide;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class GameCreation extends Activity {
	//public final static String EXTRA_FIELD_SIZE = "umbrella.magic.youcanthide.FIELD_SIZE";
	//public final static String EXTRA_LOCATION = "umbrella.magic.youcanthide.LOCATION";
	//public final static String EXTRA_NUM_IT = "umbrella.magic.youcanthide.NUM_IT";
	
	// Some Variables:
	String fieldSelection;
	String numIt;
	String durationSelection;
	LocationManager locationManager;
	String myLocation;
	String provider;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		// Load the GUI
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_game_creation);
    	
    	getLocation();
    	
    }
	
	public String getLocation(){
		// Getting LocationManager object
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
 
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
 
        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);
 
        if(provider!=null && !provider.equals("")){
 
            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);
            if(location!=null)
            	return location.toString();
            //locationManager.requestLocationUpdates(provider, 20000, 1, this);
 
            /*if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();*/
 
        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
		return null;
	}
    
	/* Called when the user clicks a field size radio button */
    public void fieldSizeSelected(View view) {
    	// Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_field_sm:
                if (checked)
                	fieldSelection = "small";
                break;
            case R.id.radio_field_md:
                if (checked)
                	fieldSelection = "medium";
                break;
            case R.id.radio_field_lg:
                if (checked)
                	fieldSelection = "large";
                break;
        }
    }
    
    /* Called when the user clicks a number its radio button */
    public void numItSelected(View view) {
    	// Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_num_it_sm:
                if (checked)
                	numIt = "small";
                break;
            case R.id.radio_num_it_md:
                if (checked)
                	numIt = "medium";
                break;
            case R.id.radio_num_it_lg:
                if (checked)
                	numIt = "large";
                break;
        }
    }
    
    /* Called when the user clicks a duration radio button */
    public void durationSelected(View view) {
    	// Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_duration_sm:
                if (checked)
                	durationSelection = "small";
                break;
            case R.id.radio_duration_md:
                if (checked)
                	durationSelection = "medium";
                break;
            case R.id.radio_duration_lg:
                if (checked)
                	durationSelection = "large";
                break;
        }
    }
    
    private class AsyncCallWS extends AsyncTask<String, Void, Boolean> {
		private static final String TAG ="AsyncCallWS";
		
        @Override
        protected Boolean doInBackground(String... params) {
            //helloSOAP();
        	
        	String SOAP_ACTION = "http://WS/DatabaseService/createGame";
	        String METHOD_NAME = "createGame";
	        String NAMESPACE = "http://WS/";
	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	        int time=0, numIt=0;
	        try { 
	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	            Request.addProperty("location", params[0]); // Phone number here!
	            Request.addProperty("size", params[1]);
	            if(params[3]!=null){
	            	if(params[3].equals("small"))
	            		numIt = 1;
	            	if(params[3].equals("medium"))
	            		numIt = 2;
	            	if(params[3].equals("large"))
	            		numIt = -1;
	            }
	            Request.addProperty("numit", numIt);
	            if(params[2]!=null){
	            	if(params[2].equals("small"))
	            		time = 20;
	            	if(params[2].equals("medium"))
	            		time = 30;
	            	if(params[2].equals("large"))
	            		time = 60;
	            }
	            Request.addProperty("time", time);
	            
	            
	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            soapEnvelope.setOutputSoapObject(Request);

	            HttpTransportSE transport= new HttpTransportSE(URL);

	            transport.call(SOAP_ACTION, soapEnvelope);
	            SoapPrimitive result = (SoapPrimitive)soapEnvelope.getResponse();
	            
	            Log.i(TAG, "Result: " + result);
	            //return result;
	            return true;
	        }
	        catch(Exception ex) {
	        	Log.i(TAG, "Result: error " + ex.toString());
	        }
	        return false;
        }
    }
	
    /* Called when the user clicks the Create button */
    public void createGame(View view) {
    	/*
    	 * Here we should send the data gathered by the radio buttons to the Web Service.
    	 */
    	
    	AsyncCallWS createGame = new AsyncCallWS();
    	String args[]=new String[4];
    	
    	args[0]="here";//getLocation();
    	args[1]=fieldSelection;
    	args[2]=durationSelection;
    	args[3]=numIt;
    	
    	createGame.execute(args);
    	
    	Intent intent = new Intent(this, GameList.class);
    	startActivity(intent);
    }
}
