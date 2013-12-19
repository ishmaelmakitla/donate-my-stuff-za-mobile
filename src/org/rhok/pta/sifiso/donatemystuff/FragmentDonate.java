package org.rhok.pta.sifiso.donatemystuff;


/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
import org.rhok.pta.sifiso.donatemystuff.adapter.DonateAdapter;
import org.rhok.pta.sifiso.donatemystuff.model.UserSession;

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
		DonateAdapter da = new DonateAdapter(getActivity());
		da.setSession(session);
		grid.setAdapter(da);
		
		return v;
	}
	
	private UserSession session;
	public void setSession(UserSession _session){ this.session = _session; }

}
