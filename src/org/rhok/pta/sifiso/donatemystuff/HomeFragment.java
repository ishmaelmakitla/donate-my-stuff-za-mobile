package org.rhok.pta.sifiso.donatemystuff;

import org.rhok.pta.sifiso.donatemystuff.model.UserSession;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * 
 * @author sifiso mtshweni
 * 
 */
public class HomeFragment extends Fragment {
    private UserSession session;
	public HomeFragment() {
		
	}
	
	public void setSession(UserSession _session){
		this.session = _session;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		return rootView;
	}

}
