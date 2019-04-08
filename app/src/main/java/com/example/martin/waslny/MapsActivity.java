package com.example.martin.waslny;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private MarkerOptions marker;


    private ImageButton done_button;

    private Double latitude;
    private Double longitude;
    private boolean get_address ;
    private order w_order;
    private String type;
    private String address;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        done_button=findViewById(R.id.done_button);


        w_order=(order) getIntent().getSerializableExtra("order");
        type = getIntent().getStringExtra("type");




        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done();
            }
        });


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.clear();
                address=place.getAddress().toString();
                LatLng point=place.getLatLng();
                latitude=point.latitude;
                longitude=point.longitude;
                marker=new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).title(address);
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,18));
                get_address=true;


            }

            @Override
            public void onError(Status status) {}
        });




    }







    public void done(){
        if(get_address) {
            if(type.equals("sender")){
                w_order.setS_address(address);
                w_order.setS_lat(latitude);
                w_order.setS_long(longitude);
            }else if(type.equals("receiver")){
                w_order.setR_address(address);
                w_order.setE_lat(latitude);
                w_order.setE_long(longitude);
            }
            Intent main = new Intent(MapsActivity.this, Main.class);
              main.putExtra("order",w_order);
              main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
              finish();
            startActivity(main);
        }else{
            Toast.makeText(MapsActivity.this,"should search at least one time",Toast.LENGTH_LONG).show();
        }
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

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude=latLng.latitude;
                longitude=latLng.longitude;

                Geocoder geo =new Geocoder(MapsActivity.this);
                List<Address> list =new ArrayList<>();
                try {
                    list=geo.getFromLocation(latitude,longitude,1);
                } catch (IOException e) {

                }
                if(list.size()>0) {
                    mMap.clear();
                    address = list.get(0).getAddressLine(0);
                    marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).title(address);
                    mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    get_address = true;
                }else{
                    Toast.makeText(MapsActivity.this,"must location contain address or you can use search box",Toast.LENGTH_LONG).show();
                }

            }
        });


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(30.0440680, 31.2355120);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18));
    }
}
