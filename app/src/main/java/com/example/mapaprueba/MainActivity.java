package com.example.mapaprueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Button btnSearch;
    Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frameMap);
        mapFragment.getMapAsync(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager locationManager;
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }

                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    1);

                            // LOCATION_REQUEST_CODE is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    } else {
                        // Permission has already been granted
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Toast.makeText(MainActivity.this, "GPS", Toast.LENGTH_SHORT).show();
                        }
                        mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                } else {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(MainActivity.this, "No GPS", Toast.LENGTH_SHORT).show();
                    }
                    mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                /*****/

                LocationListener locationListener;
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        String nombreLugar;
                        Marker ubicacionMarker;

                        if (mLocation != null) {
                            nombreLugar = "Estabas aqui";
                            LatLng coordenadaUbicacion = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                            ubicacionMarker = mMap.addMarker(new MarkerOptions().title(nombreLugar).snippet(mLocation.getLatitude() + " , " + mLocation.getLongitude()).position(coordenadaUbicacion));
                            ubicacionMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadaUbicacion, 17));
                            //Evento
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return false;
                                }
                            });
                        } else {
                            nombreLugar = "Estas aqui";
                            LatLng coordenadaUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                            ubicacionMarker = mMap.addMarker(new MarkerOptions().title(nombreLugar).snippet(location.getLatitude() + " , " + location.getLongitude()).position(coordenadaUbicacion));
                            ubicacionMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadaUbicacion, 17));
                            //Evento
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return false;
                                }
                            });
                        }

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMyLocationEnabled(true);
    }
}
