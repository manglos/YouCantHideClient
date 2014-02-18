package umbrella.magic.youcanthide;

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
import android.widget.TextView;

public class GameResults extends Activity {

	private static final String TAG = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_results);
		
		// Retrieve the Game ID:
		Intent intent = getIntent();
		String gameID = intent.getStringExtra(GameList.EXTRA_GAME_ID);		// This value needs to be sent as an extra
		
		// Call the Web Service to get the Game Results:
		AsyncGameResults gameResultsTask = new AsyncGameResults();
		String results = "";
    	
		try {
			results = gameResultsTask.execute(gameID).get();
		} catch (InterruptedException e) {
			Log.i(TAG, "InterruptedException");
		} catch (ExecutionException e) {
			Log.i(TAG, "ExecutionException");
		}
	
		// Set the result String in the layout:
		TextView textView = (TextView) findViewById(R.id.game_results);
		textView.append(results);
	}
	
	public void backToGameList(View view) {
		Intent intent = new Intent(this, Registration.class);
    	startActivity(intent);
	}
	
	// Call the WebService to get the game results:
	private class AsyncGameResults extends AsyncTask<String, Void, String> {
	 		private static final String TAG ="AsyncCallWS";
	 		
	         @Override
	         protected String doInBackground(String...string) {
	        	 String result = userexistsSOAP(string[0]);
	             return result;
	         }

	 		private String userexistsSOAP(String gameID) {
	 			String SOAP_ACTION = "http://WS/DatabaseService/GameResults";
	 	        String METHOD_NAME = "GameResults";
	 	        String NAMESPACE = "http://WS/";
	 	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	 	        
	 	        try { 
	 	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	 	            Request.addProperty("game", gameID);

	 	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	 	            soapEnvelope.setOutputSoapObject(Request);

	 	            HttpTransportSE transport= new HttpTransportSE(URL);

	 	            transport.call(SOAP_ACTION, soapEnvelope);
	 	           SoapPrimitive soapResult = (SoapPrimitive)soapEnvelope.getResponse();
	 	           return soapResult.toString();
	 	           		
	 	        } catch(Exception ex) {
	 	        	Log.i(TAG, "UserExistsByPhone Error: " + ex.toString());
	 	        	return "Game Over";
	 	        }
	 		}
    }
}