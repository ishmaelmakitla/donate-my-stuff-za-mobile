package org.rhok.pta.sifiso.donatemystuff;

import java.sql.Date;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

public class DetailItemViewActivity extends Activity {

	EditText donor_id;
	EditText donated_date;
	EditText item_name;
	EditText item_code;
	EditText donor_quantity;
	Button requestSubmit;
	private ActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_item_view);
		
		
		setField();
	}

	private void setField() {
		Bundle b = getIntent().getExtras();
		donor_id = (EditText) findViewById(R.id.donor_id);
		donated_date = (EditText) findViewById(R.id.donated_date);
		item_name = (EditText) findViewById(R.id.item_name);
		item_code = (EditText) findViewById(R.id.item_code);
		donor_quantity = (EditText) findViewById(R.id.donor_quantity);
		requestSubmit = (Button) findViewById(R.id.requestSubmit);

		donor_id.setText(b.getString("id"));
		donated_date.setText(new Date(b.getLong("offerdate")).toString());
		item_name.setText(b.getString("name"));
		if (b.getInt("gendercode") == 0) {
			item_code.setText("Male");
		} else {
			item_code.setText( "Female");
		}

		donor_quantity.setText(b.getInt("quantity") + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_item_view, menu);
		
		getActionBar().setTitle("Back");
		getActionBar().setIcon(R.drawable.ic_drawer);
		getActionBar().setHomeButtonEnabled(true);
	
		return true;
	}

}
