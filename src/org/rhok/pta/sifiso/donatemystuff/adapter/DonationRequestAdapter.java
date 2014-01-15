package org.rhok.pta.sifiso.donatemystuff.adapter;

import org.rhok.pta.sifiso.donatemystuff.R;
import org.rhok.pta.sifiso.donatemystuff.ViewDonationActivity;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;
import org.rhok.pta.sifiso.donatemystuff.util.FontChanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class DonationRequestAdapter extends BaseAdapter {
	
	private static final String TAG = DonationRequestAdapter.class.getSimpleName();
	public enum DonationType {
		SHOES, CLOTHES, BOOKS, BLANKETS
	};

	private Context context;
	private String[] items;
	private static final int[] mThumbIds = { R.drawable.books,
			R.drawable.clothes, R.drawable.shoes, R.drawable.blankets, };
	
	private boolean isViewingMineOnly = false;
	private int mode = DonateMyStuffGlobals.MODE_REQUESTS_LIST;

	public DonationRequestAdapter(Context context) {
		initialize(context, false, -1, null);	
	}
	
	public DonationRequestAdapter(Context context, boolean viewMineONLY, int _mode, UserSession _session) {
		initialize(context, viewMineONLY, _mode, _session);	
	}
	private UserSession session;
	
	/**
	 * This method simply initialized the Class properties
	 * 
	 * @param context
	 * @param viewMineONLY
	 */
	private void initialize(Context context, boolean viewMineONLY, int _mode, UserSession _session){
		this.context = context;
		this.isViewingMineOnly = viewMineONLY;	
		items = context.getResources().getStringArray(R.array.donatio_items);
		this.mode = (_mode== -1? DonateMyStuffGlobals.MODE_REQUESTS_LIST:_mode);
		this.session = _session;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater infalter = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalter.inflate(R.layout.cards, parent, false);
			holder.img = (ImageView) convertView
					.findViewById(R.id.recarga_thumb);
			holder.click = (View) convertView.findViewById(R.id.clickView2);
			holder.textItem = (TextView) convertView.findViewById(R.id.item);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Picasso.with(context).load(mThumbIds[position]).fit().into(holder.img);
		holder.click.setOnClickListener(new CustomClickListener(
				items[position], position));
		holder.textItem.setText(items[position]);
		holder.textItem.setTypeface(FontChanger.setRobotoLight(context));
		DonationType type = deriveDonationType(position);
		holder.type = type;
		convertView.setTag(holder);

		Log.d(TAG, "Item At Position "
				+ position + " is " + type);
		return convertView;
	}

	private DonationType deriveDonationType(int typeCode) {
		DonationType donationType = null;
		switch (typeCode) {
		case 0:
			donationType = DonationType.BOOKS;
			break;
		case 1:
			donationType = DonationType.CLOTHES;
			break;
		case 2:
			donationType = DonationType.SHOES;
			break;
		case 3:
			donationType = DonationType.BLANKETS;
			break;

		}

		return donationType;
	}

	public class ViewHolder {
		ImageView img;
		View click;
		TextView textItem;
		DonationType type;
	}

	public class CustomClickListener implements View.OnClickListener {
		String args;
		int position;

		public CustomClickListener(String args, int viewPosition) {
			this.args = args;
			this.position = viewPosition;
			Log.d(DonationRequestAdapter.class.getSimpleName(),
					" CustomClickListener:: " + args + " position::" + position);
		}

		@Override
		public void onClick(View view) {
			Intent intent = null;
			Bundle b = new Bundle();
			//indicate whether the list of requests/offers to be shown for each category are the ones submitted by current user
			b.putBoolean(DonateMyStuffGlobals.FLAG_VIEW_MINE_ONLY, isViewingMineOnly);
			
			DonationType type = deriveDonationType(position);
			
			b.putInt(DonateMyStuffGlobals.KEY_MODE, mode);
			b.putSerializable(DonateMyStuffGlobals.KEY_SESSION, session);
			
			Log.d(DonationRequestAdapter.class.getSimpleName(),
					" Clicked-Donation-Type:: " + type);
			switch (type) {
			case BOOKS:
				b.putString("type", "books");
				intent = new Intent(context, ViewDonationActivity.class)
						.putExtras(b);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				break;

			case CLOTHES:
				b.putString("type", "clothes");
				intent = new Intent(context, ViewDonationActivity.class)
						.putExtras(b);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
			case SHOES:
				b.putString("type", "shoes");
				intent = new Intent(context, ViewDonationActivity.class)
						.putExtras(b);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
			case BLANKETS:
				b.putString("type", "blankets");
				intent = new Intent(context, ViewDonationActivity.class)
						.putExtras(b);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				break;

			}

			if (intent != null) {
				context.startActivity(intent);
			}

		}

	}
}
