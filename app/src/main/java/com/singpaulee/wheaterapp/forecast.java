package com.singpaulee.wheaterapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.singpaulee.wheaterapp.data.WeatherPreferences;
import com.singpaulee.wheaterapp.utilities.NetworkUtils;
import com.singpaulee.wheaterapp.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class forecast extends AppCompatActivity {
	//1 create a field to store the weather display textview
	//5.33 Hapus Textview mtv_weather
////	private TextView mtv_weather;

	//5.34 Tambahkan variable recyclerview
	RecyclerView mrv_weather;

	//5.35 Tambahkan variabel ForecastAdapter
	ForecastAdapter mForcecastAdapter;

	//4.10 tambahkan variabel textview untuk tampilan pesan kesalahan
	private TextView mtv_error_message;

	//4.11 Tambahkan variabel Progressbar untuk menampilkan/menyembunyikan progress bar
	private ProgressBar mpb_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forecast);

		//2 use findviewbyId to get reference to the weather display tv
		//5.36 Hapus line 47
////		mtv_weather = (TextView) findViewById(R.id.tv_weather_data);

		//5.37 Gunakan findviewbyid untuk menyambungkan variabel rv dengan id rv
		mrv_weather = (RecyclerView) findViewById(R.id.rv_forecast);

		//4.12 gunakan findviewbyid u/ menyambungkan antara variabel dengan id
		mtv_error_message = (TextView) findViewById(R.id.tv_error_message);

		//4.13 gunakan findviewbyid u/ menyambungkan antara variabel dengan id
		mpb_loading = (ProgressBar) findViewById(R.id.pb_loading);

		//5.38 Buat LayoutManager, LinearLayout dengan orientasi vertikal
		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

		//5.39 atur layoutmanager ke dalam recyclerview
		mrv_weather.setLayoutManager(layoutManager);

		//5.40 Gunakan setHasFixedSize(true) on mRecyclerView untuk menunjukan semua item pada list yg akan punya ukuran sama
		mrv_weather.setHasFixedSize(true);

		//5.41 buat objek baru
		mForcecastAdapter = new ForecastAdapter();

		//5.42 atur adapter pada recyclerview
		mrv_weather.setAdapter(mForcecastAdapter);

		//3 buat array tipe string yang berisi data cuaca tiruan
////		String[] dummyWeatherData= {
////				"Today, May 17 - Clear - 17°C / 15°C",
////				"Tomorrow - Cloudy - 19°C / 15°C",
////				"Thursday - Rainy - 30°C / 11°C",
////				"Friday - Thunderstorms - 21°C 9°C",
////				"Saturday - Thunderstorms - 16°C / 7°C",
////				"Sunday - Rainy - 16°C / 8°C",
////				"Monday - Partly Cloudy - 15°C / 10°C",
////				"Tue, May 24 - Meatballs - 16°C / 18°C",
////				"Wed, May 25 - Cloudy - 19°C / 15°C",
////				"Thu, May 26 - Stormy - 30°C / 11°C",
////				"Fry, May 27 - Hurricane - 21°C / 9°C",
////				"Sat, May 28 - Meteors - 16°C / 7°C",
////				"Sun, May 29 - Apocalypse - 16°C / 8°C",
////				"Mon, May 30 - Post Apocalypse - 15°C / 10°C",
////		};
		//2.4 hapus array diatas, langkah kali ini kamu akan mendapatkan data nyata dari internet

		//4 tambahkan tiap string tersebut ke dalam textview
////		for(String dummyWeatherDay : dummyWeatherData){
////			mtv_weather.append(dummyWeatherDay+ "\n\n\n");
////		}
		//2.3 hapus juga perulangan diatas

		//2.9 panggil loadWeatherData untuk melakukan permintaan internet untuk mendapatkan cuaca
		loadWeatherData();
	}

	//2.8 buat sebuah method yang akan mendapatkan lokasi pengguna dan mengeksekusi Asynctask anda dan memanggilnya
	private void loadWeatherData(){
		String loc = WeatherPreferences.getPreferredWeatherLocation(this);
		new FetchWeatherTask().execute(loc);
	}

	//4.14 buat sebuah method yang dinamakan showWeatherDateView yg akan menyembunyikan pesan kesalahan dan menampilkan data cuaca
	private void showWeatherDataView(){
		mtv_error_message.setVisibility(View.INVISIBLE);
		//5.45 Ganti mtvweather dengan mrv_weather
////		mtv_weather.setVisibility(View.VISIBLE);
		mrv_weather.setVisibility(View.VISIBLE);
	}

	//4.15 buat sebuah method yang dinamakan showErrorMessage yg akan menyembunyikan data cuaca dan menampilkan pesan kesalahan
	private void showErrorMessage(){
		//5.46 Ganti mtvweather dengan mrv_weather
////		mtv_weather.setVisibility(View.INVISIBLE);
		mrv_weather.setVisibility(View.INVISIBLE);
		mtv_error_message.setVisibility(View.VISIBLE);
	}

	//2.5 buat kelas yang extends Asyntask untuk melakukak permintaan networking
	public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
		//4.16 Di dalam Asynctask, override methode onPreExecute dan tampilkan loading
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			mpb_loading.setVisibility(View.VISIBLE);
		}

		//2.6 override method doInBackground untuk melakukan permintaan networking kamu
		@Override
		protected String[] doInBackground(String... params) {
			//if there's no zip code, there's nothing to look up
			if(params.length == 0){
				return null;
			}

			String location = params[0];
			URL weatherRequestURL = NetworkUtils.buildUrl(location);

			try{
				String jsonWeatherResponse = NetworkUtils
						.getResponseFromHttpUrl(weatherRequestURL);

				String[] simpleJsonWeatherData = OpenWeatherJsonUtils
						.getSimpleWeatherStringsFromJson(forecast.this, jsonWeatherResponse);

				return simpleJsonWeatherData;

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		//2.7 override method onPostExecute untuk menampilkan hasil dari permintaan network
		@Override
		protected void onPostExecute(String[] weatherList){
			//4.17 Setelah data selesai loading, sembunyikan indikator loading
			mpb_loading.setVisibility(View.INVISIBLE);

			if(weatherList != null) {
				//4.18 jika data cuaca tidak kosong, pastikan data tersebut untuk ditampilkan
				showWeatherDataView();

				//5.47 Hapus perulangan dibawah, gunakan mForecastAdapter.setWeatherlist
////				for (String dummyWeatherDay : weatherData) {
////					mtv_weather.append((dummyWeatherDay) + "\n\n\n");
////				}
				mForcecastAdapter.setWeatherList(weatherList);

			}else{
				//4.19 jika data cuaca kosong, tampilkan pesan kesalahan
				showErrorMessage();
			}
		}
	}

	// 3.5 Override onCreateOptionsMenu to inflate the menu for this Activity
	// 3.6 Return true to display the menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
		MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
		inflater.inflate(R.menu.forecast_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
		return true;
	}

	// 3.7 Override onOptionsItemSelected to handle clicks on the refresh button
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == com.singpaulee.wheaterapp.R.id.action_refresh) {
			//5.48 untuk mengganti line dibawah, atur adapter menjadi null sebelum refreshing
////			mtv_weather.setText("");
			mForcecastAdapter.setWeatherList(null);
			loadWeatherData();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
