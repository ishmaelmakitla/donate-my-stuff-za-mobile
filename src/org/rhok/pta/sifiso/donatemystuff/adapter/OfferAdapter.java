package org.rhok.pta.sifiso.donatemystuff.adapter;

import java.util.List;

import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.DetailItemViewActivity;
import org.rhok.pta.sifiso.donatemystuff.R;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class OfferAdapter extends ArrayAdapter<DonationOffer> {

	private List<DonationOffer> donationOffers;
	private LayoutInflater inflater;
	private Context context;
	private int mResource;
	private String delivery;
	private UserSession session;
	private int type;
	private static final int[] mThumbIds = { R.drawable.books, R.drawable.clothes,
		R.drawable.shoes, R.drawable.blankets };

	public OfferAdapter(Context context, int resource,
			List<DonationOffer> offer, UserSession _session, int type) {
		super(context, resource, offer);
		this.context = context;
		mResource = resource;
		this.type = type;
		donationOffers = offer;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		donationOffers = offer;
		this.session = _session;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			 
			convertView = inflater.inflate(mResource, parent, false);
			holder.logo  = (ImageView) convertView.findViewById(R.id.logo);
			holder.describtion = (TextView) convertView
					.findViewById(R.id.description);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			holder.quantity = (TextView) convertView
					.findViewById(R.id.quantity);
			holder.deliver = (TextView) convertView.findViewById(R.id.deliver);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		
		final DonationOffer dOffer = donationOffers.get(position);
		Picasso.with(context).load(mThumbIds[type]).resize(200, 200).centerInside().into(holder.logo); 
		holder.describtion.setText(dOffer.getItem().getName());
		holder.size.setText("Size " + dOffer.getItem().getSize());
		holder.quantity.setText("Quantity " + dOffer.getQuantity());

		holder.deliver.setText(delivery);
		if (dOffer.isDeliver() == true) {
			delivery = "Deliver";
			holder.deliver.setTextColor(Color.BLUE);
		} else {
			delivery = "Collect";
			holder.deliver.setTextColor(Color.GREEN);
		}
		holder.describtion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				// new: instead of putting each field in a bundle, put the whole
				// object as a serializable
				bundle.putSerializable("offer", dOffer);
				bundle.putSerializable(DonateMyStuffGlobals.KEY_SESSION,
						session);

				bundle.putString("id", dOffer.getId());
				bundle.putString("donorid", dOffer.getDonorId());
				bundle.putString("donationrequestid",
						dOffer.getDonationRequestId());
				bundle.putLong("offerdate", dOffer.getOfferDate().getTime());
				bundle.putInt("quantity", dOffer.getQuantity());
				bundle.putString("itemid", dOffer.getItem().getId());

				bundle.putLong("age", dOffer.getItem().getAge());
				bundle.putInt("agerestriction", dOffer.getItem()
						.getAgeRestriction());
				bundle.putString("name", dOffer.getItem().getName());
				bundle.putInt("gendercode", dOffer.getItem().getGenderCode());
				bundle.putString("type", dOffer.getItem().getType());
				bundle.putInt("size", dOffer.getItem().getSize());
				context.startActivity(new Intent(context,
						DetailItemViewActivity.class).setFlags(
						Intent.FLAG_ACTIVITY_NEW_TASK).putExtras(bundle));

			}
		});
		return convertView;
	}

	class Holder {
		ImageView logo;
		TextView describtion;
		TextView size;
		TextView quantity;
		TextView deliver;
	}

}