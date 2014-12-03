package com.rdr.rmaptest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rdr.rmaptest.dto.LatLngDTO;
import com.rdr.rmaptest.dto.MarkerDTO;

public class NuevoActivity extends Activity {
	static final LatLng SALAMANCA = new LatLng(40.965, -5.665);
	GoogleMap map;
	Marker marker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		if (map != null) {
			// Move the camera instantly to SALAMANCA with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(SALAMANCA, 15));
			
		
			map.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng arg0) {
					marker = map.addMarker(new MarkerOptions().position(arg0));		
				}
			});
			

			
		}
		
		Button btnEnviar = (Button) findViewById(R.id.button_enviar);
		btnEnviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText txtTitulo = (EditText) findViewById(R.id.txt_titulo);
				String titulo = txtTitulo.getText().toString();
				
				EditText txtTexto = (EditText) findViewById(R.id.txt_texto);
				String snippet = txtTexto.getText().toString();

				if(marker!=null){
//					LatLng pos = marker.getPosition();
					double lat = marker.getPosition().latitude;
					double lng = marker.getPosition().longitude;	
					MarkerDTO marker = new MarkerDTO();
					marker.setTitle(titulo);
					marker.setSnippet(snippet);  
					marker.setLatlng(new LatLngDTO(lat,lng));
						
					new UploadMarkerAsynkTask().execute(marker);
					
				}
			}
		});
		
	}
	
	/**
	 * 
	 * @author rderandom
	 *
	 */
	public class UploadMarkerAsynkTask extends AsyncTask<MarkerDTO, String, Void> {
		private static final String ENVIANDO_MOVIDAS = "Enviando movidas al servidor...";
		final String USER_AGENT = "Mozilla/5.0";
		Toast toast;
		
		@Override
		protected void onPostExecute(Void v){
		    toast.cancel();
			NuevoActivity.this.finish();
		}
		
		@Override
		protected Void doInBackground(MarkerDTO... marker) {
//			Gson gson = new Gson();
//			String toJson = gson.toJson(marker);
			
		    toast = Toast.makeText(NuevoActivity.this, ENVIANDO_MOVIDAS, Toast.LENGTH_LONG*2);
		    toast.show();
		    
			try {

				String lat = String.valueOf(marker[0].getLatlng().getLatitude());
				String lng =  String.valueOf(marker[0].getLatlng().getLongitude());
				String title = marker[0].getTitle();
				String snippet = marker[0].getSnippet();

//		        URL url = new URL("http://rderecursivacom.ipage.com/ws/ServicioAlta.php"+"?data="+toJson);
				StringBuilder params = new StringBuilder("?");
				params.append("lat=");
				params.append(lat);
				params.append("&");
				params.append("lng=");
				params.append(lng);
				params.append("&");
				params.append("title=");
				params.append(title);
				params.append("&");
				params.append("snippet=");
				params.append(snippet);			
				
				
		        URL url = new URL("http://rderecursivacom.ipage.com/ws/ServicioAlta.php"+params.toString());
		    	
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
				slurp(in, 255);
				if(in!=null){
					in.close();
	
				}
				if(urlConnection != null){
					urlConnection.disconnect();
		
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
	}
	
	
	/**
	 * Usado por DownloadMarkersAsynkTask....
	 * @param is
	 * @param bufferSize
	 * @return
	 */
	public static String slurp(final InputStream is, final int bufferSize) {
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		try {
			final Reader in = new InputStreamReader(is, "UTF-8");
			try {
				for (;;) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
			} finally {
				in.close();
			}
		} catch (UnsupportedEncodingException ex) {
			/* ... */
		} catch (IOException ex) {
			/* ... */
		}
		return out.toString();
	}
}
