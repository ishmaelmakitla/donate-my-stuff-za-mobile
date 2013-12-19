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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class LoginActivity extends Activity {
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private EditText log_username;
	private EditText log_password;
	private Button log_submit;
	private Button create_account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setField();
	}

	private void setField() {
		log_username = (EditText) findViewById(R.id.log_username);
		log_password = (EditText) findViewById(R.id.log_password);
		log_submit = (Button) findViewById(R.id.log_submit);
		create_account = (Button) findViewById(R.id.create_account);
		create_account.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						RegisterDonorActivity.class));
				finish();

			}
		});
		log_submit.setOnClickListener(logSubmitListner);
	}

	private OnClickListener logSubmitListner = new OnClickListener() {
		@Override
		public void onClick(View v) {

			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			JSONObject offerJson;
			try {
				offerJson = createOfferJSON();
                Log.d(TAG, "Login Request JSON = \n"+offerJson.toString());
                
				JsonObjectRequest request = new JsonObjectRequest(
				Request.Method.POST, DonateMyStuffGlobals.LOGIN_SERVLET_URL,
				offerJson, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					Log.d(TAG, response.toString());
					try {
							String loginResponseMessage = response.getString("message");
							if (response.getInt("status") == 100) {
								Bundle loginBundle = new Bundle();
								//create a session object and put it in the bundle
								UserSession session = new UserSession();
								session.setUserID(loginResponseMessage);
								session.setUsername(log_username.getText().toString());
								loginBundle.putSerializable(DonateMyStuffGlobals.KEY_SESSION, session);
								//show the main screen												
								startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtras(loginBundle));
							}
							else{
									//login failed:
									Toast.makeText(getApplicationContext(), "Login Error: "+loginResponseMessage, Toast.LENGTH_LONG).show();
									return;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	private JSONObject createOfferJSON() throws JSONException {
		JSONObject json = new JSONObject();

		//json.put("username", "thabi");// log_username.getText().toString());
		//json.put("password", "thabi");// log_password.getText().toString());
		
		json.put("username", log_username.getText().toString());
		json.put("password", log_password.getText().toString());

		Log.d(TAG, "Returning donation offer json=" + json);

		return json;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
