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

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DonationRequestListAdapter extends ArrayAdapter<DonationRequest> {

	private List<DonationRequest> donationRequests;
	private LayoutInflater inflater;
	private Context context;
	private int mResource;
	private int type;
	private RoundedImageView roundedImageView;
	private UserSession session;
	private static final int[] mThumbIds = { R.drawable.book, R.drawable.cloth,
			R.drawable.shoe, R.drawable.blanket };

	public DonationRequestListAdapter(Context context, int resource,
			List<DonationRequest> requests, UserSession _session, int type) {
		super(context, resource, requests);
		this.context = context;
		mResource = resource;
		this.type = type;
		donationRequests = requests;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.session = _session;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(mResource, parent, false);
			holder.logo = (ImageView) convertView.findViewById(R.id.logo);
			holder.describtion = (TextView) convertView
					.findViewById(R.id.description);
			holder.size = (TextView) convertView.findViewById(R.id.size);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		roundedImageView = new RoundedImageView(context);
		final DonationRequest dRequest = donationRequests.get(position);

		DonatedItem item = dRequest.getRequestedDonationItem();

		Picasso.with(context).load(mThumbIds[type]).resize(200, 200)
				.centerInside().into(holder.logo);

		holder.describtion.setText(item.getName());
		holder.size.setText("Size " + item.getSize());
		holder.describtion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();

				// new: instead of putting each field in a bundle, put the whole
				// object as a serializable
				bundle.putSerializable("request", dRequest);
				bundle.putSerializable(DonateMyStuffGlobals.KEY_SESSION,
						session);

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

	}

	public class RoundedImageView extends ImageView {

		public RoundedImageView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public RoundedImageView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public RoundedImageView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
		}

		@Override
		protected void onDraw(Canvas canvas) {

			Drawable drawable = getDrawable();

			if (drawable == null) {
				return;
			}

			if (getWidth() == 0 || getHeight() == 0) {
				return;
			}
			Bitmap b = ((BitmapDrawable) drawable).getBitmap();
			Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

			int w = getWidth(), h = getHeight();

			Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
			canvas.drawBitmap(roundBitmap, 0, 0, null);

		}

		public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
			Bitmap sbmp;
			if (bmp.getWidth() != radius || bmp.getHeight() != radius)
				sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
			else
				sbmp = bmp;
			Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
					sbmp.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xffa19774;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.parseColor("#BAB399"));
			canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
					sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f,
					paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(sbmp, rect, rect, paint);

			return output;
		}

	}
}
