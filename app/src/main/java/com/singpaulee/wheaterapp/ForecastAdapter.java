package com.singpaulee.wheaterapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ASUS on 06/09/2017.
 */

//5.15 Buat sebuah kelas dengan nama ForestAdapter.java
//5.22 Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{

	//5.23 Buat string array bernama mWeatherList
	private String[] mWeatherList;

	//5.47 Buat konstruktor default
	public ForecastAdapter() {

	}

	//5.16 Buat kelas dalam ForecastAdapter dengan nama ForecastAdapterViewHolder
	//5.17 Extend RecyclerView.ViewHolder
	public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{
		//5.18 Buat variabel public final textview dengan nama mWeatherList
		public final TextView mWeatherTextView;

		//5.19 Buat konstruktor untuk kelas ini yang menyetujui sebuah view menjadi parameter
		//5.20 panggil super(view)
		//5.21 Gunakan view.findviewbyid, untuk menyambungkan antara id dan disimpan dalam mWeatherTextView
		public ForecastAdapterViewHolder(View itemView) {
			super(itemView);
			mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
		}
	}//ForecastAdapterViewHolder

	//5.24 Override onCreateViewHoldew
	//5.25 Didalam onCreateViewHolder, tempelkan forecast_list_item.xml pada view
	//5.26 Didalam onCreateViewHolder, kembalikan (return) new ForecastAdapterViewHolder dengan parameter view
	@Override
	public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		int list_item = R.layout.forecast_list_item;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(list_item, parent, false);
		return new ForecastAdapterViewHolder(view);
	}

	//5.27 Override onBindViewHolder
	//5.28 Atur teks dari TextView ke dalam cuaca untuk posisi list item
	@Override
	public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
		String weatherForThisDay = mWeatherList[position];
		holder.mWeatherTextView.setText(weatherForThisDay);
	}

	//5.29 Override getItemCount
	//5.30 Return 0 jika array list kosong, atau ukuran arraylistjika tidak kosong
	@Override
	public int getItemCount() {
		if(null == mWeatherList){
			return 0;
		}else{
			return mWeatherList.length;
		}
	}

	//5.31 Buat method setWeatherData yang menyimpan weatherList ke mweatherList
	//5.32 Setelah disimpan di mWeatherList, panggil notifydatasetchanged
	//method ini digunakan untuk mengatur ramalan cuaca di ForecastAdapter jika kita sudah membuatnya
	//Ini menangani ketika mengambil data baru dari web tetapi kita tidak ingin membuat ForecastAdapter baru untuk menampilkannya
	public void setWeatherList(String[] weatherList){
		mWeatherList = weatherList;
		notifyDataSetChanged();
	}
}
