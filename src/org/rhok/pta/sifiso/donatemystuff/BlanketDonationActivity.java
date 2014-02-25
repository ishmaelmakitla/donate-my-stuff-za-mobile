package org.rhok.pta.sifiso.donatemystuff;

import org.json.JSONException;
import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
public class BlanketDonationActivity extends Activity {
	private static final String TAG = BlanketDonationActivity.class
			.getSimpleName();

	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationoffer";
	
	private EditText quantity;
	private String valid_quantity;
	private Button blanketSubmit;
	private Bundle b;

	// the Server URL based on the mode (Request/Offer)
	private String serverURL = null;
	// session
	private UserSession session;
	// mode
	int mode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blanket_donation);
		quantity = (EditText) findViewById(R.id.quantity);

		blanketSubmit = (Button) findViewById(R.id.blanketSubmit);
		blanketSubmit.setOnClickListener(blanketSubmitListner);
		// check the mode or source of invoking this Intent (was it in Offers or
		// Requests)
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// get the mode
			mode = extras.getInt(DonateMyStuffGlobals.KEY_MODE);
			Log.i(TAG, "Current Mode at ClothDonation is " + mode);
			serverURL = (mode == DonateMyStuffGlobals.MODE_OFFERS_LIST ? DonateMyStuffGlobals.MAKE_DONATION_OFFER_SERVLET_URL
					: DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL);

			session = (UserSession) extras
					.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
			String title = getTitle().toString() + session.getUsername();
			setTitle(title);

			// if the mode is Requests, disable the Quantity field
			if (mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST) {
				quantity.setText("-1");
				quantity.setEnabled(false);
				valid_quantity = quantity.getText().toString();
			}
		}

	}

	private OnClickListener blanketSubmitListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;

			quantity.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					
					Log.d(TAG, "Quantity1 " + valid_quantity + " and UserId "
							+ session.getUserID());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

					Log.d(TAG, "Quantity2 " + valid_quantity + " and UserId "
							+ session.getUserID());
				}

				@Override
				public void afterTextChanged(Editable s) {

					Log.d(TAG, "Quantity3 " + valid_quantity + " and UserId "
							+ session.getUserID());
					Is_Valid_Quantity_Validation(0, 4, quantity);
				}
			});
			Is_Valid_Quantity_Validation(0, 4, quantity);
			Log.d(TAG,
					"Quantity " + valid_quantity + " and UserId "
							+ session.getUserID());
			if (valid_quantity != null && session.getUserID() != null) {
				try {
					offerJson = createOfferJSON();

					JsonObjectRequest request = new JsonObjectRequest(
							Request.Method.POST,
							MAKE_DONATION_OFFER_SERVLET_URL, offerJson,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									try {

										if (response.getInt("status") == 0) {
											b = new Bundle();
											b.putSerializable(
													DonateMyStuffGlobals.KEY_SESSION,
													session);
											b.putInt(
													DonateMyStuffGlobals.KEY_MODE,
													mode);

											AlertDialog.Builder builder = new AlertDialog.Builder(
													BlanketDonationActivity.this);
											builder.setMessage(
													response.getString(
															"message")
															.toString()
															+ "\n Do you want to make another offer?")
													.setCancelable(false)
													.setPositiveButton(
															"Yes",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int id) {
																	BlanketDonationActivity.this
																			.finish();
																	startActivity(new Intent(
																			getApplicationContext(),
																			BlanketDonationActivity.class)
																			.putExtras(b));
																}
															})
													.setNegativeButton(
															"No",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int id) {

																	BlanketDonationActivity.this
																			.finish();
																	dialog.cancel();

																}
															});

											AlertDialog alert = builder
													.create();
											alert.show();

											// quantity.setText(null);

										}
									} catch (JSONException e) {

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

					e.printStackTrace();
				}

			} else {
				Toast.makeText(getApplicationContext(), "Invalid User Input",
						Toast.LENGTH_SHORT).show();
				return;

			}

		}

	};

	private JSONObject createOfferJSON() throws JSONException {
		JSONObject json = new JSONObject();
		String me = session.getUserID();
		// these are depending on the mode of operation...
		if (mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST) {
			json.put("beneficiaryid", me);
			// this is not a bid...
			json.put("donationofferid", null);
			// by default donations are delivered
			json.put("collect", false);
		} else {
			json.put("donorid", me);
			json.put("donationrequestid", null);
			json.put("deliver", true);
		}
		// donated item
		JSONObject jsonDonated = new JSONObject();
		jsonDonated.put("type", "blankets");

		json.put("item", jsonDonated);

		int count = 0;
		try {
			// quantity only set for Offers,
			if (mode != DonateMyStuffGlobals.MODE_REQUESTS_LIST) {
				count = Integer.parseInt(valid_quantity);
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

	public boolean Is_Valid_Quantity_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Quantity Number Only");
			valid_quantity = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_quantity = null;
			return false;
		} else {
			valid_quantity = edt.getText().toString();
			return true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blanket_donation, menu);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back_icon:
			BlanketDonationActivity.this.finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
