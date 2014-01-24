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
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
	private TextView textView4;
	private Bundle b;
	private Button shoeSubmit;
	private int gender;
	private String valid_shoe_name, valid_shoe_size, valid_shoe_quantity;
	// the Server URL based on the mode (Request/Offer)
	private String serverURL = null;

	// session
	private UserSession session;
	// mode
	int mode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoes_donation);
		shoeName = (EditText) findViewById(R.id.shoeName);
		shoeSize = (EditText) findViewById(R.id.shoeSize);
		shoeGender = (Spinner) findViewById(R.id.shoeGender);
		textView4 = (TextView) findViewById(R.id.textView4);
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
		textValidation();
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
				textView4.setVisibility(TextView.GONE);
				shoeQuantity.setVisibility(EditText.GONE);
				shoeQuantity.setText("1");
				shoeQuantity.setEnabled(false);
			}
		}
	}

	private OnClickListener shoeSubmitListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;
			if (valid_shoe_name != null && valid_shoe_quantity != null
					&& valid_shoe_size != null && session.getUserID() != null) {
				try {
					offerJson = createOfferJSON();

					JsonObjectRequest request = new JsonObjectRequest(
							Request.Method.POST,
							MAKE_DONATION_OFFER_SERVLET_URL, offerJson,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									try {
										Toast.makeText(
												getApplicationContext(),
												response.getString("message")
														.toString(),
												Toast.LENGTH_LONG).show();
										if (response.getInt("status") == 0) {
											b = new Bundle();
											b.putSerializable(
													DonateMyStuffGlobals.KEY_SESSION,
													session);
											finish();
											startActivity(new Intent(
													getApplicationContext(),
													ShoesDonationActivity.class)
													.putExtras(b));
											/*
											 * shoeName.setText(null);
											 * shoeSize.setText(null);
											 * shoeQuantity.setText(null);
											 */

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
				shoeName.setText(null);
				shoeQuantity.setText(null);
				shoeSize.setText(null);
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
		jsonDonated.put("name", valid_shoe_name);
		jsonDonated.put("size", valid_shoe_size);
		jsonDonated.put("gender", gender);
		jsonDonated.put("type", "shoes");

		json.put("item", jsonDonated);

		int count = 0;
		try {
			// quantity only set for Offers,
			if (mode != DonateMyStuffGlobals.MODE_REQUESTS_LIST) {
				count = Integer.parseInt(valid_shoe_quantity);
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

	private void textValidation() {
		shoeName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Is_Valid_Shoe_Name(shoeName);
			}
		});
		shoeSize.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Is_Valid_Shoe_Size_Validation(1, 4, shoeSize);
			}
		});
		shoeQuantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Is_Valid_Shoe_Quantity_Validation(1, 4, shoeQuantity);
			}
		});

	}

	public boolean Is_Valid_Shoe_Quantity_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Quantity Number Only");
			valid_shoe_quantity = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_shoe_quantity = null;
			return false;
		} else {
			valid_shoe_quantity = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Shoe_Name(EditText edt)
			throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Accept Alphabets Only.");
			valid_shoe_name = null;
			return false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_shoe_name = null;
			return false;
		} else {
			valid_shoe_name = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Shoe_Size_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Size Number Only");
			valid_shoe_size = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_shoe_size = null;
			return false;
		} else {
			valid_shoe_size = edt.getText().toString();
			return true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shoes_donation, menu);

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
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	

}
