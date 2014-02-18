package umbrella.magic.youcanthide;

import java.util.concurrent.ExecutionException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Registration extends Activity {
	public final static String EXTRA_PHONE_NUMBER = "umbrella.magic.youcanthide.PHONE_NUMBER";
	private static final String TAG = null; // For debugging
	
	// Attributes:
	String phoneNumber;
	TelephonyManager phoneManager;
	TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	// Retrieve the user's telephone number:
    	phoneManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
    	phoneNumber = phoneManager.getLine1Number();
    	
    	// Query WS to see if user already exists:
    	AsyncPlayerExists playerexiststask = new AsyncPlayerExists();
    	boolean alreadyRegistered;
    	
		try {
			alreadyRegistered = playerexiststask.execute(phoneNumber).get().booleanValue();
		} catch (InterruptedException e) {
			Log.i(TAG, "InterruptedException");
			// How do we really want to handle this (TDB)?
			alreadyRegistered = false;
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.i(TAG, "ExecutionException");
			// How do we really want to handle this (TDB)?
			alreadyRegistered = false;
			e.printStackTrace();
		}
		
    	// If it is done or not (pick a path):
    	if (! alreadyRegistered) {
    		setContentView(R.layout.activity_registration);
    	} else {
    		goToRegConfirm();
    	}
    }
    
    // Go to the next activity:
    public void goToRegConfirm(){
    	Intent intent = new Intent(this, RegistrationConfirm.class);
    	intent.putExtra(EXTRA_PHONE_NUMBER, phoneNumber);
    	startActivity(intent);
    }
    
    /* Called when the user clicks the Send button */
    public void sendMessage(View view) {
    	// Get the FirstName input:
    	EditText editText = (EditText) findViewById(R.id.reg_first_name);
    	String firstName = editText.getText().toString();
    	// Get the LastName input:
    	editText = (EditText) findViewById(R.id.reg_last_name);
    	String lastName = editText.getText().toString();
    	// Get the UserName input:
    	editText = (EditText) findViewById(R.id.reg_user_name);
    	String userName = editText.getText().toString();
    	// Get the Password input:
    	editText = (EditText) findViewById(R.id.reg_password);
    	String password = editText.getText().toString();
    	// Get the ConfirmPassword input:
    	editText = (EditText) findViewById(R.id.reg_password_confirm);
    	String passwordConfirm = editText.getText().toString();
    	
    	validateInput(firstName, lastName, userName, password, passwordConfirm);
    	//if(password!=null && passwordConfirm!=null && !password.equals(passwordConfirm)){
    	//	toast = Toast.makeText(context, text, duration);
    	//	toast.show();
    	//} 
    	
    	String[] usrargs = new String[6];
    	usrargs[0] = firstName;
    	usrargs[1] = lastName;
    	usrargs[2] = userName;
    	usrargs[3] = password;
    	usrargs[4] = phoneNumber;
    	usrargs[5] = passwordConfirm;
    	
    	// Create new player on WS:
    	AsyncCreatePlayer createplayertask = new AsyncCreatePlayer();
    	boolean createSuccessful;
		try {
			createplayertask.execute(usrargs).get();
			createSuccessful = true;
		} catch (InterruptedException e) {
			// How do we really want to handle this (TDB)?
			createSuccessful = false;
			e.printStackTrace();
		} catch (ExecutionException e) {
			// How do we really want to handle this (TDB)?
			createSuccessful = false;
			e.printStackTrace();
		}
    	
		// Where to go with this:
    	if (createSuccessful){
    		goToRegConfirm();
    	} else {
    		Log.i(TAG, "Result: player could not be created");
    		//Toast myToast = Toast.makeText(getApplicationContext(), myRegClient.getDebug(), Toast.LENGTH_LONG);
    		//myToast.show();
    	}
    }
    
    // Validates the user's input:
    private void validateInput(String firstName, String lastName,
			String userName, String password, String passwordConfirm) {
    	String debug = "";
    	if(userName.contains(" ") || password.contains(" ") || firstName.contains(" ") || lastName.contains(" ")){
			debug="No spaces allowed, epic fail!";
			//return false;	    			
		}
		
		if(userName.length()<1 || password.length()<1 || firstName.length()<1 || lastName.length()<1){
			debug="You gotta type something, dummy!";
			//return false;	    			
		}
			
		//if passwords do not match, return before server call
		if(!password.equals(passwordConfirm)){
			debug="Passwords do not match";
			//return false;
		}
		
		Log.i(TAG, "Output " + debug);
	}

	// Call the WebService to see if player already exists:
 	private class AsyncPlayerExists extends AsyncTask<String, Void, Boolean> {
 		private static final String TAG ="AsyncCallWS";
 		
         @Override
         protected Boolean doInBackground(String...string) {
        	 Boolean result = userexistsSOAP(string[0]);
        	 if (result == null) {
        		 Log.i(TAG, "NULL ISSUE");
        		 result = false;
        	 }
             return result;
         }

 		private Boolean userexistsSOAP(String phonenumber) {
 			String SOAP_ACTION = "http://WS/DatabaseService/userExistsByPhone";
 	        String METHOD_NAME = "userExistsByPhone";
 	        String NAMESPACE = "http://WS/";
 	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
 	        
 	        try { 
 	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
 	            Request.addProperty("phone", phonenumber);

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
 	        	Log.i(TAG, "UserExistsByPhone Error: " + ex.toString());
 	        	return false;
 	        }
 		}
     }
 	
 	// Call the WebService to create the player (Just a stub):
  	private class AsyncCreatePlayer extends AsyncTask<String, Void, Void> {
  		private static final String TAG ="AsyncCreatePlayer";
  		
          @Override
          protected Void doInBackground(String...string) {
              createuserSOAP(string);
              return null;
          }

  		private void createuserSOAP(String[] usrdata) {
  			String SOAP_ACTION = "http://WS/DatabaseService/create";
  	        String METHOD_NAME = "create";
  	        String NAMESPACE = "http://WS/";
  	        String URL = "http://moxie.oswego.edu:8080/DatabaseService/DatabaseService?wsdl";
  	        
  	        try { 
  	            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
  	            Request.addProperty("firstName", usrdata[0]); // first
  	            Request.addProperty("lastName", usrdata[1]); // last
  	            Request.addProperty("username", usrdata[2]); // usr
  	            Request.addProperty("password", usrdata[3]); // password
  	            Request.addProperty("phone", usrdata[4]); // phone

  	            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
  	            soapEnvelope.setOutputSoapObject(Request);

  	            HttpTransportSE transport= new HttpTransportSE(URL);

  	            transport.call(SOAP_ACTION, soapEnvelope);
  	            
  	            Log.i(TAG, "PlayerCreated");
  	        }
  	        catch(Exception ex) {
  	        	Log.i(TAG, "CreatePlayer Error: " + ex.toString());
  	        }
  		}
    }

}