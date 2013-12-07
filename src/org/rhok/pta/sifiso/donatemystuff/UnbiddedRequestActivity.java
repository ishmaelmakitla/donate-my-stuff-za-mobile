package org.rhok.pta.sifiso.donatemystuff;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class UnbiddedRequestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unbidded_request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unbidded_request, menu);
		return true;
	}

}
