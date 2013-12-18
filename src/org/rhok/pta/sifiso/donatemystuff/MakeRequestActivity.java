package org.rhok.pta.sifiso.donatemystuff;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class MakeRequestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.make_request, menu);
		return true;
	}

}
