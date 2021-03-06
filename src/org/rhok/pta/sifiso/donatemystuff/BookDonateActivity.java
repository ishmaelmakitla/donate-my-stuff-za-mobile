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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
 * @author Sifiso Mtshweni & Ishmael Makitla
 * 
 */
public class BookDonateActivity extends Activity {
	private static final String TAG = BookDonateActivity.class.getSimpleName();

	private EditText bookName;
	private EditText bookSize;
	private EditText bookAge;
	private EditText bookQuantity;
	private EditText bookAgeRest;
	private Button bookSubmit;
	private TextView textView4;
	private EditText isbn;
	private Bundle b;
	private String valid_book_name, valid_book_size, valid_book_age,
			valid_book_quantity, valid_book_age_rest, valid_isbn;

	// the Server URL based on the mode (Request/Offer)
	private String serverURL = null;

	private UserSession session;
	// mode
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
		textView4 = (TextView) findViewById(R.id.textView4);
		isbn = (EditText) findViewById(R.id.isbn);
		bookSubmit = (Button) findViewById(R.id.bookSubmit);
		bookSubmit.setOnClickListener(submitBookListner);

		// check the mode or source of invoking this Intent (was it in Offers or
		// Requests)
		textValidation();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// get the mode
			mode = extras.getInt(DonateMyStuffGlobals.KEY_MODE);

			serverURL = (mode == DonateMyStuffGlobals.MODE_OFFERS_LIST ? DonateMyStuffGlobals.MAKE_DONATION_OFFER_SERVLET_URL
					: DonateMyStuffGlobals.MAKE_DONATION_REQUEST_SERVLET_URL);

			Log.i(TAG, "Current Mode at BookDonateActivity is " + mode
					+ " URL is = " + serverURL);

			session = (UserSession) extras
					.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
			String title = getTitle().toString();
			setTitle(title + " " + session.getUsername());

			// if the mode is Requests, disable the Quantity field
			if (mode == DonateMyStuffGlobals.MODE_REQUESTS_LIST) {
				textView4.setVisibility(TextView.GONE);
				bookQuantity.setVisibility(EditText.GONE);
				bookQuantity.setText("1");
				bookQuantity.setEnabled(false);
				valid_book_quantity = bookQuantity.getText().toString();
			}
		}
	}

	private OnClickListener submitBookListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;
			textValidation();
			if (valid_book_name != null && valid_book_age != null
					&& valid_book_quantity != null
					&& valid_book_age_rest != null && valid_book_size != null
					&& session.getUserID() != null) {
				try {
					offerJson = createJSONPayload();
					JsonObjectRequest request = new JsonObjectRequest(
							Request.Method.POST, serverURL, offerJson,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									Log.d(TAG, response.toString());
									processServerResponse(response);
									// perhaps at this point we clear the fields
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
													BookDonateActivity.this);
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
																	BookDonateActivity.this
																			.finish();
																	startActivity(new Intent(
																			getApplicationContext(),
																			BookDonateActivity.class)
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

																	BookDonateActivity.this
																			.finish();
																	dialog.cancel();

																}
															});

											AlertDialog alert = builder
													.create();
											alert.show();

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

	/**
	 * Method to process the response sent by the server following the
	 * submission of a donation request/offer
	 * 
	 * @param response
	 */
	private void processServerResponse(JSONObject response) {
		try {
			String serverMessage = response.getString("message");
			int statusCode = response.getInt("status");
			Toast.makeText(getApplicationContext(), serverMessage,
					Toast.LENGTH_LONG).show();

		} catch (JSONException e) {
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
		// donated/requested item
		JSONObject jsonDonated = new JSONObject();
		jsonDonated.put("name", valid_book_name);
		jsonDonated.put("size", Integer.parseInt(valid_book_size));
		jsonDonated.put("age", Integer.parseInt(valid_book_age));
		jsonDonated
				.put("agerestriction", Integer.parseInt(valid_book_age_rest));
		jsonDonated.put("type", "book");
		jsonDonated.put("isbn", valid_isbn);

		json.put("item", jsonDonated);

		int count = 0;
		try {
			if (mode != DonateMyStuffGlobals.MODE_REQUESTS_LIST) {
				count = Integer.parseInt(valid_book_quantity);
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
		bookName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				Is_Valid_Book_Name(bookName);
			}
		});
		bookSize.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				Is_Valid_Book_Size_Validation(0, 4, bookSize);
			}
		});
		bookAge.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				Is_Valid_Book_Age_Validation(0, 5, bookAge);
			}
		});
		bookQuantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				Is_Valid_Book_Quantity_Validation(0, 4, bookQuantity);
			}
		});
		bookAgeRest.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				Is_Valid_Book_Age_Rest_Validation(0, 4, bookAgeRest);
			}
		});
		isbn.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				Is_Isbn_Validation(0, 10, isbn);
			}
		});
	}

	public boolean Is_Valid_Book_Name(EditText edt)
			throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Accept Alphabets Only.");
			valid_book_name = null;
			return false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_book_name = null;
			return false;
		} else {
			valid_book_name = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Book_Size_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Size Number Only");
			valid_book_size = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_book_size = null;
			return false;
		} else {
			valid_book_size = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Book_Age_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Age Number Only");
			valid_book_age = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_book_age = null;
			return false;
		} else {
			valid_book_age = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Isbn_Validation(int MinLen, int MaxLen, EditText edt)
			throws NumberFormatException {
	
		if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_isbn = null;
			return false;
		} else {
			valid_isbn = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Book_Age_Rest_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Age Restriction Number Only");
			valid_book_age_rest = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_book_age_rest = null;
			return false;
		} else {
			valid_book_age_rest = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Book_Quantity_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Quantity Number Only");
			valid_book_quantity = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_book_quantity = null;
			return false;
		} else {
			valid_book_quantity = edt.getText().toString();
			return true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_donate, menu);

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
			BookDonateActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
