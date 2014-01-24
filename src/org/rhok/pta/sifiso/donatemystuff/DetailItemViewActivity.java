package org.rhok.pta.sifiso.donatemystuff;

import java.sql.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;
import org.rhok.pta.sifiso.donatemystuff.model.DonationRequest;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author Sifiso Mtshweni & Ishmael Makitla
 * 
 */
public class DetailItemViewActivity extends Activity {

	private static final String TAG = DetailItemViewActivity.class
			.getSimpleName();
	// mode - the user is either seeing details of an offer or details of a
	// request
	private static final int OFFER = 0;
	private static final int REQUEST = 1;
	private static final int INVALID = -1;

	private static final String KEY_OFFER = "offer";
	private static final String KEY_REQUEST = "request";

	EditText donor_id;
	EditText donated_date;
	EditText item_name;
	EditText item_code;
	EditText donor_quantity;
	Button requestSubmit;
	private String valid_donor_input;
	private ActionBar bar;
	DonationOffer offer;
	DonationRequest request;
	// number of items to bid for
	int itemCount = 0;
	// the current mode of this UI
	int mode = INVALID;
	// user session object
	UserSession session;

	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getExtras();
		if (b.containsKey(KEY_OFFER)) {
			setContentView(R.layout.detail_request_items);
		} else if (b.containsKey(KEY_REQUEST)) {
			// setContentView(R.layout.detail_request_items);
			setContentView(R.layout.activity_detail_item_view);
		}
		setField();
	}

	private void setField() {
		Bundle b = getIntent().getExtras();
		donor_id = (EditText) findViewById(R.id.donor_id);
		donated_date = (EditText) findViewById(R.id.donated_date);
		item_name = (EditText) findViewById(R.id.item_name);
		item_code = (EditText) findViewById(R.id.item_code);
		donor_quantity = (EditText) findViewById(R.id.donor_quantity);

		requestSubmit = (Button) findViewById(R.id.requestSubmit);
		requestSubmit.setOnClickListener(onSubmitRequestClick);

		// get the session object
		session = (UserSession) b
				.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
		// set title - must do this for all UIs to be consistent
		String title = getTitle().toString() + " " + session.getUsername();
		setTitle(title);

		if (b.containsKey(KEY_OFFER)) {
			// get the offer
			offer = (DonationOffer) b.getSerializable(KEY_OFFER);
			if (offer != null) {
				mode = OFFER;
			}
			// load the offer
			loadDonationOffer(offer);
		} else if (b.containsKey(KEY_REQUEST)) {
			request = (DonationRequest) b.getSerializable(KEY_REQUEST);
			if (request != null) {
				mode = REQUEST;
			}
			// load the request
			loadDonationRequest(request);
		}

	}

	/**
	 * Method used to populate the properties of an offer onto the UI
	 * 
	 * @param offer
	 */
	private void loadDonationOffer(DonationOffer offer) {
		donor_id.setText(offer.getId());
		donated_date.setText(offer.getOfferDate().toString());
		item_name.setText(offer.getItem().getName());
		if (offer.getItem().getGenderCode() == 0) {
			item_code.setText("Male");
		} else {
			item_code.setText("Female");
		}
		donor_quantity.setText(offer.getQuantity() + "");
	}

	/**
	 * Method used to populate the properties of an offer onto the UI
	 * 
	 * @param offer
	 */
	private void loadDonationRequest(DonationRequest request) {
		donor_id.setText(request.getId());
		// donated_date.setText(request.getRequestDate().toString());
		item_name.setText(request.getRequestedDonationItem().getName());
		if (request.getRequestedDonationItem().getGenderCode() == 0) {
			item_code.setText("Male");
		} else {
			item_code.setText("Female");
		}
		// donor_quantity.setText(request.getQuantity() + "");
	}

	/**
	 * Listener for when the button is clicked - The first click
	 */
	private int clickCount = 0;
	private OnClickListener onSubmitRequestClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i(TAG,
					"onSubmitRequestClick - about to submit request/offer-for");
			clickCount += 1;
			if (clickCount > 1) {
				// at this stage, the use has already specified the number of
				// items required
				// check view mode
				switch (mode) {
				case REQUEST:
					makeOffer(itemCount);
					break;
				case OFFER:
					bidForDonationOffer(itemCount);
					break;
				}

			} else {
				// prompt the user
				promptRequiredItems();
			}

		}
	};

	/**
	 * Method for converting the Donation-Offer into a Donation-Request for
	 * (THIS offer) - The user indicates how many of the offered items he is
	 * bidding for
	 */
	private void bidForDonationOffer(int itemsBiddingFor) {

		if (offer != null) {
			Log.d(TAG,
					"bidForDonationOffer:: About to bid for Offer "
							+ offer.getId());
			int itemQuantity = itemsBiddingFor;
			while (itemsBiddingFor == 0
					|| (itemsBiddingFor > offer.getQuantity() && offer
							.getQuantity() > 0)) {
				itemQuantity = promptRequiredItems();
			}
			// make a donation request for this offer
			DonationRequest donationBid = new DonationRequest();
			// the requested item is the offered item
			donationBid.setRequestedDonationItem(offer.getItem());
			// now set the number of items required
			donationBid.setQuantity(itemQuantity);
			// specify that YOU are requesting the donation
			donationBid.setBeneficriaryId(session.getUserID());
			// specify that this is a response to an offer
			donationBid.setDonationOfferId(offer.getId());

			// now send the bid for the offer
			sendDonationOfferBid(donationBid);
		}
	}

	/**
	 * This is the method used to prompt the user for the number of offered
	 * items he is requesting.
	 * 
	 * @return
	 */
	private int promptRequiredItems() {
		int requiredItemQuantity = -1;
		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(context);

		View itemsPromptView = layoutInflater.inflate(
				R.layout.items_prompt_layout, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		// set prompts.xml to be the layout file of the alertdialog builder
		alertDialogBuilder.setView(itemsPromptView);

		final EditText input = (EditText) itemsPromptView
				.findViewById(R.id.userInput);
		// get the label for prompt message and change it based on the mode
		TextView promptLabel = (TextView) itemsPromptView
				.findViewById(R.id.tvPromptLabel);
		int label_id = (mode == OFFER ? R.string.prompt_label_offer
				: R.string.prompt_label_request);
		// set the label referenced by the identifier
		promptLabel.setText(label_id);

		// pop up prompt for required items
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// get user's input which is the number of items
						// required
						itemCount = Integer
								.parseInt(input.getText().toString());
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create an alert dialog
		AlertDialog alertD = alertDialogBuilder.create();

		// display submit on the button
		requestSubmit.setText("Submit");

		alertD.show();

		requiredItemQuantity = itemCount;

		return requiredItemQuantity;
	}

	/**
	 * This is the method for sending a donation-request in response to a
	 * donation offer
	 * 
	 * @param bid
	 *            - the request for donation.
	 */
	private void sendDonationOfferBid(DonationRequest bid) {
		JSONObject offerJson = null;
		try {
			String gsonJSONObject = (new Gson()).toJson(bid,
					DonationRequest.class);
			if (gsonJSONObject != null) {
				offerJson = new JSONObject(gsonJSONObject);
			}
			// check if we were able to construct Donation-Request JSON Object
			if (offerJson != null) {
				sendVolleyRequest(
						DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL,
						offerJson);
			} else {
				Log.w(TAG,
						"Unable To Construct Donation-Request JSON Object from received Donation-Request POJO");
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used when the user is responding to a Donation-Request by
	 * making an offer
	 * 
	 * @param itemsToOffer
	 *            - the number of items to be offered
	 */
	private void makeOffer(int itemsToOffer) {
		// for an offer to be made in response, the request object must NOT be
		// null
		if (request != null) {
			Log.d(TAG,
					"bidForDonationOffer:: About to bid for Offer "
							+ offer.getId());
			int itemQuantity = itemsToOffer;
			while (itemsToOffer == 0) {
				itemQuantity = promptRequiredItems();
			}
			// make a donation offer for this request
			DonationOffer newOffer = new DonationOffer();
			newOffer.setDeliver(true);
			// associate this offer with the original request-for-donation
			newOffer.setDonationRequestId(request.getId());
			// the offered item is the requested item
			newOffer.setItem(request.getRequestedDonationItem());
			// user specifies himself as the donor
			newOffer.setDonorId(session.getUserID());
			// set the number of items being offered for donation in response to
			// the request
			newOffer.setQuantity(itemQuantity);

			// now send the offer
			JSONObject offerJson = null;
			try {
				String gsonOfferJSONObject = (new Gson()).toJson(newOffer,
						DonationOffer.class);
				if (gsonOfferJSONObject != null) {
					offerJson = new JSONObject(gsonOfferJSONObject);
				}
				// check if we were able to construct Donation-Offer JSON Object
				if (offerJson != null) {
					sendVolleyRequest(
							DonateMyStuffGlobals.MAKE_DONATION_OFFER_SERVLET_URL,
							offerJson);
				} else {
					Log.w(TAG,
							"Unable To Construct Donation-Offer JSON Object from received Donation-Offer POJO");
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Method used to send out Volley JSON Requests.
	 * 
	 * @param url
	 *            - the url of the server for either donation-requests or
	 *            donation-offers
	 * @param payload
	 *            - the JSON payload
	 */
	private void sendVolleyRequest(final String url, JSONObject payload) {
		RequestQueue queue = Volley.newRequestQueue(context);

		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
				DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL,
				payload, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							Toast.makeText(getApplicationContext(),
									response.getString("message").toString(),
									Toast.LENGTH_LONG).show();
							if (response.getInt("status") == 0) {

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_item_view, menu);

		getActionBar().setTitle("Back");
		getActionBar().setIcon(R.drawable.ic_action_previous_item);
		getActionBar().setHomeButtonEnabled(true);
		
		return true;
	}

}
