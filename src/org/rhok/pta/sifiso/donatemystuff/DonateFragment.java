package org.rhok.pta.sifiso.donatemystuff;

import org.rhok.pta.sifiso.donatemystuff.adapter.DonationRequestAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class DonateFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.donate_fragment, container, false);
		GridView grid = (GridView) v.findViewById(R.id.gridview);
		grid.setAdapter(new DonationRequestAdapter(getActivity(), viewOwn));
		return v;

	}
	private boolean viewOwn = false;
	public void setIsViewingMineOnly(boolean value){
		this.viewOwn = value;
	}

}
