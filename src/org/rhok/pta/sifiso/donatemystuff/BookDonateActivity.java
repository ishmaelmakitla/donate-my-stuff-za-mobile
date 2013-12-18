package org.rhok.pta.sifiso.donatemystuff;

import org.json.JSONException;
import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class BookDonateActivity extends Activity {
	private static final String TAG = BookDonateActivity.class.getSimpleName();

	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationoffer";
	// private static final String MAKE_DONATION_OFFER_SERVLET_URL =
	// "http://za-donate-my-stuff.appspot.com/makedonationrequest?payload=";

	private EditText bookName;
	private EditText bookSize;
	private EditText bookAge;
	private EditText bookQuantity;
	private EditText bookAgeRest;
	private Button bookSubmit;
	
	//the Server URL based on the mode (Request/Offer)
	private String serverURL = null;
	//mode
	int mode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_donate);
		bookName = (EditText) findViewById(R.id.bookName);
		bookSize = (EditText) findViewById(R.id.bookSize);
		bookAgeRest = (EditText) findViewById(R.id.bookAgeRest);
		bookQuantity = (EditText) findViewById(R.id.bookQuantity);
		bookAge = (EditText) findViewById(R.id.bookAge);

		bookSubmit = (Button) findViewById(R.id.bookSubmit);
		bookSubmit.setOnClickListener(submitBookListner);
		
		//check the mode or source of invoking this Intent (was it in Offers or Requests)
				Bundle extras = getIntent().getExtras();
				if(extras !=null){
					//get the mode
					mode = extras.getInt(DonateMyStuffGlobals.KEY_MODE);
					
					serverURL = (mode == DonateMyStuffGlobals.MODE_OFFERS_LIST? 
							         DonateMyStuffGlobals.MAKE_DONATION_OFFER_SERVLET_URL: DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL);
					
					Log.i(TAG, "Current Mode at BookDonateActivity is "+mode+" URL is = "+serverURL);
					
					//if the mode is Requests, disable the Quantity field
					if(mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST){
						bookQuantity.setText("-1");
						bookQuantity.setEnabled(false);
					}
				}
	}

	private OnClickListener submitBookListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;
			try {
				offerJson = createJSONPayload();
				JsonObjectRequest request = new JsonObjectRequest(
						Request.Method.POST, serverURL,
						offerJson, new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Log.d(TAG, response.toString());
								processServerResponse(response);
								//perhaps at this point we clear the fields								
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e(TAG, "issues with server", error);
								error.printStackTrace();
							}

						});

				queue.add(request);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	/**
	 * Method to process the response sent by the server following the submission of a donation request/offer
	 * @param response
	 */
	private void processServerResponse(JSONObject response){
		try {
			String serverMessage = response.getString("message");
			int statusCode = response.getInt("status");
			Toast.makeText(getApplicationContext(), serverMessage, Toast.LENGTH_LONG).show();			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method fetches values from UI and creates a JSON object
	 * 
	 * @return
	 * @throws JSONException
	 */
	private JSONObject createJSONPayload() throws JSONException {
		JSONObject json = new JSONObject();
		
		//these are depending on the mode of operation...
		if(mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST){
				json.put("beneficiaryid", "1234567890"); //this must be replaced with the ID of the logged in user
				//this is not a bid...
				json.put("donationofferid", null);
				//by default donations are delivered
				json.put("collect", false);
			}
		else{
				json.put("donorid", "1234567890"); //this must be replaced with the ID of the logged in user
				json.put("donationrequestid", null);
				json.put("deliver", true);
		}
		// donated/requested item
		JSONObject jsonDonated = new JSONObject();
		jsonDonated.put("name", bookName.getText().toString());
		jsonDonated.put("size", Integer.parseInt(bookSize.getText().toString()));
		jsonDonated.put("age", Integer.parseInt(bookAge.getText().toString()));
		jsonDonated.put("agerestriction",Integer.parseInt(bookAgeRest.getText().toString()));
		jsonDonated.put("type", "book");

		json.put("item", jsonDonated);

		int count = 0;
		try {			
			   if(mode != DonateMyStuffGlobals.MODE_REQUESTS_LIST ){
				  count = Integer.parseInt(bookQuantity.getText().toString());
				  json.put("quantity", count);
			    }			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Log.d(TAG, "Returning donation offer json=" + json);

		return json;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_donate, menu);
		return true;
	}

}

/*
 * ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
 * android.R.layout.simple_spinner_item, getResources()
 * .getStringArray(R.array.gender_data)); dataAdapter
 * .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 * bookGender.setAdapter(dataAdapter); bookGender .setOnItemSelectedListener(new
 * AdapterView.OnItemSelectedListener() {
 * 
 * @Override public void onItemSelected(AdapterView<?> arg0, View arg1, int
 * arg2, long arg3) { gender = arg2; }
 * 
 * @Override public void onNothingSelected(AdapterView<?> arg0) { } });
 */
