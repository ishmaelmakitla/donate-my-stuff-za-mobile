package org.rhok.pta.sifiso.donatemystuff;


/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
import org.rhok.pta.sifiso.donatemystuff.adapter.DonateAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class FragmentDonate extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.donate, container,false);
		GridView grid=(GridView)v.findViewById(R.id.gridview);
		grid.setAdapter(new DonateAdapter(getActivity()));
		
		return v;
	}

}
