package org.rhok.pta.sifiso.donatemystuff;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class RegisterDonorActivity extends Activity {
	private static final String TAG = RegisterDonorActivity.class
			.getSimpleName();

	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/register";
	private EditText name, surname, mobile, telephone, email, username,
			password, unitnumber, unitname, streetname, areaname, city;
	private String valid_name, valid_surname, valid_email, valid_streetname,
			valid_city, valid_areaname, valid_unitname, valid_phone,
			valid_cell;
	private RadioGroup rdRole;
	private Spinner spGender, spType, spProvince;
	private Button registerSubmit, logingSubmit;
	private int gender, type, province, role;
	private String[] types;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_donor);
		setFiled();
	}

	private void setFiled() {
		// editText fields
		name = (EditText) findViewById(R.id.name);
		surname = (EditText) findViewById(R.id.surname);
		mobile = (EditText) findViewById(R.id.mobile);
		telephone = (EditText) findViewById(R.id.telephone);
		email = (EditText) findViewById(R.id.email);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.passowrd);
		unitnumber = (EditText) findViewById(R.id.unitnumber);
		unitname = (EditText) findViewById(R.id.unitname);
		streetname = (EditText) findViewById(R.id.streetname);
		areaname = (EditText) findViewById(R.id.areaname);
		city = (EditText) findViewById(R.id.city);

		// Spinners
		spGender = (Spinner) findViewById(R.id.spGender);
		spType = (Spinner) findViewById(R.id.spType);
		spProvince = (Spinner) findViewById(R.id.spProvince);

		// RadioGroup
		rdRole = (RadioGroup) findViewById(R.id.rdRole);
		rdRole.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				role = checkedId;
			}
		});
		textActions();
		// buttons
		registerSubmit = (Button) findViewById(R.id.registerSubmit);
		registerSubmit.setOnClickListener(registerSubmitListner);
		logingSubmit = (Button) findViewById(R.id.logingSubmit);
		setSpinners();
	}

	private OnClickListener registerSubmitListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;

			if (name.getError() == null && surname.getError() == null
					&& email.getError() == null && city.getError() == null
					&& areaname.getError() == null
					&& username.getText() != null && password.getText() != null
					&& mobile.getError() == null
					&& telephone.getError() == null) {
				try {
					offerJson = createOfferJSON();

					JsonObjectRequest request = new JsonObjectRequest(
							Request.Method.POST,
							MAKE_DONATION_OFFER_SERVLET_URL, offerJson,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									Log.d(TAG, response.toString());

									Toast.makeText(getApplicationContext(),
											"Registered successfully",
											Toast.LENGTH_LONG).show();
									finish();
									try {
										if (response.getInt("status") == 0) {
											name.setText(null);
											surname.setText(null);
											mobile.setText(null);
											telephone.setText(null);
											email.setText(null);
											username.setText(null);
											password.setText(null);
											unitname.setText(null);
											unitnumber.setText(null);
											streetname.setText(null);
											areaname.setText(null);
											city.setText(null);

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
									error.getMessage();
									error.printStackTrace();

								}

							});

					queue.add(request);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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

		json.put("name", valid_name);
		json.put("surname", valid_surname);
		json.put("gender", gender);
		json.put("mobile", valid_cell);
		json.put("telephone", valid_phone);
		json.put("email", valid_email);
		json.put("username", username.getText().toString());
		json.put("password", password.getText().toString());
		json.put("role", role);
		json.put("type", type);

		// donated item
		JSONObject jsonDonated = new JSONObject();
		jsonDonated.put("unitnumber", unitnumber.getText().toString());
		jsonDonated.put("unitname", valid_unitname);
		jsonDonated.put("streetname", valid_streetname);
		jsonDonated.put("areaname", valid_areaname);
		jsonDonated.put("city", valid_city);
		jsonDonated.put("province", province);
		jsonDonated.put("country", "South Africa");
		jsonDonated.put("xcoordinate", -203);
		jsonDonated.put("ycoordinate", 203);

		json.put("address", jsonDonated);

		Log.d(TAG, "Returning donation offer json=" + json);

		return json;
	}

	private void setSpinners() {

		// setting the gender values on a spinner
		ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.gender_data));
		genderAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spGender.setAdapter(genderAdapter);
		spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				gender = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// setting the type values on a spinner
		types = getResources().getStringArray(R.array.get_type);
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, types);

		typeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(typeAdapter);
		spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				type = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// setting the gender values on a spinner
		ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.get_provinces));
		provinceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spProvince.setAdapter(provinceAdapter);
		spProvince
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						province = arg2;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}

	private void textActions() {
		name.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Name(name);

			}
		});
		surname.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Surname(surname);
			}
		});
		mobile.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Cell_Number_Validation(0, 9, mobile);
			}
		});
		telephone.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Phone_Number_Validation(0, 9, telephone);
			}
		});

		email.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Email(email);
			}
		});
		unitname.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Unitname(unitname);
			}
		});
		streetname.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Streetname(streetname);
			}
		});

		areaname.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_Areaname(areaname);
			}
		});
		city.addTextChangedListener(new TextWatcher() {

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
				Is_Valid_City(city);
			}
		});
	}

	// ==================================================validations==========================================//

	public boolean Is_Valid_Name(EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Accept Alphabets Only.");
			valid_name = null;
			return false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_name = null;
			return false;
		} else {
			valid_name = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Unitname(EditText edt) throws NumberFormatException {
		if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_unitname = null;
			return false;
		} else {
			valid_unitname = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Areaname(EditText edt) throws NumberFormatException {
		if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_areaname = null;
			return false;
		} else {
			valid_areaname = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_City(EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Accept Alphabets Only.");
			valid_city = null;
			return false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_city = null;
			return false;
		} else {
			valid_city = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Cell_Number_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Cell Number Only");
			valid_cell = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_cell = null;
			return false;
		} else {
			valid_cell = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Phone_Number_Validation(int MinLen, int MaxLen,
			EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Phone Number Only");
			valid_phone = null;
			return false;
		} else if (Double.valueOf(edt.getText().toString()) < MinLen
				|| Double.valueOf(edt.getText().length()) > MaxLen) {
			edt.setError("Out of Range " + MinLen + " or " + MaxLen);
			valid_phone = null;
			return false;
		} else {
			valid_phone = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Streetname(EditText edt)
			throws NumberFormatException {
		if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_streetname = null;
			return false;
		} else {
			valid_streetname = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Surname(EditText edt) throws NumberFormatException {
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Accept Alphabets Only.");
			valid_surname = null;
			return false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			valid_surname = null;
			return false;
		} else {
			valid_surname = edt.getText().toString();
			return true;
		}

	}

	public boolean Is_Valid_Email(EditText edt) {
		if (edt.getText().toString() == null) {
			edt.setError("Invalid Email Address");
			valid_email = null;
			return false;
		} else if (isEmailValid(edt.getText().toString()) == false) {
			edt.setError("Invalid Email Address");
			valid_email = null;
			return false;
		} else {
			valid_email = edt.getText().toString();
			return true;
		}
	}

	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

	}

	// ==================================================end
	// validation==========================================//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_donor, menu);
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
			RegisterDonorActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
