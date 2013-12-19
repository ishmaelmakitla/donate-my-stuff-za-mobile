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

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class UniformsDonationActivity extends Activity {

	private static final String TAG = UniformsDonationActivity.class
			.getSimpleName();
	// private static final String MAKE_DONATION_REQUEST_SERVLET_URL =
	// "http://za-donate-my-stuff.appspot.com/makedonationrequest";
	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationoffer";

	private Spinner uniformShirts;
	private Spinner uniformPants;
	private Spinner uniformGender;
	private EditText uniformSize;
	private EditText uniformQuantity;
	private CheckBox chkUniformBlazer;

	private Button uniformSubmit;
	private int gender, shirt, pants;
	private boolean check;
	
	private UserSession session;
	
	//the Server URL based on the mode (Request/Offer)
	private String serverURL = null;
	
	//mode
	int mode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uniforms_donation);
		uniformSize = (EditText) findViewById(R.id.uniformSize);
		uniformQuantity = (EditText) findViewById(R.id.uniformQuantity);
		uniformShirts = (Spinner) findViewById(R.id.uniformShirts);
		uniformPants = (Spinner) findViewById(R.id.uniformPants);
		uniformGender = (Spinner) findViewById(R.id.uniformGender);
		chkUniformBlazer = (CheckBox) findViewById(R.id.chkUniformBlazer);
		chkUniformBlazer.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked == true) {
							check = true;
						} else {
							check = false;
						}

					}
				});
		uniformSubmit = (Button) findViewById(R.id.uniformSubmit);
		uniformSubmit.setOnClickListener(uniformSubmitListner);
		
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			//get the mode
			mode = extras.getInt(DonateMyStuffGlobals.KEY_MODE);
			
			serverURL = (mode == DonateMyStuffGlobals.MODE_OFFERS_LIST? 
					         DonateMyStuffGlobals.MAKE_DONATION_OFFER_SERVLET_URL: DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL);
			
			Log.i(TAG, "Current Mode at UniformsDonationActivity is "+mode+" URL is = "+serverURL);
			
			session = (UserSession)extras.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
			String title = getTitle().toString()+session.getUsername();
			setTitle(title);
			
			//if the mode is Requests, disable the Quantity field
			if(mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST){
				uniformQuantity.setText("-1");
				uniformQuantity.setEnabled(false);
			}
		}

		// shirt array
		ArrayAdapter<String> shirtAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.uniform_upper));
		// pants array
		ArrayAdapter<String> pantAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.uniform_pants));
		// gender array
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.gender_data));
		// shirt
		shirtAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		uniformShirts.setAdapter(shirtAdapter);
		uniformShirts
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						shirt = arg2;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// pants
		pantAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		uniformPants.setAdapter(pantAdapter);
		uniformPants
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						pants = arg2;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// gender
		genderAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		uniformGender.setAdapter(genderAdapter);
		uniformGender
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
	}

	private OnClickListener uniformSubmitListner = new OnClickListener() {

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
								//
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
		jsonDonated.put("tshirt", shirt);
		jsonDonated.put("pants", pants);
		jsonDonated.put("size", uniformSize.getText().toString());
		jsonDonated.put("gender", gender);
		jsonDonated.put("blazer", check);

		jsonDonated.put("type", "uniform");

		json.put("item", jsonDonated);

		int count = 0;
		try {
			count = Integer.parseInt(uniformQuantity.getText().toString());
			json.put("quantity", count);
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
		getMenuInflater().inflate(R.menu.uniforms_donation, menu);
		return true;
	}

}
