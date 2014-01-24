package org.rhok.pta.sifiso.donatemystuff.adapter;

import java.util.List;

import org.rhok.pta.sifiso.donatemystuff.BlanketDonationActivity;
import org.rhok.pta.sifiso.donatemystuff.BookDonateActivity;
import org.rhok.pta.sifiso.donatemystuff.ClothDonationActivity;
import org.rhok.pta.sifiso.donatemystuff.R;
import org.rhok.pta.sifiso.donatemystuff.ShoesDonationActivity;
import org.rhok.pta.sifiso.donatemystuff.ViewDonationActivity;
import org.rhok.pta.sifiso.donatemystuff.adapter.DonateAdapter.DonationType;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;
import org.rhok.pta.sifiso.donatemystuff.model.DonationRequest;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * 
 * 
 * @author Sifiso Mtshweni & Ishmael Makitla
 * 
 */
public class RequestAdapter extends BaseAdapter {
	public enum DonationType {
		SHOES, CLOTHES, BOOKS, BLANKETS;
		public static DonationType toType(String type) {
			return valueOf(type.toUpperCase());
		}
	};

	private Context context;
	private String[] items;
	private UserSession session;

	private static final int[] mThumbIds = { R.drawable.book, R.drawable.cloth,
			R.drawable.shoe, R.drawable.blanket };

	public RequestAdapter(Context _context, UserSession userSession) {
		this.context = _context;
		items = context.getResources().getStringArray(R.array.donatio_items);
		this.session = userSession;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {

		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater infalter = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalter.inflate(R.layout.cards, root, false);
			holder.img = (ImageView) convertView
					.findViewById(R.id.recarga_thumb);
			holder.click = (View) convertView.findViewById(R.id.clickView2);
			holder.textItem = (TextView) convertView.findViewById(R.id.item);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Picasso.with(context).load(mThumbIds[position]).resize(300, 300).into(holder.img);
		holder.click.setOnClickListener(new CustomClickListener(
				items[position], position));
		holder.textItem.setText(items[position]);
		holder.textItem.setTypeface(FontChanger.setRobotoLight(context));
		DonationType type = deriveDonationType(position);
		holder.type = type;
		convertView.setTag(holder);

		Log.d(RequestAdapter.class.getSimpleName(), "Item At Position "
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
			Log.d(DonateAdapter.class.getSimpleName(),
					" CustomClickListener:: " + args + " position::" + position);
		}

		@Override
		public void onClick(View view) {
			Intent donationRequestIntent = null;
			Bundle b = new Bundle();
			// indicate the list to be retrieved is a list of Donation-Requests,
			// for the category of item selected below
			b.putInt(DonateMyStuffGlobals.KEY_MODE,
					DonateMyStuffGlobals.MODE_REQUESTS_LIST);
			b.putSerializable(DonateMyStuffGlobals.KEY_SESSION, session);

			DonationType type = deriveDonationType(position);

			Log.d(RequestAdapter.class.getSimpleName(),
					" Clicked-Donation-Type:: " + type);
			switch (type) {
			case BOOKS:
				b.putString("type", "book");
				donationRequestIntent = new Intent(context,
						BookDonateActivity.class).putExtras(b);

				donationRequestIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				break;

			case CLOTHES:
				b.putString("type", "clothes");
				donationRequestIntent = new Intent(context,
						ClothDonationActivity.class).putExtras(b);
				donationRequestIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
			case SHOES:
				b.putString("type", "shoes");
				donationRequestIntent = new Intent(context,
						ShoesDonationActivity.class).putExtras(b);
				donationRequestIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
			case BLANKETS:
				b.putString("type", "blankets");
				donationRequestIntent = new Intent(context,
						BlanketDonationActivity.class).putExtras(b);
				donationRequestIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				break;
			}

			if (donationRequestIntent != null) {
				context.startActivity(donationRequestIntent);
			}

		}

	}
}
