package org.rhok.pta.sifiso.donatemystuff;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class RegisterDonorActivity extends Activity {
	private static final String TAG = RegisterDonorActivity.class
			.getSimpleName();

	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/register";
	private EditText name, surname, mobile, telephone, email, username,
			password, unitnumber, unitname, streetname, areaname, city;
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
			try {
				offerJson = createOfferJSON();

				JsonObjectRequest request = new JsonObjectRequest(
						Request.Method.POST, MAKE_DONATION_OFFER_SERVLET_URL
								, offerJson,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Log.d(TAG, response.toString());
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

		json.put("name", name.getText().toString());
		json.put("surname", surname.getText().toString());
		json.put("gender", gender);
		json.put("mobile", mobile.getText().toString());
		json.put("telephone", telephone.getText().toString());
		json.put("email", email.getText().toString());
		json.put("username", username.getText().toString());
		json.put("password", password.getText().toString());
		json.put("role", role);
		json.put("type", type);

		// donated item
		JSONObject jsonDonated = new JSONObject();
		jsonDonated.put("unitnumber", unitnumber.getText().toString());
		jsonDonated.put("unitname", unitname.getText().toString());
		jsonDonated.put("streetname", streetname.getText().toString());
		jsonDonated.put("areaname", areaname.getText().toString());
		jsonDonated.put("city", city.getText().toString());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_donor, menu);
		return true;
	}

}
