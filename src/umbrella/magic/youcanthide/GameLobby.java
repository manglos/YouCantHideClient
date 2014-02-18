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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class GameLobby extends Activity {
	
	int gameId;
	TextView playerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_lobby);
		Intent intent = getIntent();
		gameId = Integer.parseInt(intent.getStringExtra(GameList.EXTRA_GAME_ID));
		// Create the text view:
		playerList = (TextView) findViewById(R.id.game_lobby_player_list);
		playerList.setTextSize(24);
		
		getList();
		
		/*textView.setText("Finn\n");
		textView.append("Jake\n");
		textView.append("BMO\n");*/
	}
	
	public void getList(){
		String content[] = null;
		
		GetPlayerStrings gps = new GetPlayerStrings();
		
		
		try {
			String playerListString = gps.execute().get();
			Log.i("playerListString", ""+playerListString);
			if(playerListString!=null)
				content = playerListString.split(":");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(content!=null && content[0]!=null){
			playerList.setText(content[0]+"\n");
		
			for(int i=1; i<content.length;i++){
				playerList.append(content[i]+"\n");
			}
		}
	}
	private class GetRole extends AsyncTask<String, Void, String> {
		private static final String TAG ="AsyncCallWS";
		
        @Override
        protected String doInBackground(String... params) {
            //helloSOAP();
        	
        	String SOAP_ACTION = "http://WS/DatabaseService/getPlayerRoleByPhone";
	        String METHOD_NAME = "getPlayerRoleByPhone";
	        String NAMESPACE = "http://WS/";
	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	        try { 
	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	            Log.i("TESTER", params[0]+"");
	            Request.addProperty("phone", params[0]);
	            
	            
	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            soapEnvelope.setOutputSoapObject(Request);

	            HttpTransportSE transport= new HttpTransportSE(URL);

	            transport.call(SOAP_ACTION, soapEnvelope);
	            SoapPrimitive result = (SoapPrimitive)soapEnvelope.getResponse();
	            
	            Log.i(TAG, "Result: " + result);
	            //return result;
	            if(result!=null)
	            	return result.toString();
	        }
	        catch(Exception ex) {
	        	Log.i(TAG, "Result: error " + ex.toString());
	        }
	        return null;
        }
    }
	
	private class GetPlayerStrings extends AsyncTask<Void, Void, String> {
		private static final String TAG ="AsyncCallWS";
		
        @Override
        protected String doInBackground(Void... params) {
            //helloSOAP();
        	
        	String SOAP_ACTION = "http://WS/DatabaseService/getGamePlayerStrings";
	        String METHOD_NAME = "getGamePlayerStrings";
	        String NAMESPACE = "http://WS/";
	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	        try { 
	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	            Log.i("TESTER", gameId+"");
	            Request.addProperty("id", gameId);
	            
	            
	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            soapEnvelope.setOutputSoapObject(Request);

	            HttpTransportSE transport= new HttpTransportSE(URL);

	            transport.call(SOAP_ACTION, soapEnvelope);
	            SoapPrimitive result = (SoapPrimitive)soapEnvelope.getResponse();
	            
	            Log.i(TAG, "Result: " + result);
	            //return result;
	            if(result!=null)
	            	return result.toString();
	        }
	        catch(Exception ex) {
	        	Log.i(TAG, "Result: error " + ex.toString());
	        }
	        return null;
        }
    }
	
	private class StartGame extends AsyncTask<Void, Void, Void> {
		private static final String TAG ="StartGame";
		
        @Override
        protected Void doInBackground(Void... params) {
            //helloSOAP();
        	
        	String SOAP_ACTION = "http://WS/DatabaseService/startGameById";
	        String METHOD_NAME = "startGameById";
	        String NAMESPACE = "http://WS/";
	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	        try { 
	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	            Log.i(TAG, gameId+"");
	            Request.addProperty("id", gameId);
	            
	            
	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	            soapEnvelope.setOutputSoapObject(Request);

	            HttpTransportSE transport= new HttpTransportSE(URL);

	            transport.call(SOAP_ACTION, soapEnvelope);
	            SoapPrimitive result = (SoapPrimitive)soapEnvelope.getResponse();
	            
	            Log.i(TAG, "Result: " + result);
	            //return result;
	        }
	        catch(Exception ex) {
	        	Log.i(TAG, "Result: error " + ex.toString());
	        }
	        return null;
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_lobby, menu);
		return true;
	}

	/* Called when the user clicks the "Refresh" button */
    public void refresh(View view) {
    	//getList();
    	Intent intent = new Intent(this, GameLobby.class);
    	intent.putExtra(GameList.EXTRA_GAME_ID, gameId+"");
    	startActivity(intent);
    }
    
    public void startGame(View view) {
    	StartGame starter = new StartGame();
    	starter.execute();
    	TelephonyManager phoneManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
    	String phoneNumber = phoneManager.getLine1Number();
    	String params[] = new String[1];
    	params[0]=phoneNumber;
    	GetRole getter = new GetRole();
    	String myRole="";
    	try {
			myRole = getter.execute(params).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(myRole.equalsIgnoreCase("IT")){
    		Intent intent = new Intent(this, ItActivity.class);
        	intent.putExtra(GameList.EXTRA_GAME_ID, gameId+"");
        	startActivity(intent);
    	}
    	if(myRole.equalsIgnoreCase("Runner")){
    		Intent intent = new Intent(this, RunnerActivity.class);
    		intent.putExtra(GameList.EXTRA_GAME_ID, gameId+"");
        	startActivity(intent);
    	}
    }
}
