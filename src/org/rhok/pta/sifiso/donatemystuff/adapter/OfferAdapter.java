package org.rhok.pta.sifiso.donatemystuff.adapter;

import java.util.List;

import org.json.JSONObject;
import org.rhok.pta.sifiso.donatemystuff.R;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;

import android.content.Context;
import android.graphics.Paint.Join;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OfferAdapter extends ArrayAdapter<DonationOffer> {

	private List<DonationOffer> donationOffers;
	private LayoutInflater inflater;
	private Context context;
	private int mResource;

	public OfferAdapter(Context context, int resource, List<DonationOffer> offer) {
		super(context, resource, offer);
		this.context = context;
		mResource = resource;
		donationOffers = offer;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		donationOffers = offer;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(mResource, parent, false);
			holder.describtion = (TextView) convertView
					.findViewById(R.id.description);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			holder.quantity = (TextView) convertView
					.findViewById(R.id.quantity);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final DonationOffer dOffer = donationOffers.get(position);
		holder.describtion.setText(dOffer.getItem().getName());
		holder.size.setText("Size " + dOffer.getItem().getSize());
		holder.quantity.setText("Quantity " + dOffer.getQuantity());
		return convertView;
	}

	class Holder {
		TextView describtion;
		TextView size;
		TextView quantity;
	}

}