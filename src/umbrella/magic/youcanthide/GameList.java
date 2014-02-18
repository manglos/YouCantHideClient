package umbrella.magic.youcanthide;


import java.util.concurrent.ExecutionException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

@SuppressLint("NewApi")
public class GameList extends FragmentActivity implements OnItemClickListener{

	public static String EXTRA_GAME_ID = "umbrella.magic.youcanthide.GAME_ID";
	Button create, refresh; //browse
	String gamesList[]= {"No Games"};
	ListView games;
	int selectGameId;
	String phoneNumber;

	//Game [] games; **********//the array of available games to populate the SlideView
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_list);
		
		TelephonyManager phoneManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
    	phoneNumber = phoneManager.getLine1Number();
		//create = (Button) findViewById(R.id.bCreate);
		//refresh = (Button) findViewById(R.id.bRefresh);
		getList();
	}
	
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
            // Then you start a new Activity via Intent
        String s = (String)l.getItemAtPosition(position);
        
        if(!s.equals("No Games")){
	        try{
	        	selectGameId = Integer.parseInt(s.substring(5));
	        }catch(NumberFormatException nfe){
	        	Log.i("NFE", nfe.toString());
	        	selectGameId=-1;
	        }
        }
        else
        	selectGameId=-1;
        
        if(selectGameId!=-1)	
        	showDialog(this);
           /* Intent intent = new Intent();
            intent.setClass(this, GameLobby.class);
            intent.putExtra("position", position);
            // Or / And
            intent.putExtra("id", id);
            startActivity(intent);*/
    }
	
	public void getList(){
		games = (ListView) findViewById(R.id.gamesListView);
		games.setOnItemClickListener(this);
		AsyncCallWS getList = new AsyncCallWS();
		
		try {
			String gameListString = getList.execute().get();
			if(gameListString!=null)
				gamesList = gameListString.split(":");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gamesList);
		games.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_list, menu);
		return true;
	}
	
	private class AsyncCallWS extends AsyncTask<Void, Void, String> {
		private static final String TAG ="AsyncCallWS";
		
        @Override
        protected String doInBackground(Void... params) {
            //helloSOAP();
        	
        	String SOAP_ACTION = "http://WS/DatabaseService/getCurrentGameListString";
	        String METHOD_NAME = "getCurrentGameListString";
	        String NAMESPACE = "http://WS/";
	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	        try { 
	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	            
	            
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
	
	private class AddPlayerToGame extends AsyncTask<Void, Void, Void> {
		private static final String TAG ="AsyncCallWS";
		
        @Override
        protected Void doInBackground(Void... params) {
            //helloSOAP();
        	
        	String SOAP_ACTION = "http://WS/DatabaseService/addPlayerToGameByPhone";
	        String METHOD_NAME = "addPlayerToGameByPhone";
	        String NAMESPACE = "http://WS/";
	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
	        try { 
	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
	            Request.addProperty("id", selectGameId);
	            Request.addProperty("phone", phoneNumber);
	            
	            
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
	

	/* Called when the user clicks the "Create" button */
    public void goToGameCreation(View view) {
    	Intent intent = new Intent(this, GameCreation.class);
    	startActivity(intent); 
    }
    
    public void showDialog(final Activity activity){//, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //if (title != null)
            builder.setTitle(R.string.join_game_title)
            		.setPositiveButton(R.string.join_button,
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
							public void onClick(DialogInterface dialog, int whichButton) {
                                ((GameList) activity).doPositiveClick();
                            }
                        }
                    )
                    .setNegativeButton(R.string.cancel_button,
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
							public void onClick(DialogInterface dialog, int whichButton) {
                                ((GameList) activity).doNegativeClick();
                            }
                        }
                    );
        builder.show();
    }
    
    

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        Intent intent = new Intent(this, GameLobby.class);
        //EXTRA_GAME_ID = selectGameId+"";
        intent.putExtra(EXTRA_GAME_ID, selectGameId+"");
        AddPlayerToGame aptg = new AddPlayerToGame();
        aptg.execute();
    	startActivity(intent); 
    }
    
    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }
    
    /* Called when the user clicks the "Refresh" button */
    public void refresh(View view) {
    	getList();
    	//Intent intent = new Intent(this, GameList.class);
    	//startActivity(intent); 
    }
}
