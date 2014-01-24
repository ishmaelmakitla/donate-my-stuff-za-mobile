package org.rhok.pta.sifiso.donatemystuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.rhok.pta.sifiso.donatemystuff.adapter.OfferAdapter;
import org.rhok.pta.sifiso.donatemystuff.adapter.RequestDonationAdapter;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;
import org.rhok.pta.sifiso.donatemystuff.model.DonationRequest;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class TapFragmentActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private static final String TAG = TapFragmentActivity.class.getSimpleName();
	private static OfferAdapter adapter;
	private static RequestDonationAdapter requestDonationAdapter;
	private static String flag = "Requests";

	private static final String GET_DONATION_OFFER_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/donationoffers";
	private static final String GET_REQUEST_SERVLET_URL = "http://za-donate-my-stuff.appspot.com/donationrequests";

	static ListView dummyTextView;
	static Activity activity_main;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_fragment);
		final Activity activity = (Activity) this;
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						switch (position) {
						case 0:
							Toast.makeText(getApplicationContext(),
									getString(R.string.title_section1),
									Toast.LENGTH_SHORT).show();
							flag = getString(R.string.title_section1);
							// dummyTextView.setText(getString(R.string.title_section1));
							queryServer(GET_DONATION_OFFER_SERVLET_URL,
									activity);
							actionBar.setSelectedNavigationItem(position);
							break;
						case 1:
							Toast.makeText(getApplicationContext(),
									getString(R.string.title_section2),
									Toast.LENGTH_SHORT).show();
							// dummyTextView.setText(getString(R.string.title_section2));
							queryServerRequest(GET_REQUEST_SERVLET_URL,
									activity);
							flag = getString(R.string.title_section2);
							actionBar.setSelectedNavigationItem(position);
							break;

						}
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tap, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	private static UserSession session;

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			dummyTextView = (ListView) rootView
					.findViewById(R.id.section_label);
			Log.d(TAG, flag);

			queryServer(GET_DONATION_OFFER_SERVLET_URL, getActivity());

			// dummyTextView.setText(getString(R.string.title_section1));
			// dummyTextView.setText(Integer.toString(getArguments().getInt(
			// ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	private static void queryServer(String url, final Activity activity) {
		RequestQueue queue = Volley.newRequestQueue(activity
				.getApplicationContext());
		StringRequest request = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Sifiso" + response.toString());
						try {
							processDonationOffers(response, activity);
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

	private static void queryServerRequest(String url, final Activity activity) {
		RequestQueue queue = Volley.newRequestQueue(activity
				.getApplicationContext());
		StringRequest request = new StringRequest(Request.Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Sifiso" + response.toString());
						try {
							processDonationRequest(response, activity);
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

	/*
	 * Deserialize a json string
	 */
	private static void processDonationOffers(String offers, Activity activity)
			throws JSONException {

		/*
		 * if (!offers.startsWith("{") && !offers.endsWith("}")) { offers =
		 * "{ \"offers\":" + offers + "}"; }
		 */

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
						DonationOffer donationOffer = gson.fromJson(offerItem,
								DonationOffer.class);
						donationOfferList.add(donationOffer);
						Log.d(TAG, "Sakubona "
								+ donationOfferList.get(x).getOfferDate());
					}
					listAdapter(donationOfferList, activity);
				}

				return;
			}

		}

		Log.e(TAG, "Could Not Parse As JSON-Array? Jo = NULL:: \n" + offers);

	}

	/*
	 * Deserialize a json string
	 */
	private static void processDonationRequest(String request, Activity activity)
			throws JSONException {

		/*
		 * if (!offers.startsWith("{") && !offers.endsWith("}")) { offers =
		 * "{ \"offers\":" + offers + "}"; }
		 */

		Gson gson = new Gson();

		JsonParser parser = new JsonParser();

		JsonElement jel = parser.parse(request);
		JsonArray jo = null;

		if (jel != null) {

			jo = jel.getAsJsonObject().get("requests").getAsJsonArray();
			if (jo != null) {
				Log.d(TAG, "Parsed JSON successfully");

				List<DonationRequest> donationRequestList = new ArrayList<DonationRequest>();
				if (jo.size() > 0) {

					for (int x = 0; x < jo.size(); x++) {
						JsonObject offerItem = (JsonObject) jo.get(x);
						String strOff = offerItem.toString();
						Log.d(TAG, strOff);
						DonationRequest donationOffer = gson.fromJson(
								offerItem, DonationRequest.class);
						donationRequestList.add(donationOffer);
						Log.d(TAG, "Sakubona "
								+ donationRequestList.get(x).getRequestDate());
					}
					listAdapterRequest(donationRequestList, activity);
				}

				return;
			}

		}

		Log.e(TAG, "Could Not Parse As JSON-Array? Jo = NULL:: \n" + request);

	}

	private static void listAdapterRequest(List<DonationRequest> list,
			Activity activity) {
		if (list == null) {
			list = new ArrayList<DonationRequest>();
		}
		Log.d(TAG, "Sakubona2 " + activity.getApplicationContext());
		requestDonationAdapter = new RequestDonationAdapter(
				activity.getApplicationContext(),
				R.layout.customize_offer_list, list);
		dummyTextView.setAdapter(requestDonationAdapter);
	}

	private static void listAdapter(List<DonationOffer> list, Activity activity) {
		Bundle b = activity.getIntent().getExtras();
		int type =0;
		session = (UserSession) b
				.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
		if (list == null) {
			list = new ArrayList<DonationOffer>();
		}
		Log.d(TAG, "Sakubona2 " + activity.getApplicationContext());
		adapter = new OfferAdapter(activity.getApplicationContext(),
				R.layout.customize_offer_list, list, session, type);
		dummyTextView.setAdapter(adapter);
	}

}
