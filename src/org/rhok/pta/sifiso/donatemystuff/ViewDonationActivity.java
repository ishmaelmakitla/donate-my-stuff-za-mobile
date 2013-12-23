package org.rhok.pta.sifiso.donatemystuff;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.rhok.pta.sifiso.donatemystuff.adapter.DonationRequestListAdapter;
import org.rhok.pta.sifiso.donatemystuff.adapter.OfferAdapter;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;
import org.rhok.pta.sifiso.donatemystuff.model.DonationRequest;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * 
 * @author Sifiso Mtshweni & Ishmael Makitla
 * 
 */
public class ViewDonationActivity extends Activity {

	private static final String TAG = ViewDonationActivity.class.getSimpleName();

	private static final String GET_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/donationoffers?type=";
	private ListView listRequest;
	private OfferAdapter adapter;
	private String type;
		
	private int mode = -1;
	
	private UserSession session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_request);
		setFields();
	}

	private void setFields() {
		Bundle b = getIntent().getExtras();
		type = b.getString("type");
		//now the Server URL depends on whether the mode is OFFERS or REQUESTS
		mode = b.getInt(DonateMyStuffGlobals.KEY_MODE);
		String serverURL = (mode == DonateMyStuffGlobals.MODE_OFFERS_LIST? 
									DonateMyStuffGlobals.GET_DONATION_OFFER_SERVLET_URL: DonateMyStuffGlobals.GET_DONATION_REQUEST_SERVLET_URL);
		this.session = (UserSession)b.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
		String title = getTitle().toString();
		setTitle(title+ " "+session.getUsername());
		//append the type parameter
		serverURL = serverURL + "?type="+type;
		
		//check if the items to be shown are those submitted by the user
		boolean showingMineOnly = b.getBoolean(DonateMyStuffGlobals.FLAG_VIEW_MINE_ONLY);
		if(showingMineOnly){
			Log.d(TAG, "Only Showing Items Submitted By This USER ("+session.getUserID()+")");
			//server URL must include user-id = 
			if(mode == DonateMyStuffGlobals.MODE_OFFERS_LIST){ serverURL +="&donorid="+session.getUserID();}
			else{
				serverURL +="&beneficiary="+session.getUserID();
			}
		}
		
		Log.d(TAG, type);
		listRequest = (ListView) findViewById(R.id.listRequest);
		
		RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
		StringRequest request = new StringRequest(Request.Method.GET,
				serverURL, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "VolleyResponse::" + response.toString());
						try {
							if(mode == DonateMyStuffGlobals.MODE_OFFERS_LIST){
							processDonationOffers(response);
							}
							else{
								processDonationRequests(response);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "issues with server", error);
					}

				});
		queue.add(request);

	}

	/*
	 * Deserialize a json string
	 */
	private void processDonationOffers(String offers) throws JSONException {

		Gson gson = new Gson();

		JsonParser parser = new JsonParser();

		JsonElement jel = parser.parse(offers);
		JsonArray jo = null;

		if (jel != null) {

			jo = jel.getAsJsonObject().get("offers").getAsJsonArray();
			if (jo != null) {
				Log.d(TAG, "Parsed JSON successfully");

				List<DonationOffer> donationOfferList = new ArrayList<DonationOffer>();
				if (jo.size() > 0) {

					for (int x = 0; x < jo.size(); x++) {
						JsonObject offerItem = (JsonObject) jo.get(x);
						String strOff = offerItem.toString();
						Log.d(TAG, strOff);
						DonationOffer donationOffer = gson.fromJson(offerItem,DonationOffer.class);
						donationOfferList.add(donationOffer);
					}
					listAdapter(donationOfferList);
				}

				return;
			}

		}
		else{
			Log.e(TAG, "Could Not Parse As JSON-Array? Jo = NULL:: \n" + offers);
		}		

	}
	/**
	 * Method used to process the JSON string of Donation-Requests received from Server
	 * 
	 * @param donationRequestsJSONString  -JSON of donation requests
	 */
	private void processDonationRequests(String donationRequestsJSONString){
		Gson gson = new Gson();

		JsonParser parser = new JsonParser();

		JsonElement jel = parser.parse(donationRequestsJSONString);
		JsonArray jo = null;

		if (jel != null) {

			jo = jel.getAsJsonObject().get("requests").getAsJsonArray();
			if (jo != null) {
				Log.d(TAG, "Parsed JSON successfully");

				List<DonationRequest> donationRequestsList = new ArrayList<DonationRequest>();
				if (jo.size() > 0) {

					for (int x = 0; x < jo.size(); x++) {
						JsonObject requestItem = (JsonObject) jo.get(x);
						String strOff = requestItem.toString();
						Log.d(TAG, strOff);
						DonationRequest donationRequest = gson.fromJson(requestItem,DonationRequest.class);
						donationRequestsList.add(donationRequest);
					}
					//set them on a list adapter for requests
					createDonationRequestsListAdapter(donationRequestsList);
				}

				return;
			}
		}
		else{
			Log.e(TAG, "Could Not Parse As JSON-Array? Jo = NULL:: \n" + donationRequestsJSONString);
		}
	}
	/**
	 * List adapter for Donation-Requests
	 * @param requestsList
	 */
	private void createDonationRequestsListAdapter(List<DonationRequest> requestsList){
		DonationRequestListAdapter requestsListAdapter = new DonationRequestListAdapter(getApplicationContext(),	R.layout.customize_offer_list, requestsList, session);
		listRequest.setAdapter(requestsListAdapter);
	}
	
	/**
	 * List-Adapter for Donation-Offers
	 * @param list
	 */
	private void listAdapter(List<DonationOffer> list) {
		if (list == null) {
			list = new ArrayList<DonationOffer>();
		}
		adapter = new OfferAdapter(getApplicationContext(),	R.layout.customize_offer_list, list, session);
		listRequest.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_request, menu);
		return true;
	}

}
