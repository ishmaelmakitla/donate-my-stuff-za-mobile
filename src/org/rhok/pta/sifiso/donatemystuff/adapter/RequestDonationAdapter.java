package org.rhok.pta.sifiso.donatemystuff.adapter;

import java.util.List;

import org.rhok.pta.sifiso.donatemystuff.DetailItemViewActivity;
import org.rhok.pta.sifiso.donatemystuff.R;
import org.rhok.pta.sifiso.donatemystuff.adapter.OfferAdapter.Holder;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;
import org.rhok.pta.sifiso.donatemystuff.model.DonationRequest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RequestDonationAdapter extends ArrayAdapter<DonationRequest> {
	private List<DonationRequest> donationRequest;
	private LayoutInflater inflater;
	private Context context;
	private int mResource;
	private String delivery;

	public RequestDonationAdapter(Context context, int resource,
			List<DonationRequest> reqList) {
		super(context, resource, reqList);
		this.context = context;
		mResource = resource;
		donationRequest = reqList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
			holder.deliver = (TextView) convertView.findViewById(R.id.deliver);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final DonationRequest dOffer = donationRequest.get(position);
		holder.describtion.setText(dOffer.getItem().getName());
		holder.size.setText("Size " + dOffer.getItem().getSize());
		holder.quantity.setText("Quantity " + dOffer.getQuantity());
		if (dOffer.isCollect() == true) {
			delivery = "Deliver";
			holder.deliver.setTextColor(Color.BLUE);
		} else {
			delivery = "Collect";
			holder.deliver.setTextColor(Color.GREEN);
		}
		holder.deliver.setText(delivery);
		holder.size.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				// new: instead of putting each field in a bundle, put the whole
				// object as a serializable
				bundle.putSerializable("requests", dOffer);

				bundle.putString("id", dOffer.getId());
				bundle.putString("donationOfferId", dOffer.getDonationOfferId());
				bundle.putString("beneficriaryId", dOffer.getBeneficriaryId());
				bundle.putLong("requestdate", dOffer.getRequestDate().getTime());
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
		TextView describtion;
		TextView size;
		TextView quantity;
		TextView deliver;
	}

}
