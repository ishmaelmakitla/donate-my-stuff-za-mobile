package org.rhok.pta.sifiso.donatemystuff;

import java.util.ArrayList;

import org.rhok.pta.sifiso.donatemystuff.adapter.NavDrawerListAdapter;
import org.rhok.pta.sifiso.donatemystuff.model.NavDrawerItem;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class MainActivity extends Activity {
	private static final String TAG = BookDonateActivity.class.getSimpleName();
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	private UserSession session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		//get the session object
		Bundle extras = getIntent().getExtras();
		session = (UserSession)extras.getSerializable(DonateMyStuffGlobals.KEY_SESSION);
		if(session == null){
			Toast.makeText(this, "There was a problem getting your User Information. You Need To login Afg", Toast.LENGTH_LONG).show();
			//close this Activity so the previous login page shows...
			finish();
		}
		else{
			//set title
			Log.d(TAG, session.getUsername());
			setTitle("Donate-My-Stuff: "+session.getUsername());
		}

		

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		//Make a Donate
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		
		// Make a request for donation
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// view all MY donation requests
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1), false, "22"));
		
		//view all MY donation offers
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1), false, "23"));
		
		//view all donation-offers
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1), false, "24"));
		
		//view all donation-requests
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1), false, "25"));
		
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Displaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			Log.d("check", "Home - Clicked " + session);
			HomeFragment hf = new HomeFragment();
			hf.setSession(session);
			fragment = hf;
			break;
		case 1:
			Log.d("check", "FragmentDonate - Clicked" + session);
			FragmentDonate fd = new FragmentDonate();
			fd.setSession(session);
			fragment = fd;
			break;
		case 2:
			Log.d("check", "FragmentRequest - Clicked - Session Object is "+session);
			FragmentRequest fr = new FragmentRequest();
			fr.setSession(session);
			fragment = fr;
			break;
		case 3:
			Log.d("check", " My Donations Requests - Clicked " + session);
			DonateFragment viewRequestsFragment = new DonateFragment();
			viewRequestsFragment.setIsViewingMineOnly(true);
			viewRequestsFragment.setSession(session);
			viewRequestsFragment.setMode(DonateMyStuffGlobals.MODE_REQUESTS_LIST);
			fragment = viewRequestsFragment;
			
			break;
		case 4:
			Log.d("check", " My Donations Offers- Clicked " + session);
			DonateFragment viewOffersFragment = new DonateFragment();
			viewOffersFragment.setIsViewingMineOnly(true);
			viewOffersFragment.setSession(session);
			viewOffersFragment.setMode(DonateMyStuffGlobals.MODE_OFFERS_LIST);
			fragment = viewOffersFragment;
			
			break;
		case 5:
			Log.d("check", " All Donations Offers - Clicked " + session);
			DonateFragment df = new DonateFragment();
			df.setMode(DonateMyStuffGlobals.MODE_OFFERS_LIST);
			df.setSession(session);
			fragment = df;
			break;
		case 6:
			Log.d("check", " All Donations Requests - Clicked " + session);
			DonateFragment dfRequests = new DonateFragment();
			dfRequests.setMode(DonateMyStuffGlobals.MODE_REQUESTS_LIST);
			dfRequests.setSession(session);
			fragment = dfRequests;
			break;			

		default:
			Log.d("check", " Invalid Option... " + position);
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(session.getUsername()+" "+navMenuTitles[position] );
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		Log.d(TAG, mTitle.toString());
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
