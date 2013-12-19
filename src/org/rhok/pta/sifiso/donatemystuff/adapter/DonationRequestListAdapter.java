package org.rhok.pta.sifiso.donatemystuff.adapter;

import java.util.List;

import org.rhok.pta.sifiso.donatemystuff.DetailItemViewActivity;
import org.rhok.pta.sifiso.donatemystuff.R;
import org.rhok.pta.sifiso.donatemystuff.adapter.OfferAdapter.Holder;
import org.rhok.pta.sifiso.donatemystuff.model.DonatedItem;
import org.rhok.pta.sifiso.donatemystuff.model.DonationOffer;
import org.rhok.pta.sifiso.donatemystuff.model.DonationRequest;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;
import org.rhok.pta.sifiso.donatemystuff.util.DonateMyStuffGlobals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DonationRequestListAdapter extends ArrayAdapter<DonationRequest>{


		private List<DonationRequest> donationRequests;
		private LayoutInflater inflater;
		private Context context;
		private int mResource;
		private UserSession session;

		public DonationRequestListAdapter(Context context, int resource, List<DonationRequest> requests, UserSession _session) {
			super(context, resource, requests);
			this.context = context;
			mResource = resource;
			donationRequests = requests;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.session = _session;
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

			final DonationRequest dRequest = donationRequests.get(position);
			DonatedItem item = dRequest.getRequestedDonationItem();
			
			holder.describtion.setText(item.getName());
			holder.size.setText("Size " + item.getSize());
			holder.quantity.setText("Quantity " + dRequest.getQuantity());
			holder.size.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					
					//new: instead of putting each field in a bundle, put the whole object as a serializable
					bundle.putSerializable("request", dRequest);
					bundle.putSerializable(DonateMyStuffGlobals.KEY_SESSION, session);
					
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
		}
}
