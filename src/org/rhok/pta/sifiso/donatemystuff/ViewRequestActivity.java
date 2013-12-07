package org.rhok.pta.sifiso.donatemystuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ViewRequestActivity extends Activity {

	private static final String TAG = ViewRequestActivity.class.getSimpleName();

	private static final String GET_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/donationoffers";
	private ListView listRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_request);
		setFields();
	}

	private void setFields() {
		listRequest = (ListView) findViewById(R.id.listRequest);
		RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
		StringRequest request = new StringRequest(Request.Method.GET,
				GET_DONATION_OFFER_SERVLET_URL, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Sifiso" + response.toString());
						try {
							processDonationOffers(response);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "issues with server", error);
						// error.printStackTrace();

					}

				});
		queue.add(request);

	}
	/*Deserialize a json string
	 * */
	private void processDonationOffers(String offers) throws JSONException {

		if(!offers.startsWith("{") && !offers.endsWith("}")){
			offers = "{ \"offers\":"+offers+"}";
		}

		Gson gson = new Gson();
		
    //    JSONArray jo= gson.fromJson(offers, JSONArray.class);
        JsonParser parser = new JsonParser();
        
        JsonObject offersObject = gson.fromJson(offers, JsonObject.class);
        Log.d(TAG, "offersObject = "+offersObject);
        JsonElement jel = parser.parse(offers); // offersObject.get("offers");
        JsonArray jo =null;
        
        if(jel != null){
        	Log.d(TAG, "jel = "+jel);
        	//JsonElement offersArray = jel.getAsJsonObject().get("offers").getAsJsonArray();
        	jo = jel.getAsJsonObject().get("offers").getAsJsonArray(); //jel.getAsJsonArray();
        	if(jo != null)
        	Log.d(TAG, "jo.get(0) = "+jo.get(1).toString());
        }
        
        
        if(jo != null){
        	Log.d(TAG, "Parsed JSON successfully");
        	
        	//	JsonArray jo = parser.parse(jsonOffers).getAsJsonArray();
        	//	Log.d(TAG, jo.toString());
        		List<DonationOffer> donationOfferList = new ArrayList<DonationOffer>();

        			Log.d(TAG, "Hello World");

        			if (jo.size() >0) {
        				
        				for (int x = 0; x < jo.size(); x++) {
        					JsonObject offerItem = (JsonObject)jo.get(x);
        					DonationOffer donationOffer = gson.fromJson(
        							offerItem.toString(), DonationOffer.class);
        					donationOfferList.add(donationOffer);
        					Log.d(TAG, donationOfferList.get(x).getDonorId());
        				}
        			}

        	return;
        }
        
        
        Log.e(TAG, "Could Not Parse As JSON-Array? Jo = NULL:: \n"+offers);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_request, menu);
		return true;
	}

}
