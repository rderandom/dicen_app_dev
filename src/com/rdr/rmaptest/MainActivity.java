package com.rdr.rmaptest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rdr.rmaptest.LatLngDTO;
import com.rdr.rmaptest.MarkerDTO;

public class MainActivity extends Activity {
	private static final String DESCARGANDO_MOVIDAS = "Descargando movidas...";
	static final LatLng SALAMANCA = new LatLng(40.965, -5.665);
	Typeface face;
	Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		face = Typeface.createFromAsset(getAssets(),"fonts/YanoneKaffeesatz.ttf");

		
	    //Cambiar fuente al t�tulo
	    int titleId = getResources().getIdentifier("action_bar_title", "id",  "android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	    yourTextView.setTypeface(face);
	    yourTextView.setTextSize(28);
		

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		if (map != null) {
			// Move the camera instantly to SALAMANCA with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(SALAMANCA, 15));
			new DownloadMarkersAsynkTask().execute();
		    toast = Toast.makeText(MainActivity.this, DESCARGANDO_MOVIDAS, Toast.LENGTH_LONG*2);
		    toast.show();

		}
		
		ImageView btnNuevo = (ImageView) findViewById(R.id.btn_nuevo);
		btnNuevo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, NuevoActivity.class);
				startActivity(i);
			}
		});

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//	
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_nuevo) {
//			Intent i = new Intent(this, NuevoActivity.class);
//			startActivity(i);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Descargar Listado de MarkerOptions desde
	 * HTTP_RDERECURSIVACOM_IPAGE_COM_WS_SERVICIO_LISTADO_PHP
	 *
	 */
	public class DownloadMarkersAsynkTask extends AsyncTask<String, String, List<MarkerOptions>> {
		private static final String HTTP_RDERECURSIVACOM_IPAGE_COM_WS_SERVICIO_LISTADO_PHP = "http://rderecursivacom.ipage.com/ws/ServicioListado.php";

		@Override
		protected List<MarkerOptions> doInBackground(String... arg0) {
			String resultToDisplay = "";	
			InputStream in = null;
			HttpURLConnection urlConnection = null;
			
			try {
				// HTTP GET al Servicio PHP
				URL url = new URL(HTTP_RDERECURSIVACOM_IPAGE_COM_WS_SERVICIO_LISTADO_PHP);
	
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream urlConnectionInputStream = urlConnection.getInputStream();
				in = new BufferedInputStream(urlConnectionInputStream);
				resultToDisplay = slurp(in, 255);
				
				if(urlConnectionInputStream!= null){
					urlConnectionInputStream.close();
				}
				
				if(in != null){
					in.close();
				}

				

				//Pasar JSON a lista de MarkerDTO
				Gson gson = new Gson();
				Type collectionType = new TypeToken<Collection<MarkerDTO>>(){}.getType();
				Collection<MarkerDTO> one = gson.fromJson(resultToDisplay, collectionType);
	
				//Pasar lista de MarkerDTO a lista de MarkerOptions
				List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
				for (MarkerDTO markerDTO : one) {
					LatLngDTO latlng = markerDTO.getLatlng();
							
					MarkerOptions markerOptions = new MarkerOptions()
					.position(new LatLng(latlng.getLatitude(), latlng.getLongitude()))
				    .title(markerDTO.getTitle())
				    .snippet(markerDTO.getSnippet())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.conversation)) ;

					markers.add(markerOptions);
				}

				return markers;
	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(urlConnection != null){
					urlConnection.disconnect();
	
				}

			}
	
			return new ArrayList<MarkerOptions>();
		}
	
	
	    @Override
	    protected void onPostExecute(List<MarkerOptions>  result) {
			GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		    for (int i = 0; i < result.size(); i++) {
		    	MarkerOptions markerOptions = result.get(i);
				map.addMarker(markerOptions);
			}    
		    toast.cancel();
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
