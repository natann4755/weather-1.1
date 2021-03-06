package com.example.weather.Activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weather.R;
import com.example.weather.Retrofit.ApiClient;
import com.example.weather.Retrofit.ApiInterpafe;
import com.example.weather.modulWwather.Responsee;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList  mContry;
    public static final String apikeyWeather = "2278f04dd5ede897f90362371419d187";
    public static final String keyRecponce = "keyRecponce";

    private LinearLayout linearLayout;
    private TextView degrees;
    private ImageView icon;
    private TextView situation;
    private TextView fildLIke;
    private Button moreInfo;
    private Responsee allResponsee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initVeiwResolt();

        mContry =  getIntent().getParcelableArrayListExtra(ActivityAllContreys.keycontrey);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initVeiwResolt() {
        linearLayout = findViewById(R.id.rezolt_weather_LL);
        degrees = findViewById(R.id.weather_temp_TV);
        icon = findViewById(R.id.weather_icon_IV);
        situation = findViewById(R.id.weather_situation_TV);
        fildLIke = findViewById(R.id.weather_fildLike_TV);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        Double one = (Double) mContry.get(0);
        Double to = (Double) mContry.get(1);
        LatLng Contrey = new LatLng(one,to);
        mMap.setMinZoomPreference(6f);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setMap(3f);
//                Toast.makeText(getApplicationContext(),String.valueOf(latLng.latitude+"   "+latLng.longitude),
//                        Toast.LENGTH_LONG).show();
                getWeather(latLng.latitude,latLng.longitude);
            }
        });


//        mMap.addMarker(new MarkerOptions().position(Contrey));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Contrey));

    }

    private void setMap(float flo) {
        linearLayout.setWeightSum(10f);
        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        lParams.weight = flo;
        linearLayout.setLayoutParams(lParams);
        mMap.setMinZoomPreference(6f);
    }

    private void getWeather(Double latitude, Double longitude) {
        ApiInterpafe mApiInterpafe = ApiClient.getClient2().create(ApiInterpafe.class);
        Call <Responsee> call = mApiInterpafe.getWetherPointsTest2(latitude,longitude);
        call.enqueue(new Callback<Responsee>() {
            @Override
            public void onResponse(Call<Responsee> call, Response<Responsee> response) {
//                Toast.makeText(getApplicationContext(),String.valueOf(response.body().getMain().getTemp()+" "+response.body().getWeather().get(0).getDescription()),
//                        Toast.LENGTH_LONG).show();
                allResponsee = response.body();
                setData();



            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });

    }

    private void setData() {
        degrees.setText(fixDegrees(allResponsee.getMain().getTemp()));
        fildLIke.setText(fixDegrees(allResponsee.getMain().getFeelsLike()));
        String urll = "https://openweathermap.org/img/w/" +allResponsee.getWeather().get(0).getIcon()+".png";
        Picasso.get().load(urll).into(icon);
        situation.setText(allResponsee.getWeather().get(0).getDescription());
        moreInfo = findViewById(R.id.weather_moredetails_B);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),moreDetailsActivity.class);
//                intent.putExtra(keyRecponce,allResponsee);
                startActivity(intent);
            }
        });
    }

    private String fixDegrees(Double degre) {
        Double ceDegre = degre -273.15;
        DecimalFormat numberFormat = new DecimalFormat("#.0");
         return numberFormat.format(ceDegre);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setMap(0f);
    }
}
