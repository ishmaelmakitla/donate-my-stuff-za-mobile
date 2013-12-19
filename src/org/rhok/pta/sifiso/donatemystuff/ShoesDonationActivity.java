package org.rhok.pta.sifiso.donatemystuff;

import org.json.JSONException;
import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.hardware.Camera.Size;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class ShoesDonationActivity extends Activity {
	private static final String TAG = ShoesDonationActivity.class
			.getSimpleName();
	// private static final String MAKE_DONATION_REQUEST_SERVLET_URL =
	// "http://za-donate-my-stuff.appspot.com/makedonationrequest";
	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationoffer";

	private EditText shoeName;
	private EditText shoeSize;
	private Spinner shoeGender;
	private EditText shoeQuantity;

	private Button shoeSubmit;
	private int gender;
	
	//the Server URL based on the mode (Request/Offer)
	private String serverURL = null;
	
	//session
	private UserSession session;
	//mode
	int mode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoes_donation);
		shoeName = (EditText) findViewById(R.id.shoeName);
		shoeSize = (EditText) findViewById(R.id.shoeSize);
		shoeGender = (Spinner) findViewById(R.id.shoeGender);
		shoeQuantity = (EditText) findViewById(R.id.shoeQuantity);
		shoeSubmit = (Button) findViewById(R.id.shoeSubmit);
		shoeSubmit.setOnClickListener(shoeSubmitListner);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.gender_data));
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shoeGender.setAdapter(dataAdapter);
		shoeGender
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						gender = arg2;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		
		//check the mode or source of invoking this Intent (was it in Offers or Requests)
				Bundle extras = getIntent().getExtras();
				if(extras !=null){
					//get the mode
					mode = extras.getInt(DonateMyStuffGlobals.KEY_MODE);
					Log.i(TAG, "Current Mode at ClothDonation is "+mode);
					serverURL = (mode == DonateMyStuffGlobals.MODE_OFFERS_LIST? 
							         DonateMyStuffGlobals.MAKE_DONATION_OFFER_SERVLET_URL: DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL);
					
					session = (UserSession)extras.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
					String title = getTitle().toString()+session.getUsername();
					setTitle(title);
					
					//if the mode is Requests, disable the Quantity field
					if(mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST){
						shoeQuantity.setText("-1");
						shoeQuantity.setEnabled(false);
					}
				}
	}

	private OnClickListener shoeSubmitListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;
			try {
				offerJson = createOfferJSON();

				JsonObjectRequest request = new JsonObjectRequest(
						Request.Method.POST, MAKE_DONATION_OFFER_SERVLET_URL,
						offerJson, new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								try {
									Toast.makeText(
											getApplicationContext(),
											response.getString("message")
													.toString(),
											Toast.LENGTH_LONG).show();
									if (response.getInt("status") == 0) {
										shoeName.setText(null);
										shoeSize.setText(null);
										shoeQuantity.setText(null);

									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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

	private JSONObject createOfferJSON() throws JSONException {
		JSONObject json = new JSONObject();
		String me = session.getUserID();
		//these are depending on the mode of operation...
		if(mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST){
				json.put("beneficiaryid", me); 
				//this is not a bid...
				json.put("donationofferid", null);
				//by default donations are delivered
				json.put("collect", false);
			}
	   else{
				json.put("donorid", me);
				json.put("donationrequestid", null);
				json.put("deliver", true);
		 }
		// donated item
		JSONObject jsonDonated = new JSONObject();
		jsonDonated.put("name", shoeName.getText().toString());
		jsonDonated.put("size", shoeSize.getText().toString());
		jsonDonated.put("gender", gender);
		jsonDonated.put("type", "shoes");

		json.put("item", jsonDonated);

		int count = 0;
		try {			
			 //quantity only set for Offers,			
			  if(mode != DonateMyStuffGlobals.MODE_REQUESTS_LIST ){
				  count = Integer.parseInt(shoeQuantity.getText().toString());
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
		getMenuInflater().inflate(R.menu.shoes_donation, menu);
		return true;
	}

}
