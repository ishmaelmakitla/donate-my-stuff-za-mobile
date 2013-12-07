package org.rhok.pta.sifiso.donatemystuff;

import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.model.Donor;
import org.rhok.pta.sifiso.donatemystuff.model.Item;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DonateFragment extends Fragment {

	private EditText type;
	private EditText edtUser;
	private Button add;
	
	private Button donateButton;
	private Button requestDonationButton;
	
	private Gson gson;

	private static final String TAG = DonateFragment.class.getSimpleName();
	private static final String MAKE_DONATION_REQUEST_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationrequest";
	private static final String MAKE_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/makedonationoffer";
	public DonateFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.donate_fragment, container,
				false);
		type = (EditText) rootView.findViewById(R.id.type);
		edtUser = (EditText)rootView.findViewById(R.id.edtUser);
		add = (Button) rootView.findViewById(R.id.add);
		add.setOnClickListener(new AddClickListner());
		Toast.makeText(rootView.getContext(), "hello", Toast.LENGTH_LONG)
				.show();
		return rootView;

	}

	private void setFields(View v) {

	}

	private class AddClickListner implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			RequestQueue queue = Volley.newRequestQueue(v.getContext());
			/*
			 * JsonObjectRequest jsObjRequest = new JsonObjectRequest(
			 * Request.Method.GET,
			 * "http://1.za-donate-my-stuff.appspot.com/donationrequests", null,
			 * new Response.Listener<JSONObject>() {
			 * 
			 * @Override public void onResponse(JSONObject response) { // TODO
			 * Auto-generated method stub
			 * 
			 * String string = new String(response.toString());
			 * type.setText(string);
			 * Toast.makeText(getActivity().getBaseContext(), "hello",
			 * Toast.LENGTH_LONG) .show(); Log.i("Response", "hjsdhfks"+string);
			 * 
			 * } }, new Response.ErrorListener() {
			 * 
			 * @Override public void onErrorResponse(VolleyError error) { //
			 * TODO Auto-generated method stub Log.d("Checking VolleyError",
			 * error.toString()); // Log.d("Checking Message",
			 * error.getMessage()); } });
			 * 
			 * Log.d("Checking added queue", jsObjRequest.toString());
			 * queue.add(jsObjRequest);
			 */
			
			
			
			JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
					MAKE_DONATION_OFFER_SERVLET_URL,null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							type.setText(response.toString() + " hello volley");
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
	}
	
	
	

}
