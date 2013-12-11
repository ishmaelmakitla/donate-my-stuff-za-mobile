package org.rhok.pta.sifiso.donatemystuff;

import org.rhok.pta.sifiso.donatemystuff.adapter.RequestAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class FragmentRequest extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_request, container, false);
		GridView grid = (GridView) v.findViewById(R.id.gridview);
		grid.setAdapter(new RequestAdapter(getActivity()));

		return v;
	}

}
