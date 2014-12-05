package com.rdr.rmaptest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuActivity extends Activity {
    Typeface face;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		face = Typeface.createFromAsset(getAssets(),"fonts/YanoneKaffeesatz.ttf");
		
		//Spinner adapter para cambiar fuente
        Spinner ciudades = (Spinner) findViewById(R.id.spinner1);
	    SpinnerFontAdapter spinnerFontAdapter = new SpinnerFontAdapter(this);
	    ciudades.setAdapter(spinnerFontAdapter);
		
	    
	    //Cambiar fuente al título
	    int titleId = getResources().getIdentifier("action_bar_title", "id",  "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTypeface(face);
	    yourTextView.setTextSize(28);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setTypeface(face);
		btn.setTextSize(26);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuActivity.this, MainActivity.class);
				startActivity(i);
			}
		});

	}
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_nuevo) {
//			Intent i = new Intent(this, NuevoActivity.class);
//			startActivity(i);
//			return true;		}
//		return super.onOptionsItemSelected(item);
//	}
	
	/**
	 * 
	 * Adaptador para el spinner.
	 *
	 */
	 private class SpinnerFontAdapter extends BaseAdapter {
			final String[] CIUDADES = new String[] { "Salamanca", "Madrid" };	

	        private LayoutInflater mInflater;

	        public SpinnerFontAdapter(MenuActivity ctx) {
	            mInflater = LayoutInflater.from(ctx);
	        }

	        @Override
	        public int getCount() {
	            return CIUDADES.length;
	        }

	        @Override
	        public Object getItem(int position) {
	            return position;
	        }

	        @Override
	        public long getItemId(int position) {
	            return position;
	        }

	        @SuppressLint("InflateParams")
			@Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            final ListContent holder;
	            View v = convertView;
	            if (v == null) {
	                v = mInflater.inflate(R.layout.my_spinner_style, null);
	                holder = new ListContent();
	                holder.name = (TextView) v.findViewById(R.id.textView1);
	                v.setTag(holder);
	                
	            } else {
	                holder = (ListContent) v.getTag();
	                
	            }

	            holder.name.setTypeface(face);
	            holder.name.setText("" + CIUDADES[position]);

	            return v;
	        }

	    }

	    static class ListContent {
	        TextView name;
	    }
}
