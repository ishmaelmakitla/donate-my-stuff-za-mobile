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
public class BlanketDonationActivity extends Activity {
	private static final String TAG = BlanketDonationActivity.class.getSimpleName();
	
	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationoffer";

	private EditText quantity;

	private Button blanketSubmit;
	
	//the Server URL based on the mode (Request/Offer)
	private String serverURL = null;
	//mode
	int mode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blanket_donation);
		quantity = (EditText) findViewById(R.id.quantity);
		blanketSubmit = (Button) findViewById(R.id.blanketSubmit);
		blanketSubmit.setOnClickListener(blanketSubmitListner);
		
		//check the mode or source of invoking this Intent (was it in Offers or Requests)
				Bundle extras = getIntent().getExtras();
				if(extras !=null){
					//get the mode
					mode = extras.getInt(DonateMyStuffGlobals.KEY_MODE);
					Log.i(TAG, "Current Mode at ClothDonation is "+mode);
					serverURL = (mode == DonateMyStuffGlobals.MODE_OFFERS_LIST? 
							         DonateMyStuffGlobals.MAKE_DONATION_OFFER_SERVLET_URL: DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL);
					
					//if the mode is Requests, disable the Quantity field
					if(mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST){
						quantity.setText("-1");
						quantity.setEnabled(false);
					}
				}
	}

	private OnClickListener blanketSubmitListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;
			try {
				offerJson = createOfferJSON();

				JsonObjectRequest request = new JsonObjectRequest(
						Request.Method.POST, MAKE_DONATION_OFFER_SERVLET_URL
								, offerJson,
						new Response.Listener<JSONObject>() {

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
		// donated item
		JSONObject jsonDonated = new JSONObject();
		jsonDonated.put("type", "blankets");

		json.put("item", jsonDonated);

		int count = 0;
		try {			
			 //quantity only set for Offers,			
			  if(mode != DonateMyStuffGlobals.MODE_REQUESTS_LIST ){
				  count = Integer.parseInt(quantity.getText().toString());
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
		getMenuInflater().inflate(R.menu.blanket_donation, menu);
		return true;
	}

}
