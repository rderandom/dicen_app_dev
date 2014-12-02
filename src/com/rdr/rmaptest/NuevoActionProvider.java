package com.rdr.rmaptest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

public class NuevoActionProvider extends ActionProvider {

	private Context mContext;
	
	
	
	public NuevoActionProvider(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View onCreateActionView() {
	    // Inflate the action view to be shown on the action bar.
	    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
	    View view = layoutInflater.inflate(R.layout.action_provider, null);
		
	    view.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
				Intent i = new Intent(mContext, NuevoActivity.class);
				mContext.startActivity(i);
	        }
		});

	    return view;
	}

}
